package me.not_black.spawnutils.command;

import me.not_black.spawnutils.PlayerData;
import me.not_black.spawnutils.SpawnUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class MainCommand implements TabExecutor {

    private final List<String> TAB = new ArrayList<String>() {{
        add("reload");
        add("skip");
    }};
    private final SpawnUtils plugin;

    public MainCommand(SpawnUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("su.reload") && args.length == 1 && args[0].equals("reload")) {
            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "SpawnUtils Reloaded!");
            return true;
        } else if (sender.hasPermission("su.skip") && args.length == 1 && args[0].equals("skip")) {
            if (!(sender instanceof Player)) return false;
            PlayerData data = plugin.getPlayerManager().getPlayer(((Player) sender).getUniqueId());
            data.setRespawnTimestamp(-1);
            plugin.getPlayerManager().setPlayer(data);
            ((Player) sender).setGameMode(GameMode.SURVIVAL);
            ((Player) sender).teleport(data.getLastDeath());
            sender.sendMessage(ChatColor.GREEN + "Skipped!");
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return TAB;
        return null;
    }
}
