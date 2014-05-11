package com.lightniinja.snowballwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {
	private int id = 0;
	private String name = "";
	private Location spawn = null;
	private List<UUID> players = new ArrayList<UUID>();
	
	public Arena(Location loc, int id) {
		this.spawn = loc;
		this.id = id;
		this.name = ArenaManager.getManager().getArenaName(id);
	}
	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public List<String> getPlayerNames() {
		List<String> play = new ArrayList<String>();
		for(UUID u: this.players) {
			play.add(Bukkit.getOfflinePlayer(u).getName());
		}
		return play;
	}
	public List<UUID> getPlayers() {
		return this.players;
	}
	public Location getSpawn() {
		return this.spawn;
	}
}
