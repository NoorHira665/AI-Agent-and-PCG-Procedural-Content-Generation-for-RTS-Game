package s3.ai.bt.actions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.S3PhysicalEntity;
import s3.entities.WOTree;
import s3.entities.WPlayer;
import s3.entities.WUnit;

import java.util.LinkedList;
import java.util.List;

public class GatherWood extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing gather wood action");

        //getting an idle peasant from bb
        Blackboard bb = Blackboard.getInstance();
        Integer peasantId = bb.getAll("idlePeasants").getFirst();
        WUnit peasant = gs.getUnit(peasantId);

        //finding trees and then the nearest tree from them, code from RushAI2
        List<WOTree> trees = new LinkedList<WOTree>();
        for(int i = 0;i<gs.getMap().getWidth();i++) {
            for(int j = 0;j<gs.getMap().getHeight();j++) {
                S3PhysicalEntity e = gs.getMap().getEntity(i, j);
                if (e instanceof WOTree) trees.add((WOTree)e);
            }
        }
        WOTree tree = null;
        int leastDist = 9999;
        for (WOTree unit : trees) {
            int dist = Math.abs(unit.getX() - peasant.getX())
                    + Math.abs(unit.getY() - peasant.getY());
            if (dist < leastDist) {
                leastDist = dist;
                tree = unit;
            }
        }

        //if tree exists, peasant sent to harvest it
        if (null != tree) {
            actions.add(new S3Action(peasant.entityID, S3Action.ACTION_HARVEST, tree.getX(),
                    tree.getY()));
            return TASK_SUCCESS;
        } else{
            return TASK_FAILURE;
        }
    }
}
