package me.not_black.spawnutils;

import me.not_black.spawnutils.command.MainCommand;
import me.not_black.spawnutils.event.DeathListener;
import me.not_black.spawnutils.event.JoinListener;
import me.not_black.spawnutils.event.RespawnListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public final class SpawnUtils extends JavaPlugin {
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        playerManager = new PlayerManager(this);
        reload();
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RespawnListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);
        Objects.requireNonNull(getCommand("spawnutils")).setExecutor(new MainCommand(this));
        Objects.requireNonNull(getCommand("spawnutils")).setTabCompleter(new MainCommand(this));
        this.getLogger().info("Plugin loaded.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Plugin unloaded.");
    }

    public void reload() {
        reloadConfig();
        Bukkit.getScheduler().cancelTasks(this);
        playerManager.reload();
        new RespawningController(this).runTaskTimer(this, 20L, 20L);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void randomSpawn(Player p, Location l1, Location l2) {
        Random random = new Random();
        int xmax = Math.max(l1.getBlockX(), l2.getBlockX()), xmin = Math.min(l1.getBlockX(), l2.getBlockX());
        int zmax = Math.max(l1.getBlockZ(), l2.getBlockZ()), zmin = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int tries = this.getConfig().getInt("rtpTries");
        for (int i = 1; i <= tries; i++) {
            int tx = randomInSection(random, xmin, xmax);
            int tz = randomInSection(random, zmin, zmax);
            Block top = Objects.requireNonNull(Bukkit.getWorld(Objects.requireNonNull(this.getConfig().getString("overworldName")))).getHighestBlockAt(tx, tz);
            if (top.isLiquid()) continue;
            int ty = top.getY();
            if (ty > 200) continue;
            p.teleport(new Location(top.getWorld(), tx, ty + 1, tz));
            return;
        }
        p.sendMessage(ChatColor.RED + "Failed to rtp with " + tries + " tries!");
    }

    public void randomSpread(Player p, Location center, int range) {
        Location l1 = new Location(center.getWorld(), center.getX() - range, 0, center.getZ() - range);
        Location l2 = new Location(center.getWorld(), center.getX() + range, 0, center.getZ() + range);
        randomSpawn(p, l1, l2);
    }

    public int randomInSection(Random r, int min, int max) {
        return r.nextInt(max - min + 1) + min;
    }
}
