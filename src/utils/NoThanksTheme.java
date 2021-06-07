package utils;

import com.formdev.flatlaf.FlatLaf;

public class NoThanksTheme extends FlatLaf {
    public static final String NAME = "FlatLaf Light";

    public NoThanksTheme() {
    }

    public static boolean install() {
        return install(new NoThanksTheme());
    }

    public static void installLafInfo() {
        installLafInfo("No Thanks Theme - @Author:ziu", NoThanksTheme.class);
    }

    public String getName() {
        return "No Thanks Theme";
    }

    public String getDescription() {
        return "No Thanks Theme Look and Feel";
    }

    public boolean isDark() {
        return false;
    }
}
