package edu.ntnu.texasai.controller;

import edu.ntnu.texasai.model.HandPower;
import edu.ntnu.texasai.model.cards.Card;
import edu.ntnu.texasai.model.cards.Deck;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class HandStrengthEvaluator {

    private final HandPowerRanker handPowerRanker;

    @Inject
    public HandStrengthEvaluator(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }
     //评估手牌，输入是 （玩家的手牌，公牌，玩家数量）
    public double evaluate(List<Card> playerHoleCards, List<Card> sharedCards, Integer numberOfPlayers) {
        if(sharedCards == null || sharedCards.isEmpty()){
            return 0d;
        }

        int wins = 0;
        int losses = 0;
        int ties = 0;
        
        Deck deck = new Deck();//新建一个模拟发牌
        Card hole1 = playerHoleCards.get(0);//获得手牌1.2
        Card hole2 = playerHoleCards.get(1);
        deck.removeCard(hole1);//把已知的牌删除
        deck.removeCard(hole2);
        for (Card card : sharedCards) {
                deck.removeCard(card);
        }
        //仿真来算手牌强度
        //建立一个手牌列表 如果还剩 39张牌则
        List<List<Card>> couplesOfCards = deck.fromDeckToCouplesOfCard();

        List<Card> playerCards = new ArrayList<Card>();
        playerCards.addAll(playerHoleCards);
        playerCards.addAll(sharedCards);
        HandPower playerRank = handPowerRanker.rank(playerCards);

        for (List<Card> couple : couplesOfCards) {
            List<Card> opponentCards = new ArrayList<Card>();
            opponentCards.addAll(couple);
            opponentCards.addAll(sharedCards);
            HandPower opponentRank = handPowerRanker.rank(opponentCards);

            int result = playerRank.compareTo(opponentRank);
            if (result > 0) {
                wins++;
            } else if (result < 0) {
                losses++;
            } else {
                ties++;
            }
        }
        return calculateHandStrength(wins, ties, losses, numberOfPlayers);
    }
//计算手牌强度的公式
    private double calculateHandStrength(int wins, int ties, int losses, int numberOfPlayers) {
        double num = (wins + 0.5 * ties);
        double den = (wins + losses + ties);
        return Math.pow(num / den, numberOfPlayers);
    }

}
