package org.kloppie74.betterstorage.StorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;
import java.util.ArrayList;

import static org.apache.commons.lang.math.NumberUtils.isNumber;

public class StorageGUI {

    public static Inventory getStorageInventory(String storageBlockLoc, int Tier, String Owner, int ID) {
        Inventory storageInventory = Bukkit.createInventory(null, 54, "Storage Block Inventory");

        ItemStack FillItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta FillItemmeta = FillItem.getItemMeta();
        FillItemmeta.setDisplayName("§f ");
        FillItem.setItemMeta(FillItemmeta);
        for (int i = 0; i < storageInventory.getSize(); i++) {
            storageInventory.setItem(i, FillItem);
        }

        ItemStack BreakStorage = new ItemStack(Material.BARRIER);
        ItemMeta BreakStoragemeta = BreakStorage.getItemMeta();
        BreakStoragemeta.setDisplayName("§c§lBreak Your Storage!");
        ArrayList<String> BreakStorageLore = new ArrayList<String>();
        BreakStorageLore.add("§7Break the storage!");
        BreakStorageLore.add("§cWarning!");
        BreakStorageLore.add("§cThis can't be undo! & all items will be lost!");
        BreakStoragemeta.setLore(BreakStorageLore);
        BreakStorage.setItemMeta(BreakStoragemeta);
        storageInventory.setItem(49, BreakStorage);

        ItemStack StorageStats = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta StorageStatsmeta = StorageStats.getItemMeta();
        StorageStatsmeta.setDisplayName("§b§lStorage Stats");
        ArrayList<String> StorageStatsLore = new ArrayList<String>();
        StorageStatsLore.add("§7Storage Stats");
        StorageStatsLore.add("§7");
        StorageStatsLore.add("§7Owner » " + Owner);
        StorageStatsLore.add("§7Tier » " + Tier);
        StorageStatsLore.add("§7ID » " + ID);
        StorageStatsmeta.setLore(StorageStatsLore);
        StorageStats.setItemMeta(StorageStatsmeta);
        storageInventory.setItem(4, StorageStats);


        int MaxStorage = 90000 + ( Tier * 10000 );
        int TotalBlocks = FileManager.getInstance().getPlacedStorage().getInt("Storages." + storageBlockLoc + ".Blocks");
        String Type = FileManager.getInstance().getPlacedStorage().getString("Storages." + storageBlockLoc + ".TypeBlock");

        ItemStack StorageBlock = new ItemStack(Material.CHISELED_BOOKSHELF);
        ItemMeta StorageBlockmeta = StorageBlock.getItemMeta();
        StorageBlockmeta.setDisplayName("§b§lStorage Block Info");
        ArrayList<String> StorageBlockLore = new ArrayList<String>();
        StorageBlockLore.add("§7Storage Block");
        StorageBlockLore.add("§7");
        StorageBlockLore.add("§7Block Type » " + Type);
        StorageBlockLore.add("§7Amount » " + TotalBlocks + "/" + MaxStorage);
        StorageBlockmeta.setLore(StorageBlockLore);
        StorageBlock.setItemMeta(StorageBlockmeta);
        storageInventory.setItem(22, StorageBlock);

        ItemStack AddBlocks = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta AddBlocksmeta = AddBlocks.getItemMeta();
        AddBlocksmeta.setDisplayName("§b§lAdd Blocks");
        ArrayList<String> AddBlocksLore = new ArrayList<String>();
        AddBlocksLore.add("§7Add Blocks to the storage!");
        AddBlocksLore.add("§7");
        AddBlocksLore.add("§7Place blocks in the empty spot below");
        AddBlocksLore.add("§7to add them to the storage!");
        AddBlocksmeta.setLore(AddBlocksLore);
        AddBlocks.setItemMeta(AddBlocksmeta);
        storageInventory.setItem(20, AddBlocks);

        ItemStack RemBlocks = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta RemBlocksmeta = RemBlocks.getItemMeta();
        RemBlocksmeta.setDisplayName("§b§lRemove Blocks");
        ArrayList<String> RemBlocksLore = new ArrayList<String>();
        RemBlocksLore.add("§7Remove Blocks from the storage!");
        RemBlocksLore.add("§7");
        RemBlocksLore.add("§7Click on this item & enter a amount");
        RemBlocksLore.add("§7to take that amount out of the storage!");
        RemBlocksmeta.setLore(RemBlocksLore);
        RemBlocks.setItemMeta(RemBlocksmeta);
        storageInventory.setItem(24, RemBlocks);

        ItemStack UpgradeStorage = new ItemStack(Material.ANVIL);
        ItemMeta UpgradeStoragemeta = UpgradeStorage.getItemMeta();
        UpgradeStoragemeta.setDisplayName("§b§lUpgrade Storage");
        ArrayList<String> UpgradeStorageLore = new ArrayList<String>();
        UpgradeStorageLore.add("§7Upgrade the storage!");
        UpgradeStorageLore.add("§7");
        UpgradeStorageLore.add("§7Upgrade Costs §a$100.000");
        UpgradeStorageLore.add("§7");
        UpgradeStorageLore.add("§7Next Tier » " + (Tier + 1));
        UpgradeStorageLore.add("§7Storage Space » " + MaxStorage + " §a+10.000");
        UpgradeStoragemeta.setLore(UpgradeStorageLore);
        UpgradeStorage.setItemMeta(UpgradeStoragemeta);
        storageInventory.setItem(48, UpgradeStorage);

        ItemStack Air = new ItemStack(Material.AIR);
        Air.setItemMeta(UpgradeStoragemeta);
        storageInventory.setItem(29, Air);

        if(!FileManager.getInstance().getPlacedStorage().getString("Storages." + storageBlockLoc + ".TypeBlock").equals("N/A")) {
            String Item2 = FileManager.getInstance().getPlacedStorage().getString("Storages." + storageBlockLoc + ".TypeBlock");

            ItemStack Item = new ItemStack(Material.valueOf(Item2));
            storageInventory.setItem(31, Item);
        }

        return storageInventory;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(StorageBlock.DataStorage.getWithdrawBlocks().get(player.getUniqueId()).equals("true")) {
            String Loc = StorageBlock.DataStorage.getCurrentstorage().get(player.getUniqueId());
            int CurrentStorageBlock = FileManager.getInstance().getPlacedStorage().getInt("Storages." + Loc + ".Blocks");
            String CurrentStorageBlockType = FileManager.getInstance().getPlacedStorage().getString("Storages." + Loc + ".TypeBlock");

            String Msg = event.getMessage();
            if (isNumber(Msg)) {
                int Withdraw = Integer.parseInt(Msg);
                if (Withdraw == 0) {
                    event.setCancelled(true);
                    StorageBlock.DataStorage.getWithdrawBlocks().put(player.getUniqueId(), "false");
                } else if (Withdraw >= 1) {
                    if (CurrentStorageBlock >= Withdraw) {
                        event.setCancelled(true);
                        player.sendMessage(MSG.chatColors("&aWithdrew: " + Withdraw + "x " + CurrentStorageBlockType));
                        ItemStack itemStack = new ItemStack(Material.valueOf(CurrentStorageBlockType), Withdraw);
                        player.getInventory().addItem(itemStack);

                        int newBlocks = CurrentStorageBlock - Withdraw;
                        FileManager.getInstance().getPlacedStorage().set("Storages." + Loc + ".Blocks", newBlocks);
                        FileManager.getInstance().saveFiles();
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(MSG.chatColors("&cYou cant take more blocks then you have in your storage!"));
                    }
                    StorageBlock.DataStorage.getWithdrawBlocks().put(player.getUniqueId(), "false");
                } else {
                    event.setCancelled(true);
                    player.sendMessage(MSG.chatColors("&cThats not a number! Try Again!"));
                }
            } else {
                return;
            }
        }

    }

}
