package net.botwithus.enums;

import java.util.Arrays;

public enum Fish {

    SHRIMP("Raw shrimps", "Draynor Village"),
    CRAYFISH("Raw crayfish", "Lumbridge"),
    TROUT("Raw trout", "Lumbridge");

    private String npcName;
    private String area;

    Fish(String npcName, String area) {
        this.npcName = npcName;
        this.area = area;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getArea() {
        return area;
    }

    public static String[] getAreas() {
        return Arrays.stream(values())
                .map(Fish::getArea)
                .distinct()
                .toArray(String[]::new);
    }

    public static String[] getFishesByArea(String area) {
        return Arrays.stream(values())
                .filter(fish -> fish.getArea().equals(area))
                .map(Enum::name)
                .toArray(String[]::new);
    }
}
