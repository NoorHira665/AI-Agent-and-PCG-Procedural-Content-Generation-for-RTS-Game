package s3.ai.bt;

import s3.ai.AI;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;

import java.io.IOException;
import java.util.List;

public class BtAI implements AI {
    private final String m_playerID;
    private final Task root;

    public BtAI(String player_id, String xmlPath) throws Exception{
        this.m_playerID = player_id;
        this.root = XmlBTLoader.loadBTfromXML(xmlPath);
    }

    @Override
    public void gameStarts(){}

    @Override
    public void game_cycle(S3 game, WPlayer player, List<S3Action> actions) throws ClassNotFoundException, IOException {
        if(game.getCycle() % 25 !=0){
            return;
        }
        root.run(game,player,actions);
    }

    @Override
    public void gameEnd(){
    }

    @Override
    public String getPlayerId(){
        return m_playerID;
    }
}
