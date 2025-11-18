package s3.ai.bt;

import s3.ai.bt.actions.*;
import s3.ai.bt.conditions.*;

public class TaskFactory {

    public static Task createTask(String type, String value){
        return switch (type) {
            case "BaseExists" -> new BaseExists(value);
            case "BarracksExists" -> new BarracksExists(value);
            case "BaseIdle" -> new BaseIdle();
            case "BarracksIdle" -> new BarracksIdle();
            case "EnemyAttacking" -> new EnemyAttacking();
            case "EnoughResources" -> new EnoughResources(value);
            case "IdleSoldiers" -> new IdleSoldiers(value);
            case "IdleWorkers" -> new IdleWorkers(value);
            case "WorkersLessThanEqualTo" -> new WorkersLessThanEqualTo(Integer.parseInt(value));
            case "WorkersEqualTo" -> new WorkersEqualTo(Integer.parseInt(value));
            case "AttackEnemy" -> new AttackEnemy();
            case "BuildBarracks" -> new BuildBarracks();
            case "BuildBase" -> new BuildBase();
            case "GatherGold" -> new GatherGold();
            case "GatherWood" -> new GatherWood();
            case "AttackNearestEnemyUnit" -> new AttackNearestEnemyUnit();
            case "TrainFootman" -> new TrainFootman();
            case "TrainWorker" -> new TrainWorker();
            default -> throw new RuntimeException("Unknown task: " + type);
        };
    }
}
