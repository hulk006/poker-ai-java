package    yh.ai.model.opponentmodeling;

public enum ContextPlayers {
    FEW,
    MANY;

    public static ContextPlayers valueFor(int numberOfPlayersRemaining) {
        if (numberOfPlayersRemaining < 3) {
            return FEW;
        } else {
            return MANY;
        }
    }
}
