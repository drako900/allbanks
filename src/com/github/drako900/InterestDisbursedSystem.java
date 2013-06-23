package com.github.drako900;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class InterestDisbursedSystem {
  
     MainAllBank plugin;
    public InterestDisbursedSystem(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
    }
	
		public void disburseinterest(){
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
	                	
	                	//GET DATA OF BANK MONEY...
	                	int current_money = datosp2.getInt("bankmoney.save-money");
	                	int future_money = current_money * 5 / 100 + current_money;
	                	
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
	            }
				
			}else{
				plugin.getLogger().info("System Interest Disbursted DISABLED");
			}
		}
}
