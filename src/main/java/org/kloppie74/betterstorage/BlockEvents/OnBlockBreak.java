package org.kloppie74.betterstorage.BlockEvents;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;

import java.util.UUID;

public class OnBlockBreak implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        String blockloc = event.getBlock().getLocation().toString();

        if(block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD)) {
            if (FileManager.getInstance().getTransformer().contains("Transformers." + blockloc)) {
                UUID ownerUUID = UUID.fromString(FileManager.getInstance().getTransformer().getString("Transformers." + blockloc + ".Owner"));
                if (ownerUUID.equals(player.getUniqueId())) {
                    player.sendMessage(MSG.chatColors("&cYou can not break your Transformer Block! Open the gui to break this Block!"));
                    event.setCancelled(true);
                } else {
                    player.sendMessage(MSG.chatColors("&cThis is not your Transformer Block!"));
                    event.setCancelled(true);
                }
            } else if (FileManager.getInstance().getPlacedStorage().contains("Storages." + blockloc)) {
                UUID ownerUUID = UUID.fromString(FileManager.getInstance().getPlacedStorage().getString("Storages." + blockloc + ".Owner"));
                if (ownerUUID.equals(player.getUniqueId())) {
                    player.sendMessage(MSG.chatColors("&cYou can not break your Storage Block! Open the gui to break this Block!"));
                    event.setCancelled(true);
                } else {
                    player.sendMessage(MSG.chatColors("&cThis is not your Storage Block!"));
                    event.setCancelled(true);
                }
            }
        }
    }
}
