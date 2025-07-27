package com.simonorj.mc.getmehome;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigTool {
    private static final String STORAGE_ROOT = "storage";
    private static final String SAVENAME_CHILD = "savename";
    public static final String STORAGE_SAVENAME_NODE = STORAGE_ROOT + "." + SAVENAME_CHILD;

    private static final String MESSAGE_ROOT = "message";
    private static final String PREFIX_CHILD = "prefix";
    private static final String CONTENT_COLOR_CHILD = "content-color";
    private static final String FOCUS_COLOR_CHILD = "focus-color";
    static final String MESSAGE_PREFIX_NODE = MESSAGE_ROOT + "." + PREFIX_CHILD;
    static final String MESSAGE_CONTENT_COLOR_NODE = MESSAGE_ROOT + "." + CONTENT_COLOR_CHILD;
    static final String MESSAGE_FOCUS_COLOR_NODE = MESSAGE_ROOT + "." + FOCUS_COLOR_CHILD;

    static final String WELCOME_HOME_RADIUS_NODE = "welcome-home-radius";

    public static final String CONFIG_VERSION_NODE = "config-version";
    public static final int version = 3;

    private static final String HEADER = """
            # GetMeHome by Simon Chuu
            
            # For help, follow the plugin project link below:
            # https://github.com/SimonOrJ/GetMeHome/
            """;

    private static final String STORAGE = "# Storage Settings\n";

    private static final String STORAGE_SAVENAME = """
              # should the plugin save player names with their UUID?
              # This is only for your reference when looking through the file.
            """;

    private static final String MESSAGE = "";

    private static final String MESSAGE_PREFIX = "  # Prefix for beginning of message. Use & and 0~9, a~f to set colors.\n";

    private static final String MESSAGE_CONTENT_COLOR = """
              # Colors: Accepted values are 0~9, a~f, or named colors.
              # https://minecraft.gamepedia.com/Formatting_codes
            """;

    private static final String MESSAGE_FOCUS_COLOR = "  # Color for the focus points. e.g. home names or player names\n";

    private static final String WELCOME_HOME_RADIUS = """
            # Maximum distance away from home point for "Welcome home" message to not show
            # on /home
            #   Set to -1 to disable "Welcome home" message
            """;

    private static final String CONFIG_VERSION = "# Keeps track of configuration version -- do not change!\n";

    static String saveToString(FileConfiguration config) {
        boolean storageSavename = config.getBoolean(STORAGE_SAVENAME_NODE, true);
        String messagePrefix = config.getString(MESSAGE_PREFIX_NODE, "&6[GetMeHome]");
        String messageContentColor = config.getString(MESSAGE_CONTENT_COLOR_NODE, "e");
        String messageFocusColor = config.getString(MESSAGE_FOCUS_COLOR_NODE, "f");
        int welcomeHomeRadius = config.getInt(WELCOME_HOME_RADIUS_NODE, 4);

        return HEADER + '\n' + STORAGE + STORAGE_ROOT + ":\n" + STORAGE_SAVENAME + "  " + SAVENAME_CHILD + ": " + storageSavename + '\n' + '\n' + MESSAGE + MESSAGE_ROOT + ":\n" + MESSAGE_PREFIX + "  " + PREFIX_CHILD + ": \"" + messagePrefix + "\"\n" + MESSAGE_CONTENT_COLOR + "  " + CONTENT_COLOR_CHILD + ": " + messageContentColor + '\n' + MESSAGE_FOCUS_COLOR + "  " + FOCUS_COLOR_CHILD + ": " + messageFocusColor + '\n' + '\n' + WELCOME_HOME_RADIUS + WELCOME_HOME_RADIUS_NODE + ": " + welcomeHomeRadius + '\n' + '\n' + CONFIG_VERSION + CONFIG_VERSION_NODE + ": " + version + '\n';
    }
}
