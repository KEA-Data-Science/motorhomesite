package kea.motorhome.motorhomesite.models;

import java.util.Objects;

public class Customer {

	private String driversLicence;
	private int cardID;
	private Person person;
	private boolean approved;

	public Customer() {}

	public Customer(String driversLicence, int cardID, Person person, boolean approved) {
		this.driversLicence = driversLicence;
		this.cardID = cardID;
		this.person = person;
		this.approved = approved;
	}

	public String getDriversLicence() {
		return driversLicence;
	}

	public void setDriversLicence(String driversLicence) {
		this.driversLicence = driversLicence;
	}

	public int getCardID() {
		return cardID;
	}

	public void setCardID(int cardID) {
		this.cardID = cardID;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		return cardID == customer.cardID &&
				approved == customer.approved &&
				Objects.equals(driversLicence, customer.driversLicence) &&
				Objects.equals(person, customer.person);
	}

	@Override
	public int hashCode() {
		return Objects.hash(driversLicence, cardID, person, approved);
	}

	@Override
	public String toString() {
		return "Customer{" +
				"driversLicence='" + driversLicence + '\'' +
				", cardID=" + cardID +
				", person=" + person +
				", approved=" + approved +
				'}';
	}
}