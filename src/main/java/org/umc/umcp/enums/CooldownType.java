package org.umc.umcp.enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.umc.umcp.Main;

import java.util.Date;

public enum CooldownType {
    VAPE("vape"),
    INSTITUTE_JOIN("join"),
    ENERGETICS("energetics"),
    BEER("beer");

    public Date time;
    public int acceptableCount;

    private CooldownType(Date time) {
        this.time = time;
    }

    private CooldownType(int time) {
        this.time = new Date(time);
    }

    private CooldownType(int days, int hours, int minutes, int seconds, int milliseconds) {
        this.time = new Date((((days * 24L + hours) * 60L + minutes) * 60L + seconds) * 1000L + milliseconds);
    }

    private CooldownType(@Nullable String s, @NotNull int acceptableCount) {
        if (s == null) {
            s = "0:0:0:0:0";
        }
        String[] params = s.split(":");
        this.time = new Date((((Integer.parseInt(params[0]) * 24L + Integer.parseInt(params[1])) * 60L +
                Integer.parseInt(params[2])) * 60L + Integer.parseInt(params[3])) * 1000L + Integer.parseInt(params[4]));
        this.acceptableCount = acceptableCount;
    }

    private CooldownType(@NotNull String cooldownConfigName) {
        this(Main.config.getString(String.format("cooldowns.%s.Time", cooldownConfigName)),
                Main.config.getInt(String.format("cooldowns.%s.AcceptableCount", cooldownConfigName)));
    }
}
