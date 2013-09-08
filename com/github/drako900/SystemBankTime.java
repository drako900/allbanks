package com.github.drako900;

import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

public class SystemBankTime {
	   //ECONOMY
		public static Economy econ = null;
	   public boolean setupEconomy(){
	       RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
	       if (rsp == null) {
	           return false;
	       }
	       econ = rsp.getProvider();
	       return econ != null;
	       }
	
	MainAllBank plugin;
	public SystemBankTime(MainAllBank MainAllBank) {
		this.plugin = MainAllBank;
	}
	
	public void run_system(){
		new BukkitRunnable() {
			public void run() {
				
				//BANK TIME ENABLED?
				if(!plugin.getConfig().getBoolean("BankTime.enable-bank")){
					return;
				}
				
				//GET ALL USERS IN BANK TIME
				String path  = plugin.getDataFolder() + "/pdata/";
	            File folder = new File(path);
	            
	            if(!folder.exists()){
	            	folder.mkdir();
	            	plugin.getLogger().info("New Folder: AllBanks/pdata/");
	            }
	            
	            String[] fileNames = folder.list();
	            
	            if(fileNames.length>0){
	            	for(int i = 0; i < fileNames.length; i++)
	            	{
	            		//The player is online?
            			OfflinePlayer pTo = plugin.getServer().getPlayer(fileNames[i].replace(".yml", ""));
            			
            			if (pTo == null) {
            			    pTo = plugin.getServer().getOfflinePlayer(fileNames[i].replace(".yml", ""));
            			    
            			}
            			
            			Player player = pTo.getPlayer();
	            		
	            		if(player == null){
	            			
	            			
	            		}else{
	            		
	            		if(player.isOnline()){
	            		
	                	File datosp = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + fileNames[i]);
	                	FileConfiguration datosp2 = YamlConfiguration.loadConfiguration(datosp);
	                	
	                	//GET DATA OF BANK MONEY...
	                	int current_time = 0;
	                	current_time = datosp2.getInt("banktime.time-save");
	                	int future_time = current_time + plugin.getConfig().getInt("BankTime.give-money-per-period");
	                	
	                	//Max money give reached?
	                	int max_give_money = plugin.getConfig().getInt("BankTime.max-money-give");
	                	boolean max_g_m_r = false;
	                	
	                	if(max_give_money <= -1){
	                		
	                	}else{
	                		if(current_time>max_give_money){
	                			max_g_m_r = true;
		                	}
	                	}
	                	
	                	if(!max_g_m_r){
	                		datosp2.set("banktime.time-save", future_time);
	                	
	                		try {
	                			datosp2.save(datosp);
	                		} catch (IOException e) {
	                			// TODO Auto-generated catch block
	                			e.printStackTrace();
	                		}
	                	}else{
	                		max_g_m_r = false;
	                	}
	                	
	            		}
	            		}
	            	}
	            }
			}
		}.runTaskTimerAsynchronously(plugin, 40, (1200 * plugin.getConfig().getInt("BankTime.time-per-period")));
	}
}
