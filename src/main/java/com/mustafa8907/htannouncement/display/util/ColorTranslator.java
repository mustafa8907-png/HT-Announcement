package com.mustafa8907.htannouncement.util;

import net.md_5.bungee.api.ChatColor;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTranslator {
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6})>(.*?)</gradient>");
    private static final boolean SUPPORTS_RGB;

    static {
        boolean rgbCheck = false;
        try {
            ChatColor.of("#FFFFFF");
            rgbCheck = true;
        } catch (NoSuchMethodError | Exception e) { rgbCheck = false; }
        SUPPORTS_RGB = rgbCheck;
    }

    public static String translate(String message) {
        if (message == null || message.isEmpty()) return "";
        if (SUPPORTS_RGB) {
            Matcher matcher = GRADIENT_PATTERN.matcher(message);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(buffer, applyGradient(matcher.group(3), matcher.group(1), matcher.group(2)));
            }
            matcher.appendTail(buffer);
            message = buffer.toString();
        } else {
            message = message.replaceAll("<gradient:#[A-Fa-f0-9]{6}:#[A-Fa-f0-9]{6}>", "").replaceAll("</gradient>", "");
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String applyGradient(String text, String start, String end) {
        StringBuilder builder = new StringBuilder();
        Color s = hexToColor(start), e = hexToColor(end);
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            double r = (double) i / (double) (chars.length - 1);
            Color c = new Color(
                (int) (s.getRed() * (1 - r) + e.getRed() * r),
                (int) (s.getGreen() * (1 - r) + e.getGreen() * r),
                (int) (s.getBlue() * (1 - r) + e.getBlue() * r)
            );
            builder.append(ChatColor.of(String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()))).append(chars[i]);
        }
        return builder.toString();
    }
    private static Color hexToColor(String hex) {
        return new Color(Integer.valueOf(hex.substring(1, 3), 16), Integer.valueOf(hex.substring(3, 5), 16), Integer.valueOf(hex.substring(5, 7), 16));
    }
}
