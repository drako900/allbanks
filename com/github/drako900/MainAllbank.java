package com.github.drako900; 

import net.milkbowl.vault.economy.Economy;
//import net.milkbowl.vault.economy.EconomyResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
import com.allbanks.metrics.Metrics;
 
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
	public HashMap<MainLeaderboard, String> leaderboard = new HashMap<MainLeaderboard, String>();
	
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
	

    public String langCF(String id) {
    	
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
        	out = this.parseFormatChat(out);
            return out;
        } else {
            return "(Missing Translation: " + id +") ";
        }
    }
    
    public String parseFormatChat(String txt0){
    	txt0 = txt0.replaceAll("&1", ChatColor.DARK_BLUE+"");
    	txt0 = txt0.replaceAll("&2", ChatColor.DARK_GREEN+"");
    	txt0 = txt0.replaceAll("&3", ChatColor.DARK_AQUA+"");
    	txt0 = txt0.replaceAll("&4", ChatColor.DARK_RED+"");
    	txt0 = txt0.replaceAll("&5", ChatColor.DARK_PURPLE+"");
    	txt0 = txt0.replaceAll("&6", ChatColor.GOLD+"");
    	txt0 = txt0.replaceAll("&7", ChatColor.GRAY+"");
    	txt0 = txt0.replaceAll("&8", ChatColor.DARK_GRAY+"");
    	txt0 = txt0.replaceAll("&9", ChatColor.BLUE+"");
    	txt0 = txt0.replaceAll("&0", ChatColor.BLACK+"");
    	txt0 = txt0.replaceAll("&a", ChatColor.GREEN+"");
    	txt0 = txt0.replaceAll("&b", ChatColor.AQUA+"");
    	txt0 = txt0.replaceAll("&c", ChatColor.RED+"");
    	txt0 = txt0.replaceAll("&d", ChatColor.LIGHT_PURPLE+"");
    	txt0 = txt0.replaceAll("&e", ChatColor.YELLOW+"");
    	txt0 = txt0.replaceAll("&f", ChatColor.WHITE+"");
    	txt0 = txt0.replaceAll("&l", ChatColor.BOLD+"");
    	txt0 = txt0.replaceAll("&n", ChatColor.UNDERLINE+"");
    	txt0 = txt0.replaceAll("&o", ChatColor.ITALIC+"");
    	txt0 = txt0.replaceAll("&k", ChatColor.MAGIC+"");
    	txt0 = txt0.replaceAll("&m", ChatColor.STRIKETHROUGH+"");
    	txt0 = txt0.replaceAll("&r", ChatColor.RESET+"");
		return txt0;
    }
    
    public void interestdisbursedexec(){
    	systemdisbursed = new InterestDisbursedSystem(this);
    	systemdisbursed.disburseinterest();
    }
    
    
	@Override
	public void onEnable(){
		
		getLogger().info("AllBanks LOTTERY enabled, next winner in: "+getConfig().getInt("Lottery.get-winner-per-time")+" minutes");
		
		//metrics
		try {
			if(getConfig().getBoolean("send-metrics.enable")){
				Metrics metrics = new Metrics(this);
				metrics.start();
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&b----- AllBanks - MCStats - Metrics ----"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&e<- Checking settings"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&e-> MCStats enabled"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&b-> MCStats started"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&b---------------------------------------"));
			}else{
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&b----- AllBanks - MCStats - Metrics ----"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&e<- Checking settings"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&e-> MCStats Disabled"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&b-> MCStats ignored, skip"));
				Bukkit.getConsoleSender().sendMessage(parseFormatChat("&b---------------------------------------"));
			}
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		
			//Bank lottery
		new BukkitRunnable() {
			
			
			
			public void run() {
				
				
				
				String path  = getDataFolder() + "/lottery/tickets/";
	            File folder = new File(path);

	            if(!folder.exists()){
	            	folder.mkdir();
	            }
	            
	            folder = new File(path);
	            
	            String[] fileNames = folder.list();
				
				int min = 0;
				int max = fileNames.length;
				
				if(max==0){
				     //broadcast
				     Bukkit.broadcastMessage(ChatColor.GOLD+"[AB-Lottery] "+langCF("lottery-msg1"));
					return;
				}
				
				Random rnd = new Random();
				
					 //get owner for this ticket
					 int ganador = rnd.nextInt(max-min);
					 	File ticket = new File(path+ganador+".yml");
						FileConfiguration yamlx = YamlConfiguration.loadConfiguration(ticket);
					 
						String playerg = yamlx.getString("lottery.owner");
						
				     //broadcast
				     Bukkit.broadcastMessage(ChatColor.GOLD+"[AB-Lottery] "+langCF("lottery-msg2").replace("%player%", playerg));
				     
				    int lottery_buy_cost = getConfig().getInt("Lottery.buy-cost-per-ticket");
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
				        	 Bukkit.getPlayer(playerg).sendMessage(langCF("lottery-msg3").replace("%money%", total_won+""));
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
		}.runTaskTimerAsynchronously(this, (1200 * getConfig().getInt("Lottery.get-winner-per-time")), (1200 * getConfig().getInt("Lottery.get-winner-per-time")));
         
        
        
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
			Bukkit.getConsoleSender().sendMessage(langCF("lngenerateERR"));
			getServer().getPluginManager().disablePlugin(this);
		}
        
		//VAULT
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(this, this);
        if (!setupVault() ) {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] [!] "+langCF("vaultrequired"));
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[AllBanks] "+langCF("deshab"));
        	getServer().broadcastMessage("[AllBanks] "+ langCF("deshabplugviewconsole"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }else if(!setupEconomy()){
        	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] [!] "+langCF("economypluginrequired"));
        	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[AllBanks] [!] "+langCF("essentialspexample"));
        	Bukkit.getConsoleSender().sendMessage(langCF("deshab"));
        	getServer().broadcastMessage(ChatColor.RED+"[AllBanks] "+ langCF("deshabplugviewconsole"));
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
            this.getLogger().info(langCF("generatingconfigyml"));
            saveResource("config.yml", false);
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
        //Adios comando
		getCommand("allbanks").setExecutor(new CommandsAllBanks(this));
		getCommand("lottery").setExecutor(new CommandsAllBanks(this));
		
        String _lang = getConfig().getString("Plugin.language");
        
        if(_lang.equalsIgnoreCase("spanish")){
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[AllBanks] Habilitado");
        }else if(_lang.equalsIgnoreCase("english")){
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[AllBanks] Enabled");
        }else{
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[AllBanks] Enabled");
        }
		//REPETIR FUNCION CADA CIERTO TIEMPO
		int minutoscobrar = getConfig().getInt("BankLoan.timetax");
		if(minutoscobrar==0){
			minutoscobrar = 60;
		}
  		
			
			
			
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
	
	public void checar_act(){
		updatechecker = new CheckUpdate(this);
		updatechecker.startUpdateCheck();
	}
	
    public static int xpRequiredForNextLevel[];
    public static int xpTotalToReachLevel[];
    public static int hardMaxLevel = 100000;
    
    static {
        // 25 is an arbitrary value for the initial table size - the actual value isn't critically
        // important since the tables are resized as needed.
        initLookupTables(25);
    }
    
    public int getCurrentExp(Player player) {
        int lvl = player.getLevel();
        int cur = getXpForLevel(lvl) + (int) Math.round(xpRequiredForNextLevel[lvl] * player.getExp());
        return cur;
    }
    
    public int getXpForLevel(int level) {
        if (level > hardMaxLevel) {
            throw new IllegalArgumentException("Level " + level + " > hard max level " + hardMaxLevel);
        }

        if (level >= xpTotalToReachLevel.length) {
            initLookupTables(level * 2);
        }
        return xpTotalToReachLevel[level];
    }
    
    public int getLevelForExp(int exp) {
        if (exp <= 0) {
            return 0;
        }
        if (exp > xpTotalToReachLevel[xpTotalToReachLevel.length - 1]) {
            // need to extend the lookup tables
            int newMax = calculateLevelForExp(exp) * 2;
            if (newMax > hardMaxLevel) {
                throw new IllegalArgumentException("Level for exp " + exp + " > hard max level " + hardMaxLevel);
            }
            initLookupTables(newMax);
        }
        int pos = Arrays.binarySearch(xpTotalToReachLevel, exp);
        return pos < 0 ? -pos - 2 : pos;
    }
    
    public static int calculateLevelForExp(int exp) {
        int level = 0;
        int curExp = 7;	// level 1
        int incr = 0;
        while (curExp <= exp) {
            curExp += incr;
            level++;
            incr += (level % 2 == 0) ? 3 : 4;
        }
        return level;
    }
    
    public static void initLookupTables(int maxLevel) {
        xpRequiredForNextLevel = new int[maxLevel];
        xpTotalToReachLevel = new int[maxLevel];

        xpTotalToReachLevel[0] = 0;

        // Valid for MC 1.3 and later
        int incr = 17;
        for (int i = 1; i < xpTotalToReachLevel.length; i++) {
            xpRequiredForNextLevel[i - 1] = incr;
            xpTotalToReachLevel[i] = xpTotalToReachLevel[i - 1] + incr;
            if (i >= 30) {
                incr += 7;
            } else if (i >= 16) {
                incr += 3;
            }
        }
        xpRequiredForNextLevel[xpRequiredForNextLevel.length - 1] = incr;
        
    }
    
	public int convertLevelToExp(int Levels){
    	if (Levels <= 15)
    	{
    		return 17 * Levels;
    	}
    	else if (Levels <= 30)
    	{
    		return (3*Levels*Levels/2)-(59*Levels/2)+360;
    	}
    	else
    	{
    		return (7*Levels*Levels/2)-(303*Levels/2)+2220;
    	}
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
		
		if (player.isOnline()) player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("closeaccount"));
		
		//Obtenemos el banco anteriormente usado
		File fileplayer = new File(getDataFolder()+File.separator+"pdata"+File.separator+player.getName()+".yml");
		YamlConfiguration FPlayer = YamlConfiguration.loadConfiguration(fileplayer);
//XLANG REQUIRED
		int LastBank = FPlayer.getInt("info.use-last-bank");
		if(LastBank==0){
			player.sendMessage(langCF("closebank-error-sessionclose"));
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
			player.sendMessage(langCF("closebank-error-bank-notfound-1").replaceAll("%banktype%", LastBank+""));
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
	
	public MainLeaderboard leaderboardforplayer(Player player){
		for (MainLeaderboard p : leaderboard.keySet()) {
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
		case 1: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("borrowinstructions"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;

		case 2: {
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("liquidateloaninstructions"));
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
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank XP -> "+langCF("bankxp-deposit-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bankxp-instructions-deposit-xp-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;

		case 2: {
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank XP -> "+langCF("bankxp-withdraw-lang-sign"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bankxp-instructions-withdraw-xp-chat"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;
		
		case 3: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank XP -> "+langCF("bankxp-transfer-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bankxp-instructions-transfer-xp-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(langCF("bank-xp-transfer-msg1"));
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
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bank-esmerald-instructions-chat"));
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
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Money -> "+langCF("bankxp-deposit-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bankmoney-instructions-deposit-money-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;

		case 2: {
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Money -> "+langCF("bankxp-withdraw-lang-sign"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bankmoney-instructions-withdraw-money-chat"));
				player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
		} break;
		
		case 3: {
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "Bank Money -> "+langCF("bankxp-transfer-lang-sign"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bankmoney-instructions-transfer-money-chat"));
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + ChatColor.YELLOW + "-----------------------");
			player.sendMessage(langCF("bank-money-transfer-msg4"));
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
			player.sendMessage(ChatColor.GOLD + "[AllBanks] " + langCF("bank-time-instructions"));
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
