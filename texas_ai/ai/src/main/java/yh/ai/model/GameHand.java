package    yh.ai.model;

import yh.ai.TCP.dealMsg;
import    yh.ai.model.cards.Card;
import    yh.ai.model.cards.Deck;
import    yh.ai.model.opponentmodeling.ContextAction;
import    yh.ai.model.opponentmodeling.ContextInformation;
import    yh.ai.model.gameproperties.GameProperties;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameHand {
	public int gameHandid;//用于查找是一局游戏
 
	private final Deque<Player> players;//双向队列
    private final Deck deck;  //发牌的程序
    public List<Card> sharedCards = new ArrayList<Card>(); //公共牌3~5张
    
    private final List<BettingRound> bettingRounds = new ArrayList<BettingRound>();//
    
    public BettingRound currentRound = new BettingRound();
    //建立一轮下注的信息

	private Boolean hasRemoved = true;

    public GameHand(List<Player> players) {   //一句游戏的输入为玩家列表
        this.players = new LinkedList<Player>(players);//玩家列表转化为链表

        deck = new Deck();//建立一个发牌
    }
   
    public void newBettingRound() {//新建一轮下注情况
        bettingRounds.add(new BettingRound());
    }
    
     //TODO
    //需要把牌的信息告诉ai，和 对手，对手只知道公共牌 不需要知道自己牌是哪一轮，如果有就赋值就可以
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
//获取本轮下注的名称
    public BettingRoundName getBettingRoundName(  ) {
    	
        return BettingRoundName.fromRoundNumber(bettingRounds.size());
    }
  //获取本轮下注的名称
    public BettingRoundName getBettingRoundName(final  Map<String , List<String>>  mapMsg ) {
    	String bettingRoundName = dealMsg.mapMsg.get("bettingRoundName").get(0);
    	if(bettingRoundName == "PRE_FLOP"||bettingRoundName == "pre_flop"||bettingRoundName == "PREFLOP"||bettingRoundName == "preflop")
    	{
    		return BettingRoundName.PRE_FLOP;
    	}
    	else if(bettingRoundName == "POST_FLOP"||bettingRoundName == "post_flop"||bettingRoundName == "POSTFLOP"||bettingRoundName == "postflop")
    	{
    		return BettingRoundName.POST_FLOP;
    	}
    	else if(bettingRoundName == "POST_TURN"||bettingRoundName == "post_turn"||bettingRoundName == "POSTTURN"||bettingRoundName == "postturn")
    	{
    		return BettingRoundName.POST_TURN;
    	}
    	else if(bettingRoundName == "POST_RIVER"||bettingRoundName == "post_river"||bettingRoundName == "POSTRIVER"||bettingRoundName == "postriver")
    	{
    		return BettingRoundName.POST_RIVER;
    	}
    	else
    	{
    		return BettingRoundName.PRE_FLOP;
    	}	
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
    //add myself
    public void setHoleCards(   List<Card> holeCards ) {
    	for(Player  player:players) {
    		if(player.isAi == true)//如果这个玩家是我们自己的ai，就把手牌给ai
    		{
    			if(holeCards.size() == 2)
    			{
    				Card hole1 =  holeCards.get(0) ;
    				Card hole2 =  holeCards.get(1);
       				System.out.println(String.valueOf(player.getNumber() ) + hole1.toString()+hole2.toString());
    			    player.setHoleCards(hole1, hole2);
    			    deck.removeCard(hole1);
    			    deck.removeCard(hole2);	
    			}
    			else
    			{
    				System.out.println(String.valueOf(player.getNumber() ) + "the hole card is less than 2");
    			}
    		}
    		else//这些玩家玩家是一个真实玩家或者 其他的ai，我们不知道他的手牌是多少
    		{
    			player.setHoleCards(null, null);//其他 玩家的手牌未知
    		}
    	}
    }
    
    
    //add myself
    public void setShareCards( List<Card> shareCards)
    {
    	if(shareCards.size() < 3)//公共牌一定大于3，此时没有公共牌
    	{
    		this.sharedCards = new ArrayList<Card>();;
    	}
    	else
    	{
    		this.sharedCards = shareCards;
    		for(Card card:shareCards)//从deck牌桌中删除这个3~5张公共牌
    		{
    		deck.removeCard(card);
    		}
    	}
    }
    
  

    public Deque<Player> getPlayers() {
        return this.players;
    }
    
//玩家决策，输入为：一个玩家，下注的决定，牌局的性质，手牌强度
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
    public int getGameHandid() {
 		return gameHandid;
 	}
 	public void setGameHandid(int gameHandid) {
 		this.gameHandid = gameHandid;
 	}

  //×××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××  
    // 利用内置的发牌器 deck
    private void dealSharedCard() {
        sharedCards.add(deck.removeTopCard());
    }
    
  //给每个玩家发两张手牌
    protected void dealHoleCards() {
        for (Player player : players) {
            Card hole1 = deck.removeTopCard();
            Card hole2 = deck.removeTopCard();

            player.setHoleCards(hole1, hole2);
        }
    }
//发公共牌，利用内置的发牌器 deck
    private void dealFlopCards() {
        sharedCards.add(deck.removeTopCard());
        sharedCards.add(deck.removeTopCard());
        sharedCards.add(deck.removeTopCard());
    }

}
