package org.kloppie74.betterstorage.UpdateCheckers;

import org.bukkit.configuration.InvalidConfigurationException;
import org.kloppie74.betterstorage.UpdateCheckers.FileUpdateAPI.ConfigUpdater;
import org.kloppie74.betterstorage.YMLFiles.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.kloppie74.betterstorage.BetterStorage.getBetterStorage;

public class UpdateAPI {

    public static final List<String> ignoredSections = Arrays.asList();

    public static void UpdateAPI() {

        File MessagesFile = new File(getBetterStorage().getDataFolder(), "Messages.yml");
        try {
            ConfigUpdater.update(getBetterStorage(), "Messages.yml", MessagesFile, ignoredSections);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!(FileManager.getInstance().getSettings().contains("System.FileVersion"))) {
            FileManager.getInstance().getSettings().set("System.FileVersion", "V1.1");
            File SettingsFile = new File(getBetterStorage().getDataFolder(), "Settings.yml");

            try {
                ConfigUpdater.update(getBetterStorage(), "Settings.yml", SettingsFile, ignoredSections);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!(FileManager.getInstance().getSettings().getString("System.FileVersion").equalsIgnoreCase("V1.1"))) {
            File SettingsFile = new File(getBetterStorage().getDataFolder(), "Settings.yml");
            FileManager.getInstance().getSettings().set("System.FileVersion", "V1.1");

            try {
                ConfigUpdater.update(getBetterStorage(), "Settings.yml", SettingsFile, ignoredSections);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileManager.getInstance().saveFiles();

        try {
            FileManager.getInstance().reloadFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }


    }

}
