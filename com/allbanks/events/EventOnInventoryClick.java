package com.allbanks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.drako900.BankPlayer;
import com.github.drako900.MainAllBank;

public class EventOnInventoryClick {
  
	MainAllBank plugin;
    public EventOnInventoryClick(MainAllBank MainAllBank) {
    	this.plugin = MainAllBank;
    }
	
	@EventHandler
	public void playerInventoryEvent(InventoryClickEvent event)
	{
		 if(!(event.getWhoClicked() instanceof Player)) return;
		 Player p = (Player) event.getWhoClicked();
		 final BankPlayer bPlayer = plugin.bankPlayerForPlayer(p);
		 if (bPlayer == null) return;
		 plugin.updatesignstate.updateSignStateBankEsmerald(bPlayer.getSign(), 1, p);
	}
}
