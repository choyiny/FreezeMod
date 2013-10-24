package net.techmastary.plugins.freezemod;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (FreezeMod.frozen.contains(event.getPlayer().getName())) {
			event.setTo(event.getFrom());
			event.getPlayer().sendMessage(ChatColor.RED + "You are frozen. Listen to the admins.");
			return;
		}
		if (FreezeMod.freezeAll) {
			if (!event.getPlayer().hasPermission("freeze.freeze.admin")) {
				event.setTo(event.getFrom());
				event.getPlayer().sendMessage(ChatColor.RED + "You are frozen. Listen to the admins.");
			}
			return;
		}

	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (FreezeMod.frozen.contains(event.getPlayer().getName())) {
			if (!event.getPlayer().hasPermission("freeze.freeze.admin")) {
				event.setCancelled(true);
			}
		}
		if (FreezeMod.freezeAll) {
			if (!event.getPlayer().hasPermission("freeze.freeze.admin")) {
				event.setCancelled(true);
			}
		}
	}

}
