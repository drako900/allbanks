package com.github.drako900;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings("unused")
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
					
					int maxloanuser_config = plugin.getConfig().getInt("BankLoan.maxloanuser");
					int timetax_config = plugin.getConfig().getInt("BankLoan.timetax");
					int taxesporcent_config = plugin.getConfig().getInt("BankLoan.taxesporcent");
					
					rutac.delete();
					plugin.saveResource("config.yml", true);
					
			        //FileConfiguration config2 = YamlConfiguration.loadConfiguration(rutac);
					
					if(_lang.equalsIgnoreCase("spanish")){
						plugin.getLogger().info("Actualizando config.yml [v"+vactual+"]");
						plugin.getConfig().set("Plugin.language", "spanish");
					}else if(_lang.equalsIgnoreCase("english")){
						plugin.getLogger().info("Updating config.yml [v"+vactual+"]");
						plugin.getConfig().set("Plugin.language", "english");
					}else{
						plugin.getLogger().info("Updating config.yml [v"+vactual+"]");
						plugin.getConfig().set("Plugin.language", "english");
					}
					
					//FIX 1.5.1 ACTUALIZAR VALORES DE LA CONFIG
					plugin.getConfig().set("BankLoan.maxloanuser", maxloanuser_config);
					plugin.getConfig().set("BankLoan.timetax", timetax_config);
					plugin.getConfig().set("BankLoan.taxesporcent", taxesporcent_config);
					
						plugin.saveConfig();
						
						plugin.saveResource("config.yml", true);
						plugin.reloadConfig();
						plugin.getConfig().options().copyDefaults(true);
						plugin.getConfig().options().copyHeader(true);
						
						plugin.getConfig().set("BankLoan.taxesporcent", taxesporcent_config);
						
						plugin.saveConfig();
					
					
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
