package kea.motorhome.motorhomesite.models;

public class CarModel
{
    String modelName;
    String modelnumber;
    int horsePower;
    int beds;
    float engineCapacity;
    float length;
    float width;
    float height;
    float weight;
    float hotWaterCapacity;
    float coldWaterCapacity;
    int numberOfSeats;
    boolean oven;
    boolean cruiseControl;
    boolean shower;


    public CarModel(String modelName, String modelnumber, int horsePower, int beds, float engineCapacity, float length, float width,
                    float height, float weight, float hotWaterCapacity, float coldWaterCapacity, int numberOfSeats, boolean oven, boolean cruiseControl, boolean shower) {
        this.modelName = modelName;
        this.modelnumber = modelnumber;
        this.horsePower = horsePower;
        this.engineCapacity = engineCapacity;
        this.beds = beds;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.hotWaterCapacity = hotWaterCapacity;
        this.coldWaterCapacity = coldWaterCapacity;
        this.numberOfSeats = numberOfSeats;
        this.oven = oven;
        this.cruiseControl = cruiseControl;
        this.shower = shower;
    }

    public float getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(float engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHotWaterCapacity() {
        return hotWaterCapacity;
    }

    public void setHotWaterCapacity(float hotWaterCapacity) {
        this.hotWaterCapacity = hotWaterCapacity;
    }

    public float getColdWaterCapacity() {
        return coldWaterCapacity;
    }

    public void setColdWaterCapacity(float coldWaterCapacity) {
        this.coldWaterCapacity = coldWaterCapacity;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public boolean isOven() {
        return oven;
    }

    public void setOven(boolean oven) {
        this.oven = oven;
    }

    public boolean isCruiseControl() {
        return cruiseControl;
    }

    public void setCruiseControl(boolean cruiseControl) {
        this.cruiseControl = cruiseControl;
    }

    public boolean isShower() {
        return shower;
    }

    public void setShower(boolean shower) {
        this.shower = shower;
    }

    public String getModelnumber() {
        return modelnumber;
    }

    public void setModelnumber(String modelnumber) {
        this.modelnumber = modelnumber;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
