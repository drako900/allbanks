package com.github.drako900;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandsAllBanks implements CommandExecutor {
  
	MainAllBank plugin;
    public CommandsAllBanks(MainAllBank MainAllBank) {
    	this.plugin = MainAllBank;
    	this.setupEconomy();
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
	
    public void habbank(String bank_name){
    	if(bank_name.equalsIgnoreCase("bankloan")){
    		plugin.getConfig().set("BankLoan.enable-bank", true);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GREEN+"BankLoan enabled!");
    	}else if(bank_name.equalsIgnoreCase("bankmoney")){
    		plugin.getConfig().set("BankMoney.enable-bank", true);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GREEN+"BankMoney enabled!");
    	}else if(bank_name.equalsIgnoreCase("bankxp")){
    		plugin.getConfig().set("BankXP.enable-bank", true);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GREEN+"BankXP enabled!");
    	}else if(bank_name.equalsIgnoreCase("bankesmerald")){
    		plugin.getConfig().set("BankEsmerald.enable-bank", true);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GREEN+"BankEsmerald enabled!");
    	}else if(bank_name.equalsIgnoreCase("banktime")){
    		plugin.getConfig().set("BankTime.enable-bank", true);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GREEN+"BankTime enabled!");
    	}
    	plugin.saveConfig();
    	plugin.reloadConfig();
    }
    
    public void desbank(String bank_name){
    	if(bank_name.equalsIgnoreCase("bankloan")){
    		plugin.getConfig().set("BankLoan.enable-bank", false);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GOLD+"BankLoan disabled!");
    	}else if(bank_name.equalsIgnoreCase("bankmoney")){
    		plugin.getConfig().set("BankMoney.enable-bank", false);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GOLD+"BankMoney disabled!");
    	}else if(bank_name.equalsIgnoreCase("bankxp")){
    		plugin.getConfig().set("BankXP.enable-bank", false);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GOLD+"BankXP disabled!");
    	}else if(bank_name.equalsIgnoreCase("bankesmerald")){
    		plugin.getConfig().set("BankEsmerald.enable-bank", false);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GOLD+"BankEsmerald disabled!");
    	}else if(bank_name.equalsIgnoreCase("banktime")){
    		plugin.getConfig().set("BankTime.enable-bank", false);
    		Bukkit.broadcastMessage(ChatColor.GOLD+"[INFO][AllBanks] "+ChatColor.GOLD+"BankTime disabled!");
    	}else{
    		plugin.getLogger().severe("Error in disable bank");
    	}
    	plugin.saveConfig();
    	plugin.reloadConfig();
    }
    
    public void setTypeId(final int x, final int y, final int z, final int ID, Location location, final World world){
    	new BukkitRunnable() {
    		public void run() {
    				world.getBlockAt((int) x, (int) (y), (int) z).setTypeId(ID);
    				this.cancel();
    		}
    	}.runTaskTimerAsynchronously(plugin, 10, 10);
    }
    
    public void setTypeM(int x, int y, int z, Material ID, Location location, World world){
    	world.getBlockAt((int) x, (int) (y), (int) z).setType(ID);
    }
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player && label.equalsIgnoreCase("allbanks") && args.length > 0){
			 int lottery_buy_cost = 50;
			
		if(args[0].equalsIgnoreCase("lottery")){
			if(args.length>=2){
			
			}else{
				return false;
			}
			
			//Bank Lottery
			if(args[1].equalsIgnoreCase("force-get-winner")){
				
				//permission
				if(!sender.hasPermission("allbanks.command.lottery.forcewinner")){
					sender.sendMessage(ChatColor.RED+plugin.traducir("lottery-msg10"));
					return true;
				}
				
				String path  = plugin.getDataFolder() + "/lottery/tickets/";
				String pathp  = plugin.getDataFolder() + "/lottery/";
	            File folder = new File(path);
	            File folderp = new File(pathp);
	            
	            if(!folder.exists()) folder.mkdirs();
	            if(!folderp.exists()) folderp.mkdirs();
	            
	            String[] fileNames = folder.list();
				
				int min = 0;
				
				
				int max = fileNames.length;
				
				if(max==0){
					sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
					sender.sendMessage(ChatColor.RED+plugin.traducir("lottery-msg4"));
					sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
					return true;
				}
				
				Random rnd = new Random();
				
					 //get owner for this ticket
					 int ganador = rnd.nextInt(max-min);
					 	File ticket = new File(path+ganador+".yml");
						FileConfiguration yamlx = YamlConfiguration.loadConfiguration(ticket);
					 
						String playerg = yamlx.getString("lottery.owner");
						
					 sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
				     sender.sendMessage(ChatColor.DARK_GREEN+plugin.traducir("lottery-msg5")+ChatColor.DARK_AQUA+ganador+ChatColor.DARK_GREEN);
				     sender.sendMessage(ChatColor.DARK_GREEN+plugin.traducir("lottery-msg6")+ChatColor.DARK_AQUA+playerg+ChatColor.DARK_GREEN);
				     sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
				     
				     //broadcast
				     Bukkit.broadcastMessage(ChatColor.GOLD+"[AB-Lottery] "+ChatColor.YELLOW+plugin.traducir("lottery-msg7").replace("%player%", ChatColor.DARK_AQUA+sender.getName()));
				     Bukkit.broadcastMessage(ChatColor.GOLD+"[AB-Lottery] "+ChatColor.GREEN+plugin.traducir("lottery-msg2").replace("%player%", playerg));
				     
				     int total_won = max * lottery_buy_cost;
				     
				     //deposit money
				     econ.depositPlayer(playerg, total_won);
				     
				     //get player online/ofline
				     OfflinePlayer pTo = plugin.getServer().getPlayer(playerg);

				     if (pTo == null) {
				         pTo = plugin.getServer().getOfflinePlayer(playerg);
				     }

				     if (pTo == null || !pTo.hasPlayedBefore()) {
				         // Player doesnt exists
				     } else {
				         if (pTo.isOnline()) {
				             // player exists (online)
				        	 Bukkit.getPlayer(playerg).sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				        	 Bukkit.getPlayer(playerg).sendMessage(ChatColor.DARK_GREEN+plugin.traducir("lottery-msg3").replace("%money%", total_won+""));
				        	 Bukkit.getPlayer(playerg).sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				         } else {
				             // player exists (offline)
				         }
				     }
				     
				     //deleting tickets...
						for(int i = 0; i < max; i++){
							String pathx  = plugin.getDataFolder() + "/lottery/tickets/"+i+".yml";
							File datax = new File(pathx);
							
							datax.delete();
						}
				     
				     return true;
				     
			}else if(args[1].equalsIgnoreCase("buy")){
				
				//permission
				if(!sender.hasPermission("allbanks.command.lottery.buyticket")){
					sender.sendMessage(ChatColor.RED+plugin.traducir("lottery-msg10"));
					return true;
				}
				
				if(args.length>=3){
				
				}else{
					return false;
				}
				
				int cantidad = Integer.parseInt(args[2]);
				String path  = plugin.getDataFolder() + "/lottery/tickets/";
	            File folder = new File(path);
	            
	            if(!folder.exists()){
	            	folder.mkdirs();
	            }
	            
	           
	            int total = cantidad * lottery_buy_cost;
	            
	            //check money in your account...
	            if(!econ.has(sender.getName(), total)){
					sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
					sender.sendMessage(ChatColor.RED+plugin.traducir("lottery-msg8"));
					sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
	            	return true;
	            }
	            
				String[] fileNames = folder.list();
				for(int i = 0; i < cantidad; i++){
					String pathx  = plugin.getDataFolder() + "/lottery/tickets/"+(fileNames.length+i)+".yml";
					File datax = new File(pathx);
					
					String pathx0 = plugin.getDataFolder() + "/lottery/tickets/";
					File datax0 = new File(pathx0);
					
					if(!datax0.exists()){
						datax0.mkdirs();
					}
					
					String pathx3  = plugin.getDataFolder() + File.separator + "lottery"+ File.separator +"players";
					File datax3 = new File(pathx3);
					
					if(!datax3.exists()){
						datax3.mkdirs();
					}
					
					//Participantes
					String pathx2  = plugin.getDataFolder() + File.separator + "lottery"+ File.separator +"players"+ File.separator +sender.getName()+".yml";
					File datax2 = new File(pathx2);
					
					try {
						datax.createNewFile();
						datax2.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					FileConfiguration yamlx = YamlConfiguration.loadConfiguration(datax);
					
					yamlx.set("lottery.owner", sender.getName());

					try {
						yamlx.save(datax);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
				sender.sendMessage(ChatColor.DARK_GREEN+plugin.traducir("lottery-msg9").replace("%amount%", ChatColor.DARK_AQUA+""+cantidad+ChatColor.DARK_GREEN));
				sender.sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=");
				econ.withdrawPlayer(sender.getName(), total);
				return true;
				
				
			}else{
				//Unknown command
				
			}
			
			
		}else if (args[0].equalsIgnoreCase("list-banks")) {
				
				//Permission
				if(!sender.hasPermission("allbanks.command.list-banks")){
					sender.sendMessage(ChatColor.RED+"Not have permission to use this command");
					return true;
				}
				
				sender.sendMessage(ChatColor.DARK_AQUA+"/////////////////////////////////////////////");
				sender.sendMessage(ChatColor.GOLD+"List of ID names of AllBanks:");
				sender.sendMessage(ChatColor.WHITE+"1. bankloan");
				sender.sendMessage(ChatColor.WHITE+"2. bankxp");
				sender.sendMessage(ChatColor.WHITE+"3. bankmoney");
				sender.sendMessage(ChatColor.WHITE+"4. bankesmerald");
				sender.sendMessage(ChatColor.WHITE+"5. banktime");
				sender.sendMessage(ChatColor.DARK_AQUA+"/////////////////////////////////////////////");
				return true;
			}else if (args[0].equalsIgnoreCase("help")) {
				
				//Permission
				if(!sender.hasPermission("allbanks.command.help")){
					sender.sendMessage(ChatColor.RED+"Not have permission to use this command");
					return true;
				}
				
					try{
						int page = 1;
						if(args.length>=2){
							page = Integer.parseInt(args[1]);
						}else{
							page = 1;
						}
						
						int total_paginas = 1;
						
						if(page==0|page==1|page<0){
							page = 1;
						}
							switch (page) {
								case 1:
									sender.sendMessage(ChatColor.DARK_AQUA+"/////////////////////////////////////////////");
									sender.sendMessage(ChatColor.GOLD+"AllBanks Help (Page 1 of "+total_paginas+")");
									sender.sendMessage(ChatColor.WHITE+"1. /allbanks help "+ChatColor.DARK_GREEN+"[page] "+ChatColor.DARK_AQUA+": Display help page.");
									sender.sendMessage(ChatColor.WHITE+"2. /allbanks disable-bank ? "+ChatColor.DARK_AQUA+": Display help page for this command.");
									sender.sendMessage(ChatColor.WHITE+"3. /allbanks removep "+ChatColor.DARK_GREEN+"[player] "+ChatColor.DARK_AQUA+": Remove a player from allbanks");
									sender.sendMessage(ChatColor.WHITE+"4. /allbanks lottery force-get-winner"+ChatColor.DARK_AQUA+": Gets a lottery winner");
									sender.sendMessage(ChatColor.WHITE+"5. /allbanks lottery buy "+ChatColor.DARK_GREEN+"[amount] "+ChatColor.DARK_AQUA+": Buy tickets");					
									sender.sendMessage(ChatColor.DARK_AQUA+"/////////////////////////////////////////////");
									return true;
							default:
									sender.sendMessage(ChatColor.RED+"Error, page not found. Please enter /allbanks help 1");
										return true;
							}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED+"Error, please enter a valid page. Write only numbers.");
						return true;
					}
			}else if (args[0].equalsIgnoreCase("removep")) {
				
				//Permission
				if(!sender.hasPermission("allbanks.command.removep")){
					sender.sendMessage(ChatColor.RED+"Not have permission to use this command");
					return true;
				}
				
				if(args.length==2){
					//Get a player
					sender.sendMessage(ChatColor.GOLD+"Please wait...");
					
					File datosp = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + args[1] +".yml");
	            	
	            	
	            	if(datosp.exists()){
	            		sender.sendMessage(ChatColor.GOLD+"Account found! Deleting...");
	            			if(datosp.delete()){
	            				sender.sendMessage(ChatColor.GREEN+"Account of the player "+args[1]+" deleted!");
	            			}
	            	}else{
	            		sender.sendMessage(ChatColor.DARK_AQUA+"Error, this account not been found in AllBanks Files");
	            	}
				}else{
					sender.sendMessage(ChatColor.RED+"Syntax error, please use:");
					sender.sendMessage(ChatColor.RED+"/allbanks removep [player]");
					sender.sendMessage(ChatColor.GREEN+"Example: /allbanks removep xslime2648");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("disable-bank")) {
				
				//Permission
				if(!sender.hasPermission("allbanks.command.disable-bank")){
					sender.sendMessage(ChatColor.RED+"Not have permission to use this command");
					return true;
				}
				
				if(args.length >= 2){
					if(args[1].equalsIgnoreCase("?")||args[1].equalsIgnoreCase("help")){
						sender.sendMessage(ChatColor.DARK_AQUA+"/////////////////////////////////////////////");
						sender.sendMessage(ChatColor.GOLD+"Help Page:");
						sender.sendMessage(ChatColor.WHITE+"Syntaxis:");
						sender.sendMessage(ChatColor.YELLOW+"/allbanks disable-bank "+ChatColor.DARK_AQUA+"[bank_name] [true/false]");
						sender.sendMessage(ChatColor.WHITE+"Description:");
						sender.sendMessage(ChatColor.YELLOW+"Disable or enable a bank for AllBanks.");
						sender.sendMessage(ChatColor.WHITE+"Tip:");
						sender.sendMessage(ChatColor.YELLOW+"Use "+ChatColor.WHITE+"/allbanks list-banks"+ChatColor.YELLOW+" for the list of names of banks");
						sender.sendMessage(ChatColor.DARK_AQUA+"/////////////////////////////////////////////");
						return true;
					}
				}
				
				if(args.length == 3){
					
					//example command: /allbanks disable-bank [bank_name] [true/false]
					//                  [label]    [args 0]     [args 1]    [args 2]
					if(args[1].equalsIgnoreCase("bankloan")||args[1].equalsIgnoreCase("bankmoney")||args[1].equalsIgnoreCase("bankxp")||args[1].equalsIgnoreCase("bankesmerald")||args[1].equalsIgnoreCase("banktime")){
						if(args[2].equalsIgnoreCase("true")||args[2].equalsIgnoreCase("false")){
							//CORRECT
							if(args[2].equalsIgnoreCase("true")){
								sender.sendMessage(ChatColor.GREEN+args[1]+" successfully disabled");
								desbank(args[1]);
							}else{
								sender.sendMessage(ChatColor.GREEN+args[1]+" successfully enabled");
								habbank(args[1]);
							}
						}else{
							sender.sendMessage(ChatColor.RED+"Error, "+ChatColor.WHITE+args[2]+ChatColor.RED+" is not valid. Please use: "+ChatColor.WHITE+"true or false");
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.RED+"Error, the bank "+ChatColor.WHITE+args[1]+ChatColor.RED+" does not exist.");
						sender.sendMessage(ChatColor.YELLOW+"Tip, use: "+ChatColor.WHITE+"/allbanks list-banks"+ChatColor.YELLOW+" for the list of names of banks");
						return true;
					}
					return true;
					
				}else{
					sender.sendMessage(ChatColor.RED+"Command error, please use:");
					sender.sendMessage(ChatColor.RED+"/allbanks disable-bank [bank_name] [true/false]");
					sender.sendMessage(ChatColor.GREEN+"Example: /allbanks disable-bank bankloan false");
					sender.sendMessage(ChatColor.YELLOW+"Tip, use: "+ChatColor.WHITE+"/allbanks list-banks"+ChatColor.YELLOW+" for the list of names of banks");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("reload")) {
	               
	               
	               if(sender.hasPermission("allbanks.command.reload")){
	            	   	sender.sendMessage("Reloading configuration... Successfully!");
	               		Bukkit.broadcastMessage(ChatColor.WHITE+"[Info] AllBanks reloaded by "+sender.getName()+"!");
	               		plugin.reloadConfig();
	               		return true;
	               }else{
	            	   sender.sendMessage(ChatColor.RED+"Denied! You are not authorized.");
	            	   return true;
	               }
			}
			
			
	            // do something
	            return false;
		}else{
			//ENVIADO DESDE CONSOLA
		}
		
		return false;
    }
}
