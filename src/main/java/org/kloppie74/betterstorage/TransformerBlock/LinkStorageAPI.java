package org.kloppie74.betterstorage.TransformerBlock;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;

public class LinkStorageAPI implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String EventAPI = LinkApi.LinkEvent.getLinkEvent().get(player.getUniqueId());
        String BlockLoc = event.getClickedBlock().getLocation().toString();
        FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
        String TransferLoc = StorageBlock.DataStorage.getCurrentstorage().get(player.getUniqueId());

        if(event.getClickedBlock() != null) {
            if(EventAPI.equalsIgnoreCase("Storage")) {
                if(event.getClickedBlock().getType().equals(Material.PLAYER_HEAD) || event.getClickedBlock().getType().equals(Material.PLAYER_WALL_HEAD)) {
                    if(FileManager.getInstance().getPlacedStorage().contains("Storages." + BlockLoc)) {
                        FileManager.getInstance().getTransformer().set("Transformers." + TransferLoc + ".LinkedStorage", BlockLoc);
                        LinkApi.LinkEvent.getLinkEvent().put(player.getUniqueId(), "False");

                        FileManager.getInstance().getPlacedStorage().set("Storages." + BlockLoc + ".TransferLoc", TransferLoc);

                        String MessageTXT = ChatMessage.getString("Transformer.LinkedStorage");
                        MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);
                        player.sendMessage(MSG.chatColors(MessageTXT));
                        FileManager.getInstance().saveFiles();

                    }
                }
            }
        }
    }

}