package com.github.drako900;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsAllBanks implements CommandExecutor {
  
	MainAllBank plugin;
    public CommandsAllBanks(MainAllBank MainAllBank) {
    	this.plugin = MainAllBank;
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
    	if (sender instanceof Player && label.equalsIgnoreCase("allbanks") && args.length > 0 && args[0].equalsIgnoreCase("reload") || sender instanceof Player && label.equalsIgnoreCase("allbanks:allbanks") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
               
               
               if(sender.hasPermission("allbanks.reload")){
            	   	sender.sendMessage("Reloading configuration... Successfully!");
               		Bukkit.broadcastMessage(ChatColor.WHITE+"[Info] AllBanks reloaded by "+sender.getName()+"!");
               		plugin.reloadConfig();
               		return true;
               }else{
            	   sender.sendMessage(ChatColor.RED+"Denied! You are not authorized.");
            	   return true;
               }
            } else if(label.equalsIgnoreCase("allbanks") && args.length > 0 && args[0].equalsIgnoreCase("reload")){
            		sender.sendMessage("Reloading configuration... Successfully!");
            		Bukkit.broadcastMessage(ChatColor.WHITE+"[Console] AllBanks reloaded!");
            		plugin.reloadConfig();
            		return true;
            }
            // do something
            return false;
    }
}
