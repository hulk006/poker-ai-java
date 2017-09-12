package    yh.ai.model.cards;
//CardNumber是一个枚举，CardNunber（“2”，2）代表cardNUMBER two
public enum CardNumber {
	//利用CardNumber（ ）的构造方法来定义了 ，这么多的枚举变量
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    ACE("A", 14);

    private final String symbol;
    private final int power;
    //构造方法
    private CardNumber(String symbol, int power) {//构造方法，一个card（“”，int）给card的初始化
        this.symbol = symbol;
        this.power = power;
    }

    @Override
    public String toString() {//公用函数，得到symbol
        return symbol;
    }

    public int getPower() {//得到power
        return power;
    }    
}
