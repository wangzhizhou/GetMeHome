package com.simonorj.mc.getmehome;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public final class TempUtils {
    public static TextColor convertChatColor(ChatColor chatColor) {
        return switch (chatColor) {
            case BLACK -> NamedTextColor.BLACK;
            case DARK_BLUE -> NamedTextColor.DARK_BLUE;
            case DARK_GREEN -> NamedTextColor.DARK_GREEN;
            case DARK_AQUA -> NamedTextColor.DARK_AQUA;
            case DARK_RED -> NamedTextColor.DARK_RED;
            case DARK_PURPLE -> NamedTextColor.DARK_PURPLE;
            case GOLD -> NamedTextColor.GOLD;
            case GRAY -> NamedTextColor.GRAY;
            case DARK_GRAY -> NamedTextColor.DARK_GRAY;
            case BLUE -> NamedTextColor.BLUE;
            case GREEN -> NamedTextColor.GREEN;
            case AQUA -> NamedTextColor.AQUA;
            case RED -> NamedTextColor.RED;
            case LIGHT_PURPLE -> NamedTextColor.LIGHT_PURPLE;
            case YELLOW -> NamedTextColor.YELLOW;
            case WHITE -> NamedTextColor.WHITE;
            default -> NamedTextColor.WHITE; // 默认回退
        };
    }

    public static String compoent2LegacyString(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static TextComponent legacyString2Component(String legacyString) {
        return LegacyComponentSerializer.legacySection().deserialize(legacyString);
    }

    public static boolean isEmpty(Component component) {
        if (component == null) {
            return true;
        }

        if (component instanceof TextComponent text) {
            if (!text.content().isEmpty()) {
                return false;
            }
        }

        // 检查子组件
        if (!component.children().isEmpty()) {
            return false;
        }

        // 检查是否有样式（如果有样式，即使无内容也不完全算"空"）
        return component.style().equals(Style.empty());
    }
}
