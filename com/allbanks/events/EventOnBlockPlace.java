package com.allbanks.events;

import org.bukkit.event.block.BlockPlaceEvent;

import com.github.drako900.MainAllBank;

public class EventOnBlockPlace {
  
	MainAllBank plugin;
    public EventOnBlockPlace(MainAllBank MainAllBank) {
        this.plugin = MainAllBank;
    }
	
	public void onplaceblock(BlockPlaceEvent event){
		//this event is obsolete
    }
}

