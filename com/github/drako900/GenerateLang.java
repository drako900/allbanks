package com.github.drako900;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class GenerateLang {
	
	private MainAllBank plugin; // pointer to your main class, unrequired if you don't need methods from the main class
	 
	public GenerateLang(MainAllBank plugin) {
		this.plugin = plugin;
	}
	
	
	public boolean Generatelanguajesyml(){
		boolean error = false;

		String _lang = plugin.getConfig().getString("Plugin.language");
		
		File rutac = new File(plugin.getDataFolder()+File.separator+"config.yml");
		YamlConfiguration configget = YamlConfiguration.loadConfiguration(rutac);
		String currentv = configget.getString("Plugin.current-version");
		boolean changedversion = false;
		
		String vactual = plugin.getDescription().getVersion();
		
		if(currentv==null){
			currentv = "ERROR";
		}
		
		if(!currentv.equalsIgnoreCase(vactual)){
			changedversion = true;
			
			if(_lang.equalsIgnoreCase("spanish")){
				plugin.getLogger().info("Nueva version encontrada, actualizando ...");
			}else if(_lang.equalsIgnoreCase("english")){
				plugin.getLogger().info("New version found, starting update ...");
			}else{
				plugin.getLogger().info("New version found, starting update ...");
			}
		}
		
		//COPY CONFIG
		if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
			
			  plugin.saveResource("config.yml", false);
			  error = false;
			}else{
				if(changedversion){
					
					
			        //FileConfiguration config2 = YamlConfiguration.loadConfiguration(rutac);
					
					if(_lang.equalsIgnoreCase("spanish")){
						plugin.getLogger().info("Actualizando config.yml [v"+vactual+"]");
						
					}else if(_lang.equalsIgnoreCase("english")){
						plugin.getLogger().info("Updating config.yml [v"+vactual+"]");
						
					}else{
						plugin.getLogger().info("Updating config.yml [v"+vactual+"]");
						
					}
					
					//FIX 1.6 ACTUALIZAR VALORES DE LA CONFIG
					String lang = "english";
					Boolean  off_perm = false;
					Boolean  all_banks_disabled = false;
					
					Boolean bankloan_enable = true;
					int bankloan_maxloan = 1000;
					int  bankloan_timetax = 60;
					int bankloan_taxporcent = 10;
					
					Boolean bankesmerald_enable = true;
					int bankesmerald_price = 10;
					
					Boolean bankmoney_enable = true;
					int bankmoney_maxsave = 0;
					Boolean bankmoney_system = false;
					int bankmoney_interes_porcent = 5;
					int bankmoney_disbursed_interest_per_time = 120;
					
					Boolean bankxp_enable = true;
					int bankxp_maxsave = 0;
					
					Boolean banktime_enable = true;
					int banktime_give_money_per_period = 1;
					int banktime_time_per_period = 1;
					int banktime_max_give_money = -1;
					
					Boolean update_system_enable = true;
					
					Boolean send_metrics = true;
					
					int lottery_max_globals_tickets = -1;
					int lottery_buy_cost_per_ticket = 50;
					int lottery_get_winner_per_time = 120;
					
					lang = plugin.getConfig().getString("Plugin.language");
					off_perm = plugin.getConfig().getBoolean("Plugin.off-permisions");
					all_banks_disabled = plugin.getConfig().getBoolean("Plugin.disable-all-banks");
					
					bankloan_enable = plugin.getConfig().getBoolean("BankLoan.enable-bank");
					bankloan_maxloan = plugin.getConfig().getInt("BankLoan.maxloanuser");
					bankloan_timetax = plugin.getConfig().getInt("BankLoan.timetax");
					bankloan_taxporcent = plugin.getConfig().getInt("BankLoan.taxesporcent");
					
					bankesmerald_enable = plugin.getConfig().getBoolean("BankEsmerald.enable-bank");
					bankesmerald_price = plugin.getConfig().getInt("BankEsmerald.esmerald-price");
					
					bankmoney_enable = plugin.getConfig().getBoolean("BankMoney.enable-bank");
					bankmoney_maxsave = plugin.getConfig().getInt("BankMoney.max-money-save");
					bankmoney_system = plugin.getConfig().getBoolean("BankMoney.interest-disbursted-system");
					bankmoney_interes_porcent = plugin.getConfig().getInt("BankMoney.interest-porcent");
					bankmoney_disbursed_interest_per_time = plugin.getConfig().getInt("BankMoney.disbursed-interest-per-time");
					
					bankxp_enable = plugin.getConfig().getBoolean("BankXP.enable-bank");
					bankxp_maxsave = plugin.getConfig().getInt("BankXP.max-xp-levels-save");
					
					banktime_enable = plugin.getConfig().getBoolean("BankTime.enable-bank");
					banktime_give_money_per_period = plugin.getConfig().getInt("BankTime.give-money-per-period");
					banktime_time_per_period = plugin.getConfig().getInt("BankTime.time-per-period");
					banktime_max_give_money = plugin.getConfig().getInt("BankTime.max-money-give");
					
					update_system_enable = plugin.getConfig().getBoolean("update.enable");
					
					send_metrics = plugin.getConfig().getBoolean("send-metrics.enable");

					lottery_max_globals_tickets = plugin.getConfig().getInt("Lottery.max-global-tickets");
					lottery_buy_cost_per_ticket = plugin.getConfig().getInt("Lottery.buy-cost-per-ticket");
					lottery_get_winner_per_time = plugin.getConfig().getInt("Lottery.get-winner-per-time");
					
					rutac.delete();
					plugin.saveResource("config.yml", true);
					
					//Establecemos valores
					plugin.getConfig().set("Plugin.current-version", plugin.getDescription().getVersion());
					plugin.getConfig().set("Plugin.language", lang);
					plugin.getConfig().set("Plugin.off-permisions", off_perm);
					plugin.getConfig().set("Plugin.disable-all-banks", all_banks_disabled);
					
					plugin.getConfig().set("BankLoan.enable-bank", bankloan_enable);
					plugin.getConfig().set("BankLoan.maxloanuser", bankloan_maxloan);
					plugin.getConfig().set("BankLoan.timetax", bankloan_timetax);
					plugin.getConfig().set("BankLoan.taxesporcent", bankloan_taxporcent);
					
					plugin.getConfig().set("BankEsmerald.enable-bank", bankesmerald_enable);
					plugin.getConfig().set("BankEsmerald.esmerald-price", bankesmerald_price);
					
					plugin.getConfig().set("BankMoney.enable-bank", bankmoney_enable);
					plugin.getConfig().set("BankMoney.max-money-save", bankmoney_maxsave);
					plugin.getConfig().set("BankMoney.interest-disbursted-system", bankmoney_system);
					plugin.getConfig().set("BankMoney.interest-porcent", bankmoney_interes_porcent);
					plugin.getConfig().set("BankMoney.disbursed-interest-per-time", bankmoney_disbursed_interest_per_time);
					
					plugin.getConfig().set("BankXP.enable-bank", bankxp_enable);
					plugin.getConfig().set("BankXP.max-xp-levels-save", bankxp_maxsave);
					
					plugin.getConfig().set("BankTime.enable-bank", banktime_enable);
					plugin.getConfig().set("BankTime.give-money-per-period", banktime_give_money_per_period);
					plugin.getConfig().set("BankTime.time-per-period", banktime_time_per_period);
					plugin.getConfig().set("BankTime.max-money-give", banktime_max_give_money);
					
					plugin.getConfig().set("update.enable", update_system_enable);
					
					plugin.getConfig().set("send-metrics.enable", send_metrics);
					
					plugin.getConfig().set("Lottery.max-global-tickets", lottery_max_globals_tickets);
					plugin.getConfig().set("Lottery.buy-cost-per-ticket", lottery_buy_cost_per_ticket);
					plugin.getConfig().set("Lottery.get-winner-per-time", lottery_get_winner_per_time);
					
					plugin.saveConfig();
					plugin.reloadConfig();
					
					
				}else{
					
					if(_lang.equalsIgnoreCase("spanish")){
						plugin.getLogger().info("Version Actual: [v"+vactual+"]");
					}else if(_lang.equalsIgnoreCase("english")){
						plugin.getLogger().info("Version Found: [v"+vactual+"]");
					}else{
						plugin.getLogger().info("Version Found: [v"+vactual+"]");
					}
					
					
				}
			}
		
		//COPY ESPAÑOL
		if(changedversion){
			
			if(_lang.equalsIgnoreCase("spanish")){
				plugin.getLogger().info("Lenguaje Espanol borrado...");
			}else if(_lang.equalsIgnoreCase("english")){
				plugin.getLogger().info("Spanish language deleted...");
			}else{
				plugin.getLogger().info("Spanish language deleted...");
			}
			
			File spanishfile = new File(plugin.getDataFolder()+File.separator+"spanish.yml");
			spanishfile.delete();
		}
		if (!new File(plugin.getDataFolder(), "spanish.yml").exists()||changedversion) {
			if(changedversion){
				
				if(_lang.equalsIgnoreCase("spanish")){
					plugin.getLogger().info("Actualizando lenguaje Espanol...");
				}else if(_lang.equalsIgnoreCase("english")){
					plugin.getLogger().info("Updating language spanish...");
				}else{
					plugin.getLogger().info("Updating language spanish...");
				}
				
			}else{
				
				if(_lang.equalsIgnoreCase("spanish")){
					plugin.getLogger().info("Lenguaje Espanol extraviado, copiando...");
				}else if(_lang.equalsIgnoreCase("english")){
					plugin.getLogger().info("Language Spanish not found! Copying...");
				}else{
					plugin.getLogger().info("Language Spanish not found! Copying...");
				}
				
			}
			
			  plugin.saveResource("spanish.yml", true);
			  error = false;
			}
		
		//COPY INGLES
		if(changedversion){
			
			if(_lang.equalsIgnoreCase("spanish")){
				plugin.getLogger().info("Lenguaje Ingles borrado...");
			}else if(_lang.equalsIgnoreCase("english")){
				plugin.getLogger().info("English language deleted...");
			}else{
				plugin.getLogger().info("English language deleted...");
			}
			
			File englishfile = new File(plugin.getDataFolder()+File.separator+"english.yml");
			englishfile.delete();
		}
		if (!new File(plugin.getDataFolder(), "english.yml").exists()||changedversion) {
			if(changedversion){
				
				if(_lang.equalsIgnoreCase("spanish")){
					plugin.getLogger().info("Actualizando lenguaje Ingles...");
				}else if(_lang.equalsIgnoreCase("english")){
					plugin.getLogger().info("Updating language english...");
				}else{
					plugin.getLogger().info("Updating language english...");
				}
				
				
			}else{
				if(_lang.equalsIgnoreCase("spanish")){
					plugin.getLogger().info("Lenguaje Ingles extraviado, copiando...");
				}else if(_lang.equalsIgnoreCase("english")){
					plugin.getLogger().info("Language English not found! Copying...");
				}else{
					plugin.getLogger().info("Language English not found! Copying...");
				}
				
			}
			
			
			  plugin.saveResource("english.yml", true);
			  error = false;
			}
		
		if(error==true){ return false; }else{ return true; }
	}

}
