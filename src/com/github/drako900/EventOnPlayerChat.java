package com.github.drako900;

import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EventOnPlayerChat {
  
	MainAllBank plugin;
    public EventOnPlayerChat(MainAllBank MainAllBank) {
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
	
   public void onplayerchat(AsyncPlayerChatEvent event) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		//CARGAMOS CLASS "UpdateSignStateAllBanks.java"
		plugin.updatesignstate = new UpdateSignStateAllBanks(plugin);
		
		final Player player = event.getPlayer();
		File SPlayerd = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
		YamlConfiguration SPlayer = YamlConfiguration.loadConfiguration(SPlayerd);
		final BankPlayer bPlayer = plugin.bankPlayerForPlayer(player);
		final String mensaje = event.getMessage();
		
		
		
		File playerdata = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + player.getName() +".yml");
       FileConfiguration pInv = YamlConfiguration.loadConfiguration(playerdata);
       int loanactual = pInv.getInt("user.loan");
       int cantidad = 0;
       
       //OBTENEMOS EL BANCO QUE SE ESTA USANDO ACTUALMENTE
       int bankactual = SPlayer.getInt("info.use-last-bank");
       
     	if (bPlayer == null) return;
		
		//EVITAMOS QUE AL ENVIAR EL TEXTO ESTE SE MUESTRE EN EL CHAT
		event.setCancelled(true);
		if(bankactual==4){
			switch (bPlayer.getState()){
				case 1:
					if(mensaje.equalsIgnoreCase("Ready")){
						//OBTENIENDO TODAS LAS ESMERALDAS
						int esmeraldastot = 0;
						ItemStack[] items = player.getInventory().getContents();
						for (int i=0; i<items.length; i++) {
							if(items[i]!=null){
								
								if(items[i].getTypeId()==388){
									//ES ESMERALDA LO QUE HAY EN EL COFRE! :D
									esmeraldastot = (esmeraldastot + items[i].getAmount());
								}else{
									//ES OTRO ITEM 
								}
							}
						
						}
						
						int value_esmerald = plugin.getConfig().getInt("BankEsmerald.esmerald-price");
						
						player.getInventory().remove(Material.EMERALD);
						econ.depositPlayer(player.getName(), (esmeraldastot*value_esmerald));
						
						player.sendMessage(ChatColor.BLUE+"[AllBankss]"+ChatColor.GREEN+plugin.traducir("bankesmerald.info.value.deposit")+econ.format(esmeraldastot*value_esmerald)+plugin.traducir("bankesmerald.info.value.deposit2"));
					}else{
						player.sendMessage("error");
					}
				break;
			}
		}else if(bankactual==2){	
		
		switch (bPlayer.getState()){
		case 1:
			//EVITAMOS QUE USE CARACTERES QUE NO SEAN NUMEROS
			try{
	        	cantidad = Integer.parseInt(mensaje);
	        } catch (NumberFormatException e) {
	        	cantidad = 0;
	        	event.setCancelled(true);
	        	player.sendMessage(plugin.traducir("noisanumber")+"");
	        }
			
				if(mensaje.matches("([0-9])*")&&mensaje.length()<=7){
					//Cantidad ingresad
					
					if(cantidad >= 1000001){
						player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+plugin.traducir("amountexcededborrow"));
					}else{
				        
				        //CARGAMOS LIMITE DE PRESTAMO A PEDIR
				        File config = new File(plugin.getDataFolder() + File.separator + "config.yml");
				        if (config.exists()) {
				        	//COMPROBAMOS SI PUEDE PEDIR ESO
				            String limite = plugin.getConfig().getString("BankLoan.maxloanuser");
				            int limite2=Integer.parseInt(limite);
				            if(limite2==0){
				            	player.sendMessage(ChatColor.RED+plugin.traducir("borrowerrorrefer"));
				            }else{
				            	int calculos = (limite2-loanactual-cantidad);
				            	if(calculos<0){
				            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+plugin.traducir("maxloansobrepased"));
				            	}else{
				            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+plugin.traducir("aprobateloan")+econ.format(cantidad));
				            		
				            		pInv.set("user.loan", (loanactual+cantidad));
				            		
				            		try {
										pInv.save(playerdata);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				            		
				            		
				            		//Actualizamos el mensaje
				            		plugin.updatesignstate.updateSignStateBankLoan(bPlayer.getSign(), 1, player);
				            		//Depositamos al usuario
				            		econ.depositPlayer(player.getName(), cantidad);
				            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.GREEN+plugin.traducir("addaccountm1")+econ.format(cantidad)+plugin.traducir("addaccountm2"));
				            	}
				            }
				        
				        }else{
				        	plugin.getLogger().severe(plugin.traducir("notfoundconfig")+config);
				        	player.sendMessage(ChatColor.RED+plugin.traducir("liquidateerror"));
				        }
					}
				}else{
					player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+plugin.traducir("onlynumbers"));
				}
			break;
		case 2:
			
			//EVITAMOS QUE USE CARACTERES QUE NO SEAN NUMEROS
			try{
	        	cantidad = Integer.parseInt(mensaje);
	        } catch (NumberFormatException e) {
	        	cantidad = 0;
	        	event.setCancelled(true);
	        }
			
			if(mensaje.matches("([0-9])*")&&mensaje.length()<=7){
				int resta;
				resta = (loanactual-cantidad);
				
			if(resta<0){
				player.sendMessage(ChatColor.RED+plugin.traducir("liquidateexcededmin"));
				return;
			}
			
				if(econ.has(player.getName(), cantidad)&&resta>0){
					econ.withdrawPlayer(player.getName(), cantidad);
           		pInv.set("user.loan", (resta));
           		try {
						pInv.save(playerdata);
						//Actualizamos el mensaje
						plugin.updatesignstate.updateSignStateBankLoan(bPlayer.getSign(), 2, player);
						player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.GREEN+plugin.traducir("removeloan1")+ChatColor.WHITE+econ.format(cantidad)+ChatColor.GREEN+plugin.traducir("removeloan2"));
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(resta==0&&loanactual>0&&econ.has(player.getName(), cantidad)){
						pInv.set("user.loan", (resta));
						pInv.set("user.itemdeposit", "0");
						econ.withdrawPlayer(player.getName(), cantidad);
						try {
							pInv.save(playerdata);
					        player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.GREEN+plugin.traducir("accountliquidate"));
					        plugin.updatesignstate.updateSignStateBankLoan(bPlayer.getSign(), 2, player);
						}catch (IOException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}else if(resta==0 && loanactual==0 || econ.has(player.getName(), cantidad)){
					player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.YELLOW+plugin.traducir("erronostatloan"));
				}else{
					
					player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+plugin.traducir("nomoneyliquidate"));

				}
				
		    	
			}else{
				player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+plugin.traducir("onlynumbersminus1000000"));
			}
			break;
		}
		}else if(bankactual==1){
			//BANCO DE XP
			switch (bPlayer.getState()){
				case 1:
					//DEPOSITAR LVL DE XP
					//EVITAMOS QUE USE CARACTERES QUE NO SEAN NUMEROS
					try{
			        	cantidad = Integer.parseInt(mensaje);
			        } catch (NumberFormatException e) {
			        	cantidad = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.traducir("noisanumber")+mensaje+plugin.traducir("noisanumber1"));
			        	return;
			        }
					
					int xpplayer = player.getLevel();
					int xpplayer_save = SPlayer.getInt("bankxp.xp-save");
					
					if(cantidad>xpplayer){
						player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.RED+plugin.traducir("bankxp-chat-no-xp"));
						return;
					}
					
					//TODo BIEN ENTONCES VAMOS A GUARDAR TODo EN EL ARCHIVO
					
					int max_xp_save_config = plugin.getConfig().getInt("BankXP.max-xp-levels-save");
					
					if((xpplayer_save+cantidad) > max_xp_save_config){
						if(max_xp_save_config == 0){}else{
							player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.RED+plugin.traducir("bank-xp-max-save-reached")+" "+ChatColor.YELLOW+econ.format(max_xp_save_config)+" level(s)");
							return;
						}
					}
					
					player.setLevel(xpplayer-cantidad);
					SPlayer.set("bankxp.xp-save", xpplayer_save+cantidad);
					
				try {
					SPlayer.save(SPlayerd);
					//XLANG REQUIRED
					plugin.updatesignstate.updateSignStateBankXP(bPlayer.getSign(), 1, player);
					player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.GREEN+plugin.traducir("bankxp-chat-save-succesfull-1")+cantidad+plugin.traducir("bankxp-chat-save-succesfull-2"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
				
				case 2:
					//RETIRAR LVL DE XP
					//EVITAMOS QUE USE CARACTERES QUE NO SEAN NUMEROS
					try{
			        	cantidad = Integer.parseInt(mensaje);
			        	
						int xpplayer_save2 = SPlayer.getInt("bankxp.xp-save");
						
						if(xpplayer_save2<cantidad){
							player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+plugin.traducir("bankxp-error-withdraw-no-xp-inbank"));
							return;
						}
						
						//Todo BIEN ENTONCES SEGUIMOS CON EL PRROCESO
						
						SPlayer.set("bankxp.xp-save", xpplayer_save2 - cantidad);
						
						
						try {
							SPlayer.save(SPlayerd);
							
							int actualxp = player.getLevel();
							
							player.setLevel(cantidad + actualxp);
							
							plugin.updatesignstate.updateSignStateBankXP(bPlayer.getSign(), 2, player);
							player.sendMessage(ChatColor.GREEN+plugin.traducir("bankxp-withdraw-succesfull"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
			        } catch (NumberFormatException e) {
			        	cantidad = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.traducir("noisanumber")+mensaje+plugin.traducir("noisanumber1"));
			        	return;
			        }
				break;
				
				case 3:
					//TRANSFERIR LVL DE XP
					//EVITAMOS QUE USE CARACTERES QUE NO SEAN NUMEROS
					try{
			        	cantidad = Integer.parseInt(mensaje);
			        } catch (NumberFormatException e) {
			        	cantidad = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.traducir("noisanumber")+mensaje+plugin.traducir("noisanumber1"));
			        	return;
			        }
				break;	
			}
		}else if(bankactual==3){
			//BANCO DE MONEY
			switch (bPlayer.getState()){
				case 1:
					try{
			        	cantidad = Integer.parseInt(mensaje);
			        	
			        	//CALCULAMOS TODOO PARA VER SI ES CORRECTO
			        	if(econ.has(player.getName(), cantidad)){
			    			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			    			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			    			
			    			int moneyact = FPlayer.getInt("bankmoney.save-money");
			    			
			    			//1.6 check max-money save
			    			int money_save_total = cantidad+moneyact;
			    			int money_max_save_config = plugin.getConfig().getInt("BankMoney.max-money-save");
			    			
			    			if(money_save_total > money_max_save_config){
			    				//MAXIMO DE DINERO A GUARDAR ALCANZADO
			    				if(money_max_save_config==0){}else{
			    				player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.RED+plugin.traducir("bank-money-max-save-reached")+ChatColor.YELLOW+" "+econ.format(money_max_save_config));
			    				return;
			    				}
			    			}
			    			
			    			FPlayer.set("bankmoney.save-money",cantidad+moneyact);
			    			
			    			try {
								FPlayer.save(fileplayer);
								econ.withdrawPlayer(player.getName(), cantidad);
								player.sendMessage(ChatColor.GREEN + plugin.traducir("bankmoney-save-succesfull-1") +econ.format(cantidad)+plugin.traducir("bankmoney-save-succesfull-2"));
								
								plugin.updatesignstate.updateSignStateBankMoney(bPlayer.getSign(), 1, player);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			
			        	}else{
			        		player.sendMessage(ChatColor.RED+plugin.traducir("bankmoney-error-no-money"));
			        	}
			        } catch (NumberFormatException e) {
			        	cantidad = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.traducir("noisanumber")+mensaje+plugin.traducir("noisanumber1"));
			        	return;
			        }
				break;
				case 2:
					try{
			        	cantidad = Integer.parseInt(mensaje);
			        } catch (NumberFormatException e) {
			        	cantidad = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.traducir("noisanumber")+mensaje+plugin.traducir("noisanumber1"));
			        	return;
			        }
					
	    			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
	    			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
	    			
	    			int moneyact = FPlayer.getInt("bankmoney.save-money");
	    			if(cantidad>moneyact){
	    				player.sendMessage(ChatColor.RED+plugin.traducir("bankmoney-error-withdraw-no-moneysave-1")+econ.format(cantidad)+plugin.traducir("bankmoney-error-withdraw-no-moneysave-2"));
	    			}else{
	    				FPlayer.set("bankmoney.save-money", moneyact-cantidad);
	    				
	    				try {
							FPlayer.save(fileplayer);
							econ.depositPlayer(player.getName(), cantidad);
							plugin.updatesignstate.updateSignStateBankMoney(bPlayer.getSign(), 2, player);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    			}
					
				break;
				case 3:
					
				break;	
			}
		}else{
			//XLANG REQUIRED
			player.sendMessage(ChatColor.RED+plugin.traducir("AllBanks-error-sign-error-unknowreason"));
		}
   }
}
