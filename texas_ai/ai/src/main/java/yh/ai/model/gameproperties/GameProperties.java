package    yh.ai.model.gameproperties;
//一场游戏的配置信息
import    yh.ai.model.Player;
import    yh.ai.TCP.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GameProperties {
    private final int smallBlind;
    private final int bigBlind;
    private final int initialMoney;
    private final int numberOfHands;
    private final List<Player> players = new ArrayList<Player>();
    
    //新建一个游戏信息（玩多少手，每个人初始的钱，大盲，小盲）
    protected GameProperties(int numberOfHands, int initialMoney, int bigBlind, int smallBlind) //靠外界输入的消息
    {
        this.numberOfHands = numberOfHands;
        this.initialMoney = initialMoney;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
    }
    
    protected GameProperties(Map<String, List<String>> mapMsg) //靠外界输入的消息
    {
        this.numberOfHands = 10000;//不会用到
        this.initialMoney =Integer.valueOf(mapMsg.get("initialMoney").get(0));
        this.bigBlind = Integer.valueOf(mapMsg.get("blind").get(1));
        this.smallBlind = Integer.valueOf(mapMsg.get("blind").get(0));
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public int getNumberOfHands() {
        return numberOfHands;
    }

    public List<Player> getPlayers() {
        return players;
    }

    protected void addPlayer(Player player){
        players.add(player);
    }
}
