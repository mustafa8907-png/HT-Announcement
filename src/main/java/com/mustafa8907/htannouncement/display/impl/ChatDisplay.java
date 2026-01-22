package com.mustafa8907.htannouncement.display.impl;

import com.mustafa8907.htannouncement.display.DisplayHandler;
import org.bukkit.entity.Player;
import java.util.List;

public class ChatDisplay implements DisplayHandler {
    @Override
    public void send(Player player, List<String> messages) {
        for (String msg : messages) {
            player.sendMessage(msg);
        }
    }
}
