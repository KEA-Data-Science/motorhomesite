package kea.motorhome.motorhomesite.models;
// kcn
import java.util.Objects;

public class Address {

	private int addressID;
	private String country;
	private String roadName;
	private String houseNumber;
	private String postCode;

	public Address() {}

	public Address(int addressID, String country, String roadName, String houseNumber, String postCode)
	{
		this.addressID = addressID;
		this.country = country;
		this.roadName = roadName;
		this.houseNumber = houseNumber;
		this.postCode = postCode;
	}

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return addressID == address.addressID &&
				Objects.equals(country, address.country) &&
				Objects.equals(roadName, address.roadName) &&
				Objects.equals(houseNumber, address.houseNumber) &&
				Objects.equals(postCode, address.postCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(addressID, country, roadName, houseNumber, postCode);
	}

	@Override
	public String toString() {
		return "Address{" +
				"addressID=" + addressID +
				", country='" + country + '\'' +
				", roadName='" + roadName + '\'' +
				", houseNumber='" + houseNumber + '\'' +
				", postCode='" + postCode + '\'' +
				'}';
	}
}