package    yh.ai.model.opponentmodeling;

public class ContextInformation {
    private final ContextAction contextAction;//一个玩家的下注信息
    private final double handStrength;//手牌强度

    public ContextInformation(ContextAction contextAction, double handStrength) {
        this.contextAction = contextAction;
        this.handStrength = handStrength;
    }

    public ContextAction getContextAction() {
        return contextAction;
    }

    public double getHandStrength() {
        return handStrength;
    }
}
