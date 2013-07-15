package com.github.drako900; 
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CheckUpdate {
    MainAllBank plugin;
    public CheckUpdate(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
        currentVersion = MainAllBank.getDescription().getVersion();
    }
 
    
  private String currentVersion;
    private String readurl = "https://raw.github.com/drako900/allbanks/master/mainversion.txt";
 
    public void startUpdateCheck() {
    	
    	
            Logger log = plugin.getLogger();
            
            boolean check_update = plugin.getConfig().getBoolean("update.enable");
            
            if(check_update){
            	
            }else{
            	log.warning("CHECK UPDATE DISABLED! Enable in config.yml");
            	return;
            }
            
            
            try {
                log.info("Checking for a new version...");
                URL url = new URL(readurl);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                int i = 0;
                boolean update_found = false;
                
                
                while ((str = br.readLine()) != null) {
                	i++;
                    String line = str;
                    String new_v = null;
                    
                    if (line.equalsIgnoreCase("version: "+currentVersion) && i==1) {
                        log.info("No update found...");
                    }else if(i==1){
                    	
                    	log.info("Update found! ");
                    	log.info("current version: "+currentVersion);
                    	log.info("new version found, "+line);
                    	new_v = line;
                    	update_found = true;
                    	
                    	
                		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                			if(player.isOp()||player.hasPermission("allbanks.info.updateavaible")){
                				player.sendMessage(ChatColor.GOLD+"[AllBanks] "+ChatColor.AQUA+"[INFO] New Update Found!");
                				player.sendMessage(ChatColor.GOLD+"[AllBanks] "+ChatColor.AQUA+"New version found: "+new_v);
                				
                			}
                		}
                    }
                    
                    //2DA LINEA FORCE UPDATE
                    if(i==2 && update_found == true){
                		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                			if(player.isOp()||player.hasPermission("allbanks.info.updateavaible")){
                				player.sendMessage(ChatColor.GOLD+"[AllBanks] "+ChatColor.AQUA+"Download (Clic URL) "+line);
                			}
                		}
						
                    }
                    
                    if(i==3 && update_found == true){
                    	if(line.equalsIgnoreCase("force: true")){
                    		log.warning("You need to upgrade now!");
                    		
                    		//List<String> players = new ArrayList<String>();

                    		
                    	}
                    }
                }
                br.close();
            } catch (IOException e) {
                log.severe("The UpdateChecker URL is invalid! Please let me know!");
            }
    }
    
    
    public void startUpdateCheck2(Player player) {
		Logger log = plugin.getLogger();
        
        boolean check_update = plugin.getConfig().getBoolean("update.enable");
        
        if(check_update){
        	
        }else{
        	log.warning("CHECK UPDATE DISABLED! Enable in config.yml");
        	return;
        }
        
        try {
            URL url = new URL(readurl);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            int i = 0;
            boolean update_found = false;
            while ((str = br.readLine()) != null) {
            	i++;
                String line = str;
                String new_v = null;
                
                if (line.equalsIgnoreCase("version: "+currentVersion) && i==1) {
                }else if(i==1){
                	new_v = line;
                	update_found = true;
                	
            			if(player.isOp()||player.hasPermission("allbanks.info.updateavaible")){
            				player.sendMessage(ChatColor.GOLD+"[AllBanks] "+ChatColor.AQUA+"[INFO] New Update Found!");
            				player.sendMessage(ChatColor.GOLD+"[AllBanks] "+ChatColor.AQUA+"New version found: "+new_v);
            				
            			}
                }
                
                //2DA LINEA FORCE UPDATE
                if(i==2 && update_found == true){
                	player.sendMessage(ChatColor.GOLD+"[AllBanks] "+ChatColor.AQUA+"Download (Clic URL) "+line);
                }
                
                if(i==3 && update_found == true){
                	if(line.equalsIgnoreCase("force: true")){

                		
                	}
                }
            }
            br.close();
        } catch (IOException e) {
        	
        }
}
