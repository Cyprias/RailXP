package com.cyprias.railxp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.cyprias.railxp.Config.heightInfo;


public class RailXP extends JavaPlugin {
	public static String chatPrefix = "§f[§cRailXP§f] ";
	public Config config;
	public Events events;
	public YML yml;
	
	public static List<heightInfo> taxHeights = new ArrayList<heightInfo>();
	public static List<heightInfo> grantHeights = new ArrayList<heightInfo>();
	
	String pluginName;
	public void onEnable() {
		pluginName = getDescription().getName();
		
		this.config = new Config(this);
		this.events = new Events(this);
		this.yml = new YML(this);
		
		getServer().getPluginManager().registerEvents(this.events, this);
	}
	
	
	
	public void info(String msg) {
		getServer().getConsoleSender().sendMessage(chatPrefix + msg);
	}
	
	public static int getTotalExperience(Player player){
		// player.getTotalExperience() sometimes reports the wrong XP due to a bug with enchanting not updating player's total XP.
		double userLevel = player.getLevel() + player.getExp();
		return (int) Math.ceil(1.75D * Math.pow(userLevel, 2.0D) + 5.0D * userLevel);
	}
	public static void setExp(Player player, int amount){
		//Clear player's XP.
		player.setTotalExperience(0);
		player.setLevel(0);
		player.setExp(0.0F);

		//Give player XP minus the enchant cost.
		player.giveExp(amount);
	}
	
	public static void takeExp(Player player, int amount){
		int current = getTotalExperience(player);
		setExp(player, (current - amount));
	}
	
	public void sendMessage(CommandSender sender, String message, Boolean showConsole) {
		if (sender instanceof Player && showConsole == true) {
			info("§e" + sender.getName() + "->§f" + message);
		}
		sender.sendMessage(chatPrefix + message);
	}	
	
	public boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;
		if (player.isOp()) {
			return true;
		}

		if (player.isPermissionSet(node))
			return player.hasPermission(node);

		String[] temp = node.split("\\.");
		String wildNode = temp[0];
		for (int i = 1; i < (temp.length); i++) {
			wildNode = wildNode + "." + temp[i];

			if (player.isPermissionSet(wildNode + ".*"))
				// plugin.info("wildNode1 " + wildNode+".*");
				return player.hasPermission(wildNode + ".*");

		}
		if (player.isPermissionSet(wildNode))
			return player.hasPermission(wildNode);

		if (player.isPermissionSet(wildNode))
			return player.hasPermission(wildNode);

		
		return player.hasPermission(pluginName.toLowerCase() + ".*");
	}
}
