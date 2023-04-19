package me.not_black.spawnutils;

import org.bukkit.Location;

import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private Location lastDeath;
    private long respawnTimestamp;
    private boolean respawnImmediately;

    public PlayerData(UUID uuid, Location lastDeath, long respawnTimestamp, boolean respawnImmediately) {
        this.uuid = uuid;
        this.lastDeath = lastDeath;
        this.respawnTimestamp = respawnTimestamp;
        this.respawnImmediately = respawnImmediately;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isRespawnImmediately() {
        return respawnImmediately;
    }

    public Location getLastDeath() {
        return lastDeath;
    }

    public long getRespawnTimestamp() {
        return respawnTimestamp;
    }

    public void setLastDeath(Location lastDeath) {
        this.lastDeath = lastDeath;
    }

    public void setRespawnImmediately(boolean respawnImmediately) {
        this.respawnImmediately = respawnImmediately;
    }

    public void setRespawnTimestamp(long respawnTimestamp) {
        this.respawnTimestamp = respawnTimestamp;
    }
}
