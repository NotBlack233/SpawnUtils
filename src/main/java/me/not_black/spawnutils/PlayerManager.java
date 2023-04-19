package me.not_black.spawnutils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PlayerManager {
    private final File playersFolder;
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final CopyOnWriteArrayList<UUID> respawningList = new CopyOnWriteArrayList<>();
    private final SpawnUtils plugin;

    public PlayerManager(SpawnUtils plugin) {
        this.plugin = plugin;
        this.playersFolder = new File(plugin.getDataFolder(), "players");
        if (!playersFolder.exists()) playersFolder.mkdir();
        reload();
    }

    public void reload() {
        playerDataMap.clear();
        respawningList.clear();
        if (playersFolder.listFiles() == null) return;
        for (File file : playersFolder.listFiles()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            Location location = fc.getLocation("location");
            long resTime = fc.getLong("resTime");
            boolean resImme = fc.getBoolean("resImme");
            if (System.currentTimeMillis() >= resTime && resTime != -1) {
                resImme = true;
                resTime=-1;
            }
            else respawningList.add(uuid);
            playerDataMap.put(uuid, new PlayerData(uuid, location, resTime, resImme));
        }
    }

    public void setPlayer(PlayerData p) {
        playerDataMap.put(p.getUUID(), p);
        File file = new File(playersFolder, p.getUUID() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        fc.set("location", p.getLastDeath());
        fc.set("resTime", p.getRespawnTimestamp());
        fc.set("resImme", p.isRespawnImmediately());
        if (System.currentTimeMillis() < p.getRespawnTimestamp()) respawningList.add(p.getUUID());
        else respawningList.remove(p.getUUID());
        try {
            fc.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasPlayer(UUID p) {
        return playerDataMap.containsKey(p);
    }

    public PlayerData getPlayer(UUID p) {
        return playerDataMap.get(p);
    }

    public List<UUID> getRespawningList() {
        return respawningList;
    }
}
