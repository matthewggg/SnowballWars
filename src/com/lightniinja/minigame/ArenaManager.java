package com.lightniinja.minigame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArenaManager {
	private Map<UUID, Location> locs = new HashMap<UUID, Location>();
	private static ArenaManager am;
	private Map<UUID, ItemStack[]> inv = new HashMap<UUID, ItemStack[]>();
	private Map<UUID, ItemStack[]> armor = new HashMap<UUID, ItemStack[]>();
	private Map<UUID, Double> health = new HashMap<UUID, Double>();
	private Map<UUID, Integer> food = new HashMap<UUID, Integer>();
	private Map<Arena, String> arenas = new HashMap<Arena, String>();
	private int arenaSize = 0;
	private static Plugin pl;
	public ArenaManager(Plugin pl) {
		ArenaManager.pl = pl;
	}
	public ArenaManager() {
		// required
	}
	public static ArenaManager getManager() {
		if(am == null) {
			am = new ArenaManager();
		}
		return am;
	}
	public Arena getArena(int i) {
		for(Arena a : arenas.keySet()) {
			if(a.getId() == i) {
				return a;
			}
		}
		return null;
	}
	public Arena getArena(String name) {
		for(Arena a : arenas.keySet()) {
			if(a.getName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}
	public Arena getArena(String p, int i) {
		for(Arena arena : arenas.keySet()) {
			if(arena.getPlayerNames().contains(p)) 
				return arena;
		}
		return null;
	}
	public Arena getArena(UUID u) {
		for(Arena arena : arenas.keySet()) {
			if(arena.getPlayers().contains(u))
				return arena;
		}
		return null;
	}
	public String getArenaName(int i) {
		for(Arena a : arenas.keySet()) {
			if(a.getId() == i) {
				return (String) arenas.values().toArray()[i];
			}
		}
		return null;
	}
	public void addPlayer(Player p, int i) {
		Arena a = getArena(i);
		if(a == null) {
			p.sendMessage(ChatColor.RED + "Invalid arena!");
			return;
		}
		a.getPlayers().add(p.getUniqueId());
		for(UUID u:a.getPlayers()) {
			Bukkit.getPlayer(u).sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.YELLOW + p.getName() + ChatColor.GREEN + " has joined the arena!");
		}
		inv.put(p.getUniqueId(), p.getInventory().getContents());
		armor.put(p.getUniqueId(), p.getInventory().getArmorContents());
		p.getInventory().setArmorContents(null);
		p.getInventory().clear();
		health.put(p.getUniqueId(), p.getHealth());
		food.put(p.getUniqueId(), p.getFoodLevel());
		locs.put(p.getUniqueId(), p.getLocation());
		p.teleport(a.getSpawn());
	}
	public void addPlayer(Player p, String n) {
		addPlayer(p, getArena(n).getId());
	}
	public void removePlayer(Player p) {
		Arena a = null;
		for(Arena arena : arenas.keySet()) {
			if(arena.getPlayers().contains(p.getUniqueId())) {
				a = arena;
			}
		}
		if(a == null) {
			p.sendMessage(ChatColor.RED + "You are not in an arena!");
			return;
		}
		for(UUID u:a.getPlayers()) {
			Bukkit.getPlayer(u).sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.YELLOW + p.getName() + ChatColor.RED + " has left the arena!");
		}
		a.getPlayers().remove(p.getUniqueId());
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getInventory().setContents(inv.get(p.getUniqueId()));
		p.getInventory().setArmorContents(armor.get(p.getUniqueId()));
		inv.remove(p.getUniqueId());
		armor.remove(p.getUniqueId());
		p.setHealth(health.get(p.getUniqueId()));
		p.setFoodLevel(food.get(p.getUniqueId()));
		health.remove(p.getUniqueId());
		food.remove(p.getUniqueId());
		p.teleport(locs.get(p.getUniqueId()));
		locs.remove(p.getUniqueId());
		p.setFireTicks(0);
	}
	public Arena createArena(Location l, String name) {
		arenaSize++;
		Arena a = new Arena(l, arenaSize);
		arenas.put(a, name);
		pl.getConfig().set("Arenas." + arenaSize + ".Location", serializeLoc(l));
		pl.getConfig().set("Arenas." + arenaSize + ".Name", name);
		List<Integer> list = pl.getConfig().getIntegerList("Arenas.Arenas");
		list.add(arenaSize);
		pl.getConfig().set("Arenas.Arenas", list);
		pl.saveConfig();
		return a;
	}
	public void loadArenas() {
		for(Integer i : pl.getConfig().getIntegerList("Arenas.Arenas")) {
			Location l = deserializeLoc(pl.getConfig().getString("Arenas." + i + ".Location"));
			String name = pl.getConfig().getString("Arenas." + i + ".Name");
			Arena a = new Arena(l, i);
			arenas.put(a, name);
		}
	}
	public boolean isInGame(String p) {
		return (getArena(p, 0) != null);
	}
	public boolean isInGame(UUID u) {
		return (getArena(u) != null);
	}
	public String serializeLoc(Location l){
        return l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ();
    }
    public Location deserializeLoc(String s){
        String[] st = s.split(",");
        return new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3]));
    }
}
