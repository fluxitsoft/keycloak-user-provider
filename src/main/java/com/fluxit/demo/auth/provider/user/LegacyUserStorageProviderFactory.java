package com.fluxit.demo.auth.provider.user;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LegacyUserStorageProviderFactory implements UserStorageProviderFactory<LegacyUserStorageProvider> {

	private static final String LEGACY_USER_PROVIDER = "legacy-user-provider";

	private static final Logger log = LoggerFactory.getLogger(LegacyUserStorageProviderFactory.class);
	
	protected List<ProviderConfigProperty> configMetadata;
	
	protected Properties properties = new Properties();

	@Override
	public void init(Config.Scope config) {
		InputStream is = getClass().getClassLoader().getResourceAsStream("/legacy.properties");

		if (is == null) {
			log.warn("Could not find legacy.properties in classpath");
		} else {
			try {
				properties.load(is);
				
				ProviderConfigurationBuilder builder = ProviderConfigurationBuilder.create();
						
				builder.property("config.key.jdbc.driver", "JDBC Driver Class",
						"JDBC Driver Class", ProviderConfigProperty.STRING_TYPE,
						properties.get("config.key.jdbc.driver"), null);
				
				builder.property("config.key.jdbc.url", "JDBC Url",
						"JDBC Url", ProviderConfigProperty.STRING_TYPE,
						properties.get("config.key.jdbc.url"), null);
				
				builder.property("config.key.db.username", "DB Username",
						"DB Username", ProviderConfigProperty.STRING_TYPE,
						properties.get("config.key.db.username"), null);
				
				builder.property("config.key.db.password", "DB password",
						"DB Password", ProviderConfigProperty.STRING_TYPE,
						properties.get("config.key.db.password"), null);
				
				builder.property("config.key.validation.query", "Validation Query",
						"Validation Query", ProviderConfigProperty.STRING_TYPE,
						properties.get("config.key.validation.query"), null);
				
				configMetadata = builder.build();
				
			} catch (IOException ex) {
				log.error("Failed to load legacy.properties file", ex);
			}
		}
	}
	 
	 
	@Override
	public LegacyUserStorageProvider create(KeycloakSession session, ComponentModel model) {
		log.info("creating new LegacyUserStorageProvider");
		return new LegacyUserStorageProvider(session, model);
	}

	@Override
	public String getId() {
		return LEGACY_USER_PROVIDER;
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return configMetadata;
	}

	@Override
	public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config)
			throws ComponentValidationException {

		try (Connection c = LegacyDBConnection.getConnection(config)) {
			log.info("Testing connection");
			c.createStatement().execute(config.get("config.key.validation.query"));
			log.info("Connection OK");
		} catch (Exception ex) {
			log.warn("Unable to validate connection: ex={}", ex.getMessage());
			throw new ComponentValidationException("Unable to validate database connection", ex);
		}
	}

	@Override
	public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
		log.info("onUpdate()");
	}

	@Override
	public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
		log.info("onCreate()");
	}

}
