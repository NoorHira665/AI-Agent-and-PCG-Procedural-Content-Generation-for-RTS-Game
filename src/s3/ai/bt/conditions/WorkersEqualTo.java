package s3.ai.bt.conditions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WUnit;

import java.util.List;

public class WorkersEqualTo extends Task {
    private final int workersNum;

    public WorkersEqualTo(int workersNum) {
        this.workersNum = workersNum;
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        List<WUnit> peasants = gs.getUnitTypes(c, WPeasant.class);
        if (peasants.size() == workersNum) {
            return TASK_SUCCESS;
        }
        return TASK_FAILURE;
    }
}
