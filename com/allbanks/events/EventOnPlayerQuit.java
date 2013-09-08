package com.allbanks.events;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.drako900.BankPlayer;
import com.github.drako900.MainAllBank;
import com.github.drako900.UpdateSignStateAllBanks;

public class EventOnPlayerQuit {
	
	MainAllBank plugin;
    public EventOnPlayerQuit(MainAllBank MainAllBank) {
    	this.plugin = MainAllBank;
    }
	
   public void onplayerquit(PlayerQuitEvent event){
		BankPlayer bPlayer = plugin.bankPlayerForPlayer(event.getPlayer());
		//CARGAMOS CLASS "UpdateSignStateAllBanks.java"
		plugin.updatesignstate = new UpdateSignStateAllBanks(plugin);
		
		if (bPlayer != null) {
			
			//OBTENEMOS EL BANCO ULTIMO USADO
			File fileplayer = new File(plugin.getDataFolder()+File.separator+"pdata"+File.separator+event.getPlayer().getName()+".yml");
			YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);

			int actualbank = FPlayer.getInt("info.use-last-bank");
			
			if(actualbank == 1){
				plugin.updatesignstate.updateSignStateBankXP(bPlayer.getSign(), 0, event.getPlayer());
			}else if(actualbank == 2){
				plugin.updatesignstate.updateSignStateBankLoan(bPlayer.getSign(), 0, event.getPlayer());
			}else if(actualbank==3){
				plugin.updatesignstate.updateSignStateBankMoney(bPlayer.getSign(), 0, event.getPlayer());
			}else if(actualbank==4){
				plugin.updatesignstate.updateSignStateBankEsmerald(bPlayer.getSign(), 0, event.getPlayer());
			}else if(actualbank == 5){
				plugin.updatesignstate.updateSignStateBankTime(bPlayer.getSign(), 0, event.getPlayer());
			}else{
				//ERROR ._. Y COMO DESCONOCEMOS QUE MALDITO "SIGN" ES NO HACEMOS NADA
				//SKIP
			}
			
			plugin.bankUsers.remove(bPlayer);
			plugin.moveCheck.remove(event.getPlayer().getName());
		}
		Bukkit.getConsoleSender().sendMessage(plugin.langCF("pleavep1").replace("%player%", event.getPlayer().getName()));
   }
}
