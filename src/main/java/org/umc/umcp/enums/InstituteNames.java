package org.umc.umcp.enums;

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

    private InstituteNames(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }
}
