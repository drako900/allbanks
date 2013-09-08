package com.github.drako900;

import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

public class SystemChargeLoanRunnable {
	
   MainAllBank plugin;
   public SystemChargeLoanRunnable(MainAllBank MainAllBank) {
       this.plugin = MainAllBank;
   }
   
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
   
   public void run_system(){
	   
		//REPETIR FUNCION CADA CIERTO TIEMPO
		final int minutoscobrar = plugin.getConfig().getInt("BankLoan.timetax");
		if(minutoscobrar==0){
			return;
		}
		final int totaltime = (minutoscobrar*1200);
		
	new BukkitRunnable() {
		public void run() {

	  		
	  		//END PREFIX
			
			
			final int totaltime = (minutoscobrar*1200);
			
			//Guardamos el dato del ultimo cobro
			java.util.Date date= new java.util.Date();
			long timedate = date.getTime();
			File savetime = new File(plugin.getDataFolder() + File.separator + "loandate.yml");
			YamlConfiguration getymltime = YamlConfiguration.loadConfiguration(savetime);
			
			if(!savetime.exists()){
				try {
					savetime.createNewFile();
					getymltime.set("AllBanks.last-time-loan", "0");
					plugin.getLogger().info("File loandate.yml Created!");
					try {
						getymltime.save(savetime);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//OBTENEMOS ULTIMA FECHA DEL COBRO
			int lastloan = getymltime.getInt("AllBanks.last-time-loan");
			//OBTENEMOS EL TIEMPO ACTUAL
			int actualloan = (int) timedate;
			//CONVERTIMOS A SEGUNDOS
			int Slastloan = lastloan / 1000;
			int Sactualloan = actualloan / 1000;
			
			//CALCULAMOS LA DIFERENCIA EN SEGUNDOS DESDE EL ULTIMO COBRO
			int Sdif = Sactualloan - Slastloan;
			
			//CALCULAMOS SI ESTE TIEMPO ES MAYOR AL QUE SE HIZO LA ULTIMA VEZ
			int minutesconfig = plugin.getConfig().getInt("BankLoan.timetax");
			
			//CONVERTIMOS A SEGUNDOS
			int secondsconfig = minutesconfig * 60;
			
			//CALCULAMOS SI YA SE HABIA HECHO OTRO COBRO EN MENOS DE ESTE TIEMPO
			int cancharge = 0;
			
			if(Sdif >= secondsconfig || lastloan==0){
				getymltime.set("AllBanks.last-time-loan", timedate);
				cancharge = 1;
				try {
					getymltime.save(savetime);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				cancharge = 0;
			}
			
			
			
			String path  = plugin.getDataFolder() + "/pdata/";
            File folder = new File(path);
            
            if(!folder.exists()){
            	folder.mkdir();
            	plugin.getLogger().info("New Folder: AllBanks/pdata/");
            }
            
            String[] fileNames = folder.list();
            
            int totaldeudores = 0;
            
            if(cancharge==1 && fileNames.length>0){
            for(int i = 0; i < fileNames.length; i++)
            {
            	File datosp = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + fileNames[i]);
            	FileConfiguration deudap = YamlConfiguration.loadConfiguration(datosp);
            	int deudaactual = deudap.getInt("user.loan");
            	if(deudaactual<=0){
            		//No tiene deuda, no le cobraremos
            	}else{
            		//Si tiene deuda, a cobrarle :P
            		int taxesporcent = plugin.getConfig().getInt("BankLoan.taxesporcent");
            		
            		if(taxesporcent<=0.0){
            			//ERROR, ¿Porciento de cobro en cero?
            			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[AllBanks] "+plugin.langCF("time-configerror"));
            			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[AllBanks] "+plugin.langCF("time-configerror2"));
            			
            		}else{
            			//Yeha! a cobrar
            			totaldeudores = totaldeudores + 1;
            			int subtotal = (int) (deudaactual * taxesporcent / 100);
            			int total = subtotal;
            			
            			int pendienteactual = deudap.getInt("user.pendingtax");
            			int total2 = total + pendienteactual;
            			OfflinePlayer pTo = plugin.getServer().getPlayer(fileNames[i].replace(".yml", ""));
            			
            			if (pTo == null) {
            			    pTo = plugin.getServer().getOfflinePlayer(fileNames[i].replace(".yml", ""));
            			    
            			}
            			
            			Player player = pTo.getPlayer();

            			if (pTo == null || !pTo.hasPlayedBefore()) {
            			    // Player doesnt exists
            				plugin.getLogger().warning(plugin.langCF("time-cantfindplayer").replace("%player%", fileNames[i].replace(".yml", "")));
            			} else {
            			    if (pTo.isOnline()) {
            			        // player exists (online)
            			    	player.sendMessage(ChatColor.GOLD+"[AllBanks] "+plugin.langCF("time-newcharge1").replace("%ammount%", total2+""));
            			    	econ.withdrawPlayer(fileNames[i].replace(".yml", ""), total2);
            			    } else {
            			        // player exists (offline)
            			    	deudap.set("user.pendingtax", total2);
                    			try {
    								deudap.save(datosp);
    							} catch (IOException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    							}
            			    }
            			}
            			
            			
            		}
            	}
                
            }
           
			   String fraseti="";
               Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b----- AllBanks - BankLoan - System ----"));
			   Bukkit.getConsoleSender().sendMessage(plugin.langCF("time-info-console").replace("%number%", totaldeudores+""));
			   Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] "+plugin.langCF("time-newinfochar"));
				Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] "+plugin.langCF("time-newinfochar2"));
			if(minutoscobrar>59){
				fraseti = plugin.langCF("hours");
				Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] "+(minutoscobrar/60)+" "+fraseti+" (Time:"+totaltime+")");
				
			}else{
				fraseti = plugin.langCF("minutes");
				Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] "+minutoscobrar+" "+fraseti+" (Time:"+totaltime+")");
			}
        	Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b---------------------------------------"));
            }else if(fileNames.length>0){
                Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b----- AllBanks - BankLoan - System ----"));
            	Bukkit.getConsoleSender().sendMessage(plugin.langCF("time-already-last-loan"));
            	Bukkit.getConsoleSender().sendMessage(plugin.langCF("time-already-last-loan2"));
            	Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b---------------------------------------"));
            }else{
                Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b----- AllBanks - BankLoan - System ----"));
            	Bukkit.getConsoleSender().sendMessage(plugin.langCF("time-already-last-loan3"));
            	Bukkit.getConsoleSender().sendMessage(plugin.parseFormatChat("&b---------------------------------------"));
            }
				
			
		}
		}.runTaskTimerAsynchronously(plugin, 40, totaltime);
   }
}
