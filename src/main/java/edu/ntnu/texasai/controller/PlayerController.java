package edu.ntnu.texasai.controller;

import edu.ntnu.texasai.model.BettingDecision;
import edu.ntnu.texasai.model.BettingRound;
import edu.ntnu.texasai.model.GameHand;
import edu.ntnu.texasai.model.Player;
import edu.ntnu.texasai.model.cards.Card;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public abstract class PlayerController {
    public BettingDecision decide(Player player, GameHand gameHand) {
        List<Card> cards = new ArrayList<Card>();
        cards.addAll(gameHand.getSharedCards());
        cards.addAll(player.getHoleCards());
        
        if (cards.size() == 2) {
            return decidePreFlop(player, gameHand, cards);
        } else {
            return decideAfterFlop(player, gameHand, cards);
        }
    }
    
   
    
//让牌的条件 你当前的下注追平了最高注，你上一把的下注
    protected boolean canCheck(GameHand gameHand, Player player) {
        BettingRound bettingRound = gameHand.getCurrentBettingRound();
        return bettingRound.getHighestBet() == bettingRound.getBetForPlayer(player);
    }

    protected abstract BettingDecision decidePreFlop(Player player,
                                                     GameHand gameHand, List<Card> cards);

    protected abstract BettingDecision decideAfterFlop(Player player,
                                                       GameHand gameHand, List<Card> cards);
   
 
   
    
    
}
