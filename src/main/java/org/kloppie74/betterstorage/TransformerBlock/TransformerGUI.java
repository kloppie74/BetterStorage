package org.kloppie74.betterstorage.TransformerBlock;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;
import org.kloppie74.betterstorage.YMLFiles.FileManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransformerGUI {


    public static Inventory getTransformerInventory(Player player) {
        Inventory storageInventory = Bukkit.createInventory(null, 27, "Transformer Block");

        ItemStack FillItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta FillItemmeta = FillItem.getItemMeta();
        FillItemmeta.setDisplayName("§f ");
        FillItem.setItemMeta(FillItemmeta);
        for (int i = 0; i < storageInventory.getSize(); i++) {
            storageInventory.setItem(i, FillItem);
        }

        ItemStack LinkChest = new ItemStack(Material.CHEST);
        ItemMeta LinkChestmeta = LinkChest.getItemMeta();
        LinkChestmeta.setDisplayName("§a§lConnect a chest!");
        ArrayList<String> LinkChestLore = new ArrayList<String>();
        String Loc = StorageBlock.DataStorage.getCurrentstorage().get(player.getUniqueId());
        String LocChest = FileManager.getInstance().getTransformer().getString("Transformers." + Loc + ".LinkedChest");;
        String LocStorage = FileManager.getInstance().getTransformer().getString("Transformers." + Loc + ".LinkedStorage");

        String locChestString = LocChest;

        if(locChestString != null) {
            locChestString = locChestString.replaceAll("[a-zA-Z{}=]", "");
            String[] partsChest = locChestString.split(",");
            if (partsChest.length == 6) {
                String worldName = partsChest[0].trim();
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    world = player.getWorld();
                }
                try {
                    double x = Double.parseDouble(partsChest[1].trim());
                    double y = Double.parseDouble(partsChest[2].trim());
                    double z = Double.parseDouble(partsChest[3].trim());
                    float pitch = Float.parseFloat(partsChest[4].trim());
                    float yaw = Float.parseFloat(partsChest[5].trim());

                    Location yourLocation = new Location(world, x, y, z, yaw, pitch);
                    String NewLoc = "x" + x + " y" + y + " z" + z;

                    if (FileManager.getInstance().getTransformer().contains("Transformers." + Loc + ".LinkedChest")) {
                        LinkChestLore.add("§7Linked Chest » §b" + NewLoc);
                    } else {
                        LinkChestLore.add("§7Connect a chest to check for items to transfer!");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Invalid number format in locString");
                }
            } else {
                System.out.println("Invalid format in locString");
            }

        } else {
            LinkChestLore.add("§7Connect a chest to check for items to transfer!");
        }


        LinkChestmeta.setLore(LinkChestLore);
        LinkChest.setItemMeta(LinkChestmeta);
        storageInventory.setItem(10, LinkChest);

        ItemStack Mode = new ItemStack(Material.PAPER);
        ItemMeta Modemeta = Mode.getItemMeta();
        Modemeta.setDisplayName("§a§lChange the mode from the transformer!");
        ArrayList<String> ModeLore = new ArrayList<String>();
        //get location block!
        String locString = Loc;
        locString = locString.replaceAll("[a-zA-Z{}=]", "");
        String[] parts = locString.split(",");
        if (parts.length == 6) {
            String worldName = parts[0].trim();
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                world = player.getWorld();
            }
            try {
                double x = Double.parseDouble(parts[1].trim());
                double y = Double.parseDouble(parts[2].trim());
                double z = Double.parseDouble(parts[3].trim());
                float pitch = Float.parseFloat(parts[4].trim());
                float yaw = Float.parseFloat(parts[5].trim());

                Location yourLocation = new Location(world, x, y, z, yaw, pitch);
                String CurrentMode = FileManager.getInstance().getTransformer().getString("Transformers." + Loc + ".Mode");
                ModeLore.add("§7Current mode: " + CurrentMode);
                if(CurrentMode.equals("ItemToStorage")) {
                    ModeLore.add("§7Next mode: ItemToChest");
                } else {
                    ModeLore.add("§7Next mode: ItemToStorage");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Invalid number format in locString");
            }
        } else {
            System.out.println("Invalid format in locString");
        }
        Modemeta.setLore(ModeLore);
        Mode.setItemMeta(Modemeta);
        storageInventory.setItem(11, Mode);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§b§lConnect a Storage!");

        List<String> lores = new ArrayList<>();

        String locStorageString = LocStorage;
        if(locStorageString != null) {

            locStorageString = locStorageString.replaceAll("[a-zA-Z{}=]", "");
            String[] partsStorage = locStorageString.split(",");
            if (parts.length == 6) {
                String worldName = partsStorage[0].trim();
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    world = player.getWorld();
                }
                try {
                    double x = Double.parseDouble(partsStorage[1].trim());
                    double y = Double.parseDouble(partsStorage[2].trim());
                    double z = Double.parseDouble(partsStorage[3].trim());
                    float pitch = Float.parseFloat(partsStorage[4].trim());
                    float yaw = Float.parseFloat(partsStorage[5].trim());

                    Location yourLocation = new Location(world, x, y, z, yaw, pitch);

                    String NewLoc = "x" + x + " y" + y + " z" + z;

                    if (FileManager.getInstance().getTransformer().contains("Transformers." + Loc + ".LinkedStorage")) {
                        lores.add("§7Linked Storage » §b" + NewLoc);
                    } else {
                        lores.add("§7Connect a storage to the transfer block!");
                        lores.add("§7This is required to interact with a storage!");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Invalid number format in locString");
                }
            } else {
                System.out.println("Invalid format in locString");
            }

        } else {
            lores.add("§7Connect a storage to the transfer block!");
            lores.add("§7This is required to interact with a storage!");
        }

        meta.setLore(lores);
        SkullMeta skullMeta = (SkullMeta) meta;
        PlayerProfile pp = (PlayerProfile) Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        PlayerTextures pt = pp.getTextures();
        try {
            pt.setSkin(new URL("http://textures.minecraft.net/texture/" + "6df3556ec2ad2796811ef69851015cc87e11bd8dfa8eb306784c9b935dd66dec"));
        } catch (MalformedURLException e) {e.printStackTrace();}
        pp.setTextures(pt);
        skullMeta.setOwnerProfile(pp);
        item.setItemMeta(meta);
        storageInventory.setItem(12, item);

        ItemStack BreakStorage = new ItemStack(Material.BARRIER);
        ItemMeta BreakStoragemeta = BreakStorage.getItemMeta();
        BreakStoragemeta.setDisplayName("§c§lBreak Your Transformer!");
        ArrayList<String> BreakStorageLore = new ArrayList<String>();
        BreakStorageLore.add("§7Break the Transformer!");
        BreakStoragemeta.setLore(BreakStorageLore);
        BreakStorage.setItemMeta(BreakStoragemeta);
        storageInventory.setItem(16, BreakStorage);


        return storageInventory;
    }
}
