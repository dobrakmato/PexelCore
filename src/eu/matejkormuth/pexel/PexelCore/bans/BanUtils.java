package eu.matejkormuth.pexel.PexelCore.bans;

public class BanUtils {
    public static final String formatBannedMessage(final BanBase ban) {
        if (ban.isPermanent()) {
            return "You have been banned from " + ban.getBanned().getBannableName()
                    + " permanently!";
        }
        else {
            return "You have been banned from " + ban.getBanned().getBannableName() + " for "
                    + ban.getLength() / 1000 + " seconds!";
        }
    }
}
