package com.cyprias.railxp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class Config {
	private RailXP plugin;
	private static Configuration config;
	
	public Config(RailXP railXP) {
		plugin = railXP;
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
		plugin.saveConfig();
		
		loadConfigOpts();
	}

	public void reloadOurConfig(){
		plugin.reloadConfig();
		config = plugin.getConfig().getRoot();
		loadConfigOpts();
	}
	
	public static int taxAmount, grantAmount;
	static List<Integer> railIDs;
	public static String stNotEnoughExpToPlace, stNotEnoughExpToBreak;
	
	public static class heightInfo {
		int min, max;
		public heightInfo(int min, int max) {
			this.min = min;
			this.max = max;
		}
	}
	

	
	private void loadConfigOpts(){
		taxAmount = config.getInt("taxAmount");
		grantAmount = config.getInt("grantAmount");
		stNotEnoughExpToPlace = config.getString("stNotEnoughExpToPlace").replaceAll("(?i)&([a-k0-9])", "\u00A7$1");
		stNotEnoughExpToBreak = config.getString("stNotEnoughExpToBreak").replaceAll("(?i)&([a-k0-9])", "\u00A7$1");
		

		
		railIDs = config.getIntegerList("railIDs");

		//railIDs
	}
	
}
