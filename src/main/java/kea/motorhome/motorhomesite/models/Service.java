package kea.motorhome.motorhomesite.models;

import java.util.Objects;

public class Service {

	private int serviceID;

	private String description;
	private float unitPrice;

	public Service()
	{
	}

	public Service(int serviceID, String description, float unitPrice)
	{
		this.serviceID = serviceID;
		this.description = description;
		this.unitPrice = unitPrice;
	}

	public int getServiceID()
	{
		return serviceID;
	}

	public void setServiceID(int serviceID)
	{
		this.serviceID = serviceID;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public float getUnitPrice()
	{
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice)
	{
		this.unitPrice = unitPrice;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Service)) return false;
		Service service = (Service)o;
		return getServiceID() == service.getServiceID() &&
			   Float.compare(service.getUnitPrice(), getUnitPrice()) == 0 &&
			   Objects.equals(getDescription(), service.getDescription());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getServiceID(), getDescription(), getUnitPrice());
	}

	@Override
	public String toString()
	{
		return "Service{" +
			   "serviceID=" + serviceID +
			   ", description='" + description + '\'' +
			   ", unitPrice=" + unitPrice +
			   '}';
	}
}