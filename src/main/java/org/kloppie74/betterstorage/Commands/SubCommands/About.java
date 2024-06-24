package org.kloppie74.betterstorage.Commands.SubCommands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kloppie74.betterstorage.BetterStorage;
import org.kloppie74.betterstorage.Commands.SubCommand;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;


import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getPlayer;

public class About extends SubCommand {


    @Override
    public String getName() {
        return "about";
    }

    @Override
    public String getDescription() {
        return "Get information about the plugin!";
    }

    @Override
    public String getSyntax() {
        return "/betterstorage about";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(player.hasPermission("BetterStorage.commands.about")) {

            player.sendMessage(MSG.chatColors("&7&l&m-----==&r&7&l[ &eBetterStorage &r&7&l]&7&l&m==-----"));
            player.sendMessage(MSG.chatColors("&7"));
            player.sendMessage(MSG.chatColors("&eBetterStorage &7The #1 Storage Plugin!"));
            player.sendMessage(MSG.chatColors("&7Information about &eBetterStorage"));
            player.sendMessage(MSG.chatColors("&7"));
            player.sendMessage(MSG.chatColors("&7&l&m-----==&r&7&l[ &eInformation &r&7&l]&7&l&m==-----"));
            player.sendMessage(MSG.chatColors("&ePlugin Version » &6" + BetterStorage.getBetterStorage().getDescription().getVersion()));
            player.sendMessage(MSG.chatColors("&eServer Version » &6" + Bukkit.getServer().getVersion()));
            player.sendMessage(MSG.chatColors("&eAuthor » &6Kloppie74"));
            player.sendMessage(MSG.chatColors("&eOfficial Discord » &6https://discord.gg/74A4PsQrdv"));
            player.sendMessage(MSG.chatColors("&eSpigot Link » &6https://www.spigotmc.org/resources/betterstorage.115108/"));
            player.sendMessage(MSG.chatColors("&7"));

        } else {
            String AntiswearMSG = FileManager.getInstance().getMessages().getString("Commands.No_Perms");
            AntiswearMSG = PlaceholderAPI.setPlaceholders(getPlayer(player.getName()), AntiswearMSG);

            player.sendMessage(MSG.chatColors(AntiswearMSG));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("about");
        }

        return completions;
    }

}
