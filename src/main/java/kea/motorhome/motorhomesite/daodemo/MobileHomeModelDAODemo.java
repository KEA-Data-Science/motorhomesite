package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.MobileHomeModel;
import kea.motorhome.motorhomesite.models.Service;

import java.util.ArrayList;
import java.util.List;
// demo klasse
public class MobileHomeModelDAODemo implements IDAO<MobileHomeModel, Integer> {

    List<MobileHomeModel> mobilehomeModels;

    public MobileHomeModelDAODemo()
    {
        mobilehomeModels = new ArrayList<>();
    }

    @Override
    public boolean create(MobileHomeModel thing) {

        {
            return mobilehomeModels.add(thing);
        }
    }

    @Override
    public MobileHomeModel read(Integer id)
    {
        for(int i = 0; i < mobilehomeModels.size(); i++)
        {
            if(mobilehomeModels.get(i).getCatalogueId() == id)
            {
                return mobilehomeModels.get(i);
            }
        }

        return null;
    }

    @Override
    public List<MobileHomeModel> readall()
    {
        return mobilehomeModels;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(MobileHomeModel thing)
    {
        for(int i = 0; i < mobilehomeModels.size(); i++)
        {
            if(thing.getCatalogueId() == mobilehomeModels.get(i).getCatalogueId())
            {
                mobilehomeModels.set(i,thing);
            }
        }

        return false;
    }

    /**
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        for(int i = 0; i < mobilehomeModels.size(); i++)
        {
            if(id == mobilehomeModels.get(i).getCatalogueId())
            {
                mobilehomeModels.remove(i);
                return true;
            }
        }

        return false;
    }

//    public void addMobilehomeModels()
//    {
//        MobileHomeModel model1 = new MobileHomeModel(1, "SuperCamper", 2002, "Big comfortable camper", 200, 10, "/img");
//        MobileHomeModel model2 = new MobileHomeModel(2, "BigTraveler", 2016, "With full toilet, oven and flatscreen + 4G internet", 500, 15, "/img");
//        MobileHomeModel model3 = new MobileHomeModel(3, "EcoHome", 1996, "Comes with everything you need", 90, 8, "/img");
//        mobilehomeModels.add(model1);
//        mobilehomeModels.add(model2);
//        mobilehomeModels.add(model3);
//
//
//    }
}
