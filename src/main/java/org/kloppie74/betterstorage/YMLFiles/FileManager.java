package org.kloppie74.betterstorage.YMLFiles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private FileManager() { }

    static FileManager instance = new FileManager();

    public static FileManager getInstance() {
        return instance;
    }

    Plugin p;

    FileConfiguration Settings;
    File Settingsfile;

    FileConfiguration PlacedStorage;
    File PlacedStoragefile;

    FileConfiguration Transformer;
    File Transformerfile;

    FileConfiguration Messages;
    File Messagesfile;

    FileConfiguration LinkedChest;
    File LinkedChestfile;



    public boolean setup(Plugin p) {

        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        File PlayerDataFolder = new File(p.getDataFolder(), "DataCollector");
        if(!PlayerDataFolder.exists()) {
            PlayerDataFolder.mkdirs();
        }

        Settingsfile = new File(p.getDataFolder(), "Settings.yml");
        if(!Settingsfile.exists()){
            new FileCopy().copy(p.getResource("Settings.yml"), Settingsfile);
        }
        Settings = YamlConfiguration.loadConfiguration(Settingsfile);

        Transformerfile = new File(p.getDataFolder(), "DataCollector/TransformerData.yml");
        if(!Transformerfile.exists()){
            new FileCopy().copy(p.getResource("DataCollector/TransformerData.yml"), Transformerfile);
        }
        Transformer = YamlConfiguration.loadConfiguration(Transformerfile);

        PlacedStoragefile = new File(p.getDataFolder(), "DataCollector/PlacedStorageData.yml");
        if(!PlacedStoragefile.exists()){
            new FileCopy().copy(p.getResource("DataCollector/PlacedStorageData.yml"), PlacedStoragefile);
        }
        PlacedStorage = YamlConfiguration.loadConfiguration(PlacedStoragefile);

        LinkedChestfile = new File(p.getDataFolder(), "DataCollector/LinkedChestData.yml");
        if(!LinkedChestfile.exists()){
            new FileCopy().copy(p.getResource("DataCollector/LinkedChestData.yml"),LinkedChestfile);
        }
        LinkedChest = YamlConfiguration.loadConfiguration(LinkedChestfile);

        Messagesfile = new File(p.getDataFolder(), "Messages.yml");
        if(!Messagesfile.exists()){
            new FileCopy().copy(p.getResource("Messages.yml"), Messagesfile);
        }
        Messages = YamlConfiguration.loadConfiguration(Messagesfile);


        return true;
    }




    public FileConfiguration getSettings() {
        return Settings;
    }

    public FileConfiguration getPlacedStorage() {
        return PlacedStorage;
    }

    public FileConfiguration getTransformer() {
        return Transformer;
    }

    public FileConfiguration getLinkedChest() {
        return LinkedChest;
    }

    public FileConfiguration getMessages() {
        return Messages;
    }



    public void reloadFiles() throws IOException, InvalidConfigurationException {
        Settings.load(Settingsfile);
        PlacedStorage.load(PlacedStoragefile);
        Transformer.load(Transformerfile);
        Messages.load(Messagesfile);
        LinkedChest.load(LinkedChestfile);
    }



    public void saveFiles() {
        try {
            Settings.save(Settingsfile);
            PlacedStorage.save(PlacedStoragefile);
            Transformer.save(Transformerfile);
            Messages.save(Messagesfile);
            LinkedChest.save(LinkedChestfile);
        }
        catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save YMLFiles!");
        }
    }

    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }


}
