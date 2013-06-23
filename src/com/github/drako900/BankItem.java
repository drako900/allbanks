package com.github.drako900;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
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
