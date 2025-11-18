package s3.ai.bt.actions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.*;

import java.util.ArrayList;
import java.util.List;
public class AttackEnemy extends Task {

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing attack enemy action");
        Blackboard bb = Blackboard.getInstance();

        //getting enemy ids and idle footmen ids from bb
        List<Integer> enemyIds = new ArrayList<>(bb.getAll("enemy"));
        List<Integer> footmenIds = new ArrayList<>(bb.getAll("idleSoldiers"));

        //if all enemies are already dead
        if (enemyIds.isEmpty()) {
            bb.clearKey("idleSoldiers");
            return TASK_SUCCESS;
        }

        //if our footmen are all dead
        if (footmenIds.isEmpty()) return TASK_FAILURE;

        //assigns all footmen to each enemy attacking
        int i = 0;
        while (i < enemyIds.size()) {
            Integer currTargetId = enemyIds.get(i);
            for (int f : footmenIds) {
                actions.add(new S3Action(f, S3Action.ACTION_ATTACK, currTargetId));
            }
            i++;
        }
        return TASK_SUCCESS;
    }
}
