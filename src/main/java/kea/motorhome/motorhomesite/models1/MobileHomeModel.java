package kea.motorhome.motorhomesite.models1;

public class MobileHomeModel
{
    private int catalogueId;
    private String modelName;
    private int productionYear;
    private String description;
    private float perDayPrice;
    private int minimumDaysOfRental;

    private String fuelType = "Oil";
    private String imageURL;

    public MobileHomeModel(int catalogueId, String modelName, int productionYear, String description,
                           float perDayPrice, int minimumDaysOfRental, String imageURL)
    {
        this.catalogueId = catalogueId;
        this.modelName = modelName;
        this.productionYear = productionYear;
        this.description = description;
        this.perDayPrice = perDayPrice;
        this.minimumDaysOfRental = minimumDaysOfRental;
        this.imageURL = imageURL;
    }

    public MobileHomeModel(){ }

    @Override
    public String toString()
    {
        return "MobileHomeModel{" +
               "catalogueId=" + catalogueId +
               ", modelName='" + modelName + '\'' +
               ", productionYear=" + productionYear +
               ", description='" + description + '\'' +
               ", perDayPrice=" + perDayPrice +
               ", minimumDaysOfRental=" + minimumDaysOfRental +
               ", imageURL='" + imageURL + '\'' +
               '}';
    }

    public int getCatalogueId(){ return catalogueId; }

    public void setCatalogueId(int catalogueId){ this.catalogueId = catalogueId; }

    public String getModelName(){ return modelName; }

    public void setModelName(String modelName){ this.modelName = modelName; }

    public int getProductionYear(){ return productionYear; }

    public void setProductionYear(int productionYear){ this.productionYear = productionYear; }

    public String getDescription(){ return description; }

    public void setDescription(String description){ this.description = description; }

    public float getPerDayPrice(){ return perDayPrice; }

    public void setPerDayPrice(float perDayPrice){ this.perDayPrice = perDayPrice; }

    public int getMinimumDaysOfRental(){ return minimumDaysOfRental; }

    public void setMinimumDaysOfRental(int minimumDaysOfRental)
    {
        this.minimumDaysOfRental =
                minimumDaysOfRental;
    }

    public String getImageURL(){ return imageURL; }

    public void setImageURL(String imageURL){ this.imageURL = imageURL; }

    public String getFuelType(){ return fuelType; }

    public void setFuelType(String fuelType){ this.fuelType = fuelType; }
}
