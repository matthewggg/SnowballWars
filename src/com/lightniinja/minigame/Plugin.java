package com.lightniinja.minigame;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		ArenaManager.getManager().loadArenas();
		this.getServer().getPluginManager().registerEvents(new EventHandlers(), this);
	}
}
