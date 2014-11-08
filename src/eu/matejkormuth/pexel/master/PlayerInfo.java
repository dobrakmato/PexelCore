package eu.matejkormuth.pexel.master;

import java.util.UUID;

/**
 * Class that represents network player.
 */
public class PlayerInfo {
    public final UUID   uuid;
    public final String lastName;
    public final String email;
    
    public PlayerInfo(final UUID uuid, final String lastName, final String email) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.email = email;
    }
}
