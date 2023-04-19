package me.not_black.spawnutils.event;

import me.not_black.spawnutils.PlayerData;
import me.not_black.spawnutils.PlayerManager;
import me.not_black.spawnutils.SpawnUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class JoinListener implements Listener {
    private final PlayerManager pm;
    private final SpawnUtils plugin;

    public JoinListener(SpawnUtils plugin) {
        this.pm = plugin.getPlayerManager();
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (event.getPlayer().hasPlayedBefore()) {
            PlayerData tmp = pm.getPlayer(event.getPlayer().getUniqueId());
            if (tmp.isRespawnImmediately()) {
                tmp.setRespawnImmediately(false);
                pm.setPlayer(tmp);
                event.getPlayer().setGameMode(GameMode.SURVIVAL);
                if (event.getPlayer().getBedSpawnLocation()!=null) event.getPlayer().teleport(event.getPlayer().getBedSpawnLocation());
                else {
                    plugin.randomSpread(event.getPlayer(), tmp.getLastDeath(), plugin.getConfig().getInt("rangeOnDeathRtp"));
                }
            }
        } else {
            Location l1 = new Location(event.getPlayer().getWorld(), plugin.getConfig().getInt("range.xmin"), 0, plugin.getConfig().getInt("range.zmin"));
            Location l2 = new Location(event.getPlayer().getWorld(), plugin.getConfig().getInt("range.xmax"), 255, plugin.getConfig().getInt("range.zmax"));
            plugin.randomSpawn(event.getPlayer(), l1, l2);
            pm.setPlayer(new PlayerData(event.getPlayer().getUniqueId(), null, -1, false));
        }
    }
}
