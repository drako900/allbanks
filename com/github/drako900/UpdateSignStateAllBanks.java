package com.github.drako900;

import java.io.File;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class UpdateSignStateAllBanks {
	public static Economy econ = null;
    MainAllBank plugin;
   public UpdateSignStateAllBanks(MainAllBank MainAllBank) {
       this.plugin = MainAllBank;
   }
	
   //ECONOMY
   public boolean setupEconomy(){
       RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
       if (rsp == null) {
           return false;
       }
       econ = rsp.getProvider();
       return econ != null;
       }
   
	public void updateSignStateBankXP(Sign sign, int state, Player player) {
		switch (state) {

		case 0: {
			sign.setLine(0, "" + ChatColor.GREEN + "Bank -> XP");
			sign.setLine(1, "" + ChatColor.GREEN + "~~~~~~~~~~~~");
			sign.setLine(2, "" + plugin.langCF("signwelcome"));
			sign.setLine(3, "" + ChatColor.WHITE + "");
		} break;

		case 1: {
	        
			sign.setLine(0, "" + ChatColor.GREEN + "Bank -> XP");
			sign.setLine(1, "" + ChatColor.GREEN +"~~~~~~~~~~~~");
			//OBTENEMOS LOS NIVELES DE EXPERIENCIA GUARDADOS ACTUALMENTE
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			int xpplayer_save = FPlayer.getInt("bankxp.xp-save");
			int xpplayer_save2 = FPlayer.getInt("bankxp.xp-save-exp");
			int xp_calcu = 0;
			
			if (xpplayer_save <= 15)
			{
				xp_calcu = 17 * xpplayer_save;
			}
			else if (xpplayer_save <= 30)
			{
				xp_calcu = (3*xpplayer_save*xpplayer_save/2)-(59*xpplayer_save/2)+360;
			}
			else
			{
				xp_calcu = (7*xpplayer_save*xpplayer_save/2)-(303*xpplayer_save/2)+2220;
			}
			
			
			
			//XLANG REQUIRED
				sign.setLine(2, "" + plugin.langCF("bankxp-deposit-lang-sign"));
				sign.setLine(3, "" + ChatColor.YELLOW + (xp_calcu+xpplayer_save2)+" exp");
		} break;
		
		case 2: {
			//OBTENEMOS LOS NIVELES DE EXPERIENCIA GUARDADOS ACTUALMENTE
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			int xpplayer_save = FPlayer.getInt("bankxp.xp-save");
			int xpplayer_save2 = FPlayer.getInt("bankxp.xp-save-exp");
			int xp_calcu = 0;
			
			if (xpplayer_save <= 15)
			{
				xp_calcu = 17 * xpplayer_save;
			}
			else if (xpplayer_save <= 30)
			{
				xp_calcu = (3*xpplayer_save*xpplayer_save/2)-(59*xpplayer_save/2)+360;
			}
			else
			{
				xp_calcu = (7*xpplayer_save*xpplayer_save/2)-(303*xpplayer_save/2)+2220;
			}
			
			sign.setLine(0, "" + ChatColor.GREEN+ "Bank -> XP");
			sign.setLine(1, "" + ChatColor.GREEN+"~~~~~~~~~~~~");
			sign.setLine(2, "" + plugin.langCF("bankxp-withdraw-lang-sign"));
			sign.setLine(3, "" + ChatColor.YELLOW + (xp_calcu+xpplayer_save2)+" exp");
		} break;
		
		case 3: {
			//OBTENEMOS LOS NIVELES DE EXPERIENCIA GUARDADOS ACTUALMENTE
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			int xpplayer_save = FPlayer.getInt("bankxp.xp-save");
			int xpplayer_save2 = FPlayer.getInt("bankxp.xp-save-exp");
			int xp_calcu = 0;
			
			if (xpplayer_save <= 15)
			{
				xp_calcu = 17 * xpplayer_save;
			}
			else if (xpplayer_save <= 30)
			{
				xp_calcu = (3*xpplayer_save*xpplayer_save/2)-(59*xpplayer_save/2)+360;
			}
			else
			{
				xp_calcu = (7*xpplayer_save*xpplayer_save/2)-(303*xpplayer_save/2)+2220;
			}
			
			sign.setLine(0, "" + ChatColor.GREEN + "Bank -> XP");
			sign.setLine(1, "" + ChatColor.GREEN +"~~~~~~~~~~~~");
			sign.setLine(2, "" + plugin.langCF("bankxp-transfer-lang-sign"));
			sign.setLine(3, "" + ChatColor.YELLOW + (xp_calcu+xpplayer_save2)+" exp");
		} break;
		
		}
		
		sign.update(true);
	}
	
	//UPDATE SIGN FOR BANKS
	
	public void updateSignStateBankLoan(Sign sign, int state, Player player) {
		switch (state) {

		case 0: {
			sign.setLine(0, "" + ChatColor.AQUA + "Bank -> Loan");
			sign.setLine(1, "" + ChatColor.AQUA + "~~~~~~~~~~~~");
			sign.setLine(2, "" + plugin.langCF("signwelcome"));
			sign.setLine(3, "" + ChatColor.WHITE + "");
		} break;

		case 1: {
			
			String limite = plugin.getConfig().getString("BankLoan.maxloanuser");
            int limite2=Integer.parseInt(limite);
			File playerdata = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + player.getName() +".yml");
	        FileConfiguration pInv = YamlConfiguration.loadConfiguration(playerdata);
	        int loanactual = pInv.getInt("user.loan");
	        int resta=(limite2-loanactual);
	        
			sign.setLine(0, "" + ChatColor.AQUA + "Bank -> Loan");
			sign.setLine(1, "" + ChatColor.AQUA +"~~~~~~~~~~~~");
			if(resta==0){
				sign.setLine(2, "" + plugin.langCF("maxreach1"));
				sign.setLine(3, "" + plugin.langCF("maxreach2"));
			}else{
				sign.setLine(2, "" + plugin.langCF("signask"));
				sign.setLine(3, "" + ChatColor.GREEN + econ.format(resta));
			}
		} break;
		
		case 2: {
			
			File playerdata = new File(plugin.getDataFolder() + File.separator + "pdata" + File.separator + player.getName() +".yml");
	        FileConfiguration pInv = YamlConfiguration.loadConfiguration(playerdata);
	        int loanactual = pInv.getInt("user.loan");
	        
			sign.setLine(0, "" + ChatColor.AQUA+ "Bank -> Loan");
			sign.setLine(1, "" + ChatColor.AQUA+"~~~~~~~~~~~~");
			sign.setLine(2, "" + plugin.langCF("signliquidate"));
			sign.setLine(3, "" + ChatColor.RED + econ.format(loanactual));
		} break;
		
		}
		
		sign.update(true);
	}
	

	
	public void updateSignStateBankEsmerald(Sign sign, int state, Player player) {
		switch (state) {

		case 0: {
			sign.setLine(0, "" + ChatColor.YELLOW + "Bank Esmerald");
			sign.setLine(1, "" + ChatColor.YELLOW + "~~~~~~~~~~~~");
			sign.setLine(2, "" + plugin.langCF("signwelcome"));
			sign.setLine(3, "" + ChatColor.WHITE + "");
		} break;

		case 1: {
	        
			//OBTENEMOS LA CANTIDAD DE ESMERALDAS EN EL INVENTARIO DEL JUGADOR
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
			
			sign.setLine(0, "" + ChatColor.YELLOW + "Bank Esmerald");
			sign.setLine(1, "" + ChatColor.YELLOW +"~~~~~~~~~~~~");
			//XLANG REQUIRED
				sign.setLine(2, "" + ChatColor.AQUA + "Inventory");
				sign.setLine(3, "" + ChatColor.YELLOW + esmeraldastot+" esmeralds");
		} break;
		
		}
		
		sign.update(true);
	}	
	
	public void updateSignStateBankMoney(Sign sign, int state, Player player) {
		switch (state) {

		case 0: {
			sign.setLine(0, "" + ChatColor.WHITE + "Bank -> Money");
			sign.setLine(1, "" + ChatColor.WHITE + "~~~~~~~~~~~~");
			sign.setLine(2, "" + plugin.langCF("signwelcome"));
			sign.setLine(3, "" + ChatColor.WHITE + "");
		} break;

		case 1: {
	        
			sign.setLine(0, "" + ChatColor.WHITE + "Bank -> Money");
			sign.setLine(1, "" + ChatColor.WHITE +"~~~~~~~~~~~~");
			//OBTENEMOS LOS NIVELES DE EXPERIENCIA GUARDADOS ACTUALMENTE
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			int moneyp_save = FPlayer.getInt("bankmoney.save-money");
			
			//XLANG REQUIRED
				sign.setLine(2, "" + ChatColor.AQUA + plugin.langCF("bankxp-deposit-lang-sign"));
				sign.setLine(3, "" + ChatColor.YELLOW + "$"+moneyp_save);
		} break;
		
		case 2: {
			//OBTENEMOS LOS NIVELES DE EXPERIENCIA GUARDADOS ACTUALMENTE
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			int moneyp_save = FPlayer.getInt("bankmoney.save-money");
			
			sign.setLine(0, "" + ChatColor.WHITE+ "Bank -> Money");
			sign.setLine(1, "" + ChatColor.WHITE+"~~~~~~~~~~~~");
			sign.setLine(2, "" + ChatColor.AQUA + plugin.langCF("bankxp-withdraw-lang-sign"));
			sign.setLine(3, "" + ChatColor.YELLOW + "$"+moneyp_save);
		} break;
		
		case 3: {
			//OBTENEMOS LOS NIVELES DE EXPERIENCIA GUARDADOS ACTUALMENTE
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
			int moneyp_save = FPlayer.getInt("bankmoney.save-money");
			
			sign.setLine(0, "" + ChatColor.WHITE + "Bank -> Money");
			sign.setLine(1, "" + ChatColor.WHITE +"~~~~~~~~~~~~");
			sign.setLine(2, "" + ChatColor.AQUA + plugin.langCF("bankxp-transfer-lang-sign"));
			sign.setLine(3, "" + ChatColor.YELLOW + "$"+moneyp_save);
		} break;
		
		}
		
		sign.update(true);
	}
	
	public void updateSignStateBankTime(Sign sign, int state, Player player){
		
		
		
		switch (state) {
		
			case 0: {
				sign.setLine(0, "" + ChatColor.WHITE+ "Bank -> Time");
				sign.setLine(1, "" + ChatColor.WHITE + "~~~~~~~~~~~~");
				sign.setLine(2, "" + plugin.langCF("signwelcome"));
				sign.setLine(3, "" + ChatColor.WHITE + "");
			} break;
		
			case 1: {
				//OBTENEMOS LOS NIVELES DE EXPERIENCIA GUARDADOS ACTUALMENTE
				File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
				YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
				int actual_time = FPlayer.getInt("banktime.time-save");
				
				
				sign.setLine(0, "" + ChatColor.WHITE+ "Bank -> Time");
				sign.setLine(1, plugin.langCF("signwelcome"));
				sign.setLine(2, ChatColor.AQUA+""+ChatColor.BOLD+player.getPlayer().getName());
				sign.setLine(3, ChatColor.GREEN+""+actual_time+" minutes");
			} break;
			
			
			
		}
		
		sign.update(true);
	}
}
