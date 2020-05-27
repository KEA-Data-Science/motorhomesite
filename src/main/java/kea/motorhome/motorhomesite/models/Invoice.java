package kea.motorhome.motorhomesite.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import java.util.Objects;

public class Invoice {

	private int invoiceID;
	private String customerID;
	private Period billPeriod;
	private Motorhome motorhome;
	private List<Service> services;
	private boolean isCompleted;
	private Period reservationPeriod;

	public Invoice() {}

	public Invoice(int invoiceID, String customerID, Period billPeriod, Motorhome motorhome, List<Service> services, boolean isCompleted, Period reservationPeriod)
	{
		this.invoiceID = invoiceID;
		this.customerID = customerID;
		this.billPeriod = billPeriod;
		this.motorhome = motorhome;
		this.services = services;
		this.isCompleted = isCompleted;
		this.reservationPeriod = reservationPeriod;
	}

	public int getInvoiceID() {
		return invoiceID;
	}

	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public Period getBillPeriod() {
		return billPeriod;
	}

	public void setBillPeriod(Period billPeriod) {
		this.billPeriod = billPeriod;
	}

	public Motorhome getMotorhome() {
		return motorhome;
	}

	public void setMotorhome(Motorhome motorhome) {
		this.motorhome = motorhome;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public Period getReservationPeriod() {
		return reservationPeriod;
	}

	public void setReservationPeriod(Period reservationPeriod) {
		this.reservationPeriod = reservationPeriod;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Invoice invoice = (Invoice) o;
		return invoiceID == invoice.invoiceID &&
				isCompleted == invoice.isCompleted &&
				Objects.equals(customerID, invoice.customerID) &&
				Objects.equals(billPeriod, invoice.billPeriod) &&
				Objects.equals(motorhome, invoice.motorhome) &&
				Objects.equals(services, invoice.services) &&
				Objects.equals(reservationPeriod, invoice.reservationPeriod);
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoiceID, customerID, billPeriod, motorhome, services, isCompleted, reservationPeriod);
	}

	@Override
	public String toString() {
		return "Invoice{" +
				"invoiceID=" + invoiceID +
				", customerID='" + customerID + '\'' +
				", billPeriod=" + billPeriod +
				", motorhome=" + motorhome +
				", services=" + services +
				", isCompleted=" + isCompleted +
				", reservationPeriod=" + reservationPeriod +
				'}';
	}
}