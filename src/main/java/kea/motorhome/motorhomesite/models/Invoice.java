package kea.motorhome.motorhomesite.models;

import java.util.List;

import java.util.Objects;

public class Invoice {

	private int invoiceID;
	private String customerID;
	private Period billPeriod;
	private List<Service> services;

	public Invoice() {}

	public Invoice(int invoiceID, String customerID, Period billPeriod, List<Service> services) {
		this.invoiceID = invoiceID;
		this.customerID = customerID;
		this.billPeriod = billPeriod;
		this.services = services;
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

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Invoice invoice = (Invoice) o;
		return invoiceID == invoice.invoiceID &&
				customerID == invoice.customerID &&
				Objects.equals(billPeriod, invoice.billPeriod) &&
				Objects.equals(services, invoice.services);
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoiceID, customerID, billPeriod, services);
	}

	@Override
	public String toString() {
		return "Invoice{" +
				"invoiceID=" + invoiceID +
				", customerID=" + customerID +
				", billPeriod=" + billPeriod +
				", services=" + services +
				'}';
	}
}