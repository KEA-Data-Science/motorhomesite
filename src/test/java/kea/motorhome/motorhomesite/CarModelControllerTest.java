/** LNS **/
/** Failed attempt at controller testing **/

package kea.motorhome.motorhomesite;

import kea.motorhome.motorhomesite.controllers.CarModelController;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.CarModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CarModelController.class)
public class CarModelControllerTest {

    @MockBean
    private SiteDAOCollection dao;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void carModelsPage() throws Exception{
        List<CarModel> carModels = new ArrayList<>();
        CarModel carModel = new CarModel();
        carModel.setCarModelID(1);
        carModel.setBeds(100);
        carModel.setColdWaterCapacity(10);
        carModel.setCruiseControl(true);
        carModel.setEngineCapacity(4);
        carModel.setHeight(40);
        carModel.setHorsePower(400);
        carModel.setHotWaterCapacity(2);
        carModel.setLength(20);
        carModel.setModelName("Car Car");
        carModel.setModelnumber("fdvjsdk");
        carModel.setNumberOfSeats(9);
        carModel.setOven(true);
        carModel.setShower(false);
        carModel.setWeight(90);
        carModel.setWidth(900);
        carModels.add(carModel);
        given(dao.carModelDAO().readall()).willReturn(carModels);

        this.mockMvc.perform(get("/carModels/carModels/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("fdvjsdk")));
    }
}
// Ressources:
// https://www.linkedin.com/learning/learning-spring-with-spring-boot-2/test-a-controller-mockmvc?u=36836804