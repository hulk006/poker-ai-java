package edu.ntnu.texasai.controller;

import com.google.inject.Inject;
import edu.ntnu.texasai.model.cards.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EquivalenceClassController {
	private final Collection<EquivalenceClass> equivalenceClasses;//表示所有手牌的组成的值的可能的组合 13*2 + 13×12×*2
   //注入
	@Inject
	public EquivalenceClassController() {
	    equivalenceClasses = new ArrayList<EquivalenceClass>();
	}
	
	/**
	 * Converts two cards into their corrispondent equivalence class把两张牌转化为等价的值 便于比较，转化为值的接口输入是两张手牌
	 * */
	public EquivalenceClass cards2Equivalence(Card card1, Card card2) {
		EquivalenceClass equivalenceClass;
		if (card1.getSuit().equals(card2.getSuit())) { // suited手牌同花
			    equivalenceClass = new EquivalenceClassSuited(card1.getNumber(),card2.getNumber());
			} else {// unsuited手牌不同花
			    equivalenceClass = new EquivalenceClassUnsuited(card1.getNumber(),card2.getNumber());
			}
		return equivalenceClass;
	}

	public Collection<EquivalenceClass> getEquivalenceClasses() {
		return equivalenceClasses;
	}
	
	public void generateAllEquivalenceClass()  {
		EquivalenceClass equivalenceClass;
		List<CardNumber> allCardNumbers = new ArrayList<CardNumber>();
		
		//generateThePairs产生对子手牌的 2:14
		for (CardNumber number : CardNumber.values()) {
			equivalenceClass = new EquivalenceClassUnsuited(number,number);	//非同花
			equivalenceClasses.add(equivalenceClass);
			allCardNumbers.add(number);
		}
		//产生其他手牌的组合 大小 如 （2，5 ）s 	
		//generate other equivalences 		
		for(int i = 0; i < allCardNumbers.size(); i++){
			for(int j = i+1; j < allCardNumbers.size();j++){				
				equivalenceClass = new EquivalenceClassUnsuited(allCardNumbers.get(i),allCardNumbers.get(j));
				equivalenceClasses.add(equivalenceClass);				
				equivalenceClass = new EquivalenceClassSuited(allCardNumbers.get(i),allCardNumbers.get(j));
				equivalenceClasses.add(equivalenceClass);				
			}			
		}		
	}
}
