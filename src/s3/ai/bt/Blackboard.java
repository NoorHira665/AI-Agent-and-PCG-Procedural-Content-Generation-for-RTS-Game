package s3.ai.bt;

import java.util.*;

public class Blackboard {
    //One instance of the blackboard to be used by the game
    private static final Blackboard INSTANCE = new Blackboard();

    private Blackboard() {}

    public static Blackboard getInstance(){
        return INSTANCE;
    }

    Map<String,List<Integer>> blackboard = new HashMap<>();


    public void put(String key, Integer val){
        blackboard.putIfAbsent(key, new ArrayList<>());
        blackboard.get(key).add(val);
    }

    public void putMultiple(String key,List<Integer> itemList) {
        blackboard.putIfAbsent(key, new ArrayList<>());
        for (Integer item: itemList) {
            blackboard.get(key).add(item);
        }
    }

    public List<Integer> getAll(String key){
        return blackboard.get(key);
    }

    public void clearKey(String key) {
        blackboard.put(key, new ArrayList<>());
    }
}

