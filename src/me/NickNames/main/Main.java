package me.NickNames.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.NickNames.main.MyEvents;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	// Messages
	final String invalidNumOfArgsMessage = ChatColor.RED + "Invalid number of arguements";
	final String noPermissionMessage = ChatColor.RED + "You do not have permission to run this command";
	final String mustBeAPlayerMessage = ChatColor.RED + "Must be a player to run this command";
	final String nickNameSet = ChatColor.GRAY + "Nickname set";
	final String nickNameDisabled = ChatColor.GRAY + "Nickname disabled";
	final String colorCodes = "--Chat Colors--\n" + ChatColor.DARK_RED + "&4 : " + "DARK RED\n" + ChatColor.RED
			+ "&c : " + "RED\n" + ChatColor.GOLD + "&6 : " + "GOLD\n" + ChatColor.YELLOW + "&e : " + "YELLOW\n"
			+ ChatColor.DARK_GREEN + "&2 : " + "DARK GREEN\n" + ChatColor.GREEN + "&a : " + "GREEN\n" + ChatColor.AQUA
			+ "&b : " + "AQUA\n" + ChatColor.DARK_AQUA + "&3 : " + "DARK AQUA\n" + ChatColor.DARK_BLUE + "&1 : "
			+ "DARK BLUE\n" + ChatColor.BLUE + "&9 : " + "BLUE\n" + ChatColor.LIGHT_PURPLE + "&d : " + "LIGHT PURPLE\n"
			+ ChatColor.DARK_PURPLE + "&5 : " + "DARK PURPLE\n" + ChatColor.WHITE + "&f : " + "WHITE\n" + ChatColor.GRAY
			+ "&7 : " + "GRAY\n" + ChatColor.DARK_GRAY + "&8 : " + "DARK GRAY\n" + ChatColor.BLACK + "&0 : " + "BLACK";
	// ---------

	public NickNameData nickNamesData;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String subCommand = args[0].toLowerCase();

		if (subCommand.equals("help")) {
			helpMenu(sender, args);
		} else if (subCommand.equals("lookup")) {
			lookup(sender, args);
		} else if (subCommand.equals("colorcodes")) {
			sender.sendMessage(colorCodes);
		} else {
			nickCommand(sender, args);
		}

		return true;

	}

	private void lookup(CommandSender sender, String[] args) {
		if (!sender.hasPermission("nicknames.lookup")) {
			sender.sendMessage(noPermissionMessage);
			return;
		}

		boolean multiple = false;
		String message = "";
		Object[] keys = nickNamesData.getNickNames().getKeys(false).toArray();

		for (int userId = 0; userId < keys.length; userId++) {
			if (nickNamesData.getNickNames().getString(keys[userId] + ".nickname").equals(args[1])) {
				if (!multiple) {
					message += args[1] + " is " + nickNamesData.getNickNames().getString(keys[userId] + ".username");
					multiple = true;
				} else {
					message += ", " + nickNamesData.getNickNames().getString(keys[userId] + ".username");
				}
			}
		}

		sender.sendMessage(message);
	}

	private void helpMenu(CommandSender sender, String[] args) {
		String message = ChatColor.GRAY + "";

		message += "Commands:\n";
		message += "/nick <nickname>: Sets your own nickname\n";
		message += "/nick <nickname> off: Removes your nickname\n";
		message += "/nick <player name> <nickname>: Sets another players nickname\n";
		message += "/nick <player name> off: Removes another players nickname\n";
		message += "/nick lookup <nickname>: Looks up a username from a nickname\n";
		message += "/nick colorcodes: Shows the color codes";
		// finish

		sender.sendMessage(message);
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

			if (!sender.hasPermission("nicknames.set")) {
				sender.sendMessage(noPermissionMessage);
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
			if (!sender.hasPermission("nicknames.set.other")) {
				sender.sendMessage(noPermissionMessage);
				return;
			}

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
		if (name != null) {
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
		}

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
