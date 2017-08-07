package edu.ntnu.texasai.model;

import java.util.ArrayList;
import java.util.List;
//定义一个game
public class Game {
    private final List<Player> players;//玩家
    private final List<GameHand> gameHands = new ArrayList<GameHand>();

    public Game(){
        players = new ArrayList<Player>();
    }

    public Game(List<Player> players) {
        this.players = new ArrayList<Player>(players);
    }

    public void setNextDealer() {
        Player formerDealer = players.remove(0);
        players.add(formerDealer);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addGameHand(GameHand gameHand) {
        gameHands.add(gameHand);
    }

    public int gameHandsCount() {
        return gameHands.size();
    }
}
