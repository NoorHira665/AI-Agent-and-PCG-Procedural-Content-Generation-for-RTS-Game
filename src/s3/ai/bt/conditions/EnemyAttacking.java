package s3.ai.bt.conditions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;
import s3.entities.WUnit;

import java.util.List;

public class EnemyAttacking extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing enemy attacking condition");
        //clearing blackboard at "enemy" key at start for clean slate
        Blackboard bb = Blackboard.getInstance();
        bb.clearKey("enemy");

        WPlayer enemy = null;
        for (WPlayer p: gs.getPlayers()){
            if (p != null && p != c) {
                enemy = p;
                break;
            }
        }
        if(enemy==null) return TASK_FAILURE; //if no enemy exists, task fails

        //gathering enemy unit ids that are in attack status
        for (WUnit unit: gs.getUnits()){
            if (enemy.getOwner().equals(unit.getOwner())){
                if (unit.getStatus() != null && unit.getStatus().m_action == S3Action.ACTION_ATTACK){
                    bb.put("enemy", unit.getEntityID());
                    return TASK_SUCCESS;
                }
            }
        }
        return TASK_FAILURE;
    }
}
