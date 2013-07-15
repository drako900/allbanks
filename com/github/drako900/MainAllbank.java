package com.github.drako900; 

import net.milkbowl.vault.economy.Economy;
//import net.milkbowl.vault.economy.EconomyResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;  
import org.bukkit.scheduler.BukkitRunnable;

import com.allbanks.events.EventOnBlockBreak;
import com.allbanks.events.EventOnBlockPlace;
import com.allbanks.events.EventOnInventoryClick;
import com.allbanks.events.EventOnPlayerChat;
import com.allbanks.events.EventOnPlayerInteract;
import com.allbanks.events.EventOnPlayerJoined;
import com.allbanks.events.EventOnPlayerQuit;
import com.allbanks.events.EventOnSignChange;
 
public final class MainAllBank extends JavaPlugin implements Listener, CommandExecutor {
  
	static MainAllBank p;
	public static Economy econ = null;
	private CheckUpdate updatechecker;
	private InterestDisbursedSystem systemdisbursed;
	public UpdateSignStateAllBanks updatesignstate;
	private UpdateSignStateAllBanks changesignstate;
	private SystemChargeLoanRunnable systemchargeloan;
	private SystemBankTime systembanktime;
	private EventOnBlockBreak OnBlockBreak_class;
	private EventOnPlayerJoined OnPlayerJoined_class;
	private EventOnSignChange OnSignChange_class;
	private EventOnPlayerChat OnPlayerChat_class;
	private EventOnPlayerQuit OnPlayerQuit_class;
	private EventOnBlockPlace OnBlockPlace_class;
	private EventOnPlayerInteract OnPlayerInteract_class;
	private EventOnInventoryClick OnInvetoryClick;
	public HashMap<BankPlayer, Sign> bankUsers = new HashMap<BankPlayer, Sign>();
	HashMap<Player, Sign> chestopen = new HashMap<Player, Sign>();
	public HashMap<String, Location> moveCheck = new HashMap<String, Location>();
	
	//JDBC
	String user = "allbanks";
	String pass = "all1234";
	String url = "jdbc:mysql://localhost:3306/allbanks";
	
	public void create_inicial_tables_JDBC() throws SQLException, ClassNotFoundException { //Change "SampleFunction" to your own function name (Can be anything, unless it already exists)
		
	    // Load the database driver  
	    Class.forName("com.mysql.jdbc.Driver");  
		Connection conn = DriverManager.getConnection(url, user, pass); //Creates the connection
			
		String sql = ""; 
		PreparedStatement sampleQueryStatement = conn.prepareStatement(sql); //Put your query in the quotes
		
		sampleQueryStatement.executeUpdate(); //Executes the query
		sampleQueryStatement.close(); //Closes the query
		conn.close(); //Closes the connection
	}
	
    public String traducir(String id) {
        String _lang = getConfig().getString("Plugin.language");
        File languagef = new File(this.getDataFolder() + File.separator + _lang + ".yml");
        if (!languagef.exists()) {
        	languagef = new File(this.getDataFolder() + File.separator + "english.yml");
            
        }
        
        if(!languagef.exists()){
        	getServer().getPluginManager().disablePlugin(this);
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Languaje not found!");
        }else{

        }
        
        YamlConfiguration language2 = YamlConfiguration.loadConfiguration(languagef);
        
        String out = language2.getString("languaje."+id);
        if (out != null) {
            return out;
        } else {
            return "(Missing Translation: " + id +") ";
        }
    }
   
    public void interestdisbursedexec(){
    	systemdisbursed = new InterestDisbursedSystem(this);
    	systemdisbursed.disburseinterest();
    }
    
    
	@Override
	public void onEnable(){
			//Bank lottery
		new BukkitRunnable() {
			public void run() {
				
				
				String path  = getDataFolder() + "/lottery/tickets/";
	            File folder = new File(path);
	            
	            String[] fileNames = folder.list();
				
				int min = 0;
				int max = fileNames.length;
				
				if(max==0){
				     //broadcast
				     Bukkit.broadcastMessage(ChatColor.GOLD+"[AB-Lottery] "+ChatColor.RED+traducir("lottery-msg1"));
					return;
				}
				
				Random rnd = new Random();
				
					 //get owner for this ticket
					 int ganador = rnd.nextInt(max-min);
					 	File ticket = new File(path+ganador+".yml");
						FileConfiguration yamlx = YamlConfiguration.loadConfiguration(ticket);
					 
						String playerg = yamlx.getString("lottery.owner");
						
				     //broadcast
				     Bukkit.broadcastMessage(ChatColor.GOLD+"[AB-Lottery] "+ChatColor.GREEN+traducir("lottery-msg2").replace("%player%", playerg));
				     
				     int lottery_buy_cost = 50;
					int total_won = max * lottery_buy_cost ;
				     
				     //deposit money
				     econ.depositPlayer(playerg, total_won);
				     
				     //get player online/ofline
				     OfflinePlayer pTo = getServer().getPlayer(playerg);

				     if (pTo == null) {
				         pTo = getServer().getOfflinePlayer(playerg);
				     }

				     if (pTo == null || !pTo.hasPlayedBefore()) {
				         // Player doesnt exists
				     } else {
				         if (pTo.isOnline()) {
				             // player exists (online)
				        	 Bukkit.getPlayer(playerg).sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				        	 Bukkit.getPlayer(playerg).sendMessage(ChatColor.DARK_GREEN+traducir("lottery-msg3").replace("%money%", total_won+""));
				        	 Bukkit.getPlayer(playerg).sendMessage(ChatColor.DARK_PURPLE+"-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				         } else {
				             // player exists (offline)
				         }
				     }
				     
				     //deleting tickets...
						for(int i = 0; i < max; i++){
							String pathx  = getDataFolder() + "/lottery/tickets/"+i+".yml";
							File datax = new File(pathx);
							
							datax.delete();
						}
			}
		}.runTaskTimerAsynchronously(this, 216000, 216000);
         
        
        
		/*
		getLogger().info("[AllBanks][JDBC] Creating a new tables for JDBC");
		 try {
			 create_inicial_tables_JDBC(); //Runs the function
			 getLogger().info("[AllBanks][JDBC] Succesfull!");
		 } catch (SQLException e) {
			 e.printStackTrace();
		 } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		//CARGAMOS CLASS
		systemdisbursed = new InterestDisbursedSystem(this);
		changesignstate = new UpdateSignStateAllBanks(this);
		systemchargeloan = new SystemChargeLoanRunnable(this);
		systembanktime = new SystemBankTime(this);
		OnPlayerJoined_class = new EventOnPlayerJoined(this);
		OnPlayerChat_class = new EventOnPlayerChat(this);
		OnPlayerInteract_class = new EventOnPlayerInteract(this);
		OnInvetoryClick = new EventOnInventoryClick(this);
		
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		GenerateLang GenerateLang = new GenerateLang(this);
		if(GenerateLang.Generatelanguajesyml()){
		}else{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+traducir("lngenerateERR"));
			getServer().getPluginManager().disablePlugin(this);
		}
        
		//VAULT
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(this, this);
        if (!setupVault() ) {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] [!] "+traducir("vaultrequired"));
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[AllBanks] "+traducir("deshab"));
        	getServer().broadcastMessage(ChatColor.RED+"[AllBanks] "+traducir("deshabplugviewconsole"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }else if(!setupEconomy()){
        	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] [!] "+traducir("economypluginrequired"));
        	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] [!] "+traducir("essentialspexample"));
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED+traducir("deshab"));
        	getServer().broadcastMessage(ChatColor.RED+"[AllBanks] "+traducir("deshabplugviewconsole"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
        //INICIAMOS ECONOMY EN LAS OTRAS CLASS
        changesignstate.setupEconomy();
        systemchargeloan.setupEconomy();
        OnPlayerJoined_class.setupEconomy();
        OnPlayerChat_class.setupEconomy();
        OnPlayerInteract_class.setupEconomy();
		
        //pl = new CommandInfo(this);
        //this.getLogger().info("Vote is Enabled!");
        //PluginManager pm = getServer().getPluginManager();
        //pm.registerEvents(this.pl, this);
        File config = new File(getDataFolder() + File.separator + "config.yml");
     
        if (!config.exists()) {
            this.getLogger().info(traducir("generatingconfigyml"));
            saveResource("config.yml", false);
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
        //Adios comando
		getCommand("allbanks").setExecutor(new CommandsAllBanks(this));
		
        String _lang = getConfig().getString("Plugin.language");
        
        if(_lang.equalsIgnoreCase("spanish")){
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[AllBanks] Habilitado");
        }else if(_lang.equalsIgnoreCase("english")){
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[AllBanks] Enabled");
        }else{
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[AllBanks] Enabled");
        }
		//REPETIR FUNCION CADA CIERTO TIEMPO
		final int minutoscobrar = getConfig().getInt("BankLoan.timetax");
		boolean errorm = false;
		if(minutoscobrar==0){
			errorm = true;
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[AllBanks] "+traducir("time-nofound-timetax"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[AllBanks] "+traducir("time-nofound-timetax2"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[AllBanks] "+traducir("time-nofound-timetax3"));
			//getConfig().set("BankLoan.timetax", 60);
			
			getServer().getPluginManager().disablePlugin(this);
		}
  		
  		//END PREFIX
		if(errorm == false){
			
			
			
			//INTEREST DISBURSTED
			
			new BukkitRunnable() {
				public void run() {
					interestdisbursedexec();
				
				}
			}.runTaskTimerAsynchronously(this, 80, 60*1200);
			
			
			
			systemchargeloan = new SystemChargeLoanRunnable(this);
			systemchargeloan.run_system();
			
			systembanktime = new SystemBankTime(this);
			systembanktime.run_system();
			
			//CHECK UPDATES CADA 6 HORAS
				new BukkitRunnable() {
					public void run() {
						checar_act();
					}
				}.runTaskTimerAsynchronously(this, 0, 432000);
		}
		
	}
	
	public void checar_act(){
		updatechecker = new CheckUpdate(this);
		updatechecker.startUpdateCheck();
	}
	
	public void checar_act_p(Player player){
		updatechecker = new CheckUpdate(this);
		updatechecker.startUpdateCheck2(player);
	}
	
	@EventHandler
	public void onPlayerJoinEvent (final PlayerJoinEvent event) throws IOException {
		OnPlayerJoined_class = new EventOnPlayerJoined(this);
		OnPlayerJoined_class.onplayerjoined(event);
    }
	

	
	
	
	public void closeBankForPlayer(BankPlayer bPlayer) {
		Player player = bPlayer.getPlayer();
		Sign sign = bPlayer.getSign();
		
		if(!bankUsers.containsKey(bPlayer)){
			
			return;
		}
		
		//CARGAMOS CLASS "UpdateSignStateAllBanks.java"
		updatesignstate = new UpdateSignStateAllBanks(this);
		
		if (player.isOnline()) player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW +traducir("closeaccount"));
		
		//Obtenemos el banco anteriormente usado
		File fileplayer = new File(getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
		YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
//XLANG REQUIRED
		int LastBank = FPlayer.getInt("info.use-last-bank");
		if(LastBank==0){
			player.sendMessage(ChatColor.RED+traducir("closebank-error-sessionclose"));
		}else if(LastBank==1){
			//BANK XP == 1
			updatesignstate.updateSignStateBankXP(sign, 0, player);
		}else if(LastBank==2){
			//BANK LOAN == 2
			updatesignstate.updateSignStateBankLoan(sign, 0, player);
		}else if(LastBank==3){
			//BANK MONEY == 3
			updatesignstate.updateSignStateBankMoney(sign, 0, player);
		}else if(LastBank == 4){
			updatesignstate.updateSignStateBankEsmerald(sign, 0, player);
		}else if(LastBank == 5){
			updatesignstate.updateSignStateBankTime(sign, 0, player);
		}else{
			player.sendMessage(ChatColor.RED+traducir("closebank-error-bank-notfound-1")+ChatColor.WHITE+LastBank+ChatColor.RED+traducir("closebank-error-bank-notfound-2"));
		}
		
		
		
		bankUsers.remove(bPlayer);
		
	}
	

	
   //OBTAIN PLAYER WITH BANKSTATUS
	
	public BankPlayer bankPlayerForPlayer(Player player) {
		for (BankPlayer p : bankUsers.keySet()) {
			if (p.getPlayer().equals(player)) return p;
		}
		return null;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockY() != event.getTo().getBlockY()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ())
        {

        	if (moveCheck.isEmpty()) return;
    		if (!moveCheck.containsKey(event.getPlayer().getName())) return;
    		
    		Player player = event.getPlayer();
    		Location pLoc = player.getLocation();
    		Location cLoc = moveCheck.get(player.getName());
    		
    		if (!pLoc.getWorld().equals(cLoc.getWorld()) || (pLoc.distance(cLoc) > 2 && event.getFrom().distance(event.getTo()) > 0) ) {
    			BankPlayer bPlayer = bankPlayerForPlayer(player.getPlayer());
    			if (bPlayer != null) closeBankForPlayer(bPlayer);
    			
    			moveCheck.remove(player.getName());
    		}

        }
        
	}
	public boolean checksignAllBanksinarea(Block bloque){
		if(bloque.getType()==Material.WALL_SIGN||bloque.getType()==Material.SIGN_POST){
			return false;
		}
		return true;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		OnBlockBreak_class = new EventOnBlockBreak(this);
		OnBlockBreak_class.onblockbreak(event);
	}
	
	@EventHandler
    public void onSignChange(SignChangeEvent event) {
		OnSignChange_class = new EventOnSignChange(this);
		OnSignChange_class.onSingChange(event);
    }
                
            //AÑADIDO INICIO
	
	public void switchBankStateBankLoan(BankPlayer bPlayer) {
		int state = bPlayer.getState();
		Player player = bPlayer.getPlayer();
		Sign sign = bPlayer.getSign();
		// switch state
		state++; if (state > 2) state = 1;
		
		// reset
		bPlayer.setTransferTemp(0);
		bPlayer.setTransferPlayer(null);

		switch (state) {
		case 4: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + traducir("instdiamond"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("instdiamond2"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;
		
		case 1: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + traducir("action")+ChatColor.AQUA+ChatColor.ITALIC+traducir("borrow"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("borrowinstructions"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;

		case 2: {
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + traducir("action")+ChatColor.AQUA+ChatColor.ITALIC+traducir("liquidateloan"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("liquidateloaninstructions"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;

		}
		
		bPlayer.setState(state);
		updatesignstate = new UpdateSignStateAllBanks(this);
		updatesignstate.updateSignStateBankLoan(sign, state, player);
	}
	
	public void switchBankStateBankXP(BankPlayer bPlayer) {
		int state = bPlayer.getState();
		Player player = bPlayer.getPlayer();
		Sign sign = bPlayer.getSign();
		
		// switch state
		state++; if (state > 3) state = 1;
		
		// reset
		bPlayer.setTransferTemp(0);
		bPlayer.setTransferPlayer(null);

		switch (state) {
		//XLANG REQUIRED
		case 1: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank XP -> "+traducir("bankxp-deposit-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("bankxp-instructions-deposit-xp-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;

		case 2: {
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank XP -> "+traducir("bankxp-withdraw-lang-sign"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("bankxp-instructions-withdraw-xp-chat"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;
		
		case 3: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank XP -> "+traducir("bankxp-transfer-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("bankxp-instructions-transfer-xp-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.DARK_AQUA+traducir("bank-xp-transfer-msg1"));
	} break;

		}
		
		bPlayer.setState(state);
		//CARGAMOS CLASS "UpdateSignStateAllBanks.java"
		updatesignstate = new UpdateSignStateAllBanks(this);
		updatesignstate.updateSignStateBankXP(sign, state, player);
	}

	public void switchBankStateBankEsmerald(BankPlayer bPlayer) {
		int state = bPlayer.getState();
		Player player = bPlayer.getPlayer();
		Sign sign = bPlayer.getSign();
		
		// switch state
		state++; if (state > 1) state = 1;
		
		// reset
		bPlayer.setTransferTemp(0);
		bPlayer.setTransferPlayer(null);

		switch (state) {
		//XLANG REQUIRED
		case 1: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Esmerald");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("bank-esmerald-instructions-chat")+ChatColor.WHITE+""+ChatColor.BOLD+"Ready");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;
		}
		
		bPlayer.setState(state);
		updatesignstate = new UpdateSignStateAllBanks(this);
		updatesignstate.updateSignStateBankEsmerald(sign, state, player);
	}	
	
	public void switchBankStateBankMoney(BankPlayer bPlayer) {
		int state = bPlayer.getState();
		Player player = bPlayer.getPlayer();
		Sign sign = bPlayer.getSign();
		
		// switch state
		state++; if (state > 3) state = 1;
		
		// reset
		bPlayer.setTransferTemp(0);
		bPlayer.setTransferPlayer(null);

		switch (state) {
		//XLANG REQUIRED
		case 1: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Money -> "+traducir("bankxp-deposit-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("bankmoney-instructions-deposit-money-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;

		case 2: {
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Money -> "+traducir("bankxp-withdraw-lang-sign"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("bankmoney-instructions-withdraw-money-chat"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;
		
		case 3: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Money -> "+traducir("bankxp-transfer-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.GREEN + traducir("bankmoney-instructions-transfer-money-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD+"[INFO] "+traducir("bank-money-transfer-msg4"));
	} break;

		}
		
		bPlayer.setState(state);
		updatesignstate = new UpdateSignStateAllBanks(this);
		updatesignstate.updateSignStateBankMoney(sign, state, player);
	}

	public void switchBankStateBankTime(BankPlayer bPlayer) {
		int state = bPlayer.getState();
		Player player = bPlayer.getPlayer();
		Sign sign = bPlayer.getSign();
		
		// switch state
		state++; if (state > 1) state = 1;
		
		// reset
		bPlayer.setTransferTemp(0);
		bPlayer.setTransferPlayer(null);

		switch (state) {
		//XLANG REQUIRED
		case 1: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Time");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + traducir("bank-time-instructions"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;
		}
		
		bPlayer.setState(state);
		updatesignstate = new UpdateSignStateAllBanks(this);
		updatesignstate.updateSignStateBankTime(sign, state, player);
	}	
	
	
	public BankPlayer bankPlayerForSign(Sign sign) {
		for (BankPlayer p : bankUsers.keySet()) {
			if (!p.getPlayer().isOnline()) closeBankForPlayer(p);
			else if (p.getSign().equals(sign)) return p;
		}
		return null;
	}

	public boolean isOpenBank(Sign sign) {
		return !bankUsers.containsValue(sign);
	}
	//AÑADIDO FIN
	
	//DATABASES JAVA
	

	//END DATABASES JAVA
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		OnPlayerChat_class = new EventOnPlayerChat(this);
		OnPlayerChat_class.onplayerchat(event);
	}
	
	@EventHandler
	public void playerInventoryEvent(InventoryClickEvent event){
		OnInvetoryClick = new EventOnInventoryClick(this);
		OnInvetoryClick.playerInventoryEvent(event);
	}
	
    @EventHandler
    public void onBlock(BlockPlaceEvent event){
    	OnBlockPlace_class = new EventOnBlockPlace(this);
    	OnBlockPlace_class.onplaceblock(event);
    }

	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent event) {
		OnPlayerInteract_class = new EventOnPlayerInteract(this);
		OnPlayerInteract_class.OnPlayerInteract(event);
    }
	
	
	@EventHandler
	public void onPlayerQuitEvent (PlayerQuitEvent event) {
		OnPlayerQuit_class = new EventOnPlayerQuit(this);
		OnPlayerQuit_class.onplayerquit(event);
	}
	
    private boolean setupVault() {
    	if (getServer().getPluginManager().getPlugin("Vault") == null) {
    		return false;
    	}else{
    		return true;
    	}
    }
    
    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
        }
    
	
 
	@Override
	public void onDisable(){
		String _lang = getConfig().getString("Plugin.language");
		
		if(_lang.equalsIgnoreCase("spanish")){
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] Deshabilitado");
		}else if(_lang.equalsIgnoreCase("english")){
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] Disabled");
		}else{
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] Disabled");
		}
		
		
	}   
}
