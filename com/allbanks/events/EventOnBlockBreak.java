package com.allbanks.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.drako900.BankPlayer;
import com.github.drako900.MainAllBank;

public class EventOnBlockBreak {
  MainAllBank plugin;
    public EventOnBlockBreak(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
    }
    
	public void onblockbreak(BlockBreakEvent event){
	Player player = event.getPlayer();
	Block b = event.getBlock();
	
	//FIX COMPROBAR AL ROMPER UN BLOQUE SI A SU ALREDEDOR HAY UN LETRERO DE AllBanks
    World bworld = event.getBlock().getWorld();

    int bx = event.getBlock().getX();
    int by = event.getBlock().getY();
    int bz = event.getBlock().getZ();
    
    boolean checkedtrue = false;
    Block bblock1 = null;
    //CALCULOS DE LOS ALREDEDORES
    if(checkedtrue==false){bblock1 = bworld.getBlockAt(bx+1, by, bz);}
    if((bblock1.getType() == Material.SIGN_POST || bblock1.getType() == Material.WALL_SIGN) && checkedtrue==false){ checkedtrue=true; }
    
    if(checkedtrue==false){bblock1 = bworld.getBlockAt(bx-1, by, bz);}
    if((bblock1.getType() == Material.SIGN_POST || bblock1.getType() == Material.WALL_SIGN) && checkedtrue==false){ checkedtrue=true; }
    
    if(checkedtrue==false){bblock1 = bworld.getBlockAt(bx, by, bz-1);}
    if((bblock1.getType() == Material.SIGN_POST || bblock1.getType() == Material.WALL_SIGN) && checkedtrue==false){ checkedtrue=true; }
    
    if(checkedtrue==false){bblock1 = bworld.getBlockAt(bx, by, bz+1);}
    if((bblock1.getType() == Material.SIGN_POST || bblock1.getType() == Material.WALL_SIGN) && checkedtrue==false){ checkedtrue=true; }

    
     
    
    if(checkedtrue==true){
    	
    	Sign sign1 = (Sign) bblock1.getState();
    	String[] lines = sign1.getLines();
    	
    	//¿TIENE PERMISOS DE ROMPER?
    	boolean cant_destroy = false;
    	
    	if(player.hasPermission("allbanks.sign.break")){
    		cant_destroy = true;
    	}else if(player.hasPermission("a.bankloan.sign.break") && lines[0].equalsIgnoreCase(ChatColor.AQUA+"Bank -> Loan")&&lines[1].equalsIgnoreCase(ChatColor.AQUA+"~~~~~~~~~~~~")){
    		cant_destroy = true;
    	}else if(player.hasPermission("a.bankmoney.sign.break") && lines[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Money")&&lines[1].equalsIgnoreCase(ChatColor.WHITE+"~~~~~~~~~~~~")){
    		cant_destroy = true;
    	}else if(player.hasPermission("a.bankxp.sign.break") && lines[0].equalsIgnoreCase(ChatColor.GREEN+"Bank -> XP")&&lines[1].equalsIgnoreCase(ChatColor.GREEN+"~~~~~~~~~~~~")){
    		cant_destroy = true;
    	}else if(player.hasPermission("a.bankesmerald.sign.break") && lines[0].equalsIgnoreCase(ChatColor.YELLOW+"Bank Esmerald")&&lines[1].equalsIgnoreCase(ChatColor.YELLOW+"~~~~~~~~~~~~")){
    		cant_destroy = true;
    	}else if(player.hasPermission("a.banktime.sign.break") && lines[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Time")&&lines[1].equalsIgnoreCase(ChatColor.WHITE+"~~~~~~~~~~~~")){
    		cant_destroy = true;
    	}
    	
    	Sign sign3 = (Sign) bblock1.getState();
    	String[] sign4 = sign3.getLines();
    	
    	if(cant_destroy==false){
    	
    		if(sign4[0].equalsIgnoreCase(ChatColor.AQUA+"Bank -> Loan")||sign4[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Money")||sign4[0].equalsIgnoreCase(ChatColor.GREEN+"Bank -> XP")||sign4[0].equalsIgnoreCase(ChatColor.YELLOW+"Bank Esmerald")||sign4[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Time")){
    			player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+plugin.traducir("signcantdestroy"));
    			event.setCancelled(true);
    			return;
    		}
    	}

    }
    
	if((b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)){
    Sign sign = (Sign) b.getState();
    String[] lines = sign.getLines(); // add here by getLine a number from 0 - 3

        if(lines[0].equalsIgnoreCase(ChatColor.AQUA+"Bank -> Loan")&&lines[1].equalsIgnoreCase(ChatColor.AQUA+"~~~~~~~~~~~~")||lines[0].equalsIgnoreCase(ChatColor.GREEN+"Bank -> XP")&&lines[1].equalsIgnoreCase(ChatColor.GREEN+"~~~~~~~~~~~~")||lines[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Money")&&lines[1].equalsIgnoreCase(ChatColor.WHITE+"~~~~~~~~~~~~")||lines[0].equalsIgnoreCase(ChatColor.YELLOW+"Bank Esmerald")&&lines[1].equalsIgnoreCase(ChatColor.YELLOW+"~~~~~~~~~~~~")&&lines[1].equalsIgnoreCase(ChatColor.WHITE+"~~~~~~~~~~~~")||lines[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Time")&&lines[1].equalsIgnoreCase(ChatColor.WHITE+"~~~~~~~~~~~~")){
        	
        	//¿TIENE PERMISOS DE ROMPER?
        	boolean cant_destroy = false;
        	
        	if(player.hasPermission("allbanks.sign.break")){
        		cant_destroy = true;
        	}else if(player.hasPermission("a.bankloan.sign.break") && lines[0].equalsIgnoreCase(ChatColor.AQUA+"Bank -> Loan")&&lines[1].equalsIgnoreCase(ChatColor.AQUA+"~~~~~~~~~~~~")){
        		cant_destroy = true;
        	}else if(player.hasPermission("a.bankmoney.sign.break") && lines[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Money")&&lines[1].equalsIgnoreCase(ChatColor.WHITE+"~~~~~~~~~~~~")){
        		cant_destroy = true;
        	}else if(player.hasPermission("a.bankxp.sign.break") && lines[0].equalsIgnoreCase(ChatColor.GREEN+"Bank -> XP")&&lines[1].equalsIgnoreCase(ChatColor.GREEN+"~~~~~~~~~~~~")){
        		cant_destroy = true;
        	}else if(player.hasPermission("a.bankesmerald.sign.break") && lines[0].equalsIgnoreCase(ChatColor.YELLOW+"Bank Esmerald")&&lines[1].equalsIgnoreCase(ChatColor.YELLOW+"~~~~~~~~~~~~")){
        		cant_destroy = true;
        	}else if(player.hasPermission("a.banktime.sign.break") && lines[0].equalsIgnoreCase(ChatColor.WHITE+"Bank -> Time")&&lines[1].equalsIgnoreCase(ChatColor.WHITE+"~~~~~~~~~~~~")){
        		cant_destroy = true;
        	}		
        	
        	if(cant_destroy == true){
        		player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.RED+plugin.traducir("signdestroy"));
	        	
        		//Borramos cuenta
    			BankPlayer bPlayer = plugin.bankPlayerForPlayer(event.getPlayer());
    		
    			if (bPlayer != null) {
    				//updateSignState(bPlayer.getSign(), 0);
    				plugin.bankUsers.remove(bPlayer);
    				plugin.moveCheck.remove(event.getPlayer().getName());
    			}
        	}else{
        		//NO TIENE PERMISOS
        		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+plugin.traducir("signcantdestroy")); 
        		event.setCancelled(true); 
        		return;
        	}
        	
        }
	}
	}
}
