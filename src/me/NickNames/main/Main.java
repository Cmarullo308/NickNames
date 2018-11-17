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
				setNickName(player, null);
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
				setNickName(player, null);
				sender.sendMessage(nickNameDisabled);
			} else {
				setNickName(player, args[1]);
				sender.sendMessage(nickNameSet);
			}
		}
	}

	public void setNickName(Player player, String name) {
		nickNamesData.getNickNames().set(player.getUniqueId().toString() + ".nickname", name);
		nickNamesData.getNickNames().set(player.getUniqueId() + ".username", player.getName());
		nickNamesData.saveNickNames();
		player.setDisplayName(name);
		player.setPlayerListName(name);
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
