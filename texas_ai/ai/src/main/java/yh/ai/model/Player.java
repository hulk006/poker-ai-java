package    yh.ai.model;

import    yh.ai.controller.PlayerController;
import    yh.ai.model.cards.Card;

import java.util.Arrays;
import java.util.List;
//定义一个玩家
public class Player {
    private final int number;//玩家的座位号
	boolean isAi; 
	public String lastestAction;
	private PlayerController playerController;//玩家控制器
    public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}

	private int money;//有多少钱
    private List<Card> holeCards; //可以知道，可以不知道
    public Player(int number, int initialMoney,
            PlayerController playerController ) {
        this.number = number;
        this.money = initialMoney;
        this.playerController = playerController;
    }
    
    public Player(int number, int initialMoney, boolean isAi) {
    	  PlayerController PlayerController  = null ;
        this.number = number;
        this.money = initialMoney;
    	this.isAi = isAi;
    	this.playerController=PlayerController;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }

        Player otherPlayer = (Player) o;

        return number == otherPlayer.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
//返回 Player # + 座位号
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Player #");
        stringBuilder.append(getNumber());

        if (holeCards != null) {
            stringBuilder.append(holeCards.toString());
        }

        return stringBuilder.toString();
    }
    
//投注的决定，在另外一个包里面，输入是一个game hand 一局游戏的信息
    public BettingDecision decide(GameHand gameHand) {
        return playerController.decide(this, gameHand);
    }

    public int getNumber() {
        return number;
    }

    public int getMoney() {
        return money;
    }

    public void removeMoney(int amount) {
        money -= amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }
//设置手牌
    public void setHoleCards(Card hole1, Card hole2) {
        holeCards = Arrays.asList(hole1, hole2);
    }

    public List<Card> getHoleCards() {
        return holeCards;
    }
//一个控制器
    public PlayerController getPlayerController() {
        return playerController;
    }
    public void SetIsAI(boolean isAI)
    {
    	this.isAi = isAI;
    }
    public boolean IsAI()
    {
    	return this.isAi;
    }

	public String getLastestAction() {
		return lastestAction;
	}

	public void setLastestAction(String lastestAction) {
		this.lastestAction = lastestAction;
	}
	 public void setMoney(int money) {
			this.money = money;
		}

		public void setHoleCards(List<Card> holeCards) {
			this.holeCards = holeCards;
		}
}
