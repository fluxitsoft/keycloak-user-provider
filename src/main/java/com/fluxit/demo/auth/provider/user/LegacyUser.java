package com.fluxit.demo.auth.provider.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

public class LegacyUser extends AbstractUserAdapter {

	private final String username;
	private final String email;
	private final String firstName;
	private final String lastName;
	private final Date birthDate;

	private LegacyUser(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, String username,
			String email, String firstName, String lastName, Date birthDate) {
		super(session, realm, storageProviderModel);
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;

	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getEmail() {
		return email;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
		attributes.add(UserModel.USERNAME, getUsername());
		attributes.add(UserModel.EMAIL, getEmail());
		attributes.add(UserModel.FIRST_NAME, getFirstName());
		attributes.add(UserModel.LAST_NAME, getLastName());
		attributes.add("birthDate", getBirthDate().toString());
		return attributes;
	}

	static class Builder {
		private final KeycloakSession session;
		private final RealmModel realm;
		private final ComponentModel storageProviderModel;
		private String username;
		private String email;
		private String firstName;
		private String lastName;
		private Date birthDate;

		Builder(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, String username) {
			this.session = session;
			this.realm = realm;
			this.storageProviderModel = storageProviderModel;
			this.username = username;
		}

		LegacyUser.Builder email(String email) {
			this.email = email;
			return this;
		}

		LegacyUser.Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		LegacyUser.Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		LegacyUser.Builder birthDate(Date birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		LegacyUser build() {
			return new LegacyUser(session, realm, storageProviderModel, username, email, firstName, lastName,
					birthDate);

		}
	}
}
