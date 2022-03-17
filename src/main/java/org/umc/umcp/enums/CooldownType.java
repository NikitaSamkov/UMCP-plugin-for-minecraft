package org.umc.umcp.enums;

import java.util.Date;

public enum CooldownType {
    VAPE(0, 0, 1, 0, 0),
    INSTITUTE_JOIN(2, 0, 0, 0, 0);

    public Date time;

    private CooldownType(Date time) {
        this.time = time;
    }

    private CooldownType(int time) {
        this.time = new Date(time);
    }

    private CooldownType(int days, int hours, int minutes, int seconds, int milliseconds) {
        this.time = new Date((((days * 24L + hours) * 60L + minutes) * 60L + seconds) * 1000L + milliseconds);
    }
}
