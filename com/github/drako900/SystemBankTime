package com.github.drako900;

import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
	                	File datosp = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + fileNames[i]);
	                	FileConfiguration datosp2 = YamlConfiguration.loadConfiguration(datosp);
	                	
	                	//GET DATA OF BANK MONEY...
	                	int current_time = 0;
	                	current_time = datosp2.getInt("banktime.time-save");
	                	int future_time = current_time + 1;
	                	
	                		datosp2.set("banktime.time-save", future_time);
	                	
	                		try {
	                			datosp2.save(datosp);
	                		} catch (IOException e) {
	                			// TODO Auto-generated catch block
	                			e.printStackTrace();
	                		}
	                	
	            	}
	            }
			}
		}.runTaskTimerAsynchronously(plugin, 40, 1200);
	}
}
