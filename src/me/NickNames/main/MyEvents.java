package me.NickNames.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MyEvents implements Listener {
	Main main;

	public MyEvents(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		String nickName = main.nickNamesData.getNickNames().getString(event.getPlayer().getUniqueId() + ".nickname");
		if (nickName != null) {
			event.setQuitMessage(nickName + ChatColor.YELLOW + " has left the game");
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		String nickName = main.nickNamesData.getNickNames().getString(event.getPlayer().getUniqueId() + ".nickname");
		if (nickName != null) {
			event.setJoinMessage(nickName + ChatColor.YELLOW + " has joined the server");
			main.nickNamesData.getNickNames().set(event.getPlayer().getUniqueId() + ".username",
					event.getPlayer().getName());

			// Delay setting nickname
			Bukkit.getScheduler().runTaskLater(main, () -> {
				main.setNickName(event.getPlayer(), nickName);
			}, 2L);
		}
	}
}
