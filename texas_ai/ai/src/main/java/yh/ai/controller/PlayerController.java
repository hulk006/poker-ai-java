package    yh.ai.controller;

import    yh.ai.model.BettingDecision;
import    yh.ai.model.BettingRound;
import    yh.ai.model.GameHand;
import    yh.ai.model.Player;
import    yh.ai.model.cards.Card;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public abstract class PlayerController {
    public BettingDecision decide(Player player, GameHand gameHand) {
        List<Card> cards = new ArrayList<Card>();
        System.out.println("当前的公共牌：" + gameHand.getSharedCards() );
        
        if(gameHand.getSharedCards().size() >= 3 )
        {
        	cards.addAll(gameHand.getSharedCards());
        }
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
