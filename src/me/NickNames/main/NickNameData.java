package me.NickNames.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.NickNames.main.Main;
import net.md_5.bungee.api.ChatColor;

public class NickNameData {
	private Main main;
	// Files and File Configs
	public FileConfiguration nicknames;
	public File nicknamesFile;

	public NickNameData(Main main) {
		this.main = main;
	}

	public void setup() {
		if (!main.getDataFolder().exists()) {
			main.getDataFolder().mkdir();
		}

		nicknamesFile = new File(main.getDataFolder(), "nicknames.yml");

		if (!nicknamesFile.exists()) {
			try {
				nicknamesFile.createNewFile();
			} catch (IOException e) {
				main.getServer().getLogger().info(ChatColor.RED + "Could not create nicknames.yml file");
			}

		}

		nicknames = YamlConfiguration.loadConfiguration(nicknamesFile);
	}

	public FileConfiguration getNickNames() {
		return nicknames;
	}

	public void saveNickNames() {
		try {
			nicknames.save(nicknamesFile);
		} catch (IOException e) {
			main.getServer().getLogger().info(ChatColor.RED + "Could not save nicknames.yml file");
		}
	}

	public void setValue(String str1, String str2) {
		nicknames.set(str1, str2);
	}

	public void reloadNickNames() {
		nicknames = YamlConfiguration.loadConfiguration(nicknamesFile);
	}
}
