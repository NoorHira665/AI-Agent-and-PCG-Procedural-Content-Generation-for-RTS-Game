package s3.ai.bt.actions;

import s3.ai.bt.Blackboard;
import s3.ai.bt.Task;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.*;

import java.util.ArrayList;
import java.util.List;

public class AttackNearestEnemyUnit extends Task {
    @Override
    public int run(S3 gs, WPlayer c, List<S3Action> actions) {
        System.out.println("Executing attack nearest enemy action");

        Blackboard bb = Blackboard.getInstance();
        //getting idle soldiers from bb and selecting first 4 from them
        List<Integer> soldierIds = new ArrayList<>(bb.getAll("idleSoldiers"));
        List<Integer> chosenSoldiers = soldierIds.subList(0,4);

        WPlayer enemy = null;
        for (WPlayer p : gs.getPlayers()) {
            if (p != c) {
                enemy = p;
                break;
            }
        }

        //gathering list of enemy units
        List<WUnit> enemyUnits = new ArrayList<>();
        WUnit u;
        if((u= gs.getUnitType(enemy, WPeasant.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WKnight.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WArcher.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WTownhall.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WBarracks.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WLumberMill.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WFortress.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WCatapult.class)) != null) enemyUnits.add(u);
        if((u= gs.getUnitType(enemy, WBlacksmith.class)) != null) enemyUnits.add(u);
        if (enemyUnits.isEmpty()) return TASK_FAILURE;

        //choosing first from the chosen soldiers to calculate nearest enemy unit
        WUnit refSoldier = gs.getUnit(chosenSoldiers.getFirst());
        WUnit nearestUnit = null;
        int leastDist = Integer.MAX_VALUE;
        int sx= refSoldier.getX();
        int sy= refSoldier.getY();

        for(WUnit b: enemyUnits){
            int d = Math.abs(b.getX() -sx) + Math.abs(b.getY()-sy);
            if (d<leastDist) {
                leastDist = d;
                nearestUnit = b;
            }
        }
        if(nearestUnit==null) return TASK_FAILURE; //no enemy unit to attack

        //sending all 4 soldiers to attack the nearest enemy unit
        int commandsIssued = 0;
        int targetId = nearestUnit.getEntityID();
        for (Integer id: chosenSoldiers){
            actions.add(new S3Action(id,S3Action.ACTION_ATTACK, targetId));
            commandsIssued++;
        }

        if (commandsIssued>0){
            return TASK_SUCCESS;
        } else {
            return TASK_FAILURE;
        }

    }
}
