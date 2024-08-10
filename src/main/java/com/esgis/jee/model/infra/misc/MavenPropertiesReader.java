package com.esgis.jee.model.infra.misc;

import java.io.IOException;

public class MavenPropertiesReader extends PropertiesReader {

	private static MavenPropertiesReader INSTANCE;
	
	public MavenPropertiesReader() throws IOException {
		super("maven.properties");
	}
	
	public static String getMavenProperty(String propName) {
		return getInstance().getProperty(propName);
	}
	
	public static MavenPropertiesReader getInstance() {
		if(INSTANCE == null)
			try {
				INSTANCE= new MavenPropertiesReader();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return INSTANCE;
	}

}
