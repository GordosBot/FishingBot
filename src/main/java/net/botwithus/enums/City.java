package net.botwithus.enums;

public enum City {
    DRAYOR_VILLAGE("Draynor Village", 12338),
    LUMBRIDGE("Lumbridge", 12850);

    private String name;
    private int regionId;

    City(String name, int regionId) {
        this.name = name;
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public int getRegionId() {
        return regionId;
    }

    public static City getName (int regionId) {
        for (City city : City.values()) {
            if (city.getRegionId() == regionId) {
                return city;
            }
        }
        return null;
    }
}


