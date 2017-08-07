package edu.ntnu.texasai.controller;

import edu.ntnu.texasai.model.*;
import edu.ntnu.texasai.model.cards.Card;
import edu.ntnu.texasai.model.cards.CardNumber;
import edu.ntnu.texasai.model.cards.CardSuit;
import edu.ntnu.texasai.utils.MapList;

import java.util.*;
//手牌强度的一个排序工具，类
public class HandPowerRanker {
	//利用Comparator 建立一个比较器，按照cardNumber排序cardNumberComparator是一个比较器的类×××
    private final Comparator<CardNumber> cardNumberComparator = new Comparator<CardNumber>() 
    {

        public int compare(CardNumber cardNumber1, CardNumber cardNumber2)
        {
            return cardNumber1.getPower() - cardNumber2.getPower();
        }
    };

    public HandPower rank(List<Card> cards) {
    	//牌的花色与 数值容器
        MapList<CardNumber, Card> numberGroup = getNumberGroup(cards);//map 是键值对应 
        MapList<CardSuit, Card> suitGroup = getSuitGroup(cards);
        List<Card> cardsSortedByNumber = getCardsSortedByNumber(cards); //按照大小排序

        CardNumber straightFlushNumber = getStraightFlushNumber(suitGroup);//主要用于判断同花，

        // Straight flush 同花顺
        if (straightFlushNumber != null) {
            return new HandPower(HandPowerType.STRAIGHT_FLUSH,
                    Arrays.asList(straightFlushNumber));
        }
        //4条
        CardNumber cardNumberForFour = getCardNumberForCount(4, numberGroup);
        // Four of a kind
        if (cardNumberForFour != null) 
        {
            return new HandPower(HandPowerType.FOUR_OF_A_KIND,
                    calculateSameKindTie(4, cardNumberForFour,
                            cardsSortedByNumber));
        }

        List<CardNumber> fullHouseCardNumbers = getFullHouse(numberGroup);
        // Full house葫芦3+2
        if (fullHouseCardNumbers.size() == 2) {
            return new HandPower(HandPowerType.FULL_HOUSE, fullHouseCardNumbers);
        }

        // Flush同花
        CardSuit flushSuit = getFlush(suitGroup);
        if (flushSuit != null) {
            return new HandPower(HandPowerType.FLUSH, calculateFlushTie(
                    flushSuit, suitGroup));
        }

        // Straight顺子
        CardNumber straightNumber = getStraight(numberGroup);
        if (straightNumber != null) {
            return new HandPower(HandPowerType.STRAIGHT,
                    Arrays.asList(straightNumber));
        }

        // Three of a kind3条
        CardNumber cardNumberForThree = getCardNumberForCount(3, numberGroup);
        if (cardNumberForThree != null) {
            return new HandPower(HandPowerType.THREE_OF_A_KIND,
                    calculateSameKindTie(3, cardNumberForThree,
                            cardsSortedByNumber));
        }

        // Pair(s)两对
        CardNumber cardNumberForTwo = getCardNumberForCount(2, numberGroup);
        if (cardNumberForTwo != null) {
            List<CardNumber> pairsCardNumber = getPairs(numberGroup);
            // Two pair
            if (pairsCardNumber.size() >= 2) {

                return new HandPower(HandPowerType.TWO_PAIR,
                        calculateTwoPairsTie(pairsCardNumber,
                                cardsSortedByNumber));
            }
            // One Pair对子
            else {
                return new HandPower(HandPowerType.ONE_PAIR,
                        calculateSameKindTie(2, cardNumberForTwo,
                                cardsSortedByNumber));
            }
        }

        // High Card高牌
        return new HandPower(HandPowerType.HIGH_CARD,
                bestCardsNumberInList(cardsSortedByNumber));
    }
//判断葫芦的函数输入是：牌的牌的大小的map，返回3+2
    private List<CardNumber> getFullHouse(MapList<CardNumber, Card> numberGroup)
    {
        List<CardNumber> fullHouseCardNumbers = new ArrayList<CardNumber>();

        List<CardNumber> cardNumbers = new ArrayList<CardNumber>(
                numberGroup.keySet());//2～14的数字列表
        Collections.sort(cardNumbers, cardNumberComparator);//根据number大小排序，小到大
        Collections.reverse(cardNumbers);
//values():方法是获取集合中的所有的值----没有键，没有对应关系，将Map中所有的键存入到set集合中。因为set具备迭代器。
        //所有可以迭代方式取出所有的键，再根据get方法。获取每一个键对应的值。 keySet():迭代后只能通过get()取key 
        // Find the best cards for the triple              map<number,list<card>> 找3个
        for (CardNumber cardNumber : cardNumbers) {
            if (numberGroup.get(cardNumber).size() >= 3) {
                fullHouseCardNumbers.add(cardNumber);
                break;
            }
        }

        // Find the best card for the pair 找两个 数量大与两个并且数字不等一3个的
        if (fullHouseCardNumbers.size() > 0) {
            for (CardNumber cardNumber : cardNumbers) {
                if (numberGroup.get(cardNumber).size() >= 2
                        && !cardNumber.equals(fullHouseCardNumbers.get(0))) {
                    fullHouseCardNumbers.add(cardNumber);
                    break;
                }
            }
        }

        return fullHouseCardNumbers;
    }
//输入是两对的牌的列表，和排序的7张牌的，输出是把没有的牌放到tieBreakingInformation
    private List<CardNumber> calculateTwoPairsTie(
            List<CardNumber> pairsCardNumber, List<Card> cardsSortedByNumber) 
    {
        Collections.sort(pairsCardNumber, cardNumberComparator);
        Collections.reverse(pairsCardNumber);
        List<CardNumber> tieBreakingInformation = new ArrayList<CardNumber>(
                pairsCardNumber);

        for (int i = cardsSortedByNumber.size() - 1; i >= 0; i--) {
            CardNumber cardNumber = cardsSortedByNumber.get(i).getNumber();//这是牌的大小
            if (!pairsCardNumber.contains(cardNumber)) {//如果没有包含
                tieBreakingInformation.add(cardNumber);
                return tieBreakingInformation;
            }
        }
        return null;
    }
//获取对子
    private List<CardNumber> getPairs(MapList<CardNumber, Card> numberGroup) {
        List<CardNumber> pairsCardNumber = new ArrayList<CardNumber>();
        for (List<Card> cards : numberGroup)//遍历value 
        {
            if (cards.size() == 2) {
                pairsCardNumber.add(cards.get(0).getNumber());
            }
        }
        Collections.sort(pairsCardNumber, cardNumberComparator);
        Collections.reverse(pairsCardNumber);

        if (pairsCardNumber.size() > 2) {
            pairsCardNumber.remove(pairsCardNumber.size() - 1);
        }

        return pairsCardNumber;
    }

    private List<CardNumber> calculateFlushTie(CardSuit flushSuit,
            MapList<CardSuit, Card> suitGroup) {
        List<Card> cards = suitGroup.get(flushSuit);
        return bestCardsNumberInList(cards);
    }
//找到5张最大的牌
    private List<CardNumber> bestCardsNumberInList(List<Card> cards) {
        List<CardNumber> cardNumbers = cardsToCardNumber(cards);
        Collections.sort(cardNumbers, cardNumberComparator);
        Collections.reverse(cardNumbers);
        return cardNumbers.subList(0, 5);
    }
//排序
    private List<Card> getCardsSortedByNumber(List<Card> cards) {
        List<Card> cardsSortedByNumber = new ArrayList<Card>(cards);
        Collections.sort(cardsSortedByNumber);

        return cardsSortedByNumber;
    }

    private List<CardNumber> calculateSameKindTie(Integer sameKindCount,
            CardNumber sameKindCardNumber, List<Card> cardsSortedByNumber) {
        List<CardNumber> tieBreakingInformation = new ArrayList<CardNumber>();
        tieBreakingInformation.add(sameKindCardNumber);

        int left = 5 - sameKindCount;
        for (int i = cardsSortedByNumber.size() - 1; i >= 0; i--) {
            Card card = cardsSortedByNumber.get(i);

            if (!card.getNumber().equals(sameKindCardNumber) && left > 0) {
                tieBreakingInformation.add(card.getNumber());
                left--;
            }
        }

        return tieBreakingInformation;
    }

    private CardNumber getCardNumberForCount(Integer count,
            MapList<CardNumber, Card> numberGroup) {
        for (Map.Entry<CardNumber, List<Card>> entry : numberGroup.entrySet()) {
            if (entry.getValue().size() == count) {
                return entry.getKey();
            }
        }
        return null;
    }

    private CardNumber getStraight(MapList<CardNumber, Card> numberGroup) {
        List<CardNumber> cardNumbers = new ArrayList<CardNumber>(
                numberGroup.keySet());
        return getStraightNumber(cardNumbers);
    }

    private CardNumber getStraightFlushNumber(MapList<CardSuit, Card> suitGroup) {
        CardSuit flushSuit = getFlush(suitGroup);
        if (flushSuit == null) {
            return null;
        }

        List<Card> cards = suitGroup.get(flushSuit);
        List<CardNumber> cardNumbers = cardsToCardNumber(cards);

        return getStraightNumber(cardNumbers);
    }

    private List<CardNumber> cardsToCardNumber(List<Card> cards) {
        List<CardNumber> cardNumbers = new ArrayList<CardNumber>();

        for (Card card : cards) {
            cardNumbers.add(card.getNumber());
        }
        return cardNumbers;
    }
//找顺子
    private CardNumber getStraightNumber(List<CardNumber> cardNumbers) {
        CardNumber straightNumber = null;
        int straightCount = 1;
        int prevPower = 0;
        Collections.sort(cardNumbers, cardNumberComparator);
        for (CardNumber cardNumber : cardNumbers) {
            if (cardNumber.getPower() == prevPower + 1) {
                straightCount++;
                if (straightCount >= 5) {
                    straightNumber = cardNumber;
                }
            } else {
                straightCount = 1;
            }
            prevPower = cardNumber.getPower();
        }
        return straightNumber;
    }
//找同花
    private CardSuit getFlush(MapList<CardSuit, Card> suitGroup) {
        for (List<Card> cards : suitGroup) {
            if (cards.size() >= 5) {
                return cards.get(0).getSuit();
            }
        }
        return null;
    }
    
    
//建立map数据
    private MapList<CardNumber, Card> getNumberGroup(List<Card> cards) {
        MapList<CardNumber, Card> numberGroup = new MapList<CardNumber, Card>();
        for (Card card : cards) {
            numberGroup.add(card.getNumber(), card);
        }
        return numberGroup;
    }

    private MapList<CardSuit, Card> getSuitGroup(List<Card> cards) {
        MapList<CardSuit, Card> suitGroup = new MapList<CardSuit, Card>();
        for (Card card : cards) {
            suitGroup.add(card.getSuit(), card);
        }
        return suitGroup;
    }
}
