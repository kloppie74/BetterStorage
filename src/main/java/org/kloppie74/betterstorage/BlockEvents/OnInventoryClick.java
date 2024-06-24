package org.kloppie74.betterstorage.BlockEvents;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.kloppie74.betterstorage.BetterStorage;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;
import org.kloppie74.betterstorage.TransformerBlock.LinkApi;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnInventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (StorageBlock.DataStorage.getStorageGUI().get(player.getUniqueId()).equals("Storage")) {
            String Loc = StorageBlock.DataStorage.getCurrentstorage().get(player.getUniqueId());

            int CurrentStorageBlock = FileManager.getInstance().getPlacedStorage().getInt("Storages." + Loc + ".Blocks");
            int CurrentStorageMaxBlocks = FileManager.getInstance().getPlacedStorage().getInt("Storages." + Loc + ".MaxBlocks");
            int CurrentStorageTier = FileManager.getInstance().getPlacedStorage().getInt("Storages." + Loc + ".Tier");
            int CurrentStorageID = FileManager.getInstance().getPlacedStorage().getInt("Storages." + Loc + ".ID");
            String CurrentStorageBlockType = FileManager.getInstance().getPlacedStorage().getString("Storages." + Loc + ".TypeBlock");
            String CurrentStorageOwner = FileManager.getInstance().getPlacedStorage().getString("Storages." + Loc + ".Owner");

            if(event.getClickedInventory() instanceof PlayerInventory) {

            } else if(event.getSlot() == 29) {
                InventoryClickEvent clickEvent = (InventoryClickEvent) event;
                InventoryAction action = clickEvent.getAction();
                ItemStack placeditems = null;

                if (clickEvent.getCursor() != null) {

                    if(CurrentStorageBlockType.equals("N/A")) {

                        if (action == InventoryAction.PLACE_ONE) {
                            event.setCancelled(true);
                            player.sendMessage(MSG.chatColors("&cOnly Left Click Supported!"));
                        } else if (action == InventoryAction.PLACE_ALL) {

                            int Amount = clickEvent.getCursor().getAmount();
                            int NewAmount = Amount + CurrentStorageBlock;

                            Material newItem = clickEvent.getCursor().getType();
                            String typeName = newItem.name();

                            FileManager.getInstance().getPlacedStorage().set("Storages." + Loc + ".TypeBlock", typeName);
                            FileManager.getInstance().getPlacedStorage().set("Storages." + Loc + ".Blocks", NewAmount);
                            FileManager.getInstance().saveFiles();

                            player.sendMessage(MSG.chatColors("&aSuccessfully added x" + Amount + " " + newItem + " to your storage!"));
                            player.sendMessage(MSG.chatColors("&aNew Storage: " + NewAmount + "/" + CurrentStorageMaxBlocks));

                            clickEvent.setCursor(null);
                        }
                    } else {
                        Material newItem = clickEvent.getCursor().getType();
                        String typeName = newItem.name();

                        if(typeName.equals(CurrentStorageBlockType)) {
                            if (action == InventoryAction.PLACE_ONE) {
                                event.setCancelled(true);
                                player.sendMessage(MSG.chatColors("&cOnly Left Click Supported!"));
                            } else if (action == InventoryAction.PLACE_ALL) {

                                int Amount = clickEvent.getCursor().getAmount();
                                int NewAmount = Amount + CurrentStorageBlock;
                                Material Item = clickEvent.getCursor().getType();

                                FileManager.getInstance().getPlacedStorage().set("Storages." + Loc + ".Blocks", NewAmount);
                                FileManager.getInstance().saveFiles();

                                player.sendMessage(MSG.chatColors("&aSuccessfully added x" + Amount + " " + Item + " to your storage!"));
                                player.sendMessage(MSG.chatColors("&aNew Storage: " + NewAmount + "/" + CurrentStorageMaxBlocks));

                                clickEvent.setCursor(null);
                            }
                        } else {
                            player.sendMessage(MSG.chatColors("&cThats not the same item as in the storage!"));
                            event.setCancelled(true);
                        }
                    }
                }


            } else if (clicked == null) {
                event.setCancelled(true);
            } else if (clicked.getItemMeta().getDisplayName().equals("§b§lRemove Blocks")){
                StorageBlock.DataStorage.getWithdrawBlocks().put(player.getUniqueId(), "true");
                player.closeInventory();
                player.sendMessage(MSG.chatColors("&7Enter how much you want to withdraw! Enter 0 to cancel!"));


            } else if (clicked.getItemMeta().getDisplayName().equals("§c§lBreak Your Storage!")) {
                event.setCancelled(true);

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

                        Block block = yourLocation.getBlock();
                        block.setType(Material.AIR);

                        System.out.println("Block removed at: " + yourLocation.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        System.out.println("Invalid number format in locString");
                    }
                } else {
                    System.out.println("Invalid format in locString");
                }

                ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§aStorage Block");

                List<String> lores = new ArrayList<>();
                lores.add("§7Tier »§f " + CurrentStorageTier);
                lores.add("§7ID »§f " + CurrentStorageID);
                meta.setLore(lores);

                SkullMeta skullMeta = (SkullMeta) meta;
                PlayerProfile pp = (PlayerProfile) Bukkit.createPlayerProfile(UUID.randomUUID(), "");
                PlayerTextures pt = pp.getTextures();
                try {
                    pt.setSkin(new URL("http://textures.minecraft.net/texture/" + "6df3556ec2ad2796811ef69851015cc87e11bd8dfa8eb306784c9b935dd66dec"));
                } catch (MalformedURLException e) {e.printStackTrace();}
                pp.setTextures(pt);
                skullMeta.setOwnerProfile(pp);
                meta = skullMeta;
                item.setItemMeta(meta);

                player.getInventory().addItem(item);

                player.closeInventory();
                StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "false");

                FileManager.getInstance().getPlacedStorage().set("Storages." + Loc, null);
                FileManager.getInstance().saveFiles();


            } else if (clicked.getItemMeta().getDisplayName().equals("§b§lUpgrade Storage")) {
                event.setCancelled(true);
                int UpgradeCosts = FileManager.getInstance().getSettings().getInt("Storage.UpgradeCosts");
                int MaxTier = FileManager.getInstance().getSettings().getInt("Storage.MaxTier");

                if(CurrentStorageTier >= MaxTier) {

                    FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
                    String MessageTXT = ChatMessage.getString("Storage.MaxTier");
                    MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);

                    String MessageAPI = MessageTXT.replace("{tier}", String.valueOf(CurrentStorageTier));
                    player.sendMessage(MSG.chatColors(MessageAPI));


                } else {
                    if (BetterStorage.getEconomy().getBalance(player) >= UpgradeCosts) {
                        BetterStorage.getEconomy().withdrawPlayer(player, UpgradeCosts);

                        int NewTier = CurrentStorageTier + 1;
                        int NewMaxBlocks = CurrentStorageMaxBlocks + 10000;

                        FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
                        String MessageTXT = ChatMessage.getString("Storage.UpgradeSuccess");
                        MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);

                        String MessageAPI = MessageTXT.replace("{tier}", String.valueOf(NewTier));
                        player.sendMessage(MSG.chatColors(MessageAPI));

                        FileManager.getInstance().getPlacedStorage().set("Storages." + Loc + ".Tier", NewTier);
                        FileManager.getInstance().getPlacedStorage().set("Storages." + Loc + ".MaxBlocks", NewMaxBlocks);
                        FileManager.getInstance().saveFiles();

                        player.closeInventory();
                        StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "false");
                    } else {

                        int NewTier = CurrentStorageTier + 1;
                        FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
                        String MessageTXT = ChatMessage.getString("Storage.UpgradeFail");
                        MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);

                        String MessageAPI = MessageTXT.replace("{tier}", String.valueOf(NewTier));
                        player.sendMessage(MSG.chatColors(MessageAPI));

                    }
                }

            } else {
                event.setCancelled(true);
            }
        } else if (StorageBlock.DataStorage.getStorageGUI().get(player.getUniqueId()).equals("Transformer")) {
            String Loc = StorageBlock.DataStorage.getCurrentstorage().get(player.getUniqueId());
            if(event.getClickedInventory() instanceof PlayerInventory) {

            } else if (clicked == null) {
                event.setCancelled(true);
            } else if (clicked.getItemMeta().getDisplayName().equals("§c§lBreak Your Transformer!")) {
                event.setCancelled(true);

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

                        Block block = yourLocation.getBlock();
                        block.setType(Material.AIR);

                        System.out.println("Block removed at: " + yourLocation.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        System.out.println("Invalid number format in locString");
                    }
                } else {
                    System.out.println("Invalid format in locString");
                }

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
                    pt.setSkin(new URL("http://textures.minecraft.net/texture/" + "3452c9137a718819407fcd37120b51711371e0d89e659f53b6f591759bd86cfd"));
                } catch (MalformedURLException e) {e.printStackTrace();}
                pp.setTextures(pt);
                skullMeta.setOwnerProfile(pp);
                meta = skullMeta;
                item.setItemMeta(meta);

                player.getInventory().addItem(item);
                player.closeInventory();

                StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "false");
                FileManager.getInstance().getTransformer().set("Transformers." + Loc, null);
                FileManager.getInstance().saveFiles();


            } else if (clicked.getItemMeta().getDisplayName().equals("§a§lConnect a chest!")) {
                LinkApi.LinkEvent.getLinkEvent().put(player.getUniqueId(), "Chest");
                player.closeInventory();

                FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
                String MessageTXT = ChatMessage.getString("Transformer.LinkAChest");
                MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);
                player.sendMessage(MSG.chatColors(MessageTXT));

                event.setCancelled(true);
            } else if (clicked.getItemMeta().getDisplayName().equals("§b§lConnect a Storage!")) {
                LinkApi.LinkEvent.getLinkEvent().put(player.getUniqueId(), "Storage");
                player.closeInventory();

                FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
                String MessageTXT = ChatMessage.getString("Transformer.LinkAStorage");
                MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);
                player.sendMessage(MSG.chatColors(MessageTXT));
                event.setCancelled(true);
            } else if (clicked.getItemMeta().getDisplayName().equals("§a§lChange the mode from the transformer!")) {
                event.setCancelled(true);
                String CurrentMode = FileManager.getInstance().getTransformer().getString("Transformers." + Loc + ".Mode");
                if(CurrentMode.equals("ItemToStorage")) {
                    // New Mode ItemToChest
                    FileManager.getInstance().getTransformer().set("Transformers." + Loc + ".Mode", "ItemToChest");

                    FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
                    String MessageTXT = ChatMessage.getString("Transformer.SwitchSuccess");
                    MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);

                    player.sendMessage(MSG.chatColors(MessageTXT));
                    player.closeInventory();
                } else {
                    // New Mode ItemToStorage
                    FileManager.getInstance().getTransformer().set("Transformers." + Loc + ".Mode", "ItemToStorage");

                    FileConfiguration ChatMessage = FileManager.getInstance().getMessages();
                    String MessageTXT = ChatMessage.getString("Transformer.SwitchSuccess");
                    MessageTXT = PlaceholderAPI.setPlaceholders(player, MessageTXT);

                    player.sendMessage(MSG.chatColors(MessageTXT));
                    player.closeInventory();
                }

            } else {
                event.setCancelled(true);
            }
        }
    }
}
