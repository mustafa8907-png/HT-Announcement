package com.mustafa8907.htannouncement;

import com.mustafa8907.htannouncement.display.DisplayHandler;
import com.mustafa8907.htannouncement.display.impl.ActionbarDisplay;
import com.mustafa8907.htannouncement.display.impl.ChatDisplay;
import com.mustafa8907.htannouncement.display.impl.TitleDisplay;
import com.mustafa8907.htannouncement.util.ColorTranslator;
import com.mustafa8907.htannouncement.util.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class AnnouncementManager {

    private final HTAnnouncementPlugin plugin;
    private final Map<String, BukkitTask> activeTasks = new HashMap<>();
    private final Map<String, DisplayHandler> handlers = new HashMap<>();
    private final Map<String, AnnouncementData> cachedAnnouncements = new HashMap<>();
    private final Set<UUID> disabledPlayers = new HashSet<>();

    public AnnouncementManager(HTAnnouncementPlugin plugin) {
        this.plugin = plugin;
        setupHandlers();
    }

    private void setupHandlers() {
        handlers.put("CHAT", new ChatDisplay());
        handlers.put("ACTIONBAR", new ActionbarDisplay());
        handlers.put("TITLE", new TitleDisplay());
        handlers.put("SUBTITLE", new TitleDisplay());
    }

    public void stopAllTasks() {
        activeTasks.values().forEach(BukkitTask::cancel);
        activeTasks.clear();
        cachedAnnouncements.clear();
    }

    public void loadAnnouncements() {
        stopAllTasks();
        plugin.reloadConfig();

        List<String> filesToLoad = plugin.getConfig().getStringList("announcement_files");
        File dir = new File(plugin.getDataFolder(), "announcements");

        int loadedCount = 0;
        for (String fileName : filesToLoad) {
            File file = new File(dir, fileName);
            if (!file.exists()) {
                plugin.getLogger().warning("Dosya bulunamadi: " + fileName);
                continue;
            }
            String id = fileName.replace(".yml", "");
            loadSingleAnnouncement(id, YamlConfiguration.loadConfiguration(file));
            loadedCount++;
        }
        plugin.getLogger().info("Toplam " + loadedCount + " duyuru basariyla yuklendi.");
    }

    private void loadSingleAnnouncement(String id, FileConfiguration data) {
        List<String> rawMessages = data.getStringList("message");
        List<String> coloredMessages = rawMessages.stream()
                .map(ColorTranslator::translate)
                .collect(Collectors.toList());

        String soundName = data.getString("sound");
        String modeStr = data.getString("display_mode", "CHAT").toUpperCase();
        long ticks = TimeParser.parseStringToTicks(data.getString("delay"));

        Sound sound = null;
        try {
            if (soundName != null && !soundName.isEmpty()) {
                sound = Sound.valueOf(soundName);
            }
        } catch (IllegalArgumentException e) {}

        DisplayHandler handler = handlers.getOrDefault(modeStr, handlers.get("CHAT"));
        AnnouncementData annData = new AnnouncementData(id, coloredMessages, sound, handler);
        
        cachedAnnouncements.put(id, annData);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendAnnouncementToPlayer(player, annData);
            }
        }, ticks, ticks);

        activeTasks.put(id, task);
    }

    public void sendAnnouncementToPlayer(Player player, AnnouncementData data) {
        if (disabledPlayers.contains(player.getUniqueId())) return;

        if (data.getSound() != null) {
            player.playSound(player.getLocation(), data.getSound(), 1f, 1f);
        }
        data.getHandler().send(player, data.getMessages());
    }

    public boolean togglePlayer(Player player) {
        if (disabledPlayers.contains(player.getUniqueId())) {
            disabledPlayers.remove(player.getUniqueId());
            return true; 
        } else {
            disabledPlayers.add(player.getUniqueId());
            return false;
        }
    }

    public boolean broadcastAnnouncement(String id) {
        AnnouncementData data = cachedAnnouncements.get(id);
        if (data == null) return false;

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendAnnouncementToPlayer(player, data);
        }
        return true;
    }

    public boolean sendSpecific(Player target, String id) {
        AnnouncementData data = cachedAnnouncements.get(id);
        if (data == null) return false;

        if (data.getSound() != null) {
            target.playSound(target.getLocation(), data.getSound(), 1f, 1f);
        }
        data.getHandler().send(target, data.getMessages());
        return true;
    }
}
