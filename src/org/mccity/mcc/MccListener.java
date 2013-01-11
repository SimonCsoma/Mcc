package org.mccity.mcc;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.*;
import org.bukkit.util.Vector;

import org.mccity.mcc.plot.Plot;

@SuppressWarnings("unused")
public class MccListener implements Listener{
	
	int x;
	public Map<InetAddress,Long> lastLogin = new HashMap<InetAddress, Long>();
	public static Map<String, BukkitTask> PDDList = new HashMap<String, BukkitTask>();
	BukkitScheduler scheduler;

	public MccListener() {
		scheduler = Bukkit.getScheduler();		
	}
	
	/*
	 * Gets a Map form MccCommands containing a Player that triggered a subcommand in /plot that
	 * requires another player. To Keep track of the subcommand that was triggered the subcommand
	 * is the object containing in the Map. By this subcommand the right part of MccCommands will 
	 * be re-triggered by this code.
	 */
	@EventHandler 
	public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event){
		
		Player playerClicking = event.getPlayer();
		
		//Map of players that are currently using share or unshare and need to hit a player
		if(!Mcc.plugin.mccCommands.getMapPlot().containsKey(playerClicking))
			return;
		
		Entity playerRightClicked = event.getRightClicked();
		Player player = null;
			
		if (playerRightClicked instanceof Player) {
			
			player = (Player) playerRightClicked;
			
			String subcommand = Mcc.plugin.mccCommands.getMapString().get(playerClicking);
			
			//switch for redirecting the command back to mccCommands
			switch(subcommand){
			case "share":
				Mcc.plugin.mccCommands.shareFormListener(playerClicking, player);
				return;
			case "unshare": 
				Mcc.plugin.mccCommands.unshareFormListener(playerClicking, player);
				return;
			default:
				event.getPlayer().sendMessage("Crirical error!");
				return;
			}
			
		}else{
			// Error of player 
			event.getPlayer().sendMessage("The Entity that you clicked was not a player Character.");
			return;
		}

		/*player runs command
		 * map1 <<< player, string
		 * map2 <<< player, plot
		 * wait (listen) for interact
		 * return;
		 * 
		 * 
		 * look in map1 for match
		 * run string in mccCommands(clicked player)
		 *  						 >>>>>>> clicked player
		 *  								 map2 >>> player plot
		 * return							 map1 >>> nothing
		 * 									 clear pos in map1 and map2
		 * 									 
		 */
		//Plot plotToShare = Mcc.plugin.mccCommands.getList().get(string);
		
		//if (plotToShare.addMember(player)){
			
		//	event.getPlayer().sendMessage("You have successfully added " + player.getName()+ " to your plot.");
		//	Mcc.plugin.mccCommands.removeFromList(string);
		//}else{
		//	event.getPlayer().sendMessage(player.getName() + " could not be added to your plot.");
		//}
		//return;
	}
	

	
	@EventHandler
	public void onPlayerInt(PlayerInteractEvent event){
		
		Player player = event.getPlayer();
		int curItem = player.getItemInHand().getTypeId();
		if (curItem != 277){
			return;
		}
		Action P1 = Action.LEFT_CLICK_BLOCK;
		Action P2 = Action.RIGHT_CLICK_BLOCK;
		
		//Code for selecting land
		
		if (event.getAction() == P1){
			Vector vec1 = event.getClickedBlock().getLocation().clone().toVector();
			player.sendMessage(ChatColor.BLUE + "Selected point one at: X: " + vec1.getX() + 
					", Y: "+ vec1.getY() + ", Z: "+ vec1.getZ());
			PlayerCords.setPlayerCords(event.getPlayer().getName(), vec1, true);
			if(player.isOp()){
				event.setCancelled(true);
			}
		}
		if (event.getAction() == P2){
			Vector vec2 = event.getClickedBlock().getLocation().clone().toVector();
			player.sendMessage(ChatColor.BLUE + "Selected point two at: X: " + vec2.getX() + 
					", Y: "+ vec2.getY() + ", Z: "+ vec2.getZ());
			PlayerCords.setPlayerCords(event.getPlayer().getName(), vec2, false);
		}
		
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		
		List<Player> invPlayers = Invisable.getInvPlayers();
		if (invPlayers == null){return;} 
		int i;
		for (i=0;i<invPlayers.size();i++){
			event.getPlayer().hidePlayer(invPlayers.get(i));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event){
		
		List<Player> invPlayers = Invisable.getInvPlayers();
		if (invPlayers == null){return;} 
		int i;
		for (i=0;i<invPlayers.size();i++){
			event.getPlayer().showPlayer(invPlayers.get(i));
		}
		
		Player player = event.getPlayer();
		Player[] players = event.getPlayer().getServer().getOnlinePlayers();
		if(invPlayers.contains(event.getPlayer())){
			if (players == null){
				Invisable.removeFromList(player);

				return;
			}
			for (i = 0;i<players.length; i++){
				Player off = players[i].getPlayer();
				off.showPlayer(player);
			}
			Invisable.removeFromList(player);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerWorldChange(PlayerChangedWorldEvent event){
		if (event.getPlayer().isOp()){
			event.getPlayer().setGameMode(GameMode.CREATIVE);
		}
	}
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event){
		List<Player> invPlayers = Invisable.getInvPlayers();
		if (invPlayers == null){return;} 
		int i;
		for (i=0;i<invPlayers.size();i++){
			event.getPlayer().showPlayer(invPlayers.get(i));
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		
		Player player = event.getPlayer();
		
		String name = player.getName();
		
		Location loc = event.getTo();
		
		if (loc.getBlockX() > -215 && loc.getBlockX() < -212){
			if(!(PDDList.containsKey(name)) && !(player.isDead())){
				PDDList.put(name, 
				scheduler.runTaskTimer(player.getServer().getPluginManager().getPlugin("MCC"),
						new PlayerDealDamage(player), 0L, 20L));			
			} 
		}
		else if(PDDList.containsKey(name)){
			PDDList.get(name).cancel();
			PDDList.remove(name);
			
				
		}
	}
	@EventHandler
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		if(!lastLogin.containsKey(event.getAddress())){
			lastLogin.put(event.getAddress(), date.getTime());
			return;
		}
		if(lastLogin.get(event.getAddress()) <= date.getTime() - 100000){
			lastLogin.put(event.getAddress(), date.getTime());
			return;
		}else{
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Multiple logins error");
		}		
	}
}































