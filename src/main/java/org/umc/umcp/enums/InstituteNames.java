package org.umc.umcp.enums;

import org.umc.umcp.Main;

public enum InstituteNames {
    IENIM("ИЕНиМ", "ienim"),
    INMIT("ИНМиТ", "inmit"),
    RTF("ИРИТ-РТФ", "rtf"),
    ISA("ИСА", "isa"),
    IFKSIMP("ИФКСиМП", "ifksimp"),
    INFO("ИНФО", "info"),
    HTI("ХТИ", "hti"),
    UGI("УГИ", "ugi"),
    INEU("ИНЭУ", "ineu"),
    URALENIN("УралЭНИН", "uralenin"),
    FTI("ФТИ", "fti"),
    NONE("None", "none");

    public String name;
    public String permission;

    private InstituteNames(String name, String configPermissionValue) {
        this.name = name;
        this.permission = Main.config.getString(String.format("permissions.%s", configPermissionValue));
    }
}
