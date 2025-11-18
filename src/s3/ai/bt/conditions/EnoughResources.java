package s3.ai.bt.conditions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.*;

import java.util.List;
public class EnoughResources extends Task {
    private final String value;
    public EnoughResources(String value) {
        super();
        this.value = value;
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing enough resources condition");
        int goldCost;
        int woodCost;
        //checking resources enough condition based on what we need it for
        if(value.equalsIgnoreCase("barracks")) {
            WBarracks barrack = new WBarracks();
            goldCost = barrack.getCost_gold();
            woodCost = barrack.getCost_wood();
        } else if (value.equalsIgnoreCase("base")){
            WTownhall base = new WTownhall();
            goldCost = base.getCost_gold();
            woodCost = base.getCost_wood();
        } else if (value.equalsIgnoreCase("footman")){
            WFootman footman = new WFootman();
            goldCost = footman.getCost_gold();
            woodCost = footman.getCost_wood();
        } else if (value.equalsIgnoreCase("peasant")){
            WPeasant peasant = new WPeasant();
            goldCost = peasant.getCost_gold();
            woodCost = peasant.getCost_wood();
        } else {
            woodCost = 500; //default values
            goldCost = 500;
        }
        if (c.getGold() >= goldCost && c.getWood() >= woodCost){
            return TASK_SUCCESS;
        } else {
            return TASK_FAILURE;
        }
    }
}
