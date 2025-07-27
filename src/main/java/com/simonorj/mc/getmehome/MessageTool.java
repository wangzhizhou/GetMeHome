package com.simonorj.mc.getmehome;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageTool {
    private static ClassLoader loader = null;

    private static String base(I18n i18n, Locale locale, Object... args) {
        String msg = getBundleString(i18n, locale);

        return String.format(msg, args);
    }

    private static String getBundleString(I18n i18n, Locale locale) {
        if (loader != null) {
            try {
                return ResourceBundle.getBundle("GetMeHome", locale, loader).getString(i18n.toString());
            } catch (NullPointerException | MissingResourceException ignore) {
            }
        }

        return ResourceBundle.getBundle("i18n.GetMeHome", locale).getString(i18n.toString());
    }

    static void reloadI18n(File i18nFolder) {
        if (!i18nFolder.isDirectory()) {
            loader = null;
            return;
        }

        try {
            URL[] urls = {i18nFolder.toURI().toURL()};
            loader = new URLClassLoader(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            loader = null;
        }
    }

    public static String raw(I18n i18n, CommandSender p, Object... args) {
        return base(i18n, getLocale(p), args);
    }

    public static TextComponent prefixed(I18n i18n, CommandSender p, Object... args) {
        TextComponent pre = GetMeHome.getInstance().getPrefix();
        if (TempUtils.isNotEmptyComponent(pre)) pre = pre.append(Component.space());

        TextColor focus = GetMeHome.getInstance().getFocusColor();
        TextColor content = GetMeHome.getInstance().getContentColor();

        for (int i = args.length - 1; i >= 0; i--) {
            args[i] = focus + args[i].toString() + content;
        }

        String legacyBaseString = base(i18n, getLocale(p), args);

        return pre.append(TempUtils.legacyString2Component(legacyBaseString));
    }

    public static TextComponent error(I18n i18n, CommandSender p, Object... args) {
        TextComponent pre = GetMeHome.getInstance().getPrefix();
        if (TempUtils.isNotEmptyComponent(pre)) pre = pre.append(Component.space());
        for (int i = args.length - 1; i >= 0; i--) {
            args[i] = args[i].toString();
        }
        String legacyBaseString = base(i18n, getLocale(p), args);
        return pre.append(TempUtils.legacyString2Component(legacyBaseString));
    }

    private static Locale getLocale(CommandSender sender) {
        if (!(sender instanceof Player p))
            return Locale.getDefault();
        return p.locale();
    }
}
