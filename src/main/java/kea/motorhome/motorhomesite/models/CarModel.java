package kea.motorhome.motorhomesite.models;

public class CarModel
{
    String modelName;
    String modelnumber;
    int horsePower;
    int beds;

    // jeg synes flere felter :D + kan nok være kreativ selv ;)

    public CarModel(String modelName, String modelnumber, int horsePower, int beds) {
        this.modelName = modelName;
        this.modelnumber = modelnumber;
        this.horsePower = horsePower;
        this.beds = beds;
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
