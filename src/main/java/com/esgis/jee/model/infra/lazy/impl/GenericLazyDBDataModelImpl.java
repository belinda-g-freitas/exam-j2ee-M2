package com.esgis.jee.model.infra.lazy.impl;

import com.esgis.jee.model.infra.lazy.GenericLazyDBDataModel;
import static com.esgis.jee.model.infra.lazy.GenericLazyDataModel.GLOBAL_FILTER_NAME;
import static com.esgis.jee.model.infra.lazy.GenericLazyDataModel.LOGGER;
import com.esgis.jee.model.infra.lazy.LazyTaskUtils;
import com.esgis.jee.model.infra.service.Service;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;


public class GenericLazyDBDataModelImpl<T extends Serializable, K > extends GenericLazyDataModelImpl<T, K> implements GenericLazyDBDataModel<T, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Service<T, K> repository;
	
	protected GenericLazyDBDataModelImpl(Service<T, K> repository) {
		this(repository, null, null);
	}
	
	public GenericLazyDBDataModelImpl(Service<T, K> repository, Class<T> dataClass, Class<K> idClass) {
		super();
		this.repository = repository;
		this.dataClass = dataClass;
		this.idClass = idClass;
	}
	
	public GenericLazyDBDataModelImpl(Service<T, K> repository, Class<T> dataClass, Class<K> idClass, String dataTbName) {
		this(repository, dataClass, idClass);
		this.dataTbName = dataTbName;
	}
	
	public GenericLazyDBDataModelImpl(Service<T, K> repository, Class<T> dataClass, Class<K> idClass, String[] globalFilterFields) {
		this(repository, dataClass, idClass);
		this.globalFilterFields = globalFilterFields;
	}
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// Requete de base
		CriteriaBuilder cb = repository.getEm().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(getDataClass());
		criteriaQuery.select(cb.count(root));
		// Filtres
		criteriaQuery.where(constructFilterExpressions(filterBy, cb, criteriaQuery, root));
		// Execution de la requete
		return repository
				.getEm()
				.createQuery(criteriaQuery)
				.getSingleResult()
				.intValue();
	}
	
	@Override
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		// Sauvegarde des parametres de Filtre et de Tri
		defineSortAndFilter(sortBy, filterBy);
		// Requete de base
		CriteriaBuilder cb = repository.getEm().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = cb.createQuery(getDataClass());
		Root<T> root = criteriaQuery.from(getDataClass());
		criteriaQuery.select(root);
		// Filtres
		if(!filterBy.isEmpty()) {
			criteriaQuery.where(constructFilterExpressions(filterBy, cb, criteriaQuery, root));
		}
		// Tris
		if(!sortBy.isEmpty()) {
			criteriaQuery.orderBy(constructSortExpressions(sortBy, cb, criteriaQuery, root));
		}
		// Execution de la requete
		// La necessite d'affectation du data fait partie des Effets de bord de la programmation imperative
		data = repository
				.getEm()
				.createQuery(criteriaQuery)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();
		return (List<T>) data;
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public T getRowData(String rowKey) {
		K id = getIDClass().equals(Integer.class) ? 
				(K) Integer.valueOf(rowKey) :
				getIDClass().equals(Long.class) ?
					(K) Long.valueOf(rowKey) :
					(K) rowKey;
		Optional<T> result = repository.findById(id);
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
	}*/

	/**
	 * Construit un tableau d'expressions Criteria API de filtres (general et particulier), selon les informations de filtre Primefaces
	 * @param filterBy
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return le Predicat servant de filtre selon Criteria API
	 */
	protected Predicate[] constructFilterExpressions(Map<String, FilterMeta> filterBy, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		// Elements de construction
		Collection<FilterMeta> filterCollection = filterBy.values();
		Object filterValue;
		String filterFieldName;
		Class<?> entityFieldType;
		// Construction de la requete : Filtres particuliers
		List<Predicate> filterExpressions = new ArrayList<>();
		for(FilterMeta filterMeta : filterCollection) {
			filterValue = filterMeta.getFilterValue();
			filterFieldName = filterMeta.getField();
			if(filterFieldName.equals(GLOBAL_FILTER_NAME)) {
				if(!StringUtils.isBlank(String.valueOf(filterValue))) {
					// Filtre global
					Predicate globalPredicate = constructGlobalFilterExpressions(String.valueOf(filterValue), cb, criteriaQuery, root);
					// Les expressions de filtres
					if(globalPredicate != null) {
						filterExpressions.add(globalPredicate);
					}
				}
			} else {
				// Filtres particuliers
				try {
					entityFieldType = LazyTaskUtils.getAccessibleFieldByExp(getDataClass(), filterFieldName).getType();
					if(String.class.equals(entityFieldType)) {
						// Chaines
						filterExpressions.add(constructStringFilterExpression(filterMeta.getMatchMode(), filterFieldName, (String) filterValue, cb, criteriaQuery, root));
					} else if(Number.class.isAssignableFrom(entityFieldType)) {
						// Nombres (Integer, Long, BigInteger, ...)
						filterExpressions.add(constructNumberFilterExpression(filterMeta.getMatchMode(), filterFieldName, filterValue, cb, criteriaQuery, root));
					} else if(isDate(entityFieldType)) {
						// Dates
						filterExpressions.add(constructDateFilterExpression(filterMeta.getMatchMode(), entityFieldType, filterFieldName, filterValue, cb, criteriaQuery, root));
					} else if(entityFieldType.isEnum()) {
						// Enumerations
						filterExpressions.add(constructEnumFilterExpression(filterMeta.getMatchMode(), filterFieldName, filterValue, cb, criteriaQuery, root));
					} else if(entityFieldType.isPrimitive()) {
						// Types primitifs (nombre, caractere, booleen)
						filterExpressions.add(constructPrimitiveFilterExpression(filterMeta.getMatchMode(), entityFieldType, filterFieldName, filterValue, cb, criteriaQuery, root));
					} else {
						//TODO: trouver un cas
						filterExpressions.add(constructStringFilterExpression(filterMeta.getMatchMode(), filterFieldName, (String) filterValue, cb, criteriaQuery, root));
					}
				} catch (NullPointerException  e) {
					LOGGER.log(Level.SEVERE, null, e);
				}
			}
		}
		return filterExpressions.toArray(new Predicate[0]);
	}
	
	/**
	 * Construit une expression Criteria API de filtre general, selon la valeur du filtre general Primefaces
	 * @param filterValue la valeur a utiliser pour filtrer
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return le Predicat servant de filtre selon Criteria API
	 */
	protected Predicate constructGlobalFilterExpressions(String filterValue, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		//Set<SingularAttribute<T, ?>> attributes = root.getModel().getDeclaredSingularAttributes();
		List<Predicate> globalPredicates = new ArrayList<>();
		Class<?> attributeType;
		for(String filterField : getGlobalFilterFields()) {
			attributeType = LazyTaskUtils.getAccessibleFieldByExp(getDataClass(), filterField).getType();
			if(attributeType.equals(String.class) || attributeType.equals(char.class)) {
				// Type String et char
				globalPredicates.add(cb.like(
						cb.lower(root.get(filterField)), 
						String.format("%%%s%%", filterValue.toLowerCase())));
			} else if(isDate(attributeType)) {
				// Autres Types : Date
				globalPredicates.add(cb.like(
						cb.function("TO_CHAR", String.class, root.get(filterField), cb.literal("DD/MM/YYYY HH:MI:SS")), 
						String.format("%%%s%%", filterValue.toLowerCase())));
			} else if(attributeType.isEnum()) {
				// Est un Enum
				//TODO: implement
			} else if(attributeType.equals(boolean.class)) {
				// Booleen 
				if(filterValue.equalsIgnoreCase("Oui") || filterValue.equalsIgnoreCase("Non")) {
					globalPredicates.add(cb.equal(
							root.get(filterField), 
							filterValue.equalsIgnoreCase("Oui")));
				}
				
			} else {
				globalPredicates.add(cb.like(
						cb.function("TO_CHAR", String.class, root.get(filterField), cb.literal("999999999999999D99")),  
						String.format("%%%s%%", filterValue.toLowerCase())));
			}
		}
		/*for(SingularAttribute<T, ?> attribute : attributes) {
			if(attribute.getJavaType().equals(String.class)) {
				// Type String
				globalPredicates.add(cb.like(
						cb.lower(root.get(attribute.getName())), 
						String.format("%%%s%%", filterValue.toLowerCase())));
			} else if(isDate(attribute.getJavaType())) {
				// Autres Types : Date
				globalPredicates.add(cb.like(
						cb.function("TO_CHAR", String.class, root.get(attribute.getName()), cb.literal("DD/MM/YYYY HH:MI:SS")), 
						String.format("%%%s%%", filterValue.toLowerCase())));
			} else if(attribute.getJavaType().isEnum()) {
				// Est un Enum
				//TODO: implement
			} else {
				globalPredicates.add(cb.like(
						cb.function("TO_CHAR", String.class, root.get(attribute.getName()), cb.literal("999999999999999D99")),  
						String.format("%%%s%%", filterValue.toLowerCase())));
			}
		}*/
		if(!globalPredicates.isEmpty()) {
			return cb.or(globalPredicates.toArray(new Predicate[0]));
		} else {
			return null;
		}
	}
	
	/**
	 * Construit une expression Criteria API de filtre sur un type chaine, selon les informations de filtre Primefaces
	 * @param matchMode le Mode Primefaces de correspondance
	 * @param filterFieldName le nom du champ sur lequel est effectue le filtre
	 * @param filterValue la valeur a utiliser pour filtrer
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return le Predicat servant de filtre selon Criteria API
	 */
	protected Predicate constructStringFilterExpression(MatchMode matchMode, String filterFieldName, String filterValue, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		Predicate result = null;
		switch(matchMode) {
		case CONTAINS:
			result = cb.like(cb.lower(root.get(filterFieldName)), String.format("%%%s%%", filterValue.toLowerCase()));
			break;
		case EQUALS: // equivaut en ignorant la casse
			result = cb.equal(cb.lower(root.get(filterFieldName)), String.format("%s", filterValue.toLowerCase()));
			break;
		case EXACT: // equivaut en considerant la casse
			result = cb.equal(root.get(filterFieldName), String.format("%s", filterValue));
			break;
		case STARTS_WITH:
			result = cb.like(cb.lower(root.get(filterFieldName)), String.format("%s%%", filterValue.toLowerCase()));
			break;
		case ENDS_WITH:
			result = cb.like(cb.lower(root.get(filterFieldName)), String.format("%%%s", filterValue.toLowerCase()));
			break;
		case NOT_CONTAINS:
			result = cb.notLike(cb.lower(root.get(filterFieldName)), String.format("%%%s%%", filterValue.toLowerCase()));
			break;
		case NOT_EQUALS:
			result = cb.notEqual(cb.lower(root.get(filterFieldName)), String.format("%s", filterValue.toLowerCase()));
			break;
		case NOT_EXACT:
			result = cb.notEqual(root.get(filterFieldName), String.format("%s", filterValue.toLowerCase()));
			break;
		case NOT_STARTS_WITH:
			result = cb.notLike(cb.lower(root.get(filterFieldName)), String.format("%s%%", filterValue.toLowerCase()));
			break;
		case NOT_ENDS_WITH:
			result = cb.notLike(cb.lower(root.get(filterFieldName)), String.format("%%%s", filterValue.toLowerCase()));
			break;
		default:
			result = cb.equal(cb.lower(root.get(filterFieldName)), String.format("%s", filterValue.toLowerCase()));
			break;
		}
		return result;
	}
	
	/**
	 * Construit une expression Criteria API de filtre sur un type nombre, selon les informations de filtre Primefaces
	 * @param matchMode le Mode Primefaces de correspondance
	 * @param filterFieldName le nom du champ sur lequel est effectue le filtre
	 * @param filterValue la valeur a utiliser pour filtrer
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return le Predicat servant de filtre selon Criteria API
	 */
	@SuppressWarnings("unchecked")
	protected Predicate constructNumberFilterExpression(MatchMode matchMode, String filterFieldName, Object filterValue, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		Predicate result = null;
		switch(matchMode) {
		case EQUALS: // equivaut en ignorant la casse
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		case EXACT: // equivaut en considerant la casse
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		case CONTAINS:
			result = cb.like(root.get(filterFieldName), String.format("%%%s%%", String.valueOf(filterValue)));
			break;
		case STARTS_WITH:
			result = cb.like(root.get(filterFieldName), String.format("%s%%", String.valueOf(filterValue)));
			break;
		case ENDS_WITH:
			result = cb.like(root.get(filterFieldName), String.format("%%%s", String.valueOf(filterValue)));
			break;
		case NOT_EQUALS:
			result =  cb.notEqual(root.get(filterFieldName), filterValue);
			break;
		case NOT_EXACT:
			result = cb.notEqual(root.get(filterFieldName), filterValue);
			break;
		case NOT_CONTAINS:
			result = cb.notLike(root.get(filterFieldName), String.format("%%%s%%", String.valueOf(filterValue)));
			break;
		case NOT_STARTS_WITH:
			result = cb.notLike(root.get(filterFieldName), String.format("%s%%", String.valueOf(filterValue)));
			break;
		case NOT_ENDS_WITH:
			result = cb.notLike(root.get(filterFieldName), String.format("%%%s", String.valueOf(filterValue)));
			break;
		case BETWEEN: // Un tableau de deux nombres est envoye
			if(filterValue.getClass().isArray()) {
				result = cb.between(root.get(filterFieldName), ((Comparable[])filterValue)[0], ((Comparable[])filterValue)[1]);
			}
			break;
		case GREATER_THAN:
			result = cb.greaterThan(root.get(filterFieldName), (Comparable) filterValue);
			break;
		case GREATER_THAN_EQUALS:
			result = cb.greaterThanOrEqualTo(root.get(filterFieldName), (Comparable) filterValue);
			break;
		case IN: // Un tableau est envoye
			if(filterValue.getClass().isArray()) {
				result = root.get(filterFieldName).in(Arrays.asList((Object[])filterValue));
			}
			break;
		case LESS_THAN:
			result = cb.lessThan(root.get(filterFieldName), (Comparable) filterValue);
			break;
		case LESS_THAN_EQUALS:
			result = cb.lessThanOrEqualTo(root.get(filterFieldName), (Comparable) filterValue);
			break;
		case NOT_BETWEEN: // Un tableau de deux nombres est envoye
			if(filterValue.getClass().isArray()) {
				result = cb.not(cb.between(root.get(filterFieldName), ((Comparable[])filterValue)[0], ((Comparable[])filterValue)[1]));
			}
			break;
		case NOT_IN: // Un tableau est envoye
			if(filterValue.getClass().isArray()) {
				result = cb.not(root.get(filterFieldName).in(Arrays.asList((Object[])filterValue)));
			}
			break;
		case RANGE: // <=> BETWEEN, Un tableau de deux nombres est envoye
			if(filterValue.getClass().isArray()) {
				result = cb.between(root.get(filterFieldName), ((Comparable[])filterValue)[0], ((Comparable[])filterValue)[1]);
			}
			break;
		default:
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		}
		return result;
	}
	
	/**
	 * Construit une expression Criteria API de filtre sur un type date, selon les informations de filtre Primefaces
	 * @param matchMode le Mode Primefaces de correspondance
	 * @param entityFieldType la classe du champ concernee de l'entite
	 * @param filterFieldName le nom du champ sur lequel est effectue le filtre
	 * @param filterValue la valeur a utiliser pour filtrer
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return le Predicat servant de filtre selon Criteria API
	 */
	@SuppressWarnings("unchecked")
	protected Predicate constructDateFilterExpression(MatchMode matchMode, Class<?> entityFieldType, String filterFieldName, Object filterValue, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		Predicate result = null;
		switch(matchMode) {
		case BETWEEN: // Un ArrayList de deux dates est envoye
			if(List.class.isAssignableFrom(filterValue.getClass())) {
				//TODO: Gerer le cas ou l'Entity field = LocalDate & Filtre = LocalDateTime Ou l'Entity field = LocalDateTime & Filtre = LocalDate
				if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDate.class) &&
						entityFieldType.equals(LocalDateTime.class)) {
					// On caste l'EntityField
					//TODO: Eprouver
					result = cb.between(
							root.get(filterFieldName).as(LocalDate.class), 
							((List<LocalDate>)filterValue).get(0), ((List<LocalDate>)filterValue).get(1));
				} else if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDateTime.class) &&
						entityFieldType.equals(LocalDate.class)) {
					// On caste le filtre
					result = cb.between(
							root.get(filterFieldName), 
							((List<LocalDateTime>)filterValue).get(0).toLocalDate(), ((List<LocalDateTime>)filterValue).get(1).toLocalDate());
				} else {
					result = cb.between(root.get(filterFieldName), ((List<Comparable>)filterValue).get(0), ((List<Comparable>)filterValue).get(1));
				}
			}
			break;
		case RANGE: // <=> BETWEEN, Un ArrayList de deux dates est envoye
			if(List.class.isAssignableFrom(filterValue.getClass())) {
				if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDate.class) &&
						entityFieldType.equals(LocalDateTime.class)) {
					// On caste l'EntityField
					//TODO: Eprouver
					result = cb.between(
							root.get(filterFieldName).as(LocalDate.class), 
							((List<LocalDate>)filterValue).get(0), ((List<LocalDate>)filterValue).get(1));
				}
				else if(
						((List<?>)filterValue).get(0).getClass().equals(LocalDateTime.class) &&
						entityFieldType.equals(LocalDate.class)) {
					// On caste le filtre
					result = cb.between(
							root.get(filterFieldName), 
							((List<LocalDateTime>)filterValue).get(0).toLocalDate(), ((List<LocalDateTime>)filterValue).get(1).toLocalDate());
				}
				else {
					result = cb.between(root.get(filterFieldName), ((List<Comparable>)filterValue).get(0), ((List<Comparable>)filterValue).get(1));
				}
			}
			break;
		case EQUALS: // equivaut en ignorant le temps
			if(
					filterValue.getClass().equals(LocalDate.class) &&
					entityFieldType.equals(LocalDateTime.class)) {
				// On caste l'EntityField
				//TODO: Eprouver
				result = cb.equal(root.get(filterFieldName).as(LocalDate.class), filterValue);
			} else if(
					filterValue.getClass().equals(LocalDateTime.class) &&
					entityFieldType.equals(LocalDate.class)) {
				// On caste le filtre
				result = cb.equal(root.get(filterFieldName).as(LocalDate.class), ((LocalDateTime)filterValue).toLocalDate());
			} else {
				result = cb.equal(root.get(filterFieldName), filterValue);
			}
			break;
		case EXACT: // equivaut en considerant le temps
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		case NOT_EQUALS:
			result =  cb.notEqual(root.get(filterFieldName), filterValue);
			break;
		case NOT_EXACT:
			result = cb.notEqual(root.get(filterFieldName), filterValue);
			break;
		case IN:
			if(filterValue.getClass().isArray()) {
				// Un tableau est envoye
				result = root.get(filterFieldName).in(Arrays.asList((Object[])filterValue));
			} else if(List.class.isAssignableFrom(filterValue.getClass())) {
				// Un ArrayList de plusieurs dates est envoye
				result = root.get(filterFieldName).in((List<?>)filterValue);
			}
			break;
		case NOT_IN: 
			if(filterValue.getClass().isArray()) {
				// Un tableau est envoye
				result = cb.not(root.get(filterFieldName).in(Arrays.asList((Object[])filterValue)));
			} else if(List.class.isAssignableFrom(filterValue.getClass())) {
				// Un ArrayList de plusieurs dates est envoye
				result = cb.not(root.get(filterFieldName).in((List<?>)filterValue));
			}
			break;
		default:
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		}
		return result;
	}
	
	/**
	 * Construit une expression Criteria API de filtre sur un type enumere, selon les informations de filtre Primefaces
	 * @param matchMode le Mode Primefaces de correspondance
	 * @param filterFieldName le nom du champ sur lequel est effectue le filtre
	 * @param filterValue la valeur a utiliser pour filtrer
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return le Predicat servant de filtre selon Criteria API
	 */
	protected Predicate constructEnumFilterExpression(MatchMode matchMode, String filterFieldName, Object filterValue, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		Predicate result = null;
		switch(matchMode) {
		case IN: //Un tableau est envoye
			if(filterValue.getClass().isArray()) {
				result = root.get(filterFieldName).in(Arrays.asList((Object[])filterValue));
			}
			break;
		case EQUALS:
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		case EXACT:
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		case NOT_EQUALS:
			result =  cb.notEqual(root.get(filterFieldName), filterValue);
			break;
		case NOT_EXACT:
			result = cb.notEqual(root.get(filterFieldName), filterValue);
			break;
		default:
			result = cb.equal(root.get(filterFieldName), filterValue);
			break;
		}
		return result;
	}
	
	/**
	 * Construit une expression Criteria API de filtre sur un type primitif, selon les informations de filtre Primefaces
	 * @param matchMode le Mode Primefaces de correspondance
	 * @param entityFieldType la classe du champ concernee de l'entite
	 * @param filterFieldName le nom du champ sur lequel est effectue le filtre
	 * @param filterValue la valeur a utiliser pour filtrer
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return le Predicat servant de filtre selon Criteria API
	 */
	protected Predicate constructPrimitiveFilterExpression(MatchMode matchMode, Class<?> entityFieldType, String filterFieldName, Object filterValue, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		Predicate result = null;
		if(isPrimNombre(entityFieldType)) {
			// Nombres
			result = constructNumberFilterExpression(matchMode, filterFieldName, filterValue, cb, criteriaQuery, root);
		} else if(
				entityFieldType.equals(char.class)) {
			// Caractere
			result = constructStringFilterExpression(matchMode, filterFieldName, filterFieldName, cb, criteriaQuery, root);
		} else if(
				entityFieldType.equals(boolean.class)) {
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
			if(filterValue != null) {
				switch(matchMode) {
				case EQUALS:
					result = cb.equal(root.get(filterFieldName), filterValue);
					break;
				case EXACT:
					result = cb.equal(root.get(filterFieldName), filterValue);
					break;
				case NOT_EQUALS:
					result =  cb.notEqual(root.get(filterFieldName), filterValue);
					break;
				case NOT_EXACT:
					result = cb.notEqual(root.get(filterFieldName), filterValue);
					break;
				default:
					result = cb.equal(root.get(filterFieldName), filterValue);
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Construit les expressions Criteria API de tri selon les informations de Tris Primefaces
	 * @param sortBy la Map Primefaces des informations de Tri
	 * @param cb la CriteriaBuilder de le Criteria API
	 * @param criteriaQuery la Criteria Query concernee
	 * @param root la Root concernee
	 * @return la liste des expressions de Tri selon Criteria API
	 */
	protected List<Order> constructSortExpressions(Map<String, SortMeta> sortBy, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		// Elements de construction : Tris
		Collection<SortMeta> sortCollection = sortBy.values();
		String sortFieldName;
		// Construction de la requete : Tris
		List<Order> orderExpressions = new ArrayList<>();
		for(SortMeta sortMeta : sortCollection) {
			sortFieldName = sortMeta.getField();
			if(sortMeta.getOrder().isAscending())
				orderExpressions.add(cb.asc(root.get(sortFieldName)));
			else if(sortMeta.getOrder().isDescending())
				orderExpressions.add(cb.desc(root.get(sortFieldName)));
		}
		return orderExpressions;
	}
}
