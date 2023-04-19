package me.not_black.spawnutils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public final class RespawningController extends BukkitRunnable {

    private final PlayerManager playerManager;
    private final SpawnUtils plugin;

    public RespawningController(SpawnUtils plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (UUID i : playerManager.getRespawningList()) {
            PlayerData data = playerManager.getPlayer(i);
            if (System.currentTimeMillis() >= data.getRespawnTimestamp()) {
                data.setRespawnTimestamp(-1);
                OfflinePlayer player = Bukkit.getOfflinePlayer(data.getUUID());
                if (player.isOnline()) {
                    plugin.randomSpread(player.getPlayer(), data.getLastDeath(), plugin.getConfig().getInt("rangeOnDeathRtp"));
                    player.getPlayer().setGameMode(GameMode.SURVIVAL);
                } else data.setRespawnImmediately(true);
                playerManager.setPlayer(data);
            } else {
                OfflinePlayer player = Bukkit.getOfflinePlayer(data.getUUID());
                if (player.isOnline())
                    ActionBarUtils.sendActionBar(player.getPlayer(), formatTime(data.getRespawnTimestamp() - System.currentTimeMillis()));
            }
        }
    }

    private String formatTime(long time) {
        int totalSec = (int) time / 1000;
        int sec = totalSec % 60;
        int min = totalSec / 60;
        return String.format("%d:%d", min, sec);
    }
}
