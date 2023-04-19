package me.not_black.spawnutils.event;

import me.not_black.spawnutils.PlayerData;
import me.not_black.spawnutils.SpawnUtils;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class RespawnListener implements Listener {
    private final SpawnUtils plugin;

    public RespawnListener(SpawnUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        PlayerData data = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        data.setRespawnTimestamp(System.currentTimeMillis() + plugin.getConfig().getInt("respawningTime") * 1000L);
        plugin.getPlayerManager().setPlayer(data);
    }
}
