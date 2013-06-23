package com.github.drako900;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BankPlayer {
  
	private Sign sign;
	private Player player;
	private int state;
	private int transferTemp;
	private String transferPlayer;
	
	BankPlayer(Player player, Sign sign) {
		this.player = player;
		this.sign = sign;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public int getTransferTemp() {
		return transferTemp;
	}

	public void setTransferTemp(int transferTemp) {
		this.transferTemp = transferTemp;
	}

	public String getTransferPlayer() {
		return transferPlayer;
	}

	public void setTransferPlayer(String transferPlayer) {
		this.transferPlayer = transferPlayer;
	}
	
}
