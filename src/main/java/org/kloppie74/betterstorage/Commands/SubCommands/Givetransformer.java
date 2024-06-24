package org.kloppie74.betterstorage.Commands.SubCommands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kloppie74.betterstorage.Commands.SubCommand;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getPlayer;
import static org.kloppie74.betterstorage.TransformerBlock.TransformerBlock.PutSkinINFO;

public class Givetransformer extends SubCommand {


    @Override
    public String getName() {
        return "givetransformer";
    }

    @Override
    public String getDescription() {
        return "Give a Transformer to a player!";
    }

    @Override
    public String getSyntax() {
        return "/betterstorage givetransformer";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(player.hasPermission("BetterStorage.commands.givetransformer")) {

            if(!(args.length == 2)) {
                player.sendMessage(org.kloppie74.betterstorage.YMLFiles.MSG.chatColors("&cUsage: /betterstorage givetransformer {player}"));
            } else {
                String playerName = args[1];
                Player player2 = Bukkit.getPlayer(playerName);

                if (player2 == null || !player2.isOnline()) {
                    player.sendMessage(org.kloppie74.betterstorage.YMLFiles.MSG.chatColors("&cPlayer not found or is offline."));
                }

                player.getInventory().addItem(PutSkinINFO());
            }


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
            completions.add("givetransformer");
        }
        return completions;
    }

}
