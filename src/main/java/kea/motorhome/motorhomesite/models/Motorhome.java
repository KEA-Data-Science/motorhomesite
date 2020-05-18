package kea.motorhome.motorhomesite.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Motorhome {

	private int motorhomeID;
	private String model;
	private String licensePlate;
	private float[] seasonalDailyCharge;
	private String notes;
	private String imageURL;
	private List<Service> serviceAvailable;

	public Motorhome() {}

	public Motorhome(int motorhomeID, String model, String licensePlate, float[] seasonalDailyCharge, String notes, String imageURL, List<Service> serviceAvailable) {
		this.motorhomeID = motorhomeID;
		this.model = model;
		this.licensePlate = licensePlate;
		this.seasonalDailyCharge = seasonalDailyCharge;
		this.notes = notes;
		this.imageURL = imageURL;
		this.serviceAvailable = serviceAvailable;
	}

	public int getMotorhomeID() {
		return motorhomeID;
	}

	public void setMotorhomeID(int motorhomeID) {
		this.motorhomeID = motorhomeID;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public float[] getSeasonalDailyCharge() {
		return seasonalDailyCharge;
	}

	public void setSeasonalDailyCharge(float[] seasonalDailyCharge) {
		this.seasonalDailyCharge = seasonalDailyCharge;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public List<Service> getServiceAvailable() {
		return serviceAvailable;
	}

	public void setServiceAvailable(List<Service> serviceAvailable) {
		this.serviceAvailable = serviceAvailable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Motorhome motorhome = (Motorhome) o;
		return motorhomeID == motorhome.motorhomeID &&
				Objects.equals(model, motorhome.model) &&
				Objects.equals(licensePlate, motorhome.licensePlate) &&
				Arrays.equals(seasonalDailyCharge, motorhome.seasonalDailyCharge) &&
				Objects.equals(notes, motorhome.notes) &&
				Objects.equals(imageURL, motorhome.imageURL) &&
				Objects.equals(serviceAvailable, motorhome.serviceAvailable);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(motorhomeID, model, licensePlate, notes, imageURL, serviceAvailable);
		result = 31 * result + Arrays.hashCode(seasonalDailyCharge);
		return result;
	}

	@Override
	public String toString() {
		return "Motorhome{" +
				"motorhomeID=" + motorhomeID +
				", model='" + model + '\'' +
				", licensePlate='" + licensePlate + '\'' +
				", seasonalDailyCharge=" + Arrays.toString(seasonalDailyCharge) +
				", notes='" + notes + '\'' +
				", imageURL='" + imageURL + '\'' +
				", serviceAvailable=" + serviceAvailable +
				'}';
	}
}