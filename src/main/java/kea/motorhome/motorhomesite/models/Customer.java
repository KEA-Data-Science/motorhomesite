package kea.motorhome.motorhomesite.models;

import java.util.Objects;

public class Customer {

	private String driversLicence;
	private PayCard payCard;
	private Person person;
	private boolean approved;

	public Customer() {}

	public Customer(String driversLicence, PayCard payCard, Person person, boolean approved) {
		this.driversLicence = driversLicence;
		this.payCard = payCard;
		this.person = person;
		this.approved = approved;
	}

	public String getDriversLicence() {
		return driversLicence;
	}

	public void setDriversLicence(String driversLicence) {
		this.driversLicence = driversLicence;
	}

	public PayCard getPayCard() {
		return payCard;
	}

	public void setPayCard(PayCard payCard) {
		this.payCard = payCard;
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
		return payCard == customer.payCard &&
				approved == customer.approved &&
				Objects.equals(driversLicence, customer.driversLicence) &&
				Objects.equals(person, customer.person);
	}

	@Override
	public int hashCode() {
		return Objects.hash(driversLicence, payCard, person, approved);
	}

	@Override
	public String toString() {
		return "Customer{" +
				"driversLicence='" + driversLicence + '\'' +
				", cardID=" + payCard +
				", person=" + person +
				", approved=" + approved +
				'}';
	}
}