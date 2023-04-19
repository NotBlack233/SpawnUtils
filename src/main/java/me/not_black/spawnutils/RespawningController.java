package me.not_black.spawnutils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            if (System.currentTimeMillis() >= data.getRespawnTimestamp() && data.getRespawnTimestamp()!=-1) {
                data.setRespawnTimestamp(-1);
                OfflinePlayer player = Bukkit.getOfflinePlayer(data.getUUID());
                if (player.isOnline()) {
                    if (player.getBedSpawnLocation()!=null) {
                        player.getPlayer().setGameMode(GameMode.SURVIVAL);
                        player.getPlayer().teleport(player.getBedSpawnLocation());
                        player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(""));
                        data.setRespawnImmediately(false);
                        playerManager.setPlayer(data);
                        return;
                    }
                    plugin.randomSpread(player.getPlayer(), data.getLastDeath(), plugin.getConfig().getInt("rangeOnDeathRtp"));
                    player.getPlayer().setGameMode(GameMode.SURVIVAL);
                    player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(""));
                    data.setRespawnImmediately(false);
                } else data.setRespawnImmediately(true);
                playerManager.setPlayer(data);
            } else if(data.getRespawnTimestamp()!=-1){
                OfflinePlayer player = Bukkit.getOfflinePlayer(data.getUUID());
                if (player.isOnline()) {
//                    ActionBarUtils.sendActionBar(player.getPlayer(), formatTime(data.getRespawnTimestamp() - System.currentTimeMillis()));
//                    player.getPlayer().sendTitle(" ",formatTime(data.getRespawnTimestamp() - System.currentTimeMillis()),0,40,0);
//                    plugin.getTapi().sendActionbar(player.getPlayer(), formatTime(data.getRespawnTimestamp() - System.currentTimeMillis()));
                    player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN+formatTime(data.getRespawnTimestamp() - System.currentTimeMillis())));
                }
            }
        }
    }

    private String formatTime(long time) {
        int totalSec = (int) time / 1000 + 1;
        int sec = totalSec % 60;
        int min = totalSec / 60;
        if (sec<10) return String.format("%d:0%d", min, sec);
        else return String.format("%d:%d", min, sec);
    }
}
