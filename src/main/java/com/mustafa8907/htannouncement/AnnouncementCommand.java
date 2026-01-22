package com.mustafa8907.htannouncement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnnouncementCommand implements CommandExecutor {

    private final HTAnnouncementPlugin plugin;
    private final AnnouncementManager manager;

    public AnnouncementCommand(HTAnnouncementPlugin plugin, AnnouncementManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "HT-Announcement " + ChatColor.GRAY + "by mustafa8907");
            sender.sendMessage(ChatColor.YELLOW + "/announcement reload " + ChatColor.WHITE + "- Plugini yeniler");
            sender.sendMessage(ChatColor.YELLOW + "/announcement start <isim> " + ChatColor.WHITE + "- Duyuruyu aninda yapar");
            sender.sendMessage(ChatColor.YELLOW + "/announcement player <oyuncu> <isim> " + ChatColor.WHITE + "- Oyuncuya ozel duyuru");
            sender.sendMessage(ChatColor.YELLOW + "/announcement toggle " + ChatColor.WHITE + "- Duyurulari kapat/ac");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                if (!sender.hasPermission("htannouncement.admin")) {
                    sender.sendMessage(ChatColor.RED + "Yetkiniz yok!");
                    return true;
                }
                manager.loadAnnouncements();
                sender.sendMessage(ChatColor.GREEN + "HT-Announcement basariyla yenilendi.");
                break;

            case "toggle":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Bu komutu sadece oyuncular kullanabilir.");
                    return true;
                }
                boolean state = manager.togglePlayer((Player) sender);
                if (state) {
                    sender.sendMessage(ChatColor.GREEN + "Duyurular artik gorunuyor.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Duyurular kapatildi.");
                }
                break;

            case "start":
                if (!sender.hasPermission("htannouncement.admin")) return true;
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Kullanim: /announcement start <dosyaIsmi>");
                    return true;
                }
                String annId = args[1];
                if (manager.broadcastAnnouncement(annId)) {
                    sender.sendMessage(ChatColor.GREEN + "Duyuru baslatildi: " + annId);
                } else {
                    sender.sendMessage(ChatColor.RED + "Duyuru bulunamadi! (Configde dosya ismini .yml olmadan yaz)");
                }
                break;

            case "player":
                if (!sender.hasPermission("htannouncement.admin")) return true;
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Kullanim: /announcement player <oyuncu> <dosyaIsmi>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadi.");
                    return true;
                }
                String pAnnId = args[2];
                if (manager.sendSpecific(target, pAnnId)) {
                    sender.sendMessage(ChatColor.GREEN + "Duyuru gonderildi: " + target.getName());
                } else {
                    sender.sendMessage(ChatColor.RED + "Duyuru bulunamadi!");
                }
                break;
        }

        return true;
    }
}
