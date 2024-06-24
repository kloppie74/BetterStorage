package org.kloppie74.betterstorage.StorageBlock;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.*;
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
import org.kloppie74.betterstorage.TransformerBlock.TransformerGUI;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.kloppie74.betterstorage.StorageBlock.StorageGUI.getStorageInventory;

public class StorageBlock implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        if (item.getType() == Material.PLAYER_HEAD && item.hasItemMeta() || item.getType() == Material.PLAYER_WALL_HEAD && item.hasItemMeta()) {
            if (item.getItemMeta().hasLore() && item.getItemMeta().getDisplayName().equals("§aStorage Block")) {
                player.sendMessage(MSG.chatColors("&7You've placed a §aStorage Block!"));

                int tier = getTierFromLore(item);
                int id = getIDFromLore(item);
                String ownerID = player.getUniqueId().toString();
                String storageBlockLoc = event.getBlock().getLocation().toString();

                int DefaultStorage = 90000;
                int TierStorage = tier * 10000;
                int MaxStorage = DefaultStorage + TierStorage;

                FileManager.getInstance().getPlacedStorage().set("Storages." + storageBlockLoc + ".ID", id);
                FileManager.getInstance().getPlacedStorage().set("Storages." + storageBlockLoc + ".Tier", tier);
                FileManager.getInstance().getPlacedStorage().set("Storages." + storageBlockLoc + ".Owner", ownerID);
                FileManager.getInstance().getPlacedStorage().set("Storages." + storageBlockLoc + ".Blocks", 0);
                FileManager.getInstance().getPlacedStorage().set("Storages." + storageBlockLoc + ".TypeBlock", "N/A");
                FileManager.getInstance().getPlacedStorage().set("Storages." + storageBlockLoc + ".MaxBlocks", MaxStorage);

                FileManager.getInstance().saveFiles();

            }
        }
    }

    public static class DataStorage {
        public static HashMap<UUID, String> StorageGUI = new HashMap<>();

        public static HashMap<UUID, String> getStorageGUI() {
            return StorageGUI;
        }

        public static HashMap<UUID, String> Currentstorage = new HashMap<>();

        public static HashMap<UUID, String> getCurrentstorage() {
            return Currentstorage;
        }

        public static HashMap<UUID, Block> StorageLoc = new HashMap<>();

        public static HashMap<UUID, Block> getStorageLoc() {
            return StorageLoc;
        }

        public static HashMap<UUID, String> WithdrawBlocks = new HashMap<>();

        public static HashMap<UUID, String> getWithdrawBlocks() {
            return WithdrawBlocks;
        }

    }

    public int getTierFromLore(ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();
        if (lore != null) {
            for (String line : lore) {
                if (line.matches("§7Tier »§f \\d+")) {
                    String numberPart = line.replaceAll(".*§7Tier »§f (\\d+)", "$1");
                    return Integer.parseInt(numberPart);
                }
            }
        }
        return 0;
    }

    public int getIDFromLore(ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();
        if (lore != null) {
            for (String line : lore) {
                if (line.matches("§7ID »§f \\d+")) {
                    String numberPart = line.replaceAll(".*§7ID »§f (\\d+)", "$1");
                    return Integer.parseInt(numberPart);
                }
            }
        }
        return 0;
    }

    private static final Random random = new Random();

    public static ItemStack PutSkinINFO(){

        int ID = random.nextInt(999999);
        int Tier = 1;

        return CreateStorageItem(ID, Tier,"6df3556ec2ad2796811ef69851015cc87e11bd8dfa8eb306784c9b935dd66dec");
    }

    public static ItemStack CreateStorageItem(int ID, int Tier, String URLTexture) {

        URLTexture = URLTexture.toLowerCase();
        URLTexture = URLTexture.replace("http://textures.minecraft.net/texture/", "");
        URLTexture = URLTexture.replace("https://textures.minecraft.net/texture/", "");


        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aStorage Block");

        List<String> lores = new ArrayList<>();
        lores.add("§7Tier »§f " + Tier);
        lores.add("§7ID »§f " + ID);
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
