package edu.ntnu.texasai.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//在这个包中，Java提供了一些实用的方法和数据结构。
//本章介绍Java的实用工具类库java.util包。在这个包中，Java提供了一些实用的方法和数据结构。
public class Deck {
    private final List<Card> cards = new ArrayList<Card>();//新建一个牌的列表
  //deck 为一个负责发牌洗牌的东西，构造方法为按早花色和大小的顺序得到52张的cards列表
    public Deck() {
        for (CardSuit suit : CardSuit.values()) {
            for (CardNumber number : CardNumber.values()) {
                Card card = new Card(suit, number);//新建一张牌花色为suit （string 枚举4种）大小为number  CardSuit.values()
                cards.add(card);
            }
        }

        Collections.shuffle(cards);//洗牌随机把这52张牌打乱顺序
    }

    public List<Card> getCards() {
        return cards;
    }
//发牌后把顶牌拿掉
    public Card removeTopCard() {
        return cards.remove(0);
    }
//删除某一张牌 ，删除操作为列表的操作
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }
   //发两张牌 
    public List<List<Card>> fromDeckToCouplesOfCard(){
        List<List<Card>> couplesOfCard = new ArrayList<List<Card>>();
        int i,j;
        for(i = 0; i < this.cards.size(); i++){           
            for (j = i+1; j < this.cards.size(); j++){    
                List<Card> tmpCards = new ArrayList<Card>();
                tmpCards.add(this.cards.get(i));
                tmpCards.add(this.cards.get(j));
                couplesOfCard.add(tmpCards);
            }                        
        }
        return couplesOfCard;
    }
}
