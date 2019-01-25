package ua.tkushniruk.finalproject.entity;

/**
 * User role type.
 */

public enum Role {
	ADMIN, CLIENT;

	public String getName() {
		return name().toLowerCase();
	}

}
