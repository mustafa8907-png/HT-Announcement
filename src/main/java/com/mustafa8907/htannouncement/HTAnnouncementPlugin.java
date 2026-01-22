package com.mustafa8907.htannouncement;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class HTAnnouncementPlugin extends JavaPlugin {

    private AnnouncementManager announcementManager;

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "========================================");
        getLogger().info(ChatColor.GREEN + "        HT-Announcement v1.0.0");
        getLogger().info(ChatColor.YELLOW + "         Yapimci: mustafa8907");
        getLogger().info(ChatColor.GREEN + "========================================");

        saveDefaultConfig();
        
        File dir = new File(getDataFolder(), "announcements");
        if (!dir.exists()) {
            dir.mkdirs();
            saveResource("announcements/market.yml", false);
        }

        this.announcementManager = new AnnouncementManager(this);
        this.announcementManager.loadAnnouncements();

        getCommand("announcement").setExecutor(new AnnouncementCommand(this, this.announcementManager));

        getLogger().info(ChatColor.AQUA + "Plugin Aktif! Tum sistemler calisiyor.");
    }

    @Override
    public void onDisable() {
        if (this.announcementManager != null) {
            this.announcementManager.stopAllTasks();
        }
        getLogger().info(ChatColor.RED + "HT-Announcement Deaktif Edildi.");
    }
}
