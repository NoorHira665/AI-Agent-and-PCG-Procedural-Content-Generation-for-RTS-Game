package s3.ai.bt.conditions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WBarracks;
import s3.entities.WPlayer;
import s3.entities.WTownhall;

import java.util.List;

public class BaseIdle extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing base idle condition");
        WTownhall base = (WTownhall) gs.getUnitType(c, WTownhall.class);
        if (base.getStatus() == null){
            return TASK_SUCCESS;
        }
        return TASK_FAILURE;
    }
}
