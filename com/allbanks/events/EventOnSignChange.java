package com.allbanks.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import com.github.drako900.MainAllBank;

public class EventOnSignChange {
	
	MainAllBank plugin;
    public EventOnSignChange(MainAllBank MainAllBank) {
    	this.plugin = MainAllBank;
    }
	
    public void onSingChange(SignChangeEvent event){
		
		
        String[] sign = event.getLines();
        Player player = event.getPlayer();
        
        //¿ESTA TRATANDO DE CREAR EL MENSAJE POR MEDIO DE USO DE COLORES Y LETRAS?
        if(sign[0].equalsIgnoreCase(ChatColor.AQUA+"Bank -> Loan")||sign[0].equalsIgnoreCase(ChatColor.GREEN+"Bank -> XP")||sign[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Money")||sign[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Time")||sign[0].equalsIgnoreCase(ChatColor.YELLOW+"Bank Esmerald")){
        	event.getBlock().breakNaturally();
        	player.sendMessage(ChatColor.RED+"Error...");
        	return;
        }
        

        boolean can_create = false;
        boolean disabled_bank = false;
        int bank_id = 0;
        
        if(player.hasPermission("allbanks.sign.create")&&sign[0].equalsIgnoreCase("AllBanks")||player.hasPermission("allbanks.sign.create")&&sign[0].equalsIgnoreCase("All Banks")||player.hasPermission("allbanks.sign.create")&&sign[0].equalsIgnoreCase("AllBanks")){
        	can_create = true;
        }
        
        if(sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("loan")||sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("loan")||sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("loan")) {
        	if(player.hasPermission("a.bankloan.sign.create")){
        		
        			can_create = true;
        			if(!plugin.getConfig().getBoolean("BankLoan.enable-bank")){
        				disabled_bank = true;
        				bank_id = 1;
        			}
        	}
        }
        
        if(sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("xp")||sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("xp")||sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("xp")){
        	if(player.hasPermission("a.bankxp.sign.create")){
        		can_create = true;
    			if(!plugin.getConfig().getBoolean("BankXP.enable-bank")){
    				disabled_bank = true;
    				bank_id = 2;
    			}
        	}
        }
        
        if(sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("emerald")||sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("emerald")||sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("emerald")){
        	if(player.hasPermission("a.bankesmerald.sign.create")){
        		can_create = true;
    			if(!plugin.getConfig().getBoolean("BankEsmerald.enable-bank")){
    				disabled_bank = true;
    				bank_id = 3;
    			}
        	}
        }
        
        if(sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("esmerald")||sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("esmerald")||sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("esmerald")){
        	player.sendMessage(ChatColor.RED+"Error, The bank name is "+ChatColor.WHITE+"Emerald"+ChatColor.RED+" not "+ChatColor.ITALIC+"Esmerald...");
        }
        
        if(sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("money")||sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("money")||sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("money")){
        	if(player.hasPermission("a.bankmoney.sign.create")){
        		can_create = true;
    			if(!plugin.getConfig().getBoolean("BankMoney.enable-bank")){
    				disabled_bank = true;
    				bank_id = 4;
    			}
        	}
        }
        
        if(sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("time")||sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("time")||sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("time")){
        	if(player.hasPermission("a.banktime.sign.create")){
        		can_create = true;
    			if(!plugin.getConfig().getBoolean("BankTime.enable-bank")){
    				disabled_bank = true;
    				bank_id = 5;
    			}
        	}
        }
        
        if(can_create==true && disabled_bank == true){
        	if(bank_id==1){
        		//BankLoan DISABLED
        		event.getBlock().breakNaturally();
        		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't create a bank! BankLoan disabled");
        		return;
        	}else if(bank_id==2){
        		//BankXP DISABLED
        		event.getBlock().breakNaturally();
        		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't create a bank! BankXP disabled");
        		return;
        	}else if(bank_id==3){
        		//BankEsmerald DISABLED
        		event.getBlock().breakNaturally();
        		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't create a bank! BankEsmerald disabled");
        		return;
        	}else if(bank_id==4){
        		//BankMoney DISABLED
        		event.getBlock().breakNaturally();
        		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't create a bank! BankMoney disabled");
        		return;
        	}else if(bank_id == 5){
        		//BankMoney DISABLED
        		event.getBlock().breakNaturally();
        		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't create a bank! BankTime disabled");
        		return;
        	}
        }
        
        if(can_create==false){
        	if(sign[0].equalsIgnoreCase("AllBanks")||sign[0].equalsIgnoreCase("All Banks")||sign[0].equalsIgnoreCase("[AllBanks]")){
        		event.getBlock().breakNaturally();
        		player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("signcantnew"));
        		return;
        	}
        }
        
        

        
            if(event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("loan")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("loan")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("loan")) {
                         event.setLine(0, ChatColor.AQUA+"Bank -> Loan");
                         event.setLine(1, ChatColor.AQUA+"~~~~~~~~~~~~");
                         event.setLine(2, plugin.langCF("signclickopen"));
                         
                         player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("signnewtrue"));
            }else if(event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("xp")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("xp")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("xp")){
                event.setLine(0, ChatColor.GREEN+"Bank -> XP");
                event.setLine(1, ChatColor.GREEN+"~~~~~~~~~~~~");
                event.setLine(2, plugin.langCF("signclickopen"));
                event.setLine(3, ChatColor.AQUA+"");
                
                player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("signnewtrue"));
            }else if(event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("money")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("money")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("money")){
                event.setLine(0, ChatColor.WHITE+"Bank -> Money");
                event.setLine(1, ChatColor.WHITE+"~~~~~~~~~~~~");
                event.setLine(2, plugin.langCF("signclickopen"));
                event.setLine(3, ChatColor.AQUA+"");
                
                player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("signnewtrue"));
            }else if(event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("emerald")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("emerald")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("emerald")){
                event.setLine(0, ChatColor.YELLOW+"Bank Esmerald");
                event.setLine(1, ChatColor.YELLOW+"~~~~~~~~~~~~");
                event.setLine(2, plugin.langCF("signclickopen"));
                event.setLine(3, ChatColor.AQUA+"");
                
                player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("signnewtrue"));
            }else if(event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("time")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("All Banks")&&sign[1].equalsIgnoreCase("time")||event.getBlock().getType()==Material.WALL_SIGN && sign[0].equalsIgnoreCase("AllBanks")&&sign[1].equalsIgnoreCase("time")){
                event.setLine(0, ChatColor.WHITE+"Bank -> Time");
                event.setLine(1, ChatColor.WHITE+"~~~~~~~~~~~~");
                event.setLine(2, plugin.langCF("signclickopen"));
                event.setLine(3, ChatColor.AQUA+"");
                
                player.sendMessage(ChatColor.BLUE+"[AllBanks] "+plugin.langCF("signnewtrue"));
            }else if(sign[0].equalsIgnoreCase("AllBanks")||sign[0].equalsIgnoreCase("All Banks")||sign[0].equalsIgnoreCase("AllBanks")){
            	if(event.getBlock().getType()==Material.WALL_SIGN){
            		player.sendMessage(plugin.langCF("newbank-error-bank-not-found"));
            	}else{
            		//LLAANNGG
            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+" Error! You can not place a SIGN POST, please place a Wall SIGN");
            	}
            	
            	event.getBlock().breakNaturally();
            	
            }else{
            	
            }
    }
}
