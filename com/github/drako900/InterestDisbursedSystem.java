package com.github.drako900;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class InterestDisbursedSystem {
	
     MainAllBank plugin;
    public InterestDisbursedSystem(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
    }
	
    	public String convert_time(int int0){
			//Segundos totales
    		int seconds = int0;
    		
    		//obtenemos minutos
    		int minutes = seconds / 60;
    		seconds = seconds - (minutes * 60);
    		
    		if(minutes > 0){
    			return minutes+" minutes, "+seconds+" seconds";
    		}else if(seconds > 0){
    			return seconds+" seconds";
    		}else{
    			return "NULL seconds";
    		}
    		
    	}
    
		public void disburseinterest(){
			
			//obtenemos el anterior desembolso realizado.
			int time_last_run = 0;
			int time_actual_run;
			final int time_diferencia;
			int conf_disburst_system = plugin.getConfig().getInt("BankMoney.disbursed-interest-per-time");
			final int time_disburst_sytstem;
			int time_restante_runable;
			int first_run = 0;
			
			final java.util.Date date= new java.util.Date();
			
			final File savetime = new File(plugin.getDataFolder() + File.separator + "system-times.yml");
			final YamlConfiguration filetime = YamlConfiguration.loadConfiguration(savetime);
			
			time_last_run = filetime.getInt("BankMoney.last-run");
			time_actual_run = (int) date.getTime();
			time_diferencia = ((time_actual_run - time_last_run)/1000);
			time_disburst_sytstem = (conf_disburst_system * 60);
			time_restante_runable = ((time_disburst_sytstem - time_diferencia)/60)*1000;
			
			if(time_restante_runable<=0){
				first_run = 0;
			}else{
				first_run = time_restante_runable;
			}
			
			if(!savetime.exists()){
				first_run = 0;
				try {
					savetime.createNewFile();
					YamlConfiguration filetime2 = YamlConfiguration.loadConfiguration(savetime);
					
					filetime2.set("BankMoney.last-run", date.getTime());
					filetime2.save(savetime);
					
					plugin.getConfig().set("BankMoney.interest-disbursted-system", true);
					plugin.saveConfig();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			//DEBUG
			if(first_run>0){
				Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b------------ BankMoney ----------------"));
				Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&aCalculating next system startup ... Done!, info:"));
				Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&bBankMoney - Interest Disbursed:"));
				Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b-> "+convert_time((time_disburst_sytstem - time_diferencia))+" remaining to start."));
				Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b---------------------------------------"));
			}
			
			new BukkitRunnable() {
				public void run() {

						
					
			if(plugin.getConfig().getBoolean("BankMoney.interest-disbursted-system")){
				//GET ALL USERS IN BANK MONEY
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
	                	
	                	//GET DATA OF BANK MONEY..
	                	int current_money = 0;
	                	current_money = datosp2.getInt("bankmoney.save-money");
	                	int future_money = current_money * plugin.getConfig().getInt("BankMoney.interest-porcent") / 100 + current_money;
	                	
	                	if(future_money == 0 || future_money == current_money){
	                		//NO ES NECESARIO ACTUALIZAR EVITAMOS LAG
	                	}else{
	                		datosp2.set("bankmoney.save-money", future_money);
	                	
	                		try {
	                			datosp2.save(datosp);
	                		} catch (IOException e) {
	                			// TODO Auto-generated catch block
	                			e.printStackTrace();
	                		}
	                	}
	            	}
	            	
	            	Bukkit.broadcastMessage(plugin.parseFormatChat("&6[AllBanks] &eInterest disbursed!"));
	            }
	            
				filetime.set("BankMoney.last-run", date.getTime());
				try {
					filetime.save(savetime);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				plugin.getLogger().info("System Interest Disbursted DISABLED");
			}
		}
		}.runTaskTimerAsynchronously(plugin, first_run, (1200 * plugin.getConfig().getInt("BankMoney.disbursed-interest-per-time")));
		}
}
