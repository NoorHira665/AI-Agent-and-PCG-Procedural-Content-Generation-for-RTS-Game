package s3.ai.bt.actions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.*;
import s3.util.Pair;

import java.util.List;

public class BuildBase extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing build base action");
        Blackboard bb = Blackboard.getInstance();
        //if base built, return success
        if(gs.getUnitType(c,WTownhall.class)!=null) return TASK_SUCCESS;

        //check to see if any other peasant already building base
        List<WUnit> peasants = gs.getUnitTypes(c, WPeasant.class);
        for (WUnit p : peasants) {
            if (p.getStatus() != null &&
                    p.getStatus().m_action == S3Action.ACTION_BUILD &&
                    p.getStatus().m_parameters.get(0).equals(WTownhall.class.getSimpleName())) {
                return TASK_EXECUTING;
            }
        }

        Integer peasantId = bb.getAll("idlePeasants").getFirst();
        if (null == peasantId) {
            return TASK_FAILURE;
        }

        WUnit peasant = gs.getUnit(peasantId);
        Pair<Integer, Integer> loc = gs.findFreeSpace(peasant.getX(), peasant.getY(), 3);
        if (null == loc) {
            return TASK_FAILURE;
        }
        actions.add(new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WTownhall.class.getSimpleName(), loc.m_a, loc.m_b));
        bb.clearKey("idlePeasants");
        return TASK_EXECUTING;
    }
}
