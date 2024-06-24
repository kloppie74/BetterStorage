package org.kloppie74.betterstorage.TransformerBlock;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.kloppie74.betterstorage.BetterStorage;
import org.kloppie74.betterstorage.YMLFiles.FileManager;

public class TransformerAPI {
    private static BukkitTask currentTask;

    public static void TransformerStartup() {
        FileConfiguration Settings = FileManager.getInstance().getSettings();
        int SecondsPerAction = Settings.getInt("TransFormer.SecondsPerTransformerAction");
        int ItemsPerTick = Settings.getInt("TransFormer.ItemsPerTransformerAction");

        currentTask = new BukkitRunnable() {
            public void run() {
                FileManager fileManager = FileManager.getInstance();
                FileConfiguration transformer = fileManager.getTransformer();

                for (String transformerLoc : transformer.getStringList("Transformers")) {
                    String linkedChestLoc = transformer.getString(transformerLoc + ".LinkedChest");
                    String linkedStorageLoc = transformer.getString(transformerLoc + ".LinkedStorage");

                    int CurrentStorageBlock = FileManager.getInstance().getPlacedStorage().getInt("Storages." + linkedStorageLoc + ".Blocks");
                    int CurrentStorageMaxBlocks = FileManager.getInstance().getPlacedStorage().getInt("Storages." + linkedStorageLoc + ".MaxBlocks");
                    int CurrentStorageTier = FileManager.getInstance().getPlacedStorage().getInt("Storages." + linkedStorageLoc + ".Tier");
                    int CurrentStorageID = FileManager.getInstance().getPlacedStorage().getInt("Storages." + linkedStorageLoc + ".ID");
                    String CurrentStorageBlockType = FileManager.getInstance().getPlacedStorage().getString("Storages." + linkedStorageLoc + ".TypeBlock");
                    String CurrentStorageOwner = FileManager.getInstance().getPlacedStorage().getString("Storages." + linkedStorageLoc + ".Owner");





                }
            }
        }.runTaskTimer(BetterStorage.getBetterStorage(), 20L * SecondsPerAction, 20L);
    }

    public static void stopCurrentTask() {
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
    }

}
