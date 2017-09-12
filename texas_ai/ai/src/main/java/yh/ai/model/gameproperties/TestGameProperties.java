package yh.ai.model.gameproperties;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import yh.ai.TCP.dealMsg;
import yh.ai.controller.phase1.PlayerControllerPhaseIBluff;
import yh.ai.controller.phase1.PlayerControllerPhaseINormal;
import yh.ai.controller.phase2.PlayerControllerPhaseIIBluff;
import yh.ai.controller.phase2.PlayerControllerPhaseIINormal;
import yh.ai.controller.phase3.PlayerControllerPhaseIIIAgressive;
import yh.ai.controller.phase3.PlayerControllerPhaseIIIConservative;
import yh.ai.controller.phasehuman.PlayerControllerHuman;
import yh.ai.dependencyinjection.GamePropertiesParameter;
import yh.ai.dependencyinjection.LogLevel;
import yh.ai.model.Player;

public class TestGameProperties extends GameProperties {

	 @Inject
	    public TestGameProperties(	
	    						  final PlayerControllerPhaseINormal playerControllerPhaseINormal,
	    						  final PlayerControllerPhaseIBluff playerControllerPhaseIBluff,
	    						  final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
	                              final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
	                              final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
	                              final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative,
	                              final PlayerControllerHuman playerControllerHuman
	                              ) 
	    {
		   //System.out.println(dealMsg.mapMsg.size());
		   super(dealMsg.mapMsg);//设置玩多少局，携带金额，大小盲注。//得到消息
		   List<Player> players = dealMsg.getPlayers();
		   
		   
		   
	        for (Player player:players)
	        {
	        	System.out.println("玩家"+player.getNumber()+player.getMoney());
	        	
	        	player.setPlayerController(playerControllerHuman); 	
	           addPlayer(player);//把其他玩家加入牌局，除了自ai以外的其他玩家
	        }
	        //把ai的信息加入
	        int ailevel = dealMsg.getAiLevel();
	        Player ai ;
	        switch (ailevel)
	        {
	        case 1:
	            ai = new Player(dealMsg.getAiSeat(),dealMsg.getAiChip() ,playerControllerPhaseINormal);
	        	break;
	        case 2:
	        	  ai = new Player(dealMsg.getAiSeat(),dealMsg.getAiChip() ,playerControllerPhaseIBluff);
	        	break;
	        case 3:
	        	  ai = new Player(dealMsg.getAiSeat(),dealMsg.getAiChip() ,playerControllerPhaseIIBluff);
	        	break;
	        case 4:
	               ai = new Player(dealMsg.getAiSeat(),dealMsg.getAiChip() ,playerControllerPhaseIINormal);
	        	break;
	        case 5:
	               ai = new Player(dealMsg.getAiSeat(),dealMsg.getAiChip() ,playerControllerPhaseIIIConservative);
	        	break;
	        case 6:
	               ai = new Player(dealMsg.getAiSeat(),dealMsg.getAiChip() ,playerControllerPhaseIIIAgressive);
	        	break;	
	        default:
	        		System.out.println("input wrong ai level");
	            	ai = new Player(dealMsg.getAiSeat(),dealMsg.getAiChip() ,playerControllerPhaseIIBluff);//其他情况
	        		break;
	        }
	        ai.SetIsAI(true);
	        ai.setHoleCards(dealMsg.getAiHoleCards());  
	        addPlayer(ai);         
	        
	    }

}
