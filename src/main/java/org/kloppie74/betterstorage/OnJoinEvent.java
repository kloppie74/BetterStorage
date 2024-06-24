package org.kloppie74.betterstorage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;
import org.kloppie74.betterstorage.TransformerBlock.LinkApi;

public class OnJoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "false");
        StorageBlock.DataStorage.getWithdrawBlocks().put(player.getUniqueId(), "false");
        LinkApi.LinkEvent.getLinkEvent().put(player.getUniqueId(), "false");
    }

}
