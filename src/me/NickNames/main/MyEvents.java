package me.NickNames.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class MyEvents implements Listener {
	Main main;

	public MyEvents(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onPostLogin(PlayerLoginEvent event) {
		main.setNickName(event.getPlayer(),
				main.nickNamesData.getNickNames().getString(event.getPlayer().getUniqueId() + ".nickname"));
	}
}
