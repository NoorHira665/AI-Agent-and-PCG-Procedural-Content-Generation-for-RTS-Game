package s3.ai.bt.conditions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.*;

import java.util.ArrayList;
import java.util.List;

public class IdleSoldiers extends Task {
    private final String value;

    public IdleSoldiers(String value) {
        super();
        this.value = value;
    }

    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing idle Soldiers condition");
        int requestedCount;
        boolean takeAll;
        List<WUnit> footmen = gs.getUnitTypes(c, WFootman.class);
        List<Integer> idleFootmenId = new ArrayList<>();
        Blackboard bb = Blackboard.getInstance();
        bb.clearKey("idleSoldiers"); //clearing at start for clean slate

        //either puts all idle soldiers in blackboard or the number requested
        if (value.equalsIgnoreCase("all")) {
            takeAll = true;
            requestedCount = Integer.MAX_VALUE;
        } else {
            requestedCount = Integer.parseInt(value);
            takeAll = false;
        }

        for (WUnit f : footmen) {
            if (f.getStatus() == null) {
                //case 1: adding all idle soldiers to temporary list
                if (takeAll) {
                    idleFootmenId.add(f.getEntityID());
                    //case 2: adding the requested number to temporary list
                } else if (idleFootmenId.size() < requestedCount) {
                    idleFootmenId.add(f.getEntityID());
                    if (idleFootmenId.size() == requestedCount) break;
                }
            }
        }

        //if we need all idle soldiers, add them to blackboard, unless none exist
        if (takeAll) {
            if (idleFootmenId.isEmpty()) {
                return TASK_FAILURE;
            } else {
                bb.putMultiple("idleSoldiers",idleFootmenId);
                return TASK_SUCCESS;
            }
        } //Add requested number of soldiers to bb, unless that number is not matched
        else {
            if (idleFootmenId.size() == requestedCount) {
                bb.putMultiple("idleSoldiers", idleFootmenId);
                return TASK_SUCCESS;
            } else {
                return TASK_FAILURE;
            }
        }
    }
}
