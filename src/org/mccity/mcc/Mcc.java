package org.mccity.mcc;


import java.io.File;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

import org.mccity.mcc.commands.MccComands;
import org.mccity.mcc.conversations.prompts.DefaultUser;
import org.mccity.mcc.conversations.prompts.IntroPrompt;
import org.mccity.mcc.plot.PlotCreator;
import org.mccity.mcc.plot.PlotHandler;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class Mcc extends JavaPlugin {
	
	public static Mcc plugin;
	ConversationFactory conversationFactory;
	public static WorldGuardPlugin worldGuard;
	public static Economy eco = null;
	public YamlHandler yamlHandler;
	private Map<World,PlotHandler> PlotHandlers;
	public MccComands mccCommands;
		
	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 * 
	 * Init for the plugin
	 * 
	 * 1. checking if the config and plots files exists with firstRun();
	 *    making them if non existent
	 * 2. Checking if world guard and a vault economy are present
	 *    If not, stop the server
	 * 3. Start the MccListener
	 * 
	 */ 
	
	@Override
	public void onEnable(){
		
		plugin = this;
		
		getWorldGuard();
		setupEconomy();
		
		yamlHandler = new YamlHandler(this);
		mccCommands = new MccComands(this);
		
		try{
			yamlHandler.firstRun();
		} catch (Exception e){
			e.printStackTrace();
		}
		yamlHandler.load();
		startPlotHandlers();
		if (worldGuard != null || eco != null){
			getLogger().info("WorldGuard detected!");
			getLogger().info("Vault-Economy detected!");
		} else {
			System.err.println("WorldGuard and/or Vault-Economy not loaded\nStopping the server!");
			Bukkit.shutdown();
		}
		MccListener mccListener = new MccListener(); 
		getServer().getPluginManager().registerEvents(mccListener, this);
		getLogger().info("MCC plugin enabled");
	}
	
	
	
	@Override
	public void onDisable() {
		getLogger().info("MCC plugin disabled");
		yamlHandler.save();
	}
	
	private void startPlotHandlers(){
		for(World world :Bukkit.getWorlds()){
			PlotHandlers.put(world, new PlotHandler(this, world));
		}
	}
	
	public PlotHandler getPlotHander(World world){
		return PlotHandlers.get(world);
	}
	
	private void setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            eco = economyProvider.getProvider();
        }	
	}
	
	private static void getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		
		if(plugin == null || !(plugin instanceof WorldGuardPlugin)){
			File WorldGuard = new File("WorldGuard.jar");
			try {
				plugin.getPluginLoader().loadPlugin(WorldGuard);
			} catch (UnknownDependencyException e) {
				e.printStackTrace();
			} catch (InvalidPluginException e) {
				plugin.getLogger().info("No WorldGuard Present!");
				plugin.getPluginLoader().disablePlugin(plugin);
				e.printStackTrace();
			}
		}
		worldGuard = (WorldGuardPlugin) plugin;
		return;
	}	

	public Player getPlayer(CommandSender sender){
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		return player;
	}
	
	/*
	 * Test all the available commands when any command is used 
	 */
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args){
		
		if (cmd.getName().equalsIgnoreCase("mcc")){
			
			Player player = getPlayer(sender);
			PermissionUser user = PermissionsEx.getUser(player);
			
			if(user.inGroup("users") == true || user.inGroup("admin") == true){
				this.conversationFactory = new ConversationFactory(this)
				.withModality(true)
				.withEscapeSequence("/stop")
				.withTimeout(1000)
				.thatExcludesNonPlayersWithMessage("/Go away evil console!")				
				.withFirstPrompt(new IntroPrompt(sender));
			
				if (player instanceof Conversable){
					conversationFactory.buildConversation((Conversable)player).begin();
					return true;				
				}else{
					return false;
				}
			}
			if(user.inGroup("default") == true){
				this.conversationFactory = new ConversationFactory(this)
				.withModality(true)
				.withEscapeSequence("/stop")
				.withTimeout(1000)
				.thatExcludesNonPlayersWithMessage("/Go away evil console!")				
				.withFirstPrompt(new DefaultUser(sender));
				
				if (player instanceof Conversable){
					conversationFactory.buildConversation((Conversable)player).begin();
					return true;				
				}else{
					return false;
				}
			}
		}
		
		if (cmd.getName().equals("plot")){
			
		}
		
		if (cmd.getName().equalsIgnoreCase("inv")){
			if (!sender.isOp()){
				sender.sendMessage(ChatColor.RED + "this is not meant for your eyes!");
				return true;
			}
			getPlayer(sender).sendMessage("hello world");
			MccComands.inv((Player)sender, getPlayers());			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("plotcheck")){
			if (sender.isOp()){
				MccComands.CheckOfflineTime(getServer().getOfflinePlayers());
				return true;
			}
			sender.sendMessage(ChatColor.RED + "you are not op!");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("set")){
			if (!sender.isOp()){
				sender.sendMessage(ChatColor.RED + "You are not an operator!");
				return true;
			}
			if (args.length > 1 ){
				sender.sendMessage(ChatColor.RED + "Too many arguments!");
				return true;
			}
			if (args.length == 0){
				sender.sendMessage(ChatColor.RED + "Missing argumen!");
				return true;
			}
			sender.sendMessage(MccComands.PlayerSet(sender, args[0]));
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("createplot")){
			Player player = getPlayer(sender);
			if (!sender.isOp()){
				return true;
			}
			PlotCreator plotCreator = new PlotCreator(player, this);
			plotCreator.argumentOrginazer(args);
			return true;
		}
		/*
		if (cmd.getName().equalsIgnoreCase("undo")){
			MC.PlayerUndo(sender);
			return true;
		}
		*/
		return false;
	}
	public Player[] getPlayers(){


		Player[] players = Bukkit.getServer().getOnlinePlayers();
		return players;
	}
	
	public YamlHandler getYamlHandler(){
		return yamlHandler;
	}
}




