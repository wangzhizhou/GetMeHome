package com.simonorj.mc.getmehome.config;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.simonorj.mc.getmehome.GetMeHome;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Immutable
public class PermValue {
    public static final String PERM_LIST_NODE = "perm";
    private static final String VALUE_LIST_NODE = "value";
    private static final String OPERATION_LIST_NODE = "operation";
    private static final String WORLDS_LIST_NODE = "worlds";
    public final String perm;
    public final int val;
    public final Operation oper;
    public final List<String> worlds;

    private PermValue(String perm, int val, Operation oper, List<String> worlds) {
        this.perm = perm;
        this.val = val;
        this.oper = oper;
        this.worlds = worlds;
    }

    static List<PermValue> parseList(List<Map<?, ?>> mapList) {
        ArrayList<PermValue> ret = new ArrayList<>();

        for (Map<?, ?> l : mapList) {
            try {
                ret.add(parsePermissionGroup(l));
            } catch (InvalidConfigurationException | ClassCastException e) {
                GetMeHome.getInstance().getLogger().log(Level.WARNING, "Could not parse a configuration group", e);
            }
        }
        return ret;
    }

    private static PermValue parsePermissionGroup(Map<?, ?> m) throws InvalidConfigurationException {
        String perm = (String) m.get(PERM_LIST_NODE);
        if (perm == null) {
            throw new InvalidConfigurationException("'perm' is not defined in configuration");
        }

        Integer value = (Integer) m.get(VALUE_LIST_NODE);

        if (value == null) {
            throw new InvalidConfigurationException("'value' for '" + perm + "' perm is not defined in configuration");
        }

        Operation oper = Operation.fromString((String) m.get(OPERATION_LIST_NODE));
        List<String> worlds = parseWorlds((List) m.get(WORLDS_LIST_NODE));

        return new PermValue(perm, value, oper, worlds);
    }

    private static List<String> parseWorlds(List wList) {
        if (wList != null) {
            if (wList.isEmpty())
                return null;

            ImmutableList.Builder<String> worldsBuilder = ImmutableList.builder();
            for (Object o : wList)
                worldsBuilder.add(((String) o).toLowerCase());

            return worldsBuilder.build();
        }
        return null;
    }

    enum Operation {
        SET, WORLD, ADD, SUB;

        private static Operation fromString(String input) {
            if (input == null)
                return SET;

            return switch (input.toLowerCase()) {
                case "world", "worlds" -> WORLD;
                case "add", "plus" -> ADD;
                case "sub", "subtract", "minus" -> SUB;
                default -> SET;
            };
        }
    }
}
