package com.cyprias.railxp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Events implements Listener {
	private RailXP plugin;

	public Events(RailXP railXP) {
		plugin = railXP;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();
		if (plugin.hasPermission(player, "railxp.exempt"))
			return;

		int blockID = event.getBlock().getTypeId();

		if (!Config.railIDs.contains(blockID))
			return;

		int height = event.getBlock().getY();

		if (isTaxHeight(height) == true) {
			if (plugin.getTotalExperience(player) >= Config.taxAmount) {
				plugin.takeExp(player, Config.taxAmount);
			} else {
				player.sendMessage(plugin.chatPrefix + Config.stNotEnoughExpToPlace);
				event.setCancelled(true);
			}

		} else if (isGrantHeight(height) == true) {
			player.giveExp(Config.grantAmount);
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();
		if (plugin.hasPermission(player, "railxp.exempt"))
			return;

		int blockID = event.getBlock().getTypeId();

		if (!Config.railIDs.contains(blockID))
			return;

		int height = event.getBlock().getY();
		//plugin.info("onBlockBreak: " + height);

		// plugin.info("isTaxHeight: " + isTaxHeight(height));
		// plugin.info("isGrantHeight: " + isGrantHeight(height));

		/**/
		if (isTaxHeight(height) == true) {
			player.giveExp(Config.taxAmount);
		} else if (isGrantHeight(height) == true) {
			if (plugin.getTotalExperience(player) >= Config.grantAmount) {
				plugin.takeExp(player, Config.grantAmount);
			} else {
				player.sendMessage(plugin.chatPrefix + Config.stNotEnoughExpToBreak);
				event.setCancelled(true);

			}
		}
	}

	public boolean isTaxHeight(int height) {
		for (int i = 0; i < RailXP.taxHeights.size(); i++) {
			if (height >= RailXP.taxHeights.get(i).min && height <= RailXP.taxHeights.get(i).max) {
				return true;
			}
		}
		return false;
	}

	public boolean isGrantHeight(int height) {
		for (int i = 0; i < RailXP.grantHeights.size(); i++) {
			if (height >= RailXP.grantHeights.get(i).min && height <= RailXP.grantHeights.get(i).max) {
				return true;
			}
		}
		return false;
	}
}
