package com.github.drako900;

import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EventOnPlayerJoined {
  
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
    public EventOnPlayerJoined(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
    }
    
	public void onplayerjoined(final PlayerJoinEvent event) throws IOException{
	if(event.getPlayer().isOp()||event.getPlayer().hasPermission("allbanks.info.updateavaible")){
		
		@SuppressWarnings("unused")
		BukkitTask read = new BukkitRunnable() {
			public void run() {
				plugin.checar_act_p(event.getPlayer());
				this.cancel();
			}
			
		}.runTaskTimerAsynchronously(plugin, 50, 50);
		
	}
	//getLogger().info("Juador "+event.getPlayer().getName()+" detectado. Cargando configuracion");
	File playerdata = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + event.getPlayer().getName() +".yml");
	File carpetapdata = new File(plugin.getDataFolder() + File.separator + "pdata");
		//Comprobamos si ya tiene archivo el usuario que se ha unido
    if (!playerdata.exists()) {
    	if(!carpetapdata.exists()) carpetapdata.mkdir();
    	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+plugin.traducir("joinplay1")+event.getPlayer().getName()+plugin.traducir("joinplay2"));
    	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+plugin.traducir("newacount1")+event.getPlayer().getName());
    	try {
			playerdata.createNewFile();
			//Prestamo por default = 0
			//Object defaultm = 0;
			FileConfiguration pInv2 = YamlConfiguration.loadConfiguration(playerdata);
			pInv2.set("user.loan","0");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			
		}
    }else{
    	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+plugin.traducir("joinplay1")+event.getPlayer().getName()+plugin.traducir("joinplay3"));
    }
    
    //calculamos pendientes
    File datosp = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + event.getPlayer().getName() + ".yml");
	FileConfiguration deudap = YamlConfiguration.loadConfiguration(datosp);
	
	int cobropendiente = deudap.getInt("user.pendingtax");
	
	if(cobropendiente==0){
		
	}else{
		event.getPlayer().sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.AQUA+plugin.traducir("time-infoplayercharged")+cobropendiente+plugin.traducir("time-infoplayercharged2"));
		event.getPlayer().sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.AQUA+plugin.traducir("time-infoplayercharged3")+econ.getBalance(event.getPlayer().getName())+plugin.traducir("time-infoplayercharged4"));
		event.getPlayer().sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.YELLOW+plugin.traducir("time-infoplayercharged5"));
		deudap.set("user.pendingtax", 0);
		try{
			deudap.save(datosp);
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		econ.withdrawPlayer(event.getPlayer().getName(), cobropendiente);
	}
}
}
