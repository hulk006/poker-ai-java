package edu.ntnu.texasai.model;

import edu.ntnu.texasai.model.cards.Card;
import edu.ntnu.texasai.model.cards.Deck;
import edu.ntnu.texasai.model.opponentmodeling.ContextAction;
import edu.ntnu.texasai.model.opponentmodeling.ContextInformation;
import edu.ntnu.texasai.model.gameproperties.GameProperties;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class GameHand {
    private final Deque<Player> players;//双向队列
    private final Deck deck;//发牌的程序
    private final List<Card> sharedCards = new ArrayList<Card>(); //公共牌3~5张
    private final List<BettingRound> bettingRounds = new ArrayList<BettingRound>();//
    private Boolean hasRemoved = true;

    public GameHand(List<Player> players) {   //一句游戏的输入为玩家列表
        this.players = new LinkedList<Player>(players);//玩家列表转化为链表

        deck = new Deck();//建立一个发牌
    }

    public void nextRound() {//得到第几轮信息，然后发牌
        bettingRounds.add(new BettingRound());

        if (getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            dealHoleCards();
        } else if (getBettingRoundName().equals(BettingRoundName.POST_FLOP)) {
            dealFlopCards();//3
        } else {
            dealSharedCard();//1
        }
    }
    //删除玩家，加入玩家，获得现有玩家
    public Player getNextPlayer() {
        if (!hasRemoved) {
            Player player = players.removeFirst();
            players.addLast(player);
        }
        hasRemoved = false;
        return getCurrentPlayer();
    }
    //获得所有的下注
    public int getTotalBets() {
        int totalBets = 0;
        for (BettingRound bettingRound : bettingRounds) {
            totalBets += bettingRound.getTotalBets();//把每一轮的下注加起来
        }
        return totalBets;
    }

    public BettingRoundName getBettingRoundName() {
        return BettingRoundName.fromRoundNumber(bettingRounds.size());
    }

    public Player getCurrentPlayer() {
        return players.getFirst();
    }

    public List<Card> getSharedCards() {
        return sharedCards;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public BettingRound getCurrentBettingRound() {
        return bettingRounds.get(bettingRounds.size() - 1);//获得最新的下注的一轮
    }

    public List<BettingRound> getBettingRounds() {
        return bettingRounds;
    }

    public void removeCurrentPlayer() {
        players.removeFirst();
        hasRemoved = true;
    }
//给每个玩家发两张手牌
    protected void dealHoleCards() {
        for (Player player : players) {
            Card hole1 = deck.removeTopCard();
            Card hole2 = deck.removeTopCard();

            player.setHoleCards(hole1, hole2);
        }
    }
//发公共牌
    private void dealFlopCards() {
        sharedCards.add(deck.removeTopCard());
        sharedCards.add(deck.removeTopCard());
        sharedCards.add(deck.removeTopCard());
    }

    private void dealSharedCard() {
        sharedCards.add(deck.removeTopCard());
    }

    public Deque<Player> getPlayers() {
        return this.players;
    }
//玩家决策，输入为：玩家，下注的决定，牌局的性质，手牌强度
    public void applyDecision(Player player, BettingDecision bettingDecision, GameProperties gameProperties,
                              double handStrength) {
        BettingRound currentBettingRound = getCurrentBettingRound();//先获得当前的下注信息
        double potOdds = calculatePotOdds(player);//得到玩家的胜率
        ContextAction contextAction = new ContextAction(player, bettingDecision, getBettingRoundName(),
                currentBettingRound.getNumberOfRaises(),
                getPlayersCount(), potOdds); //信息
        ContextInformation contextInformation = new ContextInformation(contextAction, handStrength);

        currentBettingRound.applyDecision(contextInformation, gameProperties);
//如果玩家fold 就删除玩家
        if (bettingDecision.equals(BettingDecision.FOLD)) {
            removeCurrentPlayer();
        }
    }
//计算奖池收益
    public double calculatePotOdds(Player player) {
        BettingRound currentBettingRound = getCurrentBettingRound();
        int amountNeededToCall = currentBettingRound.getHighestBet() - currentBettingRound.getBetForPlayer(player);
        return (double) amountNeededToCall / (amountNeededToCall + getTotalBets());
    }

    protected Deck getDeck() {
        return deck;
    }
}
