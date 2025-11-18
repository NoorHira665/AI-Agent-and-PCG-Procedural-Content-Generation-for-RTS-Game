package s3.ai.bt.conditions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;
import s3.entities.WTownhall;
import s3.entities.WUnit;

import java.util.List;

public class BaseExists extends Task {
    private final String value;
    public BaseExists(String value) {
        this.value=value;
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing base exist condition");
        WUnit base = gs.getUnitType(c, WTownhall.class);

        //if base exists and checking value is true
        if (base != null && value.equalsIgnoreCase("true")) {
            return TASK_SUCCESS;
        //if base doesn't exist and checking value is false
        } else if (base == null && value.equalsIgnoreCase("false")) {
            return TASK_SUCCESS;
        } else {
            return TASK_FAILURE;
        }
    }
}
