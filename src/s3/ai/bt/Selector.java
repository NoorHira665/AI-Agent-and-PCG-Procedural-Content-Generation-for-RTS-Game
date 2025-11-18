package s3.ai.bt;

import gatech.mmpm.GameState;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;

import java.util.ArrayList;
import java.util.List;

public class Selector extends Task{
    private int current = 0;
    private final List<Task> children = new ArrayList<>();

    public Selector(List<Task> children) {
        this.children.addAll(children);
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        //If we went through all children and none succeeded, return failure
        if(current>= children.size()){
            current=0;
            return TASK_FAILURE;
        }

        int result = children.get(current).run(gs, c, actions);

        //case 1: child succeeds, return success
        if (result == TASK_SUCCESS) {
            current=0;
            return TASK_SUCCESS;
        //case 2: child executing, wait for it
        } else if (result == TASK_EXECUTING) {
            return TASK_EXECUTING;
        //case 3: child failed, move to next one
        } else {
            current++;
            //if we have gone through all children, return failure
            if (current >= children.size()) {
                current = 0;
                return TASK_FAILURE;
            }

            return TASK_EXECUTING;

        }
    }
}
