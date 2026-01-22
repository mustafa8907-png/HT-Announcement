package com.mustafa8907.htannouncement.util;

public class TimeParser {
    public static long parseStringToTicks(String input) {
        if (input == null || input.isEmpty()) return 6000L;
        long mul = 20;
        String unit = input.replaceAll("[0-9]", "").toLowerCase();
        if (unit.contains("m")) mul = 1200;
        else if (unit.contains("h")) mul = 72000;
        else if (unit.contains("d")) mul = 1728000;
        try { return Long.parseLong(input.replaceAll("[^0-9]", "")) * mul; } 
        catch (NumberFormatException e) { return 6000L; }
    }
}
