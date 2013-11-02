package com.allbanks.events;

import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.avaje.ebean.text.TextException;
import com.github.drako900.BankPlayer;
import com.github.drako900.MainAllBank;
import com.github.drako900.UpdateSignStateAllBanks;

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
       int cantidad2 = 0;
       
       //OBTENEMOS EL BANCO QUE SE ESTA USANDO ACTUALMENTE
       int bankactual = SPlayer.getInt("info.use-last-bank");
       
     	if (bPlayer == null) return;
		
		//EVITAMOS QUE AL ENVIAR EL TEXTO ESTE SE MUESTRE EN EL CHAT
		event.setCancelled(true);
		if(bankactual==5){
			//BANK TIME
			
			switch (bPlayer.getState()){
			case 1:
				if(mensaje.equalsIgnoreCase("Change")){
					//OBTENIENDO TIEMPO ACTUAL
					int current_time = 0;
					current_time = pInv.getInt("banktime.time-save");
					
					//version 1.6.3
					int get_val_tim = 0;
					get_val_tim = plugin.getConfig().getInt("BankTime.give-money-per-time");
					
					//error #NF-1
					if(get_val_tim==0){
						plugin.getLogger().warning("value missing in config.yml: (BankTime.give-money-per-time), skip.");
					}
					
					econ.depositPlayer(player.getName(), (current_time * get_val_tim));
					
					pInv.set("banktime.time-save", 0);
					try {
						pInv.save(playerdata);
						plugin.updatesignstate.updateSignStateBankTime(bPlayer.getSign(), 1, player);
						player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("bank-time-message-player-deposited-money1").replace("%amount%", current_time+""));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			break;
			
			}
			
   		}else if(bankactual==4){
			switch (bPlayer.getState()){
				case 1:
					if(mensaje.equalsIgnoreCase("Ready")){
						//OBTENIENDO TODAS LAS ESMERALDAS
						int esmeraldastot = 0;
						ItemStack[] items = player.getInventory().getContents();
						for (int i=0; i<items.length; i++) {
							if(items[i]!=null){
								
								if(items[i].getType().equals(Material.EMERALD)){
									//ES ESMERALDA LO QUE HAY EN EL INVENTARIO
									esmeraldastot = (esmeraldastot + items[i].getAmount());
								}else{
									//ES OTRO ITEM 
								}
							}
						
						}
						
						int value_esmerald = plugin.getConfig().getInt("BankEsmerald.esmerald-price");
						
						player.getInventory().remove(Material.EMERALD);
						econ.depositPlayer(player.getName(), (esmeraldastot*value_esmerald));
						
						plugin.updatesignstate.updateSignStateBankEsmerald(bPlayer.getSign(), 1, player);
						player.sendMessage(ChatColor.BLUE+"[AllBankss] "+plugin.langCF("bankesmerald.info.value.deposit").replace("%ammount%", econ.format(esmeraldastot*value_esmerald)));
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
	        	player.sendMessage(plugin.langCF("noisanumber").replace("%value%", cantidad+""));
	        }
			
				if(mensaje.matches("([0-9])*")&&mensaje.length()<=7){
					//Cantidad ingresad
					
					if(cantidad >= 1000001){
						player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("amountexcededborrow"));
					}else{
				        
				        //CARGAMOS LIMITE DE PRESTAMO A PEDIR
				        File config = new File(plugin.getDataFolder() + File.separator + "config.yml");
				        if (config.exists()) {
				        	//COMPROBAMOS SI PUEDE PEDIR ESO
				            String limite = plugin.getConfig().getString("BankLoan.maxloanuser");
				            int limite2=Integer.parseInt(limite);
				            if(limite2==0){
				            	player.sendMessage(plugin.langCF("borrowerrorrefer"));
				            }else{
				            	int calculos = (limite2-loanactual-cantidad);
				            	if(calculos<0){
				            		player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("maxloansobrepased"));
				            	}else{
				            		player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("aprobateloan").replace("%ammount%",econ.format(cantidad)+""));
				            		
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
				            		player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("addaccountm1").replace("%ammount%", econ.format(cantidad)));
				            	}
				            }
				        
				        }else{
				        	plugin.getLogger().severe(plugin.langCF("notfoundconfig").replace("%value%", config+""));
				        	player.sendMessage(plugin.langCF("liquidateerror"));
				        }
					}
				}else{
					player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("onlynumbers"));
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
				player.sendMessage(plugin.langCF("liquidateexcededmin"));
				return;
			}
			
				if(econ.has(player.getName(), cantidad)&&resta>0){
					econ.withdrawPlayer(player.getName(), cantidad);
           		pInv.set("user.loan", (resta));
           		try {
						pInv.save(playerdata);
						//Actualizamos el mensaje
						plugin.updatesignstate.updateSignStateBankLoan(bPlayer.getSign(), 2, player);
						player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("removeloan1").replace("%ammount%",econ.format(cantidad)));
						
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
					        player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("accountliquidate"));
					        plugin.updatesignstate.updateSignStateBankLoan(bPlayer.getSign(), 2, player);
						}catch (IOException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}else if(resta==0 && loanactual==0 || econ.has(player.getName(), cantidad)){
					player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("erronostatloan"));
				}else{
					
					player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("nomoneyliquidate"));

				}
				
		    	
			}else{
				player.sendMessage(ChatColor.BLUE+"[AllBanks]"+plugin.langCF("onlynumbers"));
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
			        	player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
			        	return;
			        }
					
					if(cantidad<1){
			        	player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
			        	return;
					}
					
					int xpplayer = player.getLevel();
					int xpplayer_save3 = SPlayer.getInt("bankxp.xp-save-exp");
					
					
					if(cantidad>xpplayer){
						player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("bankxp-chat-no-xp"));
						return;
					}
					
					//TODo BIEN ENTONCES VAMOS A GUARDAR TODo EN EL ARCHIVO
					
					int max_xp_save_config = plugin.getConfig().getInt("BankXP.max-xp-levels-save");
					
					if((xpplayer_save3+plugin.convertLevelToExp(cantidad)) >= plugin.convertLevelToExp(max_xp_save_config)){
						if(max_xp_save_config == 0){}else{
							player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("bank-xp-max-save-reached").replace("%level%", max_xp_save_config+""));

							return;
						}
					}
					
					//Nuevo fix para la barra de EXPERIENCIA y calculos para el banco de xp, agregado en la 1.6.5
					int xp = ((int)plugin.getCurrentExp(player) - (int) plugin.getXpForLevel(cantidad));
					
			        if (xp < 0) {
			            xp = 0;
			        }
					
					int newLvl = plugin.getLevelForExp(xp);
					float cexp = ((float) (xp - plugin.getXpForLevel(newLvl)) / (float) MainAllBank.xpRequiredForNextLevel[newLvl]);
					player.setExp(cexp);
					
			        int curLvl = player.getLevel();
			        if (curLvl != newLvl) {
			            player.setLevel(newLvl);
			        }
			        //fin del fix
			        
					//int calc_xp = player.getLevel();
					//player.setLevel(calc_xp-cantidad);
					SPlayer.set("bankxp.xp-save-exp", plugin.convertLevelToExp(cantidad)+xpplayer_save3);
					
				try {
					SPlayer.save(SPlayerd);
					//XLANG REQUIRED
					plugin.updatesignstate.updateSignStateBankXP(bPlayer.getSign(), 1, player);
					player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("bankxp-chat-save-succesfull-1").replace("%level%", cantidad+""));
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
			        	
			        	if(cantidad <0){
			        		player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
			        		return;
			        	}
			        	
						int xpplayer_save2 = SPlayer.getInt("bankxp.xp-save-exp");
						int levelss = plugin.getLevelForExp(xpplayer_save2);
						
						if(levelss==0){
							
						}else{
							levelss = levelss + 0;
						}
						
						if((plugin.getLevelForExp(xpplayer_save2)+1)<cantidad){
							player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("bankxp-error-withdraw-no-xp-inbank"));
							player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.YELLOW+levelss+" levels currently saved in the bank.");
							return;
						}
						
						//Todo BIEN ENTONCES SEGUIMOS CON EL PRROCESO
						
						//fix secundario 1.6.5 cantidad negativa
						if(xpplayer_save2 - plugin.convertLevelToExp(cantidad)<0){
							player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("bankxp-error-withdraw-no-xp-inbank"));
							player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.YELLOW+levelss+" levels currently saved in the bank.");
							return;
						}
						
						SPlayer.set("bankxp.xp-save-exp", xpplayer_save2 - plugin.convertLevelToExp(cantidad));
						
						
						try {
							SPlayer.save(SPlayerd);
							
							//Nuevo fix para la barra de EXPERIENCIA y calculos del banco de XP, agregado en la 1.6.5
							int xp2 = ((int)plugin.getCurrentExp(player) + (int) plugin.getXpForLevel(cantidad));
							
					        if (xp2 < 0) {
					            xp = 0;
					        }
							
							int newLvl2 = plugin.getLevelForExp(xp2);
							float cexp2 = ((float) (xp2 - plugin.getXpForLevel(newLvl2)) / (float) MainAllBank.xpRequiredForNextLevel[newLvl2]);
							player.setExp(cexp2);
							
					        int curLvl2 = player.getLevel();
					        if (curLvl2 != newLvl2) {
					            player.setLevel(newLvl2);
					        }
					        //fin del fix
							
							//int actualxp = player.getLevel();
							//player.setLevel(actualxp+cantidad);
							
							plugin.updatesignstate.updateSignStateBankXP(bPlayer.getSign(), 2, player);
							player.sendMessage(plugin.langCF("bankxp-withdraw-succesfull"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
			        } catch (NumberFormatException e) {
			        	cantidad = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
			        	return;
			        }
				break;
				
				case 3:
					//TRANSFERIR LVL DE XP
					//EVITAMOS QUE USE CARACTERES QUE NO SEAN NUMEROS
					switch (bPlayer.getStatusT()){
					case 1:
						try{
							cantidad = Integer.parseInt(mensaje);
							
							if(cantidad<0){
								player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
								return;
							}
							
							//COMPROBAMOS SI EN REALIDAD PUEDE TRANSFERIR ESA CANTIDAD...
			    			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			    			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			    			
			    			int current_xp_save = FPlayer.getInt("bankxp.xp-save-exp");
			    			
			    			if((plugin.getLevelForExp(current_xp_save)+0) < cantidad){
			    				player.sendMessage(plugin.langCF("bank-xp-transfer-msg3-error").replace("%level%", (plugin.getLevelForExp(current_xp_save)+0)+""));
			    				return;
			    			}
							
			    			
			    			
							bPlayer.setStatusT(2);
							bPlayer.setAmountT(cantidad);
							
							//traduction replace
							String frase_1 = plugin.langCF("bank-xp-transfer-msg2");
							frase_1 = frase_1.replace("%level%", cantidad+"");
							
							player.sendMessage(frase_1);
						} catch (NumberFormatException e) {
							cantidad = 0;
							event.setCancelled(true);
							player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
							return;
						}
					break;
					case 2:
						
						//COMPROBAMOS SI EL JUGADOR EXISTE
						Player check_player = this.plugin.getServer().getPlayerExact(mensaje);
						if(check_player == null){
							player.sendMessage(plugin.langCF("bank-xp-transfer-msg4-error").replace("%player%", mensaje));
							return;
						}
						
						//COMPROBAMOS SI EL JUGADOR OBJETIVO NO ES EL MISMO
						if(mensaje.equalsIgnoreCase(player.getName())){
							player.sendMessage(plugin.langCF("bank-xp-transfer-msg6").replace("%player%", mensaje).replace("player%", mensaje));
							return;
						}
						
						int amount_t = bPlayer.getAmountT();
						bPlayer.setStatusT(1);
						bPlayer.setAmountT(0);
						bPlayer.setPlayerTargetT("");
						
						File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
		    			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
		    			
		    			int current_xp_p_s = FPlayer.getInt("bankxp.xp-save-exp");
		    			int sobra = current_xp_p_s - plugin.convertLevelToExp(amount_t);
		    			
		    			FPlayer.set("bankxp.xp-save-exp", sobra);
		    			
		    			//agregamos todo al jugador objetivo
						File fileplayerT = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+mensaje+".yml");
		    			YamlConfiguration FPlayerT = YamlConfiguration.loadConfiguration(fileplayerT);
		    			
		    			int current_xp_p_t = FPlayerT.getInt("bankxp.xp-save-exp");
		    			int total = current_xp_p_t + plugin.convertLevelToExp(amount_t);
		    			FPlayerT.set("bankxp.xp-save-exp", total);
		    			
		    			try {
							FPlayer.save(fileplayer);
							FPlayerT.save(fileplayerT);
							player.sendMessage(plugin.langCF("bank-xp-transfer-msg5").replace("%level%", amount_t+"").replace("%player%", mensaje));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			
						plugin.closeBankForPlayer(bPlayer);
					break;
					
					}
				break;	
			}
		}else if(bankactual==3){
			//BANCO DE MONEY
			switch (bPlayer.getState()){
				case 1:
					try{
						
						try{
							cantidad2 = Integer.parseInt(mensaje);
						}catch (NumberFormatException e){
							player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
							event.setCancelled(true);
							return;
						}
			        	
			        	//CALCULAMOS TODOO PARA VER SI ES CORRECTO
			        	if(econ.has(player.getName(), cantidad2)){
			    			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			    			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			    			
			    			int moneyact = FPlayer.getInt("bankmoney.save-money");
			    			
			    			//1.6 check max-money save
			    			int money_save_total = moneyact + cantidad2;
			    			int money_max_save_config = plugin.getConfig().getInt("BankMoney.max-money-save");
			    			
			    			if(money_save_total>money_max_save_config){
			    				//MAXIMO DE DINERO A GUARDAR ALCANZADO
			    				if(money_max_save_config==0){}else{
			    				player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("bank-money-max-save-reached").replace("%ammount%", money_max_save_config+""));
			    				return;
			    				}
			    			}
			    			
			    			FPlayer.set("bankmoney.save-money",money_save_total);
			    			
			    			try {
								FPlayer.save(fileplayer);
								econ.withdrawPlayer(player.getName(), cantidad2);
								player.sendMessage(plugin.langCF("bankmoney-save-succesfull-1").replace("%ammount%", econ.format(cantidad)));
								
								plugin.updatesignstate.updateSignStateBankMoney(bPlayer.getSign(), 1, player);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			
			        	}else{
			        		player.sendMessage(plugin.langCF("bankmoney-error-no-money"));
			        	}
			        } catch (TextException e) {
			        	cantidad2 = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
			        	return;
			        }
				break;
				case 2:
					try{
			        	cantidad = Integer.parseInt(mensaje);
			        } catch (NumberFormatException e) {
			        	cantidad = 0;
			        	event.setCancelled(true);
			        	player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
			        	return;
			        }
					
	    			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
	    			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
	    			
	    			int moneyact = FPlayer.getInt("bankmoney.save-money");
	    			if(cantidad>moneyact){
	    				player.sendMessage(plugin.langCF("bankmoney-error-withdraw-no-moneysave-1").replace("%ammount%", econ.format(cantidad)));
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
					switch (bPlayer.getStatusT()){
						case 1:
								//Get name...
								String player_namet = mensaje;
								Player player_target = Bukkit.getServer().getPlayerExact(player_namet);
								
								
								
								if(player_target == null){
									//The player no exist
									player.sendMessage(plugin.langCF("bank-money-transfer-msg1").replace("%player%", mensaje));
									return;
								}else{
									
					    			if(mensaje.equals(player.getName())){
					    				player.sendMessage(plugin.langCF("bank-money-transfer-msg6").replace("%player%", mensaje+""));
					    				
					    				return;
					    			}
									
									//Player exist...
									bPlayer.setStatusT(2);
									bPlayer.setPlayerTargetT(mensaje);
									player.sendMessage(plugin.langCF("bank-money-transfer-msg2"));
								}
							break;
						
						case 2:
							try{
								try{
									cantidad = Integer.parseInt(mensaje);
								}catch (NumberFormatException e){
									player.sendMessage(plugin.langCF("noisanumber").replace("%value%", mensaje));
									event.setCancelled(true);
									return;
								}
					        	
					        	//God! Is correctly... match amount and differences...
				    			File player_d_t_f = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+bPlayer.getPlayerTargetT()+".yml");
				    			YamlConfiguration player_d_t = YamlConfiguration.loadConfiguration(player_d_t_f);
					        	
				    			File player_d_s_f = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
				    			YamlConfiguration player_d_s = YamlConfiguration.loadConfiguration(player_d_s_f);
				    			
				    			int current_money = player_d_s.getInt("bankmoney.save-money");
				    			int total_s = current_money - cantidad;
				    			
				    			if(current_money < cantidad){
				    				player.sendMessage(plugin.langCF("bank-money-transfer-msg5").replace("%amount%", cantidad+""));
				    				return;
				    			}
				    		
				    			
				    			int current_money_t = player_d_t.getInt("bankmoney.save-money");
				    			int total_d = current_money_t + cantidad;
				    			
				    			player_d_t.set("bankmoney.save-money", total_d);
				    			player_d_s.set("bankmoney.save-money", total_s);
				    			
				    			try {
									player_d_s.save(player_d_s_f);
									player_d_t.save(player_d_t_f);
									player.sendMessage(plugin.langCF("bank-money-transfer-msg3").replace("%amount%", cantidad+"").replace("%player%", bPlayer.getPlayerTargetT()));
									bPlayer.setState(1);
									plugin.closeBankForPlayer(bPlayer);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    			
					        } catch (NumberFormatException e) {
					        	cantidad = 0;
					        	event.setCancelled(true);
					        	player.sendMessage(ChatColor.RED+plugin.langCF("noisanumber").replace("%value%", mensaje));
					        	return;
					        }
							break;
					}
				break;	
			}
		}else{
			//XLANG REQUIRED
			player.sendMessage(plugin.langCF("AllBanks-error-sign-error-unknowreason"));
		}
   }
}
