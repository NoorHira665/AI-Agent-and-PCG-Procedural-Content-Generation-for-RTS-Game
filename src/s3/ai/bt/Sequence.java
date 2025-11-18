package s3.ai.bt;

import gatech.mmpm.GameState;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Task {
    private final List<Task> children = new ArrayList<>();
    private int current = 0;


    public Sequence(List<Task> children) {
        this.children.addAll(children);
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        //if we went through all children and none failed, return success
        if (current>= children.size()) {
            current=0;
            return TASK_SUCCESS;
        }

        int result = children.get(current).run(gs, c, actions);

        //case 1: child succeeds, move to next child
        if (result == TASK_SUCCESS){
            current++;
            //if we have gine through all children and none failed, return success
            if (current >= children.size()) {
                current = 0;
                return TASK_SUCCESS;
            }
            return TASK_EXECUTING;
        //case 2: child executing, wait for it
        } else if (result == TASK_EXECUTING){
            return TASK_EXECUTING;
        //case 3: child failed, return failure
        } else {
            current=0;
            return TASK_FAILURE;
        }

    }

}
