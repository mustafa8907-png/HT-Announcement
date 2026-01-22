package com.mustafa8907.htannouncement.display;

import org.bukkit.entity.Player;
import java.util.List;

public interface DisplayHandler {
    void send(Player player, List<String> messages);
}
