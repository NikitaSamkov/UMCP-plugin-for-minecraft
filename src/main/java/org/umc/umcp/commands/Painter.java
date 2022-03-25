package org.umc.umcp.commands;

import org.umc.umcp.Main;
import org.umc.umcp.enums.InstituteNames;

import java.util.*;
import java.util.function.Function;

public class Painter {
    private static final Map<String, String> defaultPaints = new HashMap<>();

    public static void PreparePaints() {
        defaultPaints.put(InstituteNames.IENIM.name, Main.config.getString("ienim.color"));
        defaultPaints.put(InstituteNames.INMIT.name, Main.config.getString("inmit.color"));
        defaultPaints.put(InstituteNames.RTF.name, Main.config.getString("rtf.color"));
        defaultPaints.put(InstituteNames.ISA.name, Main.config.getString("isa.color"));
        defaultPaints.put(InstituteNames.IFKSIMP.name, Main.config.getString("ifksimp.color"));
        defaultPaints.put(InstituteNames.INFO.name, Main.config.getString("info.color"));
        defaultPaints.put(InstituteNames.HTI.name, Main.config.getString("hti.color"));
        defaultPaints.put(InstituteNames.UGI.name, Main.config.getString("ugi.color"));
        defaultPaints.put(InstituteNames.INEU.name, Main.config.getString("ineu.color"));
        defaultPaints.put(InstituteNames.URALENIN.name, Main.config.getString("uralenin.color"));
        defaultPaints.put(InstituteNames.FTI.name, Main.config.getString("fti.color"));
        defaultPaints.put(null, Main.config.getString("abit.color"));
    }

    public static Map<String, String> GetPainter(List<String> elements, Function<String, String> decorations) {
        Map<String, String> painter = new HashMap<>();
        for (String e: elements) {
            String paint = defaultPaints.containsKey(e) ? defaultPaints.get(e) : defaultPaints.get(null);
            painter.put(e, String.format("§%s%s§f", paint, decorations.apply(e)));
        }
        return painter;
    }

    public static Map<String, String> GetPainter(List<String> elements) {
        return GetPainter(elements, (String s) -> s);
    }
}
