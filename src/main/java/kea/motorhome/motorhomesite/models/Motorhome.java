package kea.motorhome.motorhomesite.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Motorhome {

	private int motorhomeID;
	private CarModel model;
	private String licensePlate;
	private float[] seasonalDailyCharge;
	private String notes;
	private String imageURL;
	private List<Service> serviceAvailable;
	private int catalogueId;
	private String modelName;
	private int productionYear;
	private String description;
	private float perDayPrice;
	private int minimumDaysOfRental;
	private String fuelType = "Oil";

	public Motorhome() {}

	public Motorhome(int catalogueId, CarModel carModel, int productionYear, String description,
						   float perDayPrice, int minimumDaysOfRental, String imageURL)
	{
		this.catalogueId = catalogueId;
		this.model = carModel;
		this.productionYear = productionYear;
		this.description = description;
		this.perDayPrice = perDayPrice;
		this.minimumDaysOfRental = minimumDaysOfRental;
		this.imageURL = imageURL;
	}

	public Motorhome(int motorhomeID, CarModel model, String licensePlate, float[] seasonalDailyCharge, String notes, String imageURL, List<Service> serviceAvailable) {
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

	public CarModel getModel() {
		return model;
	}

	public void setModel(CarModel model) {
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

	public int getCatalogueId() {
		return catalogueId;
	}

	public void setCatalogueId(int catalogueId) {
		this.catalogueId = catalogueId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public int getProductionYear() {
		return productionYear;
	}

	public void setProductionYear(int productionYear) {
		this.productionYear = productionYear;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPerDayPrice() {
		return perDayPrice;
	}

	public void setPerDayPrice(float perDayPrice) {
		this.perDayPrice = perDayPrice;
	}

	public int getMinimumDaysOfRental() {
		return minimumDaysOfRental;
	}

	public void setMinimumDaysOfRental(int minimumDaysOfRental) {
		this.minimumDaysOfRental = minimumDaysOfRental;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
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