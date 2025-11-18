package s3.ai.bt.conditions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WUnit;

import java.util.List;

public class WorkersLessThanEqualTo extends Task {
    private final int workersNum;
    public WorkersLessThanEqualTo(int i) {
        super();
        this.workersNum = i;
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing workers less than condition");
        List<WUnit> peasants = gs.getUnitTypes(c, WPeasant.class);
        if (peasants.size() <= workersNum) {
            return TASK_SUCCESS;
        }
        return TASK_FAILURE;
    }
}
