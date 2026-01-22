package com.mustafa8907.htannouncement.display.impl;

import com.mustafa8907.htannouncement.display.DisplayHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import java.util.List;

public class ActionbarDisplay implements DisplayHandler {
    @Override
    public void send(Player player, List<String> messages) {
        String joinedMessage = String.join(" ", messages);
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(joinedMessage));
        } catch (NoSuchMethodError | Exception e) {
            player.sendMessage(joinedMessage);
        }
    }
}
