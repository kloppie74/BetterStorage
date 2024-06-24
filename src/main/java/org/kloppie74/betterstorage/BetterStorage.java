package org.kloppie74.betterstorage;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kloppie74.betterstorage.BlockEvents.OnBlockBreak;
import org.kloppie74.betterstorage.BlockEvents.OnBlockClick;
import org.kloppie74.betterstorage.BlockEvents.OnInventoryClick;
import org.kloppie74.betterstorage.BlockEvents.OnInventoryClose;
import org.kloppie74.betterstorage.Commands.CommandManager;
import org.kloppie74.betterstorage.StorageBlock.StorageBlock;
import org.kloppie74.betterstorage.TransformerBlock.LinkApi;
import org.kloppie74.betterstorage.TransformerBlock.LinkStorageAPI;
import org.kloppie74.betterstorage.TransformerBlock.TransformerBlock;
import org.kloppie74.betterstorage.UpdateCheckers.UpdateAPI;
import org.kloppie74.betterstorage.UpdateCheckers.UpdateChecker;
import org.kloppie74.betterstorage.YMLFiles.FileManager;
import org.kloppie74.betterstorage.YMLFiles.MSG;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public final class BetterStorage extends JavaPlugin implements Listener {

    public static BetterStorage getBetterStorage(){
        return BetterStorage;
    }

    private static BetterStorage BetterStorage;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (player.isOp()) {
            new UpdateChecker(this, 115108).getVersion(version -> {
                if (!this.getDescription().getVersion().equals(version)) {

                    player.sendMessage(MSG.chatColors("&b&l&nBetterStorage"));
                    player.sendMessage(MSG.chatColors("&f"));
                    player.sendMessage(MSG.chatColors("&7There is a new &a&nupdate&r &7available! &7Current version: &a&n" + this.getDescription().getVersion()));
                    player.sendMessage(MSG.chatColors("&7New Version &a&n" + version));
                    player.sendMessage(MSG.chatColors("&r&7Download now! &b» &e&nhttps://www.spigotmc.org/resources/betterstorage.115108/"));
                }
            });

        }
    }

    @Override
    public void onEnable() {
        checkForUpdates();
        BetterStorage = this;
        FileManager.getInstance().setup(this);

        int pluginId = 20898;
        new Metrics(this, pluginId);

        getServer().getPluginManager().registerEvents(new TransformerBlock(), this);
        getServer().getPluginManager().registerEvents(new StorageBlock(), this);
        getServer().getPluginManager().registerEvents(new OnJoinEvent(), this);
        getServer().getPluginManager().registerEvents(this, this);

        getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClose(), this);
        getServer().getPluginManager().registerEvents(new OnBlockClick(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);

        getServer().getPluginManager().registerEvents(new LinkApi(), this);
        getServer().getPluginManager().registerEvents(new LinkStorageAPI(), this);

        getCommand("betterstorage").setExecutor(new CommandManager());
        getCommand("betterstorage").setTabCompleter(new CommandManager());

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();

        for(Player player : Bukkit.getOnlinePlayers()) {
            StorageBlock.DataStorage.getStorageGUI().put(player.getUniqueId(), "false");
            StorageBlock.DataStorage.getWithdrawBlocks().put(player.getUniqueId(), "false");
            LinkApi.LinkEvent.getLinkEvent().put(player.getUniqueId(), "false");
        }

        UpdateAPI.UpdateAPI();

    }

    @Override
    public void onDisable() {
        FileManager.getInstance().saveFiles();
        getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        List<Block> blocks = event.getBlocks();

        for (Block block : blocks) {
            if (block.getType() == Material.PLAYER_WALL_HEAD || block.getType() == Material.PLAYER_HEAD) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block toBlock = event.getToBlock();

        if (isForbiddenBlock(toBlock.getType())) {
            event.setCancelled(true);
        }
    }

    private boolean isForbiddenBlock(Material material) {
        return material == Material.PLAYER_WALL_HEAD || material == Material.PLAYER_HEAD;
    }

    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            getLogger().info("Only players are supported for this Example Plugin, but you should not do this!!!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getLabel().equals("test-economy")) {
            // Lets give the player 1.05 currency (note that SOME economic plugins require rounding!)
            sender.sendMessage(String.format("You have %s", econ.format(econ.getBalance(player.getName()))));
            EconomyResponse r = econ.depositPlayer(player, 1.05);
            if(r.transactionSuccess()) {
                sender.sendMessage(String.format("You were given %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            } else {
                sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
            }
            return true;
        } else if(command.getLabel().equals("test-permission")) {
            // Lets test if user has the node "example.plugin.awesome" to determine if they are awesome or just suck
            if(perms.has(player, "example.plugin.awesome")) {
                sender.sendMessage("You are awesome!");
            } else {
                sender.sendMessage("You suck!");
            }
            return true;
        } else {
            return false;
        }
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }


    private void checkForUpdates() {
        try {
            new UpdateChecker(this, 110701).getVersion(version -> {
                if (!this.getDescription().getVersion().equals(version)) {
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage("§7§l-------------------------------------------------------------------");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage("§7§l|||||  ||||| |||||| |||||| ||||| |||||   ||       ||    |||||   ");
                    Bukkit.getConsoleSender().sendMessage("§7§l||  || ||      ||     ||   ||    ||  ||  ||      ||||   ||  ||  ");
                    Bukkit.getConsoleSender().sendMessage("§7§l|||||  ||||    ||     ||   ||||  |||||   ||     ||  ||  |||||   ");
                    Bukkit.getConsoleSender().sendMessage("§7§l||  || ||      ||     ||   ||    || ||   ||     ||||||  ||  ||  ");
                    Bukkit.getConsoleSender().sendMessage("§7§l|||||  |||||   ||     ||   ||||| ||  ||  ||||| ||    || |||||   ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage("§7§lThe #1 Storage Plugin!");
                    Bukkit.getConsoleSender().sendMessage("§7§lBetterStorage, a BetterLab Plugin!");
                    Bukkit.getConsoleSender().sendMessage("§7§lReport issues on the official discord!");
                    Bukkit.getConsoleSender().sendMessage("§f");
                    Bukkit.getConsoleSender().sendMessage("§rBetterStorage found newer version, Downloading this version...");
                    Bukkit.getConsoleSender().sendMessage("§eUpgrading from §f" + this.getDescription().getVersion() + " §eTo §f" + version);
                    Bukkit.getConsoleSender().sendMessage("§f");
                    Bukkit.getConsoleSender().sendMessage("§eRestart server to apply changes!");
                    Bukkit.getConsoleSender().sendMessage("§f");
                    Bukkit.getConsoleSender().sendMessage("§7§l-------------------------------------------------------------------");
                    updatePlugin(version);
                }
            });
        } catch (Exception e) {
            getLogger().warning("Error while trying to look for new updates: " + e.getMessage());
        }
    }

    private void updatePlugin(String latestVersion) {
        try {

            File oldPluginFile = new File(getDataFolder().getParent(), "BetterStorage-" + this.getDescription().getVersion() + ".jar");
            if (oldPluginFile.exists()) {
                if (oldPluginFile.delete()) {
                    getLogger().info("Deleted old plugin jar!");
                } else {
                    getLogger().warning("Error while trying to delete old plugin jar");
                }
            }
            String downloadUrl = "https://api.spiget.org/v2/resources/115108/download";

            URL url = new URL(downloadUrl);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(getDataFolder().getParent() + File.separator + "BetterStorage-" + latestVersion + ".jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();

            getLogger().info("Plugin updated to " + latestVersion + ". Restart the server/plugin to apply changes!");
        } catch (Exception e) {
            getLogger().warning("Error while trying to update plugin: " + e.getMessage());
        }
    }

}
