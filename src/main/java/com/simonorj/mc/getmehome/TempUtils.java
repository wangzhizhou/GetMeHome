package com.simonorj.mc.getmehome;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class TempUtils {
    public static TextColor legacyChar2TextColor(char legacyChar) {
        return switch (legacyChar) {
            case '0' -> NamedTextColor.BLACK;
            case '1' -> NamedTextColor.DARK_BLUE;
            case '2' -> NamedTextColor.DARK_GREEN;
            case '3' -> NamedTextColor.DARK_AQUA;
            case '4' -> NamedTextColor.DARK_RED;
            case '5' -> NamedTextColor.DARK_PURPLE;
            case '6' -> NamedTextColor.GOLD;
            case '7' -> NamedTextColor.GRAY;
            case '8' -> NamedTextColor.DARK_GRAY;
            case '9' -> NamedTextColor.BLUE;
            case 'a' -> NamedTextColor.GREEN;
            case 'b' -> NamedTextColor.AQUA;
            case 'c' -> NamedTextColor.RED;
            case 'd' -> NamedTextColor.LIGHT_PURPLE;
            case 'e' -> NamedTextColor.YELLOW;
            case 'f' -> NamedTextColor.WHITE;
            default -> NamedTextColor.WHITE; // 默认回退
        };
    }

    public static TextComponent legacyString2Component(String legacyString) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(legacyString);
    }

    public static boolean isNotEmptyComponent(Component component) {
        if (component == null) {
            return false;
        }

        if (component instanceof TextComponent text) {
            if (!text.content().isEmpty()) {
                return true;
            }
        }

        // 检查子组件
        if (!component.children().isEmpty()) {
            return true;
        }

        // 检查是否有样式（如果有样式，即使无内容也不完全算"空"）
        return !component.style().equals(Style.empty());
    }

}
