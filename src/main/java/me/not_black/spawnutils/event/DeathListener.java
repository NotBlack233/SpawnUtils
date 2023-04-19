package me.not_black.spawnutils.event;

import me.not_black.spawnutils.PlayerData;
import me.not_black.spawnutils.SpawnUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class DeathListener implements Listener {
    private final SpawnUtils plugin;

    public DeathListener(SpawnUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        PlayerData data = plugin.getPlayerManager().getPlayer(event.getEntity().getUniqueId());
        data.setLastDeath(event.getEntity().getLocation());
        plugin.getPlayerManager().setPlayer(data);
    }
}
