package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.CarModel;


import java.util.ArrayList;
import java.util.List;

public class CarModelDAODemo implements IDAO<CarModel, Integer> {


    List<CarModel> carModels;

    public CarModelDAODemo()
    {
        carModels = new ArrayList<>();
    }
    @Override
    public boolean create(CarModel thing) {
        return carModels.add(thing);
    }

    @Override
    public List<CarModel> readall()
    {
        return carModels;
    }

    public CarModel read(Integer id)
    {
        for (CarModel carModel : carModels)
            if (carModel.getCarModelID() == id)
                return carModel;
        return null;
    }

    @Override
    public boolean update(CarModel carModel)
    {
        for (int i = 0; i < carModels.size(); i++)
        {
            if (carModels.get(i).getCarModelID() == carModel.getCarModelID()) {
                carModels.set(i, carModel);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        for (CarModel carModel : carModels)
        {
            if (id == carModel.getCarModelID())
                return carModels.remove(carModel);
        }
        return false;
    }
}
