package s3.ai.bt.conditions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WBarracks;
import s3.entities.WPlayer;

import java.util.List;

public class BarracksIdle extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing barracks idle condition");
        WBarracks barrack = (WBarracks) gs.getUnitType(c, WBarracks.class);
        if (barrack.getStatus() == null){
            return TASK_SUCCESS;
        }
        return TASK_FAILURE;
    }
}
