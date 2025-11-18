package s3.ai.bt.conditions;

import gatech.mmpm.GameState;
import s3.ai.bt.Task;
import s3.base.S3Action;
import s3.entities.WBarracks;
import s3.ai.AI;
import s3.base.S3;
import s3.entities.WPlayer;
import s3.entities.WUnit;

import java.util.List;

public class BarracksExists extends Task {
    private final String value;
    public BarracksExists(String value) {
        super();
        this.value = value;
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing barracks exist condition");
        WUnit barracks = gs.getUnitType(c, WBarracks.class);
        //if barrack exists and checking value was true
        if (barracks != null && value.equalsIgnoreCase("true")) {
            return TASK_SUCCESS;
        //if barrack does not exist and checking value was false
        } else if (barracks == null && value.equalsIgnoreCase("false")) {
            return TASK_SUCCESS;
        } else {
            return TASK_FAILURE;
        }
    }
}
