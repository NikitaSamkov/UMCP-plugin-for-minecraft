package org.umc.umcp.commands;

import java.util.*;
import java.util.function.Function;

public class Painter {
    private static final String[] defaultPaints = {"2", "3", "4", "5", "6", "7", "9", "a", "b", "c", "d", "e", "f"};

    public static Map<String, String> GetPainter(List<String> elements, String[] paints, Function<String, String> decorations) {
        List<String> painted = PaintElements(elements,
                (paints == null) ? defaultPaints : paints,
                (decorations == null) ? (String s) -> s : decorations);

        Map<String, String> painter = new HashMap<>();
        for (int i = 0; i < elements.size(); i++)
            painter.put(elements.get(i), painted.get(i));
        return painter;
    }

    public static Map<String, String> GetPainter(List<String> elements, String[] paints) {
        return GetPainter(elements, paints, null);
    }

    public static Map<String, String> GetPainter(List<String> elements) {
        return GetPainter(elements, null, null);
    }

    private static List<String> PaintElements(List<String> elements, String[] paints, Function<String, String> decorations) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            result.add("§" + paints[i % paints.length] + decorations.apply(elements.get(i)) + "§f");
        }
        return result;
    }
}
