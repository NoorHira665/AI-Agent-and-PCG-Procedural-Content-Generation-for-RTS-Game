package s3.ai.bt;

import gatech.mmpm.GameState;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;

import java.util.List;

public abstract class Task {
    public static final int TASK_SUCCESS =1;
    public static final int TASK_FAILURE =0;
    public static final int TASK_EXECUTING =2;

    public abstract int run(S3 gs, WPlayer c, List<S3Action> actions);


}
