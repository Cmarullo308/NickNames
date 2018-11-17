package me.NickNames.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.NickNames.main.MyEvents;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	final String invalidNumOfArgsMessage = ChatColor.RED + "Invalid number of arguements";
	final String noPermissionMessage = ChatColor.RED + "You do not have permission to run this command";
	final String mustBeAPlayerMessage = ChatColor.RED + "Must be a player to run this command";
	final String nickNameSet = ChatColor.GRAY + "Nickname set";
	final String nickNameDisabled = ChatColor.GRAY + "Nickname disabled";

	public NickNameData nickNamesData;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		nickCommand(sender, args);

		return true;
	}

	private void nickCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(invalidNumOfArgsMessage);
			return;
		}
		// Own Nickname
		else if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(mustBeAPlayerMessage);
				return;
			}

			Player player = (Player) sender;

			if (args[0].equalsIgnoreCase("off")) {
				removeNickName(player);
				sender.sendMessage(nickNameDisabled);
			} else {
				setNickName(player, args[0]);
				sender.sendMessage(nickNameSet);
			}
		}
		// Other nickname
		else if (args.length == 2) {
			Player player = getServer().getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(ChatColor.RED + args[0] + " does not exist in this server");
				return;
			}

			if (args[1].equals("off")) {
				removeNickName(player);
				sender.sendMessage(nickNameDisabled);
			} else {
				setNickName(player, args[1]);
				sender.sendMessage(nickNameSet);
			}
		}
	}

	public void removeNickName(Player player) {
		nickNamesData.getNickNames().set(player.getUniqueId().toString(), null);
		nickNamesData.saveNickNames();
	}

	public void setNickName(Player player, String name) {
		name = changeColorCodes(name);

		nickNamesData.getNickNames().set(player.getUniqueId() + ".username", player.getName());
		nickNamesData.getNickNames().set(player.getUniqueId().toString() + ".nickname", name);
		nickNamesData.saveNickNames();
		player.setDisplayName(name);
		player.setPlayerListName(name);
	}

	private String changeColorCodes(String name) {
		name = name.replace("&4", ChatColor.DARK_RED.toString());
		name = name.replace("&c", ChatColor.RED.toString());
		name = name.replace("&6", ChatColor.GOLD.toString());
		name = name.replace("&e", ChatColor.YELLOW.toString());
		name = name.replace("&2", ChatColor.DARK_GREEN.toString());
		name = name.replace("&a", ChatColor.GREEN.toString());
		name = name.replace("&b", ChatColor.AQUA.toString());
		name = name.replace("&3", ChatColor.DARK_AQUA.toString());
		name = name.replace("&1", ChatColor.DARK_BLUE.toString());
		name = name.replace("&9", ChatColor.BLUE.toString());
		name = name.replace("&d", ChatColor.LIGHT_PURPLE.toString());
		name = name.replace("&5", ChatColor.DARK_PURPLE.toString());
		name = name.replace("&f", ChatColor.WHITE.toString());
		name = name.replace("&7", ChatColor.GRAY.toString());
		name = name.replace("&8", ChatColor.DARK_GRAY.toString());
		name = name.replace("&0", ChatColor.BLACK.toString());

		return name;
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		loadNickNamesData();

		this.getServer().getPluginManager().registerEvents(new MyEvents(this), this);
		super.onEnable();
	}

	private void loadNickNamesData() {
		nickNamesData = new NickNameData(this);
		nickNamesData.setup();
	}
}
