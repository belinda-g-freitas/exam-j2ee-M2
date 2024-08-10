package com.esgis.jee.model.infra.lazy.impl;

import com.esgis.jee.model.infra.lazy.GenericLazyCollectionDataModel;
import com.esgis.jee.model.infra.lazy.LazySorter;
import com.esgis.jee.model.infra.lazy.LazyTaskUtils;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;

public class GenericLazyCollectionDataModelImpl<T extends Serializable, K> extends GenericLazyDataModelImpl<T, K> implements GenericLazyCollectionDataModel<T, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final List<T> dataSource;
	
	protected GenericLazyCollectionDataModelImpl(Collection<T> dataSource, String dataTbName) {
		this(dataSource, null, null);
		this.dataTbName = dataTbName;
	}
	
	protected GenericLazyCollectionDataModelImpl(Collection<T> dataSource, String[] globalFilterFields) {
		this(dataSource, null, null);
		this.globalFilterFields = globalFilterFields;
	}
	
	/**
	 * 
	 * @param dataSource la source de donnees
	 * @param dataTbName le nom Primefaces avec lequel acceder au DataTable. Ex : form1:dataTb
	 */
	public GenericLazyCollectionDataModelImpl(Collection<T> dataSource, Class<T> dataClass, Class<K> idClass, String dataTbName) {
		this(dataSource, dataClass, idClass);
		this.dataTbName = dataTbName;
	}
	
	/**
	 * Construit le DataModel avec un DataTbName par defaut : form1:dataTb
	 * @param dataSourcela source de donnees
	 */
	public GenericLazyCollectionDataModelImpl(Collection<T> dataSource, Class<T> dataClass, Class<K> idClass) {
		super();
		if(List.class.isAssignableFrom(dataSource.getClass())) {
			this.dataSource = (List<T>)dataSource;	
		} else {
			this.dataSource = dataSource.stream().collect(Collectors.toList());
		}
		this.dataClass = dataClass;
		this.idClass = idClass;
	}
	
	public GenericLazyCollectionDataModelImpl(Collection<T> dataSource, Class<T> dataClass, Class<K> idClass, String[] globalFilterFields) {
		this(dataSource, dataClass, idClass);
		this.globalFilterFields = globalFilterFields;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deleteData(T... data) {
		deleteData(Arrays.asList(data));
	}
	
	@Override
	public void deleteData(Collection<T> data) {
		dataSource.removeAll(data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateData(T... data) {
		updateData(Arrays.asList(data));
	}
	
	@Override
	public void updateData(Collection<T> data) {
		for(T d : data) {
			dataSource.set(dataSource.indexOf(d), d);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addData(T... data) {
		addData(Arrays.asList(data));
	}
	
	@Override
	public void addData(Collection<T> data) {
		dataSource.addAll(data);
	}
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return (int) filter(dataSource, filterBy)
				.count();
	}
	
	@Override
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		// Sauvegarde des paramètres de Filtre et de Tri
		defineSortAndFilter(sortBy, filterBy);
		// Load
		// La nécessité d'affectation du data fait partie des Effets de bord de la programmation impérative
		data = load(dataSource, first, pageSize, sortBy, filterBy);
		return (List<T>) data;
	}
	
	public List<T> load(List<T> list, int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        // Filtres
		if(!filterBy.isEmpty()) {
			list = filter(list, filterBy)
					.collect(Collectors.toList());
		}
        // Tris
		if(!sortBy.isEmpty()) {
			list = sort(list, sortBy);
		}
		
		// Pagination
		list = list
				.stream()
				.skip(first)
				.limit(pageSize)
				.collect(Collectors.toList());
		return list;
	}
	
	/**
	 * Filtre utilisant Primefaces comme methode
	 * @param list
	 * @param filterBy
	 * @return
	 */
	/*protected List<T> filter(List<T> list, Map<String, FilterMeta> filterBy) {
		return list.stream()
                .filter(o -> facesFilter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .collect(Collectors.toList());
	}*/
	
	protected List<T> filter(List<T> list, Map<String, FilterMeta> filterBy, int offset, int pageSize) {
		// Code duplique pour des questions de performances : la collection prend un temps
		return filter(list, filterBy)
                .skip(offset)
                .limit(pageSize)
                .collect(Collectors.toList());
	}
	
	protected List<T> facesFilter(List<T> list, Map<String, FilterMeta> filterBy, int offset, int pageSize) {
		// Code duplique pour des questions de performances : la collection prend un temps
		return list.stream()
				.skip(offset)
                .filter(o -> facesFilter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .limit(pageSize)
                .collect(Collectors.toList());
	}
	
	protected List<T> sort(List<T> list, Map<String, SortMeta> sortBy) {
		List<Comparator<T>> comparators = sortBy
				.values()
				.stream()
                .map(o -> new LazySorter<T>(o.getField(), o.getOrder()))
                .collect(Collectors.toList());
        Comparator<T> cp = ComparatorUtils.chainedComparator((Collection)comparators);
        synchronized(this) {
        	list.sort(cp); // TODO debug
        }
        return list;
	}
	
	protected boolean facesFilter(FacesContext context, Collection<FilterMeta> filterBy, T o) {
        boolean matching = true, globalMatch;
        
        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();
            if(filter.getField().equals(GLOBAL_FILTER_NAME)) {
				if(!StringUtils.isBlank(String.valueOf(filterValue))) {
					final String filterStringValue = String.valueOf(filterValue);
					globalMatch = false;
					for(String filterField : getGlobalFilterFields()) {
						globalMatch =  globalMatch || globalMatch(filterStringValue, o, filterField);
					}
	                matching = globalMatch;
				}
			} else {
	            try {
	                Object columnValue = String.valueOf(LazyTaskUtils.getFieldValueByExp(o, filter.getField()));
	                matching = constraint.isMatching(context, columnValue, filterValue, LocaleUtils.getCurrentLocale());
	            }
	            catch (IllegalArgumentException | IllegalAccessException | NullPointerException | InvocationTargetException | NoSuchMethodException e) {
	                matching = false;
	            }
			}
            if (!matching) {
                break;
            }
        }

        return matching;
    }
	
	protected Stream<T> filter(List<T> list, Map<String, FilterMeta> filterBy) {
		Collection<FilterMeta> filterCollection = filterBy.values();
		Object filterValue;
		String filterFieldName;
		Stream<T> result = list.stream();
		for(FilterMeta filterMeta : filterCollection) {
			filterValue = filterMeta.getFilterValue();
			filterFieldName = filterMeta.getField();
			if(filterFieldName.equals(GLOBAL_FILTER_NAME)) {
				// Filtre global
				if(!StringUtils.isBlank(String.valueOf(filterValue))) {
					final String filterStringValue = String.valueOf(filterValue);
					result = result
	                        .filter((t) -> {
	                            boolean match = false;
	                            // Pour chaque colonne de la DataTB
	                            for(String filterField : getGlobalFilterFields()) {
	                            	match =  match || globalMatch(filterStringValue, t, filterField);
	                            }
	                            return match;
	                        });
	                        //.collect(Collectors.toList());
				}
			}
			else {
				// Filtre particulier
				result = result
                        .filter((t) -> {
                        		// On ne passe pas la variable filterValue car n'est pas final
                                return match(filterMeta.getFilterValue(), t, filterMeta.getField(), filterMeta.getMatchMode()); });
                        //.collect(Collectors.toList());
			}
		}
		return result;
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public T getRowData(String rowKey) {
		Class<?> idClass = getIDClass();
		final K id = idClass.equals(Integer.class)?
						(K) Integer.valueOf(rowKey):
							idClass.equals(Long.class)?
								(K) Long.valueOf(rowKey):
									(K) rowKey; // Ternaire car la structure conditionnelle IF empeche le champ d'etre final. Le champ id doit etre final pour etre utilisable dans Stream
		Field idField = LazyTaskUtils.getAccessibleField(getDataClass(), "id");
		Optional<T> result = dataSource
			.stream()
			.filter((t) -> { try {
				return ((K)idField.get(t)).equals(id);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LOGGER.log(Level.SEVERE, null, e);
				return false;
			}})
			.findFirst();
		if(result.isPresent())
			return result.get();
		else
			return null;
	}
	
	@Override
	public String getRowKey(T object) {
		try {
			return String.valueOf(LazyTaskUtils.getFieldValueByExp(object, "id"));
		} catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
			LOGGER.log(Level.SEVERE, null, e);
			return "";
		}
	} */
	
    protected boolean globalMatch(String filterStringValue, T object, String fieldName) {
        try {
        	//TODO: Gerer le cas des dates et Enum differemment
			return String
					.valueOf(LazyTaskUtils.getFieldValueByExp(object, fieldName))
					.toLowerCase()
					.contains(filterStringValue.toLowerCase());
		} catch (IllegalArgumentException | IllegalAccessException | NullPointerException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.log(Level.SEVERE, null, e);
			return false;
		}
    }
    
    protected boolean match(Object filterValue, Object object, String fieldName, MatchMode matchMode) {
    	boolean result = false;
    	try {
	    	Field field = LazyTaskUtils.getAccessibleFieldByExp(object, fieldName);
	    	Class<?> objectFieldType = field.getType();
                // On utilise pas field.get car le fieldName peut être une expression pointée de type : parametre.code
	    	if(String.class.equals(objectFieldType)) {
				// Chaines
				result = stringMatch(filterValue, String.valueOf(LazyTaskUtils.getFieldValueByExp(object, fieldName)), matchMode);
			} else if(Number.class.isAssignableFrom(objectFieldType)) {
				// Nombres (Integer, Long, BigInteger, ...)
				result = numberMatch(filterValue, (Number)LazyTaskUtils.getFieldValueByExp(object, fieldName), matchMode);
			} else if(isDate(objectFieldType)) {
				// Dates
				result = dateMatch(filterValue, LazyTaskUtils.getFieldValueByExp(object, fieldName), matchMode);
			} else if(objectFieldType.isEnum()) {
				// Enumerations
				result = enumMatch(filterValue, LazyTaskUtils.getFieldValueByExp(object, fieldName), matchMode);
			} else if(objectFieldType.isPrimitive()) {
				// Types primitifs (nombre, caractere, booleen)
				result = primitiveMatch(filterValue, LazyTaskUtils.getFieldValueByExp(object, fieldName), matchMode);
			} else {
				//TODO: trouver un cas
				result = stringMatch(String.valueOf(filterValue), String.valueOf(LazyTaskUtils.getFieldValueByExp(object, fieldName)), matchMode);
			}
    	} catch (IllegalArgumentException | IllegalAccessException | NullPointerException | InvocationTargetException | NoSuchMethodException e) {
    		LOGGER.log(Level.SEVERE, null, e);
		}
    	return result;
    }
    
    private boolean primitiveMatch(Object filterValue, Object value, MatchMode matchMode) {
    	boolean result = false;
    	if(isPrimNombre(value.getClass())) {
			// Nombres
    		result = numberMatch(filterValue, (Number)value, matchMode);
		} else if(
				value.getClass().equals(char.class)) {
			// Caractere
			result = stringMatch(String.valueOf(filterValue), String.valueOf(value), matchMode);
		} else if(
				value.getClass().equals(boolean.class)) {
			// Booleen
			if(!filterValue.getClass().equals(boolean.class) && !filterValue.getClass().equals(Boolean.class)) {
				String filterValueAsString = String.valueOf(filterValue).toLowerCase();
				if(filterValueAsString.startsWith("o") || filterValueAsString.startsWith("y") || filterValueAsString.startsWith("t")) {
					filterValue = Boolean.TRUE;
				} else if(filterValueAsString.startsWith("n") || filterValueAsString.startsWith("f")) {
					filterValue = Boolean.FALSE;
				} else {
					filterValue = null;
				}
			}
			switch(matchMode) {
			case EQUALS:
				result = value.equals(filterValue);
				break;
			case EXACT:
				result = value.equals(filterValue);
				break;
			case NOT_EQUALS:
				result = !value.equals(filterValue);
				break;
			case NOT_EXACT:
				result = !value.equals(filterValue);
				break;
			default:
				result = value.equals(filterValue);
				break;
			}
		}
    	return result;
	}

	private boolean enumMatch(Object filterValue, Object value, MatchMode matchMode) {
		boolean result = false;
    	switch(matchMode) {
		case IN: //Un tableau est envoye
			if(filterValue.getClass().isArray()) {
				result = Arrays.asList((Object[])filterValue).contains(value);
			}
			break;
		case EQUALS:
			result = filterValue.equals(value);
			break;
		case EXACT:
			result = filterValue.equals(value);
			break;
		case NOT_EQUALS:
			result = !filterValue.equals(value);
			break;
		case NOT_EXACT:
			result = !filterValue.equals(value);
			break;
		default:
			result = filterValue.equals(value);
			break;
		}
    	return result;
	}

	protected boolean dateMatch(Object filterValue, Object value, MatchMode matchMode) {
    	boolean result = false;
    	switch(matchMode) {
		case BETWEEN: // Un ArrayList de deux dates est envoye
			if(List.class.isAssignableFrom(filterValue.getClass())) {
				//TODO: Gerer le cas ou l'Entity field = LocalDate & Filtre = LocalDateTime Ou l'Entity field = LocalDateTime & Filtre = LocalDate
				if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDate.class) &&
						value.getClass().equals(LocalDateTime.class)) {
					// On caste l'EntityField
					LocalDate entityField = ((LocalDateTime)value).toLocalDate();
					LocalDate filterDate1 = ((List<LocalDate>)filterValue).get(0), 
							filterDate2 = ((List<LocalDate>)filterValue).get(1);
					//TODO: Eprouver
					result = (entityField.isAfter(filterDate1) || entityField.equals(filterDate1)) &&
							(entityField.isBefore(filterDate2) || entityField.equals(filterDate2));
				} else if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDateTime.class) &&
						value.getClass().equals(LocalDate.class)) {
					// On caste le filtre
					LocalDate entityField = (LocalDate)value;
					LocalDate filterDate1 = ((List<LocalDateTime>)filterValue).get(0).toLocalDate(), 
							filterDate2 = ((List<LocalDateTime>)filterValue).get(1).toLocalDate();
					result = (entityField.isAfter(filterDate1) || entityField.equals(filterDate1)) &&
							(entityField.isBefore(filterDate2) || entityField.equals(filterDate2));
				} else {
					Comparable entityField = (Comparable)value;
					Comparable filterDate1 = ((List<Comparable>)filterValue).get(0), 
							filterDate2 = ((List<Comparable>)filterValue).get(1);
					result = entityField.compareTo(filterDate1) >= 0 &&
							entityField.compareTo(filterDate2) <= 0;
				}
			}
			break;
		case RANGE: // <=> BETWEEN, Un ArrayList de deux dates est envoye
			if(List.class.isAssignableFrom(filterValue.getClass())) {
				//TODO: Gerer le cas ou l'Entity field = LocalDate & Filtre = LocalDateTime Ou l'Entity field = LocalDateTime & Filtre = LocalDate
				if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDate.class) &&
						value.getClass().equals(LocalDateTime.class)) {
					// On caste l'EntityField
					LocalDate entityField = ((LocalDateTime)value).toLocalDate();
					LocalDate filterDate1 = ((List<LocalDate>)filterValue).get(0), 
							filterDate2 = ((List<LocalDate>)filterValue).get(1);
					//TODO: Eprouver
					result = (entityField.isAfter(filterDate1) || entityField.equals(filterDate1)) &&
							(entityField.isBefore(filterDate2) || entityField.equals(filterDate2));
				} else if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDateTime.class) &&
						value.getClass().equals(LocalDate.class)) {
					// On caste le filtre
					LocalDate entityField = (LocalDate)value;
					LocalDate filterDate1 = ((List<LocalDateTime>)filterValue).get(0).toLocalDate(), 
							filterDate2 = ((List<LocalDateTime>)filterValue).get(1).toLocalDate();
					result = (entityField.isAfter(filterDate1) || entityField.equals(filterDate1)) &&
							(entityField.isBefore(filterDate2) || entityField.equals(filterDate2));
				} else {
					Comparable entityField = (Comparable)value;
					Comparable filterDate1 = ((List<Comparable>)filterValue).get(0), 
							filterDate2 = ((List<Comparable>)filterValue).get(1);
					result = entityField.compareTo(filterDate1) >= 0 &&
							entityField.compareTo(filterDate2) <= 0;
				}
			}
			break;
		case EQUALS: // equivaut en ignorant le temps
			if(
					filterValue.getClass().equals(LocalDate.class) &&
					value.getClass().equals(LocalDateTime.class)) {
				// On caste l'EntityField
				result = ((LocalDateTime)value).toLocalDate().equals((LocalDate)filterValue);
			} else if(
					filterValue.getClass().equals(LocalDateTime.class) &&
					value.getClass().equals(LocalDate.class)) {
				// On caste le filtre
				result = ((LocalDateTime)filterValue).toLocalDate().equals((LocalDate)value);
			} else {
				result = filterValue.equals(value);
			}
			break;
		case EXACT: // equivaut en considerant le temps
			result = filterValue.equals(value);
			break;
		case NOT_EQUALS:
			if(
					filterValue.getClass().equals(LocalDate.class) &&
					value.getClass().equals(LocalDateTime.class)) {
				// On caste l'EntityField
				result = !((LocalDateTime)value).toLocalDate().equals((LocalDate)filterValue);
			} else if(
					filterValue.getClass().equals(LocalDateTime.class) &&
					value.getClass().equals(LocalDate.class)) {
				// On caste le filtre
				result = !((LocalDateTime)filterValue).toLocalDate().equals((LocalDate)value);
			} else {
				result = !filterValue.equals(value);
			}
			break;
		case NOT_EXACT:
			result = !filterValue.equals(value);
			break;
		case IN:
			if(filterValue.getClass().isArray()) {
				// Un tableau est envoye
				result = Arrays.asList((Object[])filterValue).contains(value);
			} else if(List.class.isAssignableFrom(filterValue.getClass())) {
				// Un ArrayList de plusieurs dates est envoye
				result = ((List<?>)filterValue).contains(value);
			}
			break;
		case NOT_IN: 
			if(filterValue.getClass().isArray()) {
				// Un tableau est envoye
				result = !Arrays.asList((Object[])filterValue).contains(value);
			} else if(List.class.isAssignableFrom(filterValue.getClass())) {
				// Un ArrayList de plusieurs dates est envoye
				result = !((List<?>)filterValue).contains(value);
			}
			break;
		default:
			result = filterValue.equals(value);
			break;
		}
		return result;
	}

	private boolean numberMatch(Object filterValue, Number value, MatchMode matchMode) {
    	boolean result = false;
    	switch(matchMode) {
		case EQUALS: // equivaut en ignorant la casse
			result = filterValue.equals(value);
			break;
		case EXACT: // equivaut en considerant la casse
			result = filterValue.equals(value);
			break;
		case CONTAINS:
			result = String.valueOf(value).contains(String.valueOf(filterValue));
			break;
		case STARTS_WITH:
			result = String.valueOf(value).startsWith(String.valueOf(filterValue));
			break;
		case ENDS_WITH:
			result = String.valueOf(value).endsWith(String.valueOf(filterValue));
			break;
		case NOT_EQUALS:
			result = !filterValue.equals(value);
			break;
		case NOT_EXACT:
			result = !filterValue.equals(value);
			break;
		case NOT_CONTAINS:
			result = !String.valueOf(value).contains(String.valueOf(filterValue));
			break;
		case NOT_STARTS_WITH:
			result = !String.valueOf(value).startsWith(String.valueOf(filterValue));
			break;
		case NOT_ENDS_WITH:
			result = !String.valueOf(value).endsWith(String.valueOf(filterValue));
			break;
		case BETWEEN: // Un tableau de deux nombres est envoye
			if(filterValue.getClass().isArray()) {
				result = ((Comparable)value).compareTo(((Comparable[])filterValue)[0]) >= 0 && ((Comparable)value).compareTo(((Comparable[])filterValue)[1]) <= 0;
			}
			break;
		case GREATER_THAN:
			result = ((Comparable)value).compareTo((Comparable)filterValue) > 0;
			break;
		case GREATER_THAN_EQUALS:
			result = ((Comparable)value).compareTo((Comparable)filterValue) >= 0;
			break;
		case IN: // Un tableau est envoye
			if(filterValue.getClass().isArray()) {
				result = Arrays.asList((Object[])filterValue).contains(value);
			}
			break;
		case LESS_THAN:
			result = ((Comparable)value).compareTo((Comparable)filterValue) < 0;
			break;
		case LESS_THAN_EQUALS:
			result = ((Comparable)value).compareTo((Comparable)filterValue) <= 0;
			break;
		case NOT_BETWEEN: // Un tableau de deux nombres est envoye
			if(filterValue.getClass().isArray()) {
				result = !(((Comparable)value).compareTo(((Comparable[])filterValue)[0]) >= 0 && ((Comparable)value).compareTo(((Comparable[])filterValue)[1]) <= 0);
			}
			break;
		case NOT_IN: // Un tableau est envoye
			if(filterValue.getClass().isArray()) {
				result = !Arrays.asList((Object[])filterValue).contains(value);
			}
			break;
		case RANGE: // <=> BETWEEN, Un tableau de deux nombres est envoye
			if(filterValue.getClass().isArray()) {
				result = ((Comparable)value).compareTo(((Comparable[])filterValue)[0]) >= 0 && ((Comparable)value).compareTo(((Comparable[])filterValue)[1]) <= 0;
			}
			break;
		default:
			result = filterValue.equals(value);
			break;
		}
    	return result;
	}

	protected boolean stringMatch(Object filterValue, String value, MatchMode matchMode) {
    	boolean result = false;
		switch(matchMode) {
		case CONTAINS:
			result = value.toLowerCase().contains(String.valueOf(filterValue).toLowerCase());
			break;
		case EQUALS: // equivaut en ignorant la casse
			result = value.equalsIgnoreCase(String.valueOf(filterValue));
			break;
		case EXACT: // equivaut en considerant la casse
			result = value.equals(filterValue);
			break;
		case STARTS_WITH:
			result = value.toLowerCase().startsWith(String.valueOf(filterValue).toLowerCase());
			break;
		case ENDS_WITH:
			result = value.toLowerCase().endsWith(String.valueOf(filterValue).toLowerCase());
			break;
		case NOT_CONTAINS:
			result = !value.toLowerCase().contains(String.valueOf(filterValue).toLowerCase());
			break;
		case NOT_EQUALS:
			result = !value.equalsIgnoreCase(String.valueOf(filterValue));
			break;
		case NOT_EXACT:
			result = !value.equals(filterValue);
			break;
		case NOT_STARTS_WITH:
			result = !value.toLowerCase().startsWith(String.valueOf(filterValue).toLowerCase());
			break;
		case NOT_ENDS_WITH:
			result = !value.toLowerCase().endsWith(String.valueOf(filterValue).toLowerCase());
			break;
		case IN: //Un tableau est envoye
			if(filterValue.getClass().isArray()) {
				result = Arrays.asList((Object[])filterValue).contains(value);
			}
			break;
		default:
			result = value.equalsIgnoreCase(String.valueOf(filterValue));
			break;
		}
		return result;
    }
}
