package com.simonorj.mc.getmehome.command;

import com.simonorj.mc.getmehome.GetMeHome;
import com.simonorj.mc.getmehome.I18n;
import com.simonorj.mc.getmehome.config.YamlPermValue;
import com.simonorj.mc.getmehome.storage.HomeStorageAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.simonorj.mc.getmehome.MessageTool.*;

public class ListHomesCommand implements TabExecutor {
    private static final String OTHER_PERM = "getmehome.command.listhomes.other";
    private static final String GLOBAL_FLAG = "-global";
    private static final String GLOBAL_SHORT_FLAG = "-g";
    private final GetMeHome plugin;

    public ListHomesCommand(GetMeHome plugin) {
        this.plugin = plugin;
    }

    private HomeStorageAPI getStorage() {
        return plugin.getStorage();
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, final String[] args) {
        // Get player in question
        OfflinePlayer get;
        boolean global;
        String getName;

        if (args.length != 0) {
            if (args[0].equalsIgnoreCase(GLOBAL_FLAG) || args[0].equalsIgnoreCase(GLOBAL_SHORT_FLAG)) {
                global = true;
                getName = args.length > 1 ? args[1] : null;
            } else {
                getName = args[0];
                global = false;
            }
        } else {
            getName = null;
            global = false;
        }

        if (sender.hasPermission(OTHER_PERM) && getName != null) {
            get = plugin.getPlayer(getName);
            if (get == null) {
                sender.sendMessage(error(I18n.CMD_GENERIC_PLAYER_NOT_FOUND, sender));
                return true;
            }
        } else if (sender instanceof Player) {
            get = (Player) sender;
        } else {
            sender.sendMessage("Usage: /listhomes [-global] <player>");
            return true;
        }

        listHomes(sender, get, global);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            List<String> ret = new ArrayList<>();
            String lower = args[0].toLowerCase();

            if (sender.hasPermission(OTHER_PERM)) {
                plugin.getServer().getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(lower)) {
                        ret.add(p.getName());
                    }
                });
            }

            if (GLOBAL_FLAG.startsWith(lower)) ret.add(GLOBAL_FLAG);

            return ret;
        } else if (args.length == 2 && sender.hasPermission(OTHER_PERM) && args[0].equalsIgnoreCase(GLOBAL_FLAG)) {
            return null;
        }

        return Collections.emptyList();
    }

    private void listHomes(CommandSender sender, OfflinePlayer target, boolean global) {
        YamlPermValue.WorldValue wv = (target instanceof Player) ? plugin.getLimit().calcFor((Player) target) : null;

        // Get home names owned by player
        Map<String, Location> homes = getStorage().getAllHomes(target.getUniqueId(), global || wv == null ? null : wv.worlds);
        String defaultHome = getStorage().getDefaultHomeName(target.getUniqueId());

        Iterator<Map.Entry<String, Location>> i = homes.entrySet().iterator();
        AtomicInteger exempt = new AtomicInteger(0);
        StringBuilder list;

        if (i.hasNext()) {

            ChatColor f = plugin.getFocusColor();
            ChatColor c = plugin.getContentColor();
            list = new StringBuilder(homeName(i.next(), wv, exempt, defaultHome));

            while (i.hasNext()) {
                list.append(c).append(", ").append(f).append(homeName(i.next(), wv, exempt, defaultHome));
            }
        } else {
            list = new StringBuilder(ChatColor.ITALIC.toString()).append(raw(I18n.CMD_LISTHOMES_NONE, sender));
        }

        Object total = wv == null ? null : wv.worlds != null && global ? "?" : wv.value;
        Object count = exempt.get() == 0 ? homes.size() : (homes.size() - exempt.get()) + "(+" + exempt.get() + ")";

        if (target == sender)
            sender.sendMessage(prefixed(I18n.CMD_LISTHOMES_SELF, sender, count, total, list.toString()));
        else if (target instanceof Player)
            sender.sendMessage(prefixed(I18n.CMD_LISTHOMES_OTHER, sender, target.getName(), count, total, list.toString()));
        else
            sender.sendMessage(prefixed(I18n.CMD_LISTHOMES_OTHER_OFFLINE, sender, target.getName(), count, list.toString()));
    }

    private String homeName(Map.Entry<String, Location> d, YamlPermValue.WorldValue wv, AtomicInteger exempt, String defaultHome) {
        boolean deductable = false;
        if (wv != null && wv.deducts != null) {
            String world = d.getValue().getWorld().getName().toLowerCase();

            for (YamlPermValue.WorldValue wvd : wv.deducts) {
                if (wvd.worlds.contains(world)) {
                    deductable = true;

                    if (wvd.value != 0) {
                        if (wvd.value != -1) wvd.value--;
                        exempt.incrementAndGet();
                        break;
                    }
                }
            }
        }

        StringBuilder ret = new StringBuilder();
        if (d.getKey().equals(defaultHome)) ret.append(ChatColor.BOLD);
        if (deductable) ret.append(ChatColor.ITALIC);
        ret.append(d.getKey()).append(ChatColor.RESET);
        return ret.toString();
    }
}
