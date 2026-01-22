package com.mustafa8907.htannouncement.display.impl;

import com.mustafa8907.htannouncement.display.DisplayHandler;
import org.bukkit.entity.Player;
import java.util.List;

public class TitleDisplay implements DisplayHandler {
    @Override
    public void send(Player player, List<String> messages) {
        String title = messages.isEmpty() ? "" : messages.get(0);
        String subtitle = messages.size() > 1 ? messages.get(1) : "";
        player.sendTitle(title, subtitle, 10, 70, 20);
    }
}
