package net.initialposition.hardcoremp.models;

import java.sql.Timestamp;
import java.util.UUID;

public class PlayerLogout {

    private final long lastLogin;
    private final java.util.UUID UUID;

    public PlayerLogout(UUID UUID) {
        this.UUID = UUID;

        // generate logout time
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.lastLogin = timestamp.getTime();
    }

    public long getLastLogin() {
        return this.lastLogin;
    }

    public UUID getUUID() {
        return this.UUID;
    }
}
