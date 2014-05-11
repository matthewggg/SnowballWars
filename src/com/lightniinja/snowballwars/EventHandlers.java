package com.lightniinja.snowballwars;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventHandlers implements Listener {
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		ArenaManager.getManager().removePlayer(e.getPlayer());
	}
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		ArenaManager.getManager().removePlayer(e.getPlayer());
	}
}
