package com.github.drako900;

import org.bukkit.entity.Player;

public class BankItem {
	private int status;
	private Player player;
	
	BankItem(Player player, String status){
		this.player = player;
		this.status = Integer.parseInt(status);
	}
	
	public void setstatustransfer(int status){
		this.status = status;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getstatus(){
		return status;
	}
}
