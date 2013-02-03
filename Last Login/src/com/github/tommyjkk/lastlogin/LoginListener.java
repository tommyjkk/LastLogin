package com.github.tommyjkk.lastlogin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.DateTime;
public class LoginListener implements Listener{


	LastLogin plugin;
	
	public LoginListener(LastLogin plugin) {
		this.plugin=plugin;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		String name=event.getPlayer().getName().toLowerCase();
		plugin.putInJoin(name, DateTime.now());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		String name=event.getPlayer().getName().toLowerCase();
		plugin.putInLeave(name, DateTime.now());
		plugin.calcTotalTime(name);
	}
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		String name=event.getPlayer().getName().toLowerCase();
		plugin.putInLeave(name, DateTime.now());
		plugin.calcTotalTime(name);
	}
}
