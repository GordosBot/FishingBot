package net.botwithus.enums;

import net.botwithus.constans.Constants;

import java.util.Arrays;

public enum Fish {

    SHRIMP("Raw shrimps", "Draynor Village", Constants.NET, "", "00"),
    SARDINE("Raw sardine", "Draynor Village", Constants.NET, "Fishing bait", "01"),
    CRAYFISH("Raw crayfish", "Lumbridge", Constants.BAIT, "", "10"),
    TROUT("Raw trout", "Lumbridge", Constants.BAIT, "Feather", "11");

    private String npcName;
    private String area;
    private String fishingMethod;
    private String bait;
    private String code;

    Fish(String npcName, String area, String fishingMethod, String bait, String code) {
        this.npcName = npcName;
        this.area = area;
        this.fishingMethod = fishingMethod;
        this.bait = bait;
        this.code = code;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getArea() {
        return area;
    }

    public String getFishingMethod() {
        return fishingMethod;
    }

    public String getBait() {
        return bait;
    }

    public String getCode() {
        return code;
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

    public static Fish getByCode(String code) {
        return Arrays.stream(values())
                .filter(fish -> fish.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
