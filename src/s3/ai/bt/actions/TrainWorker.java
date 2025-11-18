package s3.ai.bt.actions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.*;

import java.util.List;

public class TrainWorker extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing train worker action");
        WTownhall base = (WTownhall) gs.getUnitType(c, WTownhall.class);
        actions.add(new S3Action(base.entityID,S3Action.ACTION_TRAIN, WPeasant.class.getSimpleName()));
        return TASK_SUCCESS;
    }
    }
