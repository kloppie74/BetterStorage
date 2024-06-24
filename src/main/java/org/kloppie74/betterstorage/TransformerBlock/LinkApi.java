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

import java.util.HashMap;
import java.util.UUID;

public class LinkApi implements Listener {

    public static class LinkEvent {
        public static HashMap<UUID, String> LinkEvent = new HashMap<>();

        public static HashMap<UUID, String> getLinkEvent() {
            return LinkEvent;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getClickedBlock() != null) {

            String BlockLoc = event.getClickedBlock().getLocation().toString();
            FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
            String EventAPI = LinkApi.LinkEvent.getLinkEvent().get(player.getUniqueId());

            if(LinkEvent.getLinkEvent().get(player.getUniqueId()).equalsIgnoreCase("Chest")) {
                if(event.getClickedBlock().getType().equals(Material.CHEST)) {
                    if(!(FileManager.getInstance().getLinkedChest().contains(BlockLoc))) {
                        String TransferLoc = StorageBlock.DataStorage.getCurrentstorage().get(player.getUniqueId());
                        FileManager.getInstance().getTransformer().set("Transformers." + TransferLoc + ".LinkedChest", BlockLoc);
                        LinkEvent.getLinkEvent().put(player.getUniqueId(), "False");
                        FileManager.getInstance().getLinkedChest().set(BlockLoc, "Linked");

                        String MessageTXT = ChatMessage.getString("Transformer.LinkedChest");
                        MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);
                        player.sendMessage(MSG.chatColors(MessageTXT));
                        FileManager.getInstance().saveFiles();

                    } else {
                        String MessageTXT = ChatMessage.getString("Transformer.ChestAlreadyLinked");
                        MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);
                        player.sendMessage(MSG.chatColors(MessageTXT));
                        FileManager.getInstance().saveFiles();
                    }
                }
            } else if(EventAPI.equalsIgnoreCase("Storage")) {
                String TransferLoc = StorageBlock.DataStorage.getCurrentstorage().get(player.getUniqueId());

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
        } else {
            return;
        }
    }
}
