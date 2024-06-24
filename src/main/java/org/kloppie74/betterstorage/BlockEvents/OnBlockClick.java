package org.kloppie74.betterstorage.BlockEvents;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;
import org.kloppie74.betterstorage.TransformerBlock.LinkApi;
import org.kloppie74.betterstorage.TransformerBlock.TransformerGUI;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;

import java.util.UUID;

import static org.kloppie74.betterstorage.StorageBlock.StorageGUI.getStorageInventory;

public class OnBlockClick implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (event.getAction().isLeftClick() && clickedBlock != null && clickedBlock.getType().equals(Material.PLAYER_HEAD) || event.getAction().isLeftClick() && clickedBlock != null && clickedBlock.getType().equals(Material.PLAYER_WALL_HEAD)) {
            String BlockLoc = clickedBlock.getLocation().toString();

            if (FileManager.getInstance().getPlacedStorage().contains("Storages." + BlockLoc)) {

                if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                    player.sendMessage(MSG.chatColors("&cYou can not use these blocks when you are not in survival mode!"));
                } else if (player.getGameMode().equals(GameMode.SURVIVAL)) {

                    UUID ownerUUID = UUID.fromString(FileManager.getInstance().getPlacedStorage().getString("Storages." + BlockLoc + ".Owner"));
                    int ID = FileManager.getInstance().getPlacedStorage().getInt("Storages." + BlockLoc + ".ID");
                    int Tier = FileManager.getInstance().getPlacedStorage().getInt("Storages." + BlockLoc + ".Tier");

                    UUID playerUUID = ownerUUID;
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
                    String playerName = offlinePlayer.getName();

                    if (ownerUUID.equals(player.getUniqueId())) {
                        if(!(LinkApi.LinkEvent.getLinkEvent().get(player.getUniqueId()).equalsIgnoreCase("Storage"))) {
                            player.sendMessage(MSG.chatColors("&7Opening your &aStorage Block"));
                            Inventory storageInventory = getStorageInventory(BlockLoc, Tier, playerName, ID);
                            player.openInventory(storageInventory);

                            StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "Storage");
                            StorageBlock.DataStorage.getCurrentstorage().put(player.getUniqueId(), BlockLoc);
                        }
                    } else {
                        player.sendMessage(MSG.chatColors("&cThis Storage belongs to " + playerName));
                    }
                }

            } else if (FileManager.getInstance().getTransformer().contains("Transformers." + BlockLoc)) {

                if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                    player.sendMessage(MSG.chatColors("&cYou can not use these blocks when you are not in survival mode!"));
                } else if (player.getGameMode().equals(GameMode.SURVIVAL)) {

                    UUID ownerUUID = UUID.fromString(FileManager.getInstance().getTransformer().getString("Transformers." + BlockLoc + ".Owner"));
                    UUID playerUUID = ownerUUID;

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
                    String playerName = offlinePlayer.getName();

                    if (ownerUUID.equals(player.getUniqueId())) {
                        player.sendMessage(MSG.chatColors("&7Opening your &aTransformer Block"));

                        StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "Transformer");
                        StorageBlock.DataStorage.getCurrentstorage().put(player.getUniqueId(), BlockLoc);

                        Inventory TransformerInventory = TransformerGUI.getTransformerInventory(player);
                        player.openInventory(TransformerInventory);

                    } else {
                        player.sendMessage(MSG.chatColors("&cThis Transformer belongs to " + playerName));
                    }
                }

            }
        } else if (event.getAction().isRightClick() && clickedBlock != null && clickedBlock.getType().equals(Material.PLAYER_HEAD) || event.getAction().isRightClick() && clickedBlock != null && clickedBlock.getType().equals(Material.PLAYER_WALL_HEAD)) {
            String BlockLoc = clickedBlock.getLocation().toString();

            if (FileManager.getInstance().getPlacedStorage().contains("Storages." + BlockLoc) || FileManager.getInstance().getTransformer().contains("Transformers." + BlockLoc)) {
                player.sendMessage(MSG.chatColors("&cRight Click is currently disabled! Use left click instead!"));
            }
        }
    }
}
