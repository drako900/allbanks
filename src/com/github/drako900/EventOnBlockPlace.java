package com.github.drako900;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class EventOnBlockPlace {
  
	MainAllBank plugin;
    public EventOnBlockPlace(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
    }
	
    @SuppressWarnings("unused")
	public void onplaceblock(BlockPlaceEvent event){
    	Block blplace = event.getBlockPlaced();
    	World blworld = event.getBlockPlaced().getWorld();
    	Player player = event.getPlayer();
    	
    	//CALCULAMOS SEÑALES SI ES QUE HAY...
        int bx = event.getBlockPlaced().getX();
        int by = event.getBlockPlaced().getY();
        int bz = event.getBlockPlaced().getZ();
        
        boolean checkedtrue = false;
        Block bblock1 = null;
        //CALCULOS DE LOS ALREDEDORES
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx+1, by+1, bz);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx-1, by+1, bz);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx+1, by+1, bz+1);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx+1, by+1, bz-1);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx-1, by+1, bz+1);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx-1, by+1, bz-1);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx, by+1, bz-1);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        
        if(checkedtrue==false){bblock1 = blworld.getBlockAt(bx, by+1, bz+1);}
        if(plugin.checksignAllBanksinarea(bblock1)&&checkedtrue==false){ checkedtrue=true; }
        //FIN CALCULOS SEÑALES
    	
    	//Comprobamos si en la configuracion se soportan cofres
      	 File config = new File(plugin.getDataFolder() + File.separator + "config.yml");
   	 if(config.exists()){

   	 }else{
   		 player.sendMessage(ChatColor.RED+"[AllBanks] Config file not found! Disabling...");
   		plugin.getLogger().severe("Config file not found! Disabling...");
   		plugin.getServer().getPluginManager().disablePlugin(plugin);
   	 }
    	
    	if(blplace.getType()==Material.CHEST){
    		//OBTENEMOS DE LOS 4 PUNTOS SI HAY OTRO COFRE...
    	    Material bpos1 = blplace.getLocation().add(-1, 0, 0).getBlock().getType();
    	    Block bpos1b = blplace.getLocation().add(-1, 0, 0).getBlock();
    	    Material bpos2 = blplace.getLocation().add(+1, 0, 0).getBlock().getType();
    	    Block bpos2b = blplace.getLocation().add(-1, 0, 0).getBlock();
    	    Material bpos3 = blplace.getLocation().add(0, 0, -1).getBlock().getType();
    	    Block bpos3b = blplace.getLocation().add(-1, 0, 0).getBlock();
    	    Material bpos4 = blplace.getLocation().add(0, 0, +1).getBlock().getType();
    	    Block bpos4b = blplace.getLocation().add(-1, 0, 0).getBlock();
    	 
    	    if(bpos1 == Material.CHEST || bpos2 == Material.CHEST || bpos3 == Material.CHEST || bpos4 == Material.CHEST){
    	    	Material bposup = blplace.getLocation().add(0, +1, 0).getBlock().getType();
    	        
    	        if(bblock1.getType()==Material.SIGN_POST||bblock1.getType()==Material.WALL_SIGN){
    	        	Sign sign1 = (Sign) bblock1.getState();
    	        	String[] sign = sign1.getLines();
    	        	if(sign[0].equalsIgnoreCase(ChatColor.AQUA+"Bank -> Loan")||sign[0].equalsIgnoreCase(ChatColor.AQUA+"Bank -> Loan")&&sign[1].equalsIgnoreCase(ChatColor.AQUA+"~~~~~~~~~~~~")){
    		           	 //File config = new File(this.getDataFolder() + File.separator + "config.yml");
    		        	 if(config.exists()){
    		        		 
    		        	 }else{
    		        		 player.sendMessage(ChatColor.RED+"[AllBanks] Config file not found! Disabling...");
    		        		 plugin.getLogger().severe("Config file not found! Disabling...");
    		        		 plugin.getServer().getPluginManager().disablePlugin(plugin);
    		        	 }
    	        	}
    	        }
    	        
    	    }
    	}
    }
}
