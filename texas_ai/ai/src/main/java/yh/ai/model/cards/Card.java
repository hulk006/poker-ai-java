package    yh.ai.model.cards;

//一张牌的表示有花色 和大小
public class Card implements Comparable<Card> 
{
    private final CardSuit suit;
    private final CardNumber number;

    public Card(final CardSuit suit, final CardNumber number) {
        this.suit = suit;
        this.number = number;
    }

    @Override
    public String toString() {
        return suit.toString() + number.toString();
    }
//等与方法，判断牌是花色和大小否相同
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Card)) {
            return false;
        }

        Card other = (Card) obj;

        return suit.equals(other.suit) && number.equals(other.number);
    }

    //比较牌大小的函数
    public int compareTo(Card card) {
        return number.getPower() - card.number.getPower();
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardNumber getNumber() {
        return number;
    }
}
