package org.kloppie74.betterstorage.BlockEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;

public class OnInventoryClose implements Listener {

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();

        if(StorageBlock.DataStorage.getStorageGUI().get(player.getUniqueId()).equals("Storage") || (StorageBlock.DataStorage.getStorageGUI().get(player.getUniqueId()).equals("Transformer"))) {
            StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "false");
        }
    }

}
