package    yh.ai.model;

import java.util.ArrayList;
import java.util.List;

import yh.ai.TCP.dealMsg;
//定义一个game
public class Game {
	
	public   int gameid; //用来确认是同一局游戏
	
	private final List<Player> players;//玩家
    private final List<GameHand> gameHands = new ArrayList<GameHand>();//进行一次对局就存放一次

    public Game(){
        players = new ArrayList<Player>();
    }

    public Game(List<Player> players) {      //利用这几个玩家建立一个游戏
        this.players = new ArrayList<Player>(players);
    }

    public void setNextDealer() {
        Player formerDealer = players.remove(0);
        players.add(formerDealer);
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public Player getAISelf() {
    	int i=0;
    	Player ai = null;
        for(Player p:this.players)
        {   
        	if(p.IsAI()==true)
        	{
        		ai = players.get(i);
        		break;
        	}
        	i++;
        }  	
    	return ai;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addGameHand(GameHand gameHand) {
        gameHands.add(gameHand);
    }

    public int gameHandsCount() {
       // return gameHands.size();
        return dealMsg.getGameHandNum();
    }
    public int getGameid() {
  		return gameid;
  	}

  	public void setGameid(int gameid) {
  		this.gameid = gameid;
  	}
}
