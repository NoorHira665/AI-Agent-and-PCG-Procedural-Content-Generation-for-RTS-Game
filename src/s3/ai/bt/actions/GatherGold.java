package s3.ai.bt.actions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WGoldMine;
import s3.entities.WPlayer;
import s3.entities.WUnit;

import java.util.ArrayList;
import java.util.List;

public class GatherGold extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing gather gold action");

        //getting an idle peasant from bb
        Blackboard bb = Blackboard.getInstance();
        Integer peasantId = bb.getAll("idlePeasants").getFirst();
        WUnit peasant = gs.getUnit(peasantId);

        //finding nearest mine, code snippet from RushAI2
        List<WUnit> mines = gs.getUnitTypes(null, WGoldMine.class);
        WGoldMine mine = null;
        int leastDist = 9999;
        for (WUnit unit : mines) {
            int dist = Math.abs(unit.getX() - peasant.getX())
                    + Math.abs(unit.getY() - peasant.getY());
            if (dist < leastDist) {
                leastDist = dist;
                mine = (WGoldMine) unit;
            }
        }

        //send peasant to gather gold if mine available
        if (null != mine) {
            actions.add(new S3Action(peasant.entityID, S3Action.ACTION_HARVEST, mine.entityID));
            return TASK_SUCCESS;
        } else {
            return TASK_FAILURE;
        }
    }
}
