package yh.ai.TCP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yh.ai.model.Player;
import yh.ai.model.cards.Card;
import yh.ai.model.cards.CardNumber;
import yh.ai.model.cards.CardSuit;

public class dealMsg {
	 private  String receiveMsg;
	 static  public int debug;
	 static public   Map<String , List<String>>  mapMsg = new HashMap <String, List < String > >();
	 
	 public dealMsg(final String Msg)//构造方法，接口函数，输入为接收的消息
	 {
		 this.setReceiveMsg(Msg);
		 debug=10;
		 deal();
	 }
	 //msg to mapMsg
	 public  void  deal()
	 {
		 	//String testMsg =  " game _id:111;hole_card:AH,TS;end;\n";
	        Pattern pattern = Pattern.compile(";");

	        //分号分割  hole_card:AH,TS；
	        String[] words = pattern.split(receiveMsg);//字符串数组
	        for (String s : words)
	        {	
	            //冒号分割：hole_card          AH,TS
	        	 pattern = Pattern.compile(":");
	        	 String[] iterm = pattern.split(s);
	        	 int last = iterm.length-1;
	        	 //System.out.print(iterm.length);        	 
	        		 String key = iterm[0];//把第一个元素作为map的key
	        		 String lastIterm =  iterm[last];//需要重新分割
	        		 //出现空值，如公牌为0的时候，把value设置为null
	        		 List < String > values = new ArrayList<String>(); //初始化为空
	        		 if(last == 0)
	        		 {
	        			 lastIterm = null;
	        		 	 mapMsg.put(key,values);
	        		 	 continue;
	        		 }
	        		 
	        		 //逗号分割如每一张牌，和 每一个玩家
	        		 pattern = Pattern.compile(",");
	        		 String[] eachIterm = pattern.split(lastIterm);	
	        		 
		        	 for(String smallIterm :eachIterm) 
		        	 {
		        		 if(smallIterm!=null)
		        		 {
		        			 values.add(smallIterm);
		        		 }
		        	  }
		        	 mapMsg.put(key,values);
		        	  System.out.println( );
		             System.out.print("map key: " + key + "   ," );
		             System.out.print("map value:");
		             for(String value:values)
		             {
		            	    System.out.print(value + "  " );
		             }
     	         	
	        }
		 
	 }
	 
	 

	 
		//把收到的消息的玩家信息提取出来
		static public List<Player> getPlayers(){
			 List < Player > players = new ArrayList<Player>(); //初始化为空
			 for (String key: mapMsg.keySet()) {
				 if( key.equals("players"))
						 {
					 		List<String> stringArry = mapMsg.get(key);
					 		for (String item: stringArry )
					 		{
					 		  //1+100座位号+剩余筹码
					 		  Pattern pattern = Pattern.compile("\\+");
					 	      String[] info = pattern.split(item);//字符串数组
					 	      int id = Integer.valueOf(info[0]   ).intValue();
					 	      int chip =  Integer.valueOf(info[1]   ).intValue();
					 	      
					 		  Player  player =  new Player(id,chip,false);
					 		  player.setLastestAction( info[2]);
					 		  players.add(player);
					 		}
						 }
				 else {
					 continue;
				 }
				 
			 }
			return players;
		}
	 
		static public int getPlayerChip(int id , List<Player> players)
		{
			int money = 0;
			for(Player p:players)
			{
				if(p.getNumber() == id)
				{
					money = p.getMoney();
				}			
			}
			if(money == 0)
			{
				System.err.println("this player'chip is 0 ,may not exsit ");
			}
			return money;
		}
		
		static public Player getAiFromMsg()
		{
			
			int id = Integer.valueOf(mapMsg.get("aiSeat").get(0));
			int money = Integer.valueOf( mapMsg.get("aiChip").get(0));
			Player ai = new Player(id,money,true);
			return ai ;
		}
		static public   List<Card>  getAiHoleCards()
		{   
			List<Card> cards = new ArrayList<Card>();//新建一个牌的列表
			List<String> holeCardsString = mapMsg.get("aiHoleCards");
			//CardSuit suit; CardNumber number;AS
			for(String holeCardString:holeCardsString)
			{
				
				Card c=stringToCard( holeCardString) ;
				cards.add(c);
			}
			return cards;
		}
		
		static public   List<Card>  getComCards()
		{
			List<Card> cards = new ArrayList<Card>();//新建一个牌的列表
			List<String> comCardsString = mapMsg.get("comCards");//TODO
			//CardSuit suit; CardNumber number;AS
			for(String comCardString:comCardsString)
			{
				
				Card c=stringToCard( comCardString) ;
				cards.add(c);
			}
			return cards;
		}
		
		
		static public   Card  stringToCard( final  String cardString)
		{
			char numberString = cardString.charAt(0);
			char suitString = cardString.charAt(1);	
			CardNumber cardNumber=	CardNumber.TWO;
			CardSuit cardSuit = CardSuit.CLUB;
			//牌的大小
			switch(numberString) 
			{ 
			case '2': 
				cardNumber = cardNumber.TWO;
				break; 
			case '3': 
				cardNumber = cardNumber.THREE;
				break; 
			case '4': 
				cardNumber = cardNumber.FOUR;
				break; 
			case '5': 
				cardNumber = cardNumber.FIVE;
				break; 
			case '6': 
				cardNumber = cardNumber.SIX;
				break; 
			case '7': 
				cardNumber = cardNumber.SEVEN;
				break; 
			case '8': 
				cardNumber = cardNumber.EIGHT;
				break; 
			case '9': 
				cardNumber = cardNumber.NINE;
				break; 
			case 'T': 
				cardNumber = cardNumber.TEN;
				break; 
			case 'J': 
				cardNumber = cardNumber.JACK;
				break; 
			case 'Q': 
				cardNumber = cardNumber.QUEEN;
				break; 
			case 'K': 
				cardNumber = cardNumber.KING;
				break; 
			case 'A': 
				cardNumber = cardNumber.ACE;
				break; 
			default: 
				System.err.println(numberString+"wrong card index out of\"2~TJQKA\""); 
				break; 
			} 	
			//花色
			switch(suitString) 
			{ 
			case 'D': 
				cardSuit = cardSuit.DIAMOND;
				break; 
			case 'S': 
				cardSuit = cardSuit.SPADE;
				break; 
			case 'C': 
				cardSuit = cardSuit.CLUB;
				break; 
			case 'H': 
				cardSuit = cardSuit.HEART;
				break; 	
			default: 
				System.err.println("wrong card SUIT out of\" D.S.C.H \""); 
				break;
			}		
			Card c= new Card(cardSuit, cardNumber);
			return c;
			
		}
		static public   int getAiSeat()
		{
			List<String> number = mapMsg.get("aiSeat");
			int id = Integer.valueOf(number.get(0));
			return id;
		}
		
		static public   int getBetRound()
		{
			List<String> number = mapMsg.get("betround");
			int betround = Integer.valueOf(number.get(0));
			return betround;
		}
		
		
		
		static public   int getGameHandNum()
		{
			List<String> number = mapMsg.get("gameHandNum");
			int gameHandNum = Integer.valueOf(number.get(0));
			return gameHandNum;
		}
		static public   boolean canCheck()
		{
			List<String> number = mapMsg.get("canCheck");
			int canCheck = Integer.valueOf(number.get(0));
			if(canCheck == 1)
			{
				return true;
			}
			else {
				return false;
			}
		}
		static public int getAiChip()
		{
			List<String> number = mapMsg.get("aiChip");
			int aiChip = Integer.valueOf(number.get(0));
			return aiChip;
		}
		static public int getHighBet()
		{
			List<String> number = mapMsg.get("bet");
			int bet = Integer.valueOf(number.get(0));
			return bet;
		}
		static public int getBlind()
		{
			List<String> number = mapMsg.get("blind");
			int blind = Integer.valueOf(number.get(1));
			return blind;
		}
	 
		static public int getAiLevel()
		{
			List<String> number = mapMsg.get("aiLevel");
			int aiLevel = Integer.valueOf(number.get(0));
			return aiLevel;
		}
	 
	 
	 
	 
	public String getReceiveMsg() {
		return receiveMsg;
	}
	public void setReceiveMsg(String receiveMsg) {
		this.receiveMsg = receiveMsg;
	}

		 

}




 

 
  
