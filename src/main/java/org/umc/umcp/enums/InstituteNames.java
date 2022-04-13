package org.umc.umcp.enums;

public enum InstituteNames {
    IENIM("ИЕНиМ"),
    INMIT("ИНМиТ"),
    RTF("ИРИТ-РТФ"),
    ISA("ИСА"),
    IFKSIMP("ИФКСиМП"),
    INFO("ИНФО"),
    HTI("ХТИ"),
    UGI("УГИ"),
    INEU("ИНЭУ"),
    URALENIN("УралЭНИН"),
    FTI("ФТИ"),
    NONE("None");

    public String name;

    private InstituteNames(String name) {
        this.name = name;
    }
}
