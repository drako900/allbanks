package com.github.drako900;

import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EventOnPlayerInteract {
  
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
	
	MainAllBank plugin;
    public EventOnPlayerInteract(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
    }
	
	@SuppressWarnings("unused")
	public void OnPlayerInteract(PlayerInteractEvent event){
		
		Block clickedBlock = event.getClickedBlock();
		
		Player player = event.getPlayer();
		
		if(clickedBlock==null){
			return;
		}
		
		
			Block signBlock = clickedBlock;
    		if (signBlock == null) return;
    	
    		if (signBlock.getTypeId() != 68) return;
			Sign sign = (Sign)signBlock.getState();
			Action action = event.getAction();

			if (!sign.getLine(0).equals(ChatColor.AQUA + "Bank -> Loan")&&!sign.getLine(0).equals(ChatColor.GREEN + "Bank -> XP")&&!sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Money")&&!sign.getLine(0).equals(ChatColor.YELLOW + "Bank Esmerald")&&!sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Time")) return;
			if(action == Action.RIGHT_CLICK_BLOCK){}else{ return; }
		
		
			if (player.hasPermission("ebank.admin") && player.isSneaking()) {
				return;
			}
			event.setCancelled(true);
		
			BankPlayer bPlayer = null;
			
			//DETECTAMOS SI TIENE PERMISOS DE USAR
			boolean cant_use = false;
			
			//MODO "SIN PLUGIN DE PERMISOS" ¿Activado?
			if(plugin.getConfig().getBoolean("Plugin.off-permisions")==true){
				cant_use = true;
			}
				
			boolean disabled_bank = false;
			int bank_id = 0;
			
    		if(player.hasPermission("allbanks.sign.use")){
    			cant_use = true;
    			
    			if(plugin.getConfig().getBoolean("BankLoan.enable-bank")==false && sign.getLine(0).equals(ChatColor.AQUA + "Bank -> Loan")){
    				disabled_bank = true;
    				bank_id = 1;
    			}
    			
    			if(plugin.getConfig().getBoolean("BankMoney.enable-bank")==false && sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Money")){
    				disabled_bank = true;
    				bank_id = 2;
    			}
    			
    			if(plugin.getConfig().getBoolean("BankXP.enable-bank")==false && sign.getLine(0).equals(ChatColor.GREEN + "Bank -> XP")){
    				disabled_bank = true;
    				bank_id = 3;
    			}
    			
    			if(plugin.getConfig().getBoolean("BankEsmerald.enable-bank")==false && sign.getLine(0).equals(ChatColor.YELLOW + "Bank Esmerald")){
    				disabled_bank = true;
    				bank_id = 4;
    			}
    			
    			if(plugin.getConfig().getBoolean("BankTime.enable-bank")==false && sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Time")){
    				disabled_bank = true;
    				bank_id = 5;
    			}
    			
    		}else if(player.hasPermission("a.bankloan.sign.use") && sign.getLine(0).equals(ChatColor.AQUA + "Bank -> Loan")){
    			cant_use = true;
    			
    			if(plugin.getConfig().getBoolean("BankLoan.enable-bank")==false){
    				disabled_bank = true;
    				bank_id = 1;
    			}
    			
    		}else if(player.hasPermission("a.bankmoney.sign.use") && sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Money")){
    			cant_use = true;
    			
    			if(plugin.getConfig().getBoolean("BankMoney.enable-bank")==false){
    				disabled_bank = true;
    				bank_id = 2;
    			}
    			
    		}else if(player.hasPermission("a.bankxp.sign.use") && sign.getLine(0).equals(ChatColor.GREEN + "Bank -> XP")){
    			cant_use = true;
    			
    			if(plugin.getConfig().getBoolean("BankXP.enable-bank")==false){
    				disabled_bank = true;
    				bank_id = 3;
    			}
    			
    		}else if(player.hasPermission("a.bankesmerald.sign.use") && sign.getLine(0).equals(ChatColor.YELLOW + "Bank Esmerald")){
    			cant_use = true;
    			
    			if(plugin.getConfig().getBoolean("BankEsmerald.enable-bank")==false){
    				disabled_bank = true;
    				bank_id = 4;
    			}
    			
    		}else if(player.hasPermission("a.banktime.sign.use") && sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Time")){
    			cant_use = true;
    			
    			if(plugin.getConfig().getBoolean("BankTime.enable-bank")==false){
    				disabled_bank = true;
    				bank_id = 5;
    			}
    		}
    			
    		
    		
    		if(cant_use == false){
    			if(event.getClickedBlock().getType()==Material.WALL_SIGN||event.getClickedBlock().getType()==Material.SIGN_POST){
    				Sign sign1 = (Sign) event.getClickedBlock().getState();
    				String[] sign2 = sign1.getLines();
    				
    				if(sign2[0].equals(ChatColor.AQUA + "Bank -> Loan")||sign2[0].equals(ChatColor.WHITE + "Bank -> Money")||sign2[0].equals(ChatColor.GREEN + "Bank -> XP")||sign2[0].equals(ChatColor.YELLOW + "Bank Esmerald")||sign2[0].equals(ChatColor.WHITE + "Bank -> Time")){
    					player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.RED+plugin.traducir("nopermusechest"));
    					event.setCancelled(true);
    					return;
    				}
    			}
    		}
    		
    		if(plugin.getConfig().getBoolean("Plugin.disable-all-banks")){
    			if(sign.getLine(0).equals(ChatColor.AQUA + "Bank -> Loan")||sign.getLine(0).equals(ChatColor.GREEN + "Bank -> XP")||sign.getLine(0).equals(ChatColor.YELLOW + "Bank Esmerald")||sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Money")){
    				player.sendMessage(ChatColor.BLUE+"[AllBanks] "+ChatColor.RED+plugin.traducir("cant-use-all-banks-disabled")+ChatColor.AQUA+plugin.traducir("cant-use-all-banks-disabled2"));
    				return;
    			}
    		}
    		
    		if(disabled_bank == true){
    			if(bank_id == 1){
    				//BankLoan
            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't use this bank! "+ChatColor.BOLD+"BankLoan"+ChatColor.RED+" disabled");
            		return;
    			}if(bank_id == 2){
    				//BankMoney
            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't use this bank! "+ChatColor.BOLD+"BankMoney"+ChatColor.RED+" disabled");
            		return;
    			}if(bank_id == 3){
    				//BankXP
            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't use this bank! "+ChatColor.BOLD+"BankXP"+ChatColor.RED+" disabled");
            		return;
    			}if(bank_id == 4){
    				//BankEsmerald
            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't use this bank! "+ChatColor.BOLD+"BankEsmerald"+ChatColor.RED+" disabled");
            		return;
    			}if(bank_id == 5){
    				//BankEsmerald
            		player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't use this bank! "+ChatColor.BOLD+"BankTime"+ChatColor.RED+" disabled");
            		return;
    			}else{
    				player.sendMessage(ChatColor.BLUE+"[AllBanks]"+ChatColor.RED+"You can't use this bank! ");
    				return;
    			}
    		}
			
			//BANK LOAN - CODE
		if(sign.getLine(0).equals(ChatColor.AQUA + "Bank -> Loan")){
			if (plugin.isOpenBank(sign)) {
				if (plugin.bankPlayerForPlayer(player) != null) {
		    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("signalreadyopen"));
					return;
				}
			
				bPlayer = new BankPlayer(player, sign);

				player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
				player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.YELLOW + plugin.traducir("welcomechat1") + player.getName()+"!");
	    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.YELLOW + plugin.traducir("dispmoneychat1") + ": " + econ.format(econ.getBalance(player.getName())));
	    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
	    	
	    		plugin.bankUsers.put(bPlayer, sign);
	    		plugin.moveCheck.put(player.getName(), signBlock.getLocation());
				File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
				YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);

				FPlayer.set("info.use-last-bank", 2);
				try {
					FPlayer.save(fileplayer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				BankPlayer _bPlayer = plugin.bankPlayerForSign(sign);
				if (_bPlayer.getPlayer().equals(player)) bPlayer = _bPlayer;
			}
		
			if (bPlayer != null) {

				plugin.switchBankStateBankLoan(bPlayer);
				
			} else {
	    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("atmisused"));

			}
			
		}
		
			//BANK XP - CODE
		if(sign.getLine(0).equals(ChatColor.GREEN + "Bank -> XP")){
			if (plugin.isOpenBank(sign)) {
				if (plugin.bankPlayerForPlayer(player) != null) {
		    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("signalreadyopen"));
					return;
				}
			
				bPlayer = new BankPlayer(player, sign);
	    	
				plugin.bankUsers.put(bPlayer, sign);
				plugin.moveCheck.put(player.getName(), signBlock.getLocation());
	    		
				File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
				YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);

				FPlayer.set("info.use-last-bank", 1);
				try {
					FPlayer.save(fileplayer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				BankPlayer _bPlayer = plugin.bankPlayerForSign(sign);
				if (_bPlayer.getPlayer().equals(player)) bPlayer = _bPlayer;
			}
		
			if (bPlayer != null) {

				plugin.switchBankStateBankXP(bPlayer);
				
			} else {
	    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("atmisused"));

			}	
		}
		
		//BANK ESMERALD - CODE
	if(sign.getLine(0).equals(ChatColor.YELLOW + "Bank Esmerald")){
		if (plugin.isOpenBank(sign)) {
			if (plugin.bankPlayerForPlayer(player) != null) {
	    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("signalreadyopen"));
				return;
			}
		
			bPlayer = new BankPlayer(player, sign);
    	
			plugin.bankUsers.put(bPlayer, sign);
			plugin.moveCheck.put(player.getName(), signBlock.getLocation());
    		
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);

			FPlayer.set("info.use-last-bank", 4);
			try {
				FPlayer.save(fileplayer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			BankPlayer _bPlayer = plugin.bankPlayerForSign(sign);
			if (_bPlayer.getPlayer().equals(player)) bPlayer = _bPlayer;
		}
	
		if (bPlayer != null) {

			plugin.switchBankStateBankEsmerald(bPlayer);
			
		} else {
    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("atmisused"));

		}	
	}
		
		//BANK MONEY - CODE
	if(sign.getLine(0).equals(ChatColor.WHITE + "Bank -> Money")){
		if (plugin.isOpenBank(sign)) {
			if (plugin.bankPlayerForPlayer(player) != null) {
	    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("signalreadyopen"));
				return;
			}
		
			bPlayer = new BankPlayer(player, sign);
    	
			plugin.bankUsers.put(bPlayer, sign);
			plugin.moveCheck.put(player.getName(), signBlock.getLocation());
    		
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);

			FPlayer.set("info.use-last-bank", 3);
			try {
				FPlayer.save(fileplayer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			BankPlayer _bPlayer = plugin.bankPlayerForSign(sign);
			if (_bPlayer.getPlayer().equals(player)) bPlayer = _bPlayer;
		}
	
		if (bPlayer != null) {

			plugin.switchBankStateBankMoney(bPlayer);
			
		} else {
    		player.sendMessage(ChatColor.BLUE + "[AllBanks] " + ChatColor.WHITE + plugin.traducir("atmisused"));

		}	
	}
	
	//BANK TIME CODE
	if(sign.getLine(0).equals(ChatColor.WHITE+"Bank -> Time")){
		player.sendMessage("Coming Soon");
	}
	
	}
}
