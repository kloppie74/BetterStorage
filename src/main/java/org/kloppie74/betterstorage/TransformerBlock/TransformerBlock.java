package org.kloppie74.betterstorage.TransformerBlock;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TransformerBlock implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        if (item.getType() == Material.PLAYER_HEAD && item.hasItemMeta() || item.getType() == Material.PLAYER_WALL_HEAD && item.hasItemMeta()) {
            if (item.getItemMeta().hasLore() && item.getItemMeta().getDisplayName().equals("§cTransformer Block")) {
                player.sendMessage(MSG.chatColors("&7You've placed a §cTransformer Block!"));

                String ownerID = player.getUniqueId().toString();
                String storageBlockLoc = event.getBlock().getLocation().toString();

                FileManager.getInstance().getTransformer().set("Transformers." + storageBlockLoc + ".Owner", ownerID);
                FileManager.getInstance().getTransformer().set("Transformers." + storageBlockLoc + ".Mode", "ItemToStorage");
                FileManager.getInstance().saveFiles();
            }
        }
    }


    public static ItemStack PutSkinINFO(){
        return CreateStorageItem("3452c9137a718819407fcd37120b51711371e0d89e659f53b6f591759bd86cfd");
    }

    public static ItemStack CreateStorageItem(String URLTexture) {

        URLTexture = URLTexture.toLowerCase();
        URLTexture = URLTexture.replace("http://textures.minecraft.net/texture/", "");
        URLTexture = URLTexture.replace("https://textures.minecraft.net/texture/", "");


        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cTransformer Block");

        List<String> lores = new ArrayList<>();
        lores.add("§7Required to transfer items to a storage block!");
        lores.add("§7Place this block between a chest & a storage block and it will work!");
        meta.setLore(lores);

        SkullMeta skullMeta = (SkullMeta) meta;
        PlayerProfile pp = (PlayerProfile) Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        PlayerTextures pt = pp.getTextures();
        try {
            pt.setSkin(new URL("http://textures.minecraft.net/texture/" + URLTexture));
        } catch (MalformedURLException e) {e.printStackTrace();}
        pp.setTextures(pt);
        skullMeta.setOwnerProfile(pp);
        meta = skullMeta;
        item.setItemMeta(meta);

        return item;
    }
}
