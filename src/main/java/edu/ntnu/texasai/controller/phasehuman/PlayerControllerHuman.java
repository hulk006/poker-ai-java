package edu.ntnu.texasai.controller.phasehuman;
import edu.ntnu.texasai.controller.PlayerController;
import edu.ntnu.texasai.model.*;
import edu.ntnu.texasai.model.cards.Card;
import edu.ntnu.texasai.utils.Logger;

import javax.inject.Inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PlayerControllerHuman extends PlayerController {



   // private final HandPowerRanker handPowerRanker;
    //在抽象类中的插入新的构造方法或者方法
    @Inject
    public PlayerControllerHuman() {
    }
    //重写父类中的toString()
    @Override
    public String toString() {
        return "Phase human";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,  List<Card> cards)
    {
    	player.SetIsAI(true);
    
        
    	//引入错误的机制
    	try {
			return decideByhuman(player,cards);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
//change by myself
    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards)
    {
    	//引入错误的机制
    	try {
			return decideByhuman(player,cards);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    //add by myself
    //从键盘输入人类玩家的决定  如果玩家是一个真人的话，就让玩家从 键盘中输入一个决定cfr
    public  BettingDecision decideByhuman( Player player,  List<Card> cards) throws IOException
    {
    	 String humanDecision;
    	 // 使用 System.in 创建 BufferedReader 
    	 BufferedReader br = new BufferedReader(new  InputStreamReader(System.in));//建立从键盘输入的
    	String cardsString = "";
        for(Card card:player.getHoleCards())
        {
        	cardsString +=  card.toString();   		 
        }
         System.out.println("humanPlayer #" + player.getNumber()+"hole:" + cardsString);  	
    	 System.out.println("玩家输入字符:c, f , r, 按下 'q' 键退出。");
    	 do {
	    	 // 读取字符
	    	 humanDecision =  br.readLine();//读取一行返回字符串 read（）读取一行字符
	    	 //System.out.println(humanDecision);
	         if(humanDecision.equals( "c" ) || humanDecision.equals( "call" ) ||humanDecision.equals(  "CALL") )
	         {
	        	 System.out.println("call ");
	        	 return BettingDecision.CALL;
	         }
	         else if(humanDecision.equals("r") ||humanDecision.equals("raise") ||humanDecision.equals( "RAISE")  )
	         {
	        	 return BettingDecision.RAISE;
	         }
	         else if(humanDecision.equals( "f")||humanDecision.equals("fold")||humanDecision.equals("FOLD"))
	         {
	        	 return BettingDecision.FOLD;
	         }
	         else {
	        	 System.out.println("your input decision is not legal ");
	        	 continue ;
	        	// return null;
	         }
     }while(humanDecision != "q");
    	 return BettingDecision.CALL;//默认为跟住      
    }

}


