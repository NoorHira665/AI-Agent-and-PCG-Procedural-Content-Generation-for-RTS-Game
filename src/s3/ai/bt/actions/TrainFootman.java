package s3.ai.bt.actions;

import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WBarracks;
import s3.entities.WFootman;
import s3.entities.WPlayer;

import java.util.List;

public class TrainFootman extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing train footman action");
        WBarracks barracks = (WBarracks) gs.getUnitType(c, WBarracks.class);
        actions.add(new S3Action(barracks.entityID,S3Action.ACTION_TRAIN, WFootman.class.getSimpleName()));
        return TASK_SUCCESS;
    }
}
