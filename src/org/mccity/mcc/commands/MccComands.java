package org.mccity.mcc.commands;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import org.mccity.mcc.Invisable;
import org.mccity.mcc.Mcc;
import org.mccity.mcc.PlayerCords;
import org.mccity.mcc.Undo;
import org.mccity.mcc.plot.Plot;
import org.mccity.mcc.plot.PlotHandler;
import org.mccity.mcc.plot.ResidencePlot;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.sk89q.worldedit.BlockVector;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class MccComands
{

	public Undo undo;
	public Mcc mcc;
	private Map<Player, String> playerMapForListener = new HashMap<Player, String>();// Player that run the command + The subCommand
	private Map<Player, Plot> playerSelectedPlot = new HashMap<Player, Plot>(); // Player that run the command + plot something 
	
	public MccComands(Mcc mcc){
		this.undo = new Undo();
		this.mcc = mcc;
	}
	
	public Map<Player, String> getMapString(){
		return playerMapForListener;	
	}
	
	public Map<Player, Plot> getMapPlot(){
		return playerSelectedPlot;
	}
	
	public boolean removeFromListString(Player player){
		if(playerMapForListener.containsKey(player)){
			playerMapForListener.remove(player);
			return true;
		}
		return false;
	}
	
	public boolean removeFromListPlot(Player player){
		if(playerSelectedPlot.containsKey(player)){
			playerSelectedPlot.remove(player);
			return true;
		}
		return false;		
	}
	
	public static Player getPlayer(CommandSender sender) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		return player;
	}

	public static String rentShop(CommandSender sender, String arg1, Plugin Wg) {

		return ChatColor.GREEN + "not yet implemented!";
		
		//Player player = getPlayer(sender);
		
	}	
	
	public void shareFormListener(Player player, Player clickedOnPlayer) {
		
		Plot plot = playerSelectedPlot.get(player) ;

		if (plot.isMember(clickedOnPlayer)){
			
			player.sendMessage(clickedOnPlayer.getName() + " is already a member on this plot");
			
		}else{
			plot.addMember(clickedOnPlayer);
			player.sendMessage("You have successfully added " + clickedOnPlayer.getName()+ " to your plot.");
		}
		
	if (!removeFromListString(player)){
		Mcc.plugin.getLogger().info("Player was not removed from the removeFromListString Map");
	}
	if (!removeFromListPlot(player)){
		Mcc.plugin.getLogger().info("Player was not removed from the removeFromListPlot Map");
	}
			
	}
			
		
	
	
	public void unshareFormListener(Player player, Player clickedOnPlayer){
		Plot plot = playerSelectedPlot.get(player) ;

		if (plot.isMember(clickedOnPlayer)){
			plot.removeMember(clickedOnPlayer);
			player.sendMessage("You have successfully unshared your plot with" + clickedOnPlayer.getName());
		}else{
			player.sendMessage(clickedOnPlayer.getName() + " is not a member of your plot");

		}
		
	if (!removeFromListString(player)){
		Mcc.plugin.getLogger().info("Player was not removed from the removeFromListString Map");
	}
	if (!removeFromListPlot(player)){
		Mcc.plugin.getLogger().info("Player was not removed from the removeFromListPlot Map");
	}
					
		
	}
	
	public void claimPlot(Player player){
		
		Plot plot = mcc.getPlotHander(player.getWorld()).
				getPlotForPosition(player.getLocation().toVector());
		if(!(plot instanceof ResidencePlot)){
			player.sendMessage("You are not standing on a residence plot");
			return;
		}
		
		claimPlot(player, plot);
	}
	
	public void claimPlot(Player player, Plot plot){
		
		PermissionUser user = PermissionsEx.getUser(player);
		boolean isInGroup = user.inGroup("default");
		
		if (isInGroup != true){
			player.sendMessage(ChatColor.RED +
					"You already have claimed a plot or do not have permission to claim a plot");
			return;
		}
		
		if(!plot.hasOwner()){
			player.sendMessage("This plot is already owned!");
			return;
		}
		plot.addOwner(player);
		
		String[] group;
		group = new String[1];
		group[0] = "users";
		user.setGroups(group,null);
		
		player.sendMessage("Plot is Claimed!\nWelcome to Mccit");
		return;
	}
	
	
	public static void inv(Player player, Player[] players){
		player.sendMessage("hello world");
		if (Invisable.isInList(player)){
			int i;
			player.sendMessage("you are no longer inv");
			if (players == null){
				Invisable.removeFromList(player);

				return;
			}
			for (i = 0;i<players.length; i++){
				Player off = players[i].getPlayer();
				off.showPlayer(player);
			}
			Invisable.removeFromList(player);
			return;
		}
		Invisable.addPlayer(player);
		player.sendMessage("you are inv!");
		int i;
		if (players == null){
			return;
		}
		for (i = 0;i<players.length; i++){
			Player off = players[i].getPlayer();
			off.hidePlayer(player);
		}
		return;	
	}
	
	public static void buyPlot(CommandSender sender) {
		return;
	}
	
	
	public void sharePlot (Player player){
	
		PlotHandler plotHandler = mcc.getPlotHander(player.getWorld()); //gets the plot handler to use by first getting the world the player is in
		Plot plot = plotHandler.getPlayersPlot(player);// returns the player is currently on
		
		if (plot != null){//if the plot is not null, do the following commands
			player.sendMessage("Right click on the player you would like to share your plot with.");
			playerSelectedPlot.put(player,plot);//adds player and plot to the playerSelectedPlot Hash Map for the listener
			playerMapForListener.put(player, "share");
		}else{
			player.sendMessage("You are not on a valid plot or you are not the owner of this plot.");
		}
	}
	
	
	public void sharePlot (Player player, String playerName){
		
		PlotHandler plotHandler = mcc.getPlotHander(player.getWorld());//gets the plot handler to use by first getting the world the player is in	
		Plot plot = plotHandler.getPlayersPlot(player);// returns the plot the player is currently on
		Player playerToShareWith = Mcc.plugin.getServer().getPlayer(playerName);
		
		if (playerToShareWith == null){
			player.sendMessage("The player name you entered is not valid");
			return;
		}
		
		plot.addMember(playerToShareWith);
		player.sendMessage("You added " + playerToShareWith.getName() + " to your plot!");
		return;		
	}
	
	public void sharePlot(Player player, Plot plot){//
		
		if (plot != null){
			player.sendMessage("Right click on the player you would like to share your plot with.");
			playerSelectedPlot.put(player,plot);
			playerMapForListener.put(player, "share");
					
		}else{
			player.sendMessage("You are not on a valid plot or you are not the owner of this plot.");
		}
		return;
		
	}
	
	public void sharePlot(Player player, Plot plot, String playerName){
		
		if(!plot.isOwner(player)){
			player.sendMessage("You are not the owner of this plot");
			return;
		}
		
		Player playerToShareWith = Mcc.plugin.getServer().getPlayer(playerName);
		
		plot.addMember(playerToShareWith);
		
	}	
	
	public static void CheckOfflineTime(Object[] Offply){
		
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		long Time = date.getTime() - 1209600000;
		int x = 0;
		List<OfflinePlayer> y = new ArrayList<OfflinePlayer>();
		while(x < Offply.length){			
			OfflinePlayer offply = (OfflinePlayer) Offply[x];
			x++;
			long lastplayed = offply.getLastPlayed();
			
			if (Time >= lastplayed){
				System.out.println(offply.getName());
				y.add(offply);
			}
		}
		return;	
	}
	
	
	public void unsharePlot (Player player){
		
		PlotHandler plotHandler = mcc.getPlotHander(player.getWorld());
		Plot plot = plotHandler.getPlayersPlot(player);
		
		if (plot != null){
			player.sendMessage("Right click on the player you would like to unshare your plot with.");
			playerSelectedPlot.put(player,plot);
		}else{
			player.sendMessage("You are not on a valid plot or you are not the owner of this plot.");
		}
	}	
	
	
	public void unsharePlot (Player player, String playerName){
		
		PlotHandler plotHandler = mcc.getPlotHander(player.getWorld());	
		Plot plot = plotHandler.getPlayersPlot(player);
		Player playerToUnshareWith = Mcc.plugin.getServer().getPlayer(playerName);
		
		if (playerToUnshareWith == null){
			player.sendMessage("The player name you entered is not valid");
		}else if(plot.isMember(playerToUnshareWith)){
			plot.removeMember(playerToUnshareWith);
			player.sendMessage("You have unshared your plot with " + playerToUnshareWith);
		}else{
			player.sendMessage(playerToUnshareWith +  " is not a member of your plot");
		}
		
	}
	
		public void unsharePlot(Player player, Plot plot){
			
			if (plot != null){
				player.sendMessage("Right click on the player you would like to unshare your plot with.");
				playerSelectedPlot.put(player,plot);
				playerMapForListener.put(player, "unshare");
				 
				// Code continues in MccListener

			}else{
				player.sendMessage("You are not on a valid plot or you are not the owner of this plot.");
			}
			return;
		 
		}
		
		public void unsharePlot(Player player, Plot plot, String playerName){
			
			if(!plot.isOwner(player)){
				player.sendMessage("You are not the owner of this plot");
				return;
			}
			
			Player playerToUnshareWith = Mcc.plugin.getServer().getPlayer(playerName);
			if (plot.isMember(playerToUnshareWith)){
				plot.removeMember(playerToUnshareWith);
				
			}else{
			player.sendMessage(playerName + " is not a member of your plot");
			}
		}	
	
	
	
	/**	
	 * Handels players command /set
	 */
	
	public static String PlayerSet(CommandSender sender, String arg){
		
		Player player = getPlayer(sender);
		Vector[] PV = PlayerCords.getPlayerCords(player.getName());
		int error = Set(PV, player.getWorld(), Integer.valueOf(arg), player);
		if (error == -1){ player.sendMessage("Something went worng!");}
		
		return "Set to " + arg;		
	}
	
	/** Actual set function
	 * Calabe by other functions
	 * 
	 * @param vectors
	 * @param world
	 * @param BlockID
	 * @param player
	 * @return retruns 0 if succes full or error number
	 */
	
	public static int Set(Vector[] vectors, World world, int BlockID, Player player){
		
		if (vectors == null){
			return -1;
		}
		if (BlockID > 121){
			return -1;
		}

		int bv0,bv1,bv2;
		int ev0,ev1,ev2;
		if(vectors[0].getBlockX() <= vectors[1].getBlockX()){
			bv0 = vectors[0].getBlockX();
			ev0 = vectors[1].getBlockX();
		} else {
			bv0 = vectors[1].getBlockX();
			ev0 = vectors[0].getBlockX();
		}
		if(vectors[0].getBlockY() <= vectors[1].getBlockY()){
			bv1 = vectors[0].getBlockY();
			ev1 = vectors[1].getBlockY();
		} else {
			bv1 = vectors[1].getBlockY();
			ev1 = vectors[0].getBlockY();
		}
		if(vectors[0].getBlockZ() <= vectors[1].getBlockZ()){
			bv2 = vectors[0].getBlockZ();
			ev2 = vectors[1].getBlockZ();
		} else {
			bv2 = vectors[1].getBlockZ();
			ev2 = vectors[0].getBlockZ();
		}
		Vector beginVec =  new Vector(bv0,bv1,bv2);
		Vector endVec = new Vector(ev0,ev1,ev2);
		

		int x = endVec.getBlockX() - beginVec.getBlockX();
		int y = endVec.getBlockY() - beginVec.getBlockY();
		int z = endVec.getBlockZ() - beginVec.getBlockZ();
		int index = (x+1)*(y+1)*(z+1);
		player.sendMessage("index " + index + " " + x + " " + y+ " " + z);
		//Location[] undo = new Location[index];
		//int[] Bdata = new int[index];
		index = 0;
		Location curLoc = beginVec.toLocation(world);
		for (int c1 = 0; c1 <= x; c1++){
			curLoc.setX(c1 + beginVec.getBlockX());
			for (int c2 = 0; c2 <= y; c2++){
				curLoc.setY(c2 + beginVec.getBlockY());
				for (int c3 = 0; c3 <= z; c3++){
					curLoc.setZ(c3 + beginVec.getBlockZ());
					
					//undo[index] = new Location(null, c1 + beginVec.getBlockX(), c2 + beginVec.getBlockY(), c3 + beginVec.getBlockZ());
					//Bdata[index] = curLoc.getBlock().getTypeId();
					curLoc.getBlock().setTypeId(BlockID); 
					curLoc.getBlock().getState().update();
					index++;
				}
			}
		}
		//this.undo.addUndo(undo, Bdata, player.getName());
		return 0;
	}
	public static void CreatePlot(CommandSender sender, String[] arg){
		
		// Check for args and possibility 
		// TO-DO world gaurd check and undo
		Player player = getPlayer(sender);
		boolean force = false;
		if(arg.length > 1){
			String strArg = "";
			for (int x=0; x < arg.length; x++){
				strArg = strArg + arg[x];
			}
			if (strArg.contains("-f")){
				force = true;
				player.sendMessage(ChatColor.RED + "Forceing");
			}
		}
		
		Vector[] vecs = new Vector[4];
		
		Location curLoc = player.getLocation();
		
		/* 
		 * Uses the X and Z to get the face of the player
		 * This is used to place the sign later
		 * there are 4 possible directions
		 */
		
		vecs[2] = curLoc.getDirection();
		Byte DB = getByteDirection(vecs[2].normalize());
		if(vecs[2].getX() <=  0.0){
			vecs[2].setX(-1);
		} else {
			vecs[2].setX(1);
		}
		if(vecs[2].getZ() <= 0.0){
			vecs[2].setZ(-1);
			vecs[2].setY(0);
		}else {
			vecs[2].setZ(1);
			vecs[2].setY(0);
		}
		
		vecs[3] = curLoc.toVector();
		vecs[0] = new Vector(11,-9,11);
		vecs[1] = new Vector(0,-3,0);
		setter(vecs.clone(), player, 7);
		
		vecs[0] = new Vector(11,1,11);
		vecs[1] = new Vector(0,2,0);
		setter(vecs.clone(), player, 4);
		
		vecs[0] = new Vector(10,-7,10);
		vecs[1] = new Vector(1,0,1);
		setter(vecs.clone(), player, 2);
		
		vecs[0] = new Vector(10,25,10);
		vecs[1] = new Vector(-1,1,-1);
		setter(vecs.clone(), player, 0);
		
		vecs[0] = new Vector(2,0,2);
		vecs[0].setX(vecs[0].getBlockX() * vecs[2].getBlockX());
		vecs[0].setZ(vecs[0].getBlockZ() * vecs[2].getBlockZ());
		Vector Vec = vecs[3].clone();
		Vec.add(vecs[0]);
		Location signLoc = Vec.toLocation(player.getWorld());
		signLoc.getBlock().setTypeIdAndData(63, DB , true);
		BlockState sign = signLoc.getBlock().getState();
		sign = (Sign)sign;
		((Sign)sign).setLine(0,"PlotNumber:");
		((Sign)sign).setLine(1,arg[0]);
		((Sign)sign).setLine(2,"Free Plot!");
		sign.update();
		vecs[0] = new Vector(6,0,6);
		vecs[0].setX(vecs[0].getBlockX() * vecs[2].getBlockX());
		vecs[0].setZ(vecs[0].getBlockZ() * vecs[2].getBlockZ());
		Vec = vecs[3].clone();
		Vec.add(vecs[0]);
		Vec.toLocation(player.getWorld()).getBlock().setTypeIdAndData(6, (byte)0x0, true);
		Vector[] WGVec = new Vector[2];
		WGVec[0] = vecs[3].clone();
		WGVec[1] = vecs[3].clone();
		vecs[0] = new Vector(0,24,0);
		vecs[0].setX(vecs[0].getBlockX() * vecs[2].getBlockX());
		vecs[0].setZ(vecs[0].getBlockZ() * vecs[2].getBlockZ());
		WGVec[0].add(vecs[0]);
		vecs[0] = new Vector(11,-9,11);
		vecs[0].setX(vecs[0].getBlockX() * vecs[2].getBlockX());
		vecs[0].setZ(vecs[0].getBlockZ() * vecs[2].getBlockZ());
		WGVec[1].add(vecs[0]);
		int error = RegionCreator(Mcc.worldGuard, arg[0], sender, WGVec, force);
		if (error == -2){
			player.sendMessage(ChatColor.RED + "An unknown error occured");
		}
		if (error == -1){
			player.sendMessage(ChatColor.RED + "Failed to set region!");
		}
		
		return;
	}
		
	public static void setter(Vector[] vecs, Player player, int ID){
		
		Vector[] Vec = new Vector[2];
		
		/**
		 * Handler for position related changes in current possition
		 * of the player.  
		 */
		
		/* 
		 * Prepares the vectors for the Set function depending on the players
		 * position in the current world and sends the 2 resulting vectors
		 * to the Set function
		 */
		
		Vector PlayerVector = player.getEyeLocation().toVector();
		player.sendMessage(PlayerVector.toString());
		
		vecs[1].setX(vecs[1].getBlockX() * PlayerVector.getBlockX());
		vecs[1].setZ(vecs[1].getBlockZ() * PlayerVector.getBlockZ());
		vecs[0].setX(vecs[0].getBlockX() * PlayerVector.getBlockX());
		vecs[0].setZ(vecs[0].getBlockZ() * PlayerVector.getBlockZ());
		
		Vec[0] = vecs[3].add(vecs[0]);
		Vec[1] = vecs[3].add(vecs[1]);
		Set(Vec, player.getWorld(), ID, player);
		return;
	}
	
	public static byte getByteDirection(Vector DVector){
		if (DVector.getX() > 0.7071){
			return 0x4;
		}
		if (DVector.getX() < -0.7071){
			return 0xC;
		}
		if (DVector.getZ() < 0.7071){
			return 0x0;
		} else {
			return 0x8;
		}

	}
	public static int RegionCreator(Plugin Wg, String Args, CommandSender sender, Vector[] WGVec, boolean force){
		
		Player player = getPlayer(sender);
		player.sendMessage(ChatColor.GREEN + "Setting plot " + Args);
		World world = player.getWorld();
		RegionManager regionManager = ((WorldGuardPlugin) Wg).getRegionManager(world);
		ProtectedRegion region = regionManager.getRegion(Args);
		if (region != null && force == true){
			player.sendMessage(ChatColor.RED + Args + " already exsists overwriting");
		} else if (region != null){
			player.sendMessage(ChatColor.RED + Args + " already exsists aborting");
			return -1;
		}
		
		BlockVector pt1 = new BlockVector(WGVec[0].getBlockX(), WGVec[0].getBlockY(), WGVec[0].getBlockZ());
		BlockVector pt2 = new BlockVector(WGVec[1].getBlockX(), WGVec[1].getBlockY(), WGVec[1].getBlockZ());
		ProtectedCuboidRegion PCR = new ProtectedCuboidRegion(Args, pt1, pt2);
		regionManager.addRegion(PCR);
		
		region = regionManager.getRegion(Args);
		if (region == null){
			return -1;
		}
		region.setPriority(2);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			sender.sendMessage(ChatColor.RED + "Critical World Guard error! Notify Funonly");
			return -2;
		}
		return 0;
	}
	
	
	public void refill (CommandSender sender, String[] args){
		
		Player player = getPlayer(sender);
		if (args.length < 1){
			player.sendMessage("too few arguments");			
		}
		if (args.length > 1){
			player.sendMessage("too many arguments");
		}
		
		//Location location = new Location(world, x, y, z);
		
		//if (location.getBlock().getTypeId() == 54){
		//	Chest chest = (Chest)location.getBlock().getState();
		//	chest.getInventory().clear();
		//}


/*	     
	    
	    chestInv.addItem(new ItemStack(Material.APPLE, 1);
	 
*/
		
		
		
		
		// Some further Yaml handler to get list of event
		//String eventList;
		String event = args[0];
		
		player.sendMessage("refilling chests in " + event);
		
		
		
		return;
	}
	/*
	public void PlayerUndo(CommandSender sender){
		Location[] locs = undo.getUndo(getPlayer(sender).getName());
		if (locs == null){
			sender.sendMessage("no more undo's possile");
			return;
		}
		int[] Bdata = undo.getByte(getPlayer(sender).getName());
		int index = locs.length - 1;
		World world = getPlayer(sender).getWorld();
		Location curLoc;
		for (int x = 0; x < index + 1; x++){
			Vector curVec = locs[x].toVector();
			curLoc = curVec.toLocation(world);
			curLoc.getBlock().setTypeId(Bdata[x]);
			curLoc.getBlock().getState().update();
			
		}
	}
	*/
}























