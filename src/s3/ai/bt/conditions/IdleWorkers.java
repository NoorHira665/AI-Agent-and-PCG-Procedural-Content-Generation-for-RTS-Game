package s3.ai.bt.conditions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WUnit;

import java.util.ArrayList;
import java.util.List;

public class IdleWorkers extends Task {
    private final String value;

    public IdleWorkers(String value) {
        super();
        this.value = value;
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing idle workers condition");

        Blackboard bb = Blackboard.getInstance();
        bb.clearKey("idlePeasants"); //clearing at start for clean slate

        //either puts all idle workers in blackboard or the number requested
        int requestedCount;
        boolean takeAll;
        List<WUnit> peasants = gs.getUnitTypes(c, WPeasant.class);
        List<Integer> idlePeasantIds = new ArrayList<>();

        if (value.equalsIgnoreCase("all")) {
            takeAll = true;
            requestedCount = Integer.MAX_VALUE;
        } else {
            requestedCount = Integer.parseInt(value);
            takeAll = false;
        }

        for (WUnit p : peasants) {
            if (p.getStatus() == null) {
                //case 1: adding all idle workers to temporary list
                if (takeAll) {
                    idlePeasantIds.add(p.getEntityID());
                    //case 2: adding the requested number to temporary list
                } else if (idlePeasantIds.size() < requestedCount) {
                    idlePeasantIds.add(p.getEntityID());
                    if (idlePeasantIds.size() == requestedCount) break;
                }
            }
        }

        //if we need all idle workers, add them to blackboard, unless none exist
        if (takeAll) {
            if (idlePeasantIds.isEmpty()) {
                return TASK_FAILURE;
            } else {
                bb.putMultiple("idlePeasants", idlePeasantIds);
                return TASK_SUCCESS;
            }
            //Add requested number of workers to bb, unless that number is not matched
        } else {
            if (idlePeasantIds.size() == requestedCount) {
                bb.putMultiple("idlePeasants", idlePeasantIds);
                return TASK_SUCCESS;
            } else {
                return TASK_FAILURE;
            }
        }
    }
}
