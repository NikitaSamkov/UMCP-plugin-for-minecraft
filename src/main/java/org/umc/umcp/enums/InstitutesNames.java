package org.umc.umcp.enums;

public enum InstitutesNames {
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
    FTI("ФТИ");

    public String name;

    private InstitutesNames(String name) {
        this.name = name;
    }
}
