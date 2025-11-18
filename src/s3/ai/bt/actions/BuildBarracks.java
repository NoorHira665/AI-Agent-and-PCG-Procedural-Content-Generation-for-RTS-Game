package s3.ai.bt.actions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WBarracks;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WUnit;
import s3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BuildBarracks extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing build barracks action");
        Blackboard bb = Blackboard.getInstance();

        //if barrack built. return success
        if(gs.getUnitType(c,WBarracks.class)!=null) return TASK_SUCCESS;

        //check to see if any other peasant already building barrack, code snippet from RushAI2
        List<WUnit> peasants = gs.getUnitTypes(c, WPeasant.class);
        for (WUnit p : peasants) {
            if (p.getStatus() != null &&
                    p.getStatus().m_action == S3Action.ACTION_BUILD &&
                    p.getStatus().m_parameters.get(0).equals(WBarracks.class.getSimpleName())) {
                return TASK_EXECUTING;
            }
        }

        Integer peasantId = bb.getAll("idlePeasants").getFirst();
        WUnit peasant = gs.getUnit(peasantId);

        //finding free space, code snippet from RushAI2
        Pair<Integer, Integer> loc = gs.findFreeSpace(peasant.getX(), peasant.getY(), 4);
        if (null == loc) {
            loc = gs.findFreeSpace(peasant.getX(), peasant.getY(), 3);
            if (loc==null) return TASK_FAILURE;
        }

        //build barrack command
        actions.add(new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WBarracks.class.getSimpleName(), loc.m_a, loc.m_b));
        return TASK_EXECUTING;
    }
}
