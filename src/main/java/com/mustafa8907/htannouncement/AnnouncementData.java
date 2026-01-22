package com.mustafa8907.htannouncement;

import com.mustafa8907.htannouncement.display.DisplayHandler;
import org.bukkit.Sound;
import java.util.List;

public class AnnouncementData {
    private final String id;
    private final List<String> messages;
    private final Sound sound;
    private final DisplayHandler handler;

    public AnnouncementData(String id, List<String> messages, Sound sound, DisplayHandler handler) {
        this.id = id;
        this.messages = messages;
        this.sound = sound;
        this.handler = handler;
    }

    public String getId() { return id; }
    public List<String> getMessages() { return messages; }
    public Sound getSound() { return sound; }
    public DisplayHandler getHandler() { return handler; }
}
