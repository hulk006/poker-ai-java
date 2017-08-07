package edu.ntnu.texasai.model.gameproperties;
//一场游戏的配置信息
import edu.ntnu.texasai.model.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class GameProperties {
    private final int smallBlind;
    private final int bigBlind;
    private final int initialMoney;
    private final int numberOfHands;
    private final List<Player> players = new ArrayList<Player>();
    //新建一个游戏信息（玩多少手，每个人初始的钱，大盲，小盲）
    protected GameProperties(int numberOfHands, int initialMoney, int bigBlind, int smallBlind) {
        this.numberOfHands = numberOfHands;
        this.initialMoney = initialMoney;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
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
