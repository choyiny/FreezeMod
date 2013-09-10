package net.techmastary.plugins.freezemod;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import java.util.ArrayList;

public class FreezeMod extends JavaPlugin implements Listener {
	static ArrayList<String> frozen = new ArrayList<String>();
	FreezeListener freezelistener = new FreezeListener();
	public static boolean freezeAll = false;

	public void onDisable() {
	}

	private void loadConf() {
		getConfig().addDefault("metrics", true);
	}

	public void onEnable() {
		loadConf();
		Bukkit.getServer().getPluginManager().registerEvents(this.freezelistener, this);
		if (getConfig().getBoolean("metrics")) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				// I'm sad.
			}
		} else {
			System.out.print("[FreezeMod] Metrics was not enabled :(");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("freeze")) {
			if (args.length == 0) {
				if (sender.hasPermission("freeze.freeze.self")) {
					if (!(sender instanceof Player)) {
						return false;
					}
					if (frozen.contains(sender.getName())) {
						sender.sendMessage(ChatColor.RED + "ERROR: " + ChatColor.DARK_RED + "you are already frozen.");
						return true;
					}
					frozen.add(sender.getName());
					sender.sendMessage(ChatColor.RED + "You froze yourself.");
					return true;
				}
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if (!sender.hasPermission("freeze.freeze.other")) {
				sender.sendMessage(ChatColor.RED + "ERROR: " + ChatColor.DARK_RED + "You don't have permission to freeze " + target.getName() + ".");
				return true;
			}
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.DARK_RED + "Player not found.");
				return true;
			}
			if ((target.hasPermission("freeze.exempt") && (!sender.hasPermission("freeze.freeze.admin")))) {
				sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.DARK_RED + "You cannot freeze " + target.getName() + ".");
				return true;
			}
			if (frozen.contains(target.getName())) {
				sender.sendMessage(ChatColor.RED + "ERROR: " + ChatColor.DARK_RED + target.getName() + " is already frozen.");
				return true;
			}
			frozen.add(target.getName());
			sender.sendMessage(ChatColor.RED + "You froze " + target.getName() + ".");
			target.sendMessage(ChatColor.RED + "You were frozen by " + sender.getName() + ".");
			Bukkit.getLogger().info("Player " + target.getName() + " was frozen by " + sender.getName());
			return true;

		}
		if (cmd.getName().equalsIgnoreCase("unfreeze")) {
			if (sender.hasPermission("freeze.unfreeze.self")) {
				if (args.length == 0) {
					if (!(sender instanceof Player)) {
						return false;
					}
					if (!frozen.contains(sender.getName())) {
						sender.sendMessage(ChatColor.RED + "ERROR: " + ChatColor.DARK_RED + "you are not frozen.");
						return true;
					}
					sender.sendMessage(ChatColor.RED + "You unfroze yourself.");
					frozen.remove(sender.getName());
					return true;
				}
			} else {
				return false;
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if (!sender.hasPermission("freeze.unfreeze.other")) {
				sender.sendMessage(ChatColor.RED + "ERROR: " + ChatColor.DARK_RED + "You don't have permission to unfreeze " + target.getName() + ".");
				return true;
			}
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.DARK_RED + "Player not found.");
				return true;
			}

			if (!frozen.contains(target.getName())) {
				sender.sendMessage(ChatColor.RED + "ERROR: " + ChatColor.DARK_RED + target.getName() + " is not frozen.");
				return true;

			}
			frozen.remove(target.getName());
			sender.sendMessage(ChatColor.RED + "You unfroze " + target.getName() + ".");
			target.sendMessage(ChatColor.RED + "You were unfrozen by " + sender.getName() + ".");
			return true;

		}
		if (cmd.getName().equalsIgnoreCase("freezeall")) {
			if (sender.hasPermission("freeze.freezeall")) {
				freezeAll = true;
				Bukkit.broadcastMessage(ChatColor.RED + "" + sender.getName() + " froze everybody.");
				return true;
			} else {
				return false;
			}
		}
		if (cmd.getName().equalsIgnoreCase("unfreezeall")) {
			if (sender.hasPermission("freeze.freezeall")) {
				freezeAll = false;
				Bukkit.broadcastMessage(ChatColor.RED + "" + sender.getName() + " unfroze everybody.");
				return true;
			} else {
				return false;
			}
		}
		return true;

	}
}