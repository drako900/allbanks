package com.github.drako900;

import org.bukkit.entity.Player;

public class TransferStatus {
  
	Player player;
	Player second_p;
	int step = 0;
	
	public TransferStatus(Player player, Player second_p) {
		this.player = player;
		this.second_p = second_p;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public void SetPlayer(Player player){
		this.player = player;
	}
	
	public Player getTargetPlayer(){
		return this.second_p;
	}
	
	public void setTargetPlayer(Player player){
		this.second_p = player;
	}
	
	public int getStep(){
		return this.step;
	}
	
	public void setStep(int step2){
		this.step = step2;
	}
	
}
