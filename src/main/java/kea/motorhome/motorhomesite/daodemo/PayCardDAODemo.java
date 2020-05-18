package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.PayCard;

import java.util.ArrayList;
import java.util.List;

public class PayCardDAODemo implements IDAO<PayCard, Integer>
{
    ArrayList<PayCard> payCards;

    public PayCardDAODemo()
    {
        payCards = new ArrayList<>();
    }

    /**
     * @param thing
     */
    @Override
    public boolean create(PayCard thing)
    {
        return payCards.add(thing);
    }

    /**
     * @param id
     */
    @Override
    public PayCard read(Integer id)
    {
        for(PayCard card : payCards)
        {
            if(card.getCardID() == id){return card;}
        }

        return null;
    }

    @Override
    public List<PayCard> readall()
    {
        return payCards;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(PayCard thing)
    {
        for(PayCard card : payCards)
        {
            if(thing.getCardID() == card.getCardID())
            {
                card = thing;
                return true;
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
        for(int i = 0; i<payCards.size();i++)
        {
            if(id == payCards.get(i).getCardID())
            {
                payCards.remove(i);
                return true;
            }
        }

        return false;
    }
}
