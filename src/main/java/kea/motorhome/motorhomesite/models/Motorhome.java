package kea.motorhome.motorhomesite.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Motorhome
{

    private int motorhomeID;
    private CarModel model;
    private String licensePlate;
    private float seasonalDailyCharge;
    private String notes;
    private String imageURL;
    private List<Service> servicesAvailable;
    private int productionYear;
    private String description;
    private int minimumDaysOfRental;
    private String fuelType = "Oil";

    public Motorhome(){}

    public Motorhome(int motorhomeID, CarModel model, String licensePlate, float seasonalDailyCharge, String notes, String imageURL, List<Service> servicesAvailable, int productionYear, String description, int minimumDaysOfRental, String fuelType)
    {
        this.motorhomeID = motorhomeID;
        this.model = model;
        this.licensePlate = licensePlate;
        this.seasonalDailyCharge = seasonalDailyCharge;
        this.notes = notes;
        this.imageURL = imageURL;
        this.servicesAvailable = servicesAvailable;
        this.productionYear = productionYear;
        this.description = description;
        this.minimumDaysOfRental = minimumDaysOfRental;
        this.fuelType = fuelType;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(!(o instanceof Motorhome)) return false;
        Motorhome motorhome = (Motorhome)o;
        return getMotorhomeID() == motorhome.getMotorhomeID() &&
               getProductionYear() == motorhome.getProductionYear() &&
               getMinimumDaysOfRental() == motorhome.getMinimumDaysOfRental() &&
               Objects.equals(getModel(), motorhome.getModel()) &&
               Objects.equals(getLicensePlate(), motorhome.getLicensePlate()) &&
               Arrays.equals(getSeasonalDailyCharge(), motorhome.getSeasonalDailyCharge()) &&
               Objects.equals(getNotes(), motorhome.getNotes()) &&
               Objects.equals(getImageURL(), motorhome.getImageURL()) &&
               Objects.equals(getServicesAvailable(), motorhome.getServicesAvailable()) &&
               Objects.equals(getDescription(), motorhome.getDescription()) &&
               Objects.equals(getFuelType(), motorhome.getFuelType());
    }

    public int getMotorhomeID()
    {
        return motorhomeID;
    }

    public void setMotorhomeID(int motorhomeID)
    {
        this.motorhomeID = motorhomeID;
    }

    public int getProductionYear()
    {
        return productionYear;
    }

    public void setProductionYear(int productionYear)
    {
        this.productionYear = productionYear;
    }

    public int getMinimumDaysOfRental()
    {
        return minimumDaysOfRental;
    }

    public CarModel getModel()
    {
        return model;
    }

    public void setModel(CarModel model)
    {
        this.model = model;
    }

    public String getLicensePlate()
    {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }

    public float[] getSeasonalDailyCharge()
    {
        float[] seasonalCharges = new float[]{
                seasonalDailyCharge,
                seasonalDailyCharge*1.3f,
                seasonalDailyCharge*1.6f};
        return seasonalCharges;
    }

    public float getSeasonDailyChargeLowSeason(){return seasonalDailyCharge;}

    public void setSeasonalDailyCharge(float[] seasonalDailyCharge)
    {
        this.seasonalDailyCharge = seasonalDailyCharge[0]; // low season
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public List<Service> getServicesAvailable()
    {
        return servicesAvailable;
    }

    public void setServicesAvailable(List<Service> servicesAvailable)
    {
        this.servicesAvailable = servicesAvailable;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getFuelType()
    {
        return fuelType;
    }

    public void setFuelType(String fuelType)
    {
        this.fuelType = fuelType;
    }

    public void setMinimumDaysOfRental(int minimumDaysOfRental)
    {
        this.minimumDaysOfRental = minimumDaysOfRental;
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hash(getMotorhomeID(), getModel(), getLicensePlate(), getNotes(), getImageURL(), getServicesAvailable(), getProductionYear(), getDescription(), getMinimumDaysOfRental(), getFuelType());
        result = 31 * result + Arrays.hashCode(getSeasonalDailyCharge());
        return result;
    }

    @Override
    public String toString()
    {
        return "Motorhome{" +
               "motorhomeID=" + motorhomeID +
               ", model=" + model +
               ", licensePlate='" + licensePlate + '\'' +
               ", seasonalDailyCharge=" + seasonalDailyCharge +
               ", notes='" + notes + '\'' +
               ", imageURL='" + imageURL + '\'' +
               ", servicesAvailable=" + servicesAvailable +
               ", productionYear=" + productionYear +
               ", description='" + description + '\'' +
               ", minimumDaysOfRental=" + minimumDaysOfRental +
               ", fuelType='" + fuelType + '\'' +
               '}';
    }
}