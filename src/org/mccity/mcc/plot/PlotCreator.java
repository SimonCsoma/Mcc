package org.mccity.mcc.plot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mccity.mcc.PlayerCords;
import org.mccity.mcc.Mcc;

import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlotCreator {
	
	/**
	 * Very sloppy code needs cleaning soon!
	 */
	
	public Player player;
	int heigth = 25;
	int depth = 6;
	Vector position1 = null;
	Vector position2 = null;
	String name = null;
	
	public PlotCreator(Player player){
		this.player = player;
	}
	
	public void argumentOrginazer(String[] args){
			
		/*
		 * This for-loop pasrses the argument and makes the nessisary global fields.
		 */
		for(String arg:args) {
			
			if(arg.toLowerCase().contains("-s")) {// The -s argument will make a starters plot
				if(!player.isOp()){
					player.sendMessage("You need to be op to create a starters plot!");
					return;
				}
			} else if( arg.toLowerCase().contains("h:") || arg.toLowerCase().contains("height:")) {
				// the h:<number> or height:<number> argument can set a custum height for the given plot
				int temp = arg.indexOf(":") + 1;
				heigth = Integer.valueOf((String) arg.subSequence(temp, arg.length()));
				
			} else if( arg.toLowerCase().contains("d:") || arg.toLowerCase().contains("depth:")) {
				// the d:<number> of depth:<number> will set a custum depth for the plot and also
				// places bedrock at the proper places
				int temp = arg.indexOf(":") + 1;
				depth = Integer.valueOf((String) arg.subSequence(temp, arg.length()));
				
			} else if ( arg.toLowerCase().contains("n") || arg.toLowerCase().contains("name:")) {
				// The N:<name of plot> or name:<name of plot> will specify the name of the plot that
				// is about to be made. The plugin will terminate this action if no name is put in.
				int temp = arg.indexOf(":") + 1;
				name = (String) arg.subSequence(temp, arg.length());
				
			} else {
				// Error catching for yet unknow arguments :P
				player.sendMessage(ChatColor.RED + "The argument: " + arg + "does not exist.");
				return;
			}
		}
			
		// Error catching for the 2 posistions the player selected
		position1 = PlayerCords.getPlayerCords(player.getName())[0];
		position2 = PlayerCords.getPlayerCords(player.getName())[1];
		if(position1 == null || position2 == null) {
			player.sendMessage("You did not select 2 points to create the plot from");
			return;
		}
		//debug
		player.sendMessage(position1.getBlockY() + " 2nd " + position2.getBlockY());
		
		//You need to give a plot name.
		if (name == null){
			player.sendMessage(ChatColor.RED + "You forgot to give a plot name");
		}
		
		// return more propper vectors for the plot creation so the region can be set
		Vector[] temp = CreatePhysicalPlot.getProperVectors(position1, position2, heigth, depth);
		position1 = temp[0];
		position2 = temp[1];
		
		//makes the region and checks if this is legal on the given position and returns null
		//if an error occures
		ProtectedRegion plotRegion = setRegion();
		
		if(plotRegion == null){
			return;
		}
		
		//gets the region form the just created plot
		List<String> regionIds = Arrays.asList(plotRegion.getId());
		
		//Searches the region for any city and returns null if it fails
		CityPlot cityPlot = getCityPlot(plotRegion);
		if(cityPlot == null){
			player.sendMessage("You need to be in a city plot to make a residence plot");
			return;
		}
		
		// Builds the plot in the game world.
        CreatePhysicalPlot.CreatePlot(position1, position2, heigth, depth, player.getWorld(), player);
        // Instantates the plot object for the server to store and work with
		createResidentPlot(name, player.getWorld(), regionIds, cityPlot);		
	}
	
	public ProtectedRegion setRegion(){
		
		//double playerBalance;
		int lowX, lowZ, highX, highZ;
		int costs = 1500;
		
		if(position1.getBlockX() < position2.getBlockX()) {
			lowX = position1.getBlockX();
			highX = position2.getBlockX();
		} else if (position1.getBlockX() > position2.getBlockX()) {
			lowX = position2.getBlockX();
			highX = position1.getBlockX();
		} else {
			player.sendMessage("The selection is too small to be a plot");
			return null;
		}
			
		if(position1.getBlockZ()  < position2.getBlockZ()) {
			lowZ = position1.getBlockZ();
			highZ = position2.getBlockZ();
		} else if (position1.getBlockZ() > position2.getBlockZ()) {
			lowZ = position2.getBlockZ();
			highZ = position1.getBlockZ();
		} else {
			player.sendMessage("The selection is too small to be a plot");
			return null ;
		}
		
		if(!Mcc.eco.has(player.getName(), (highX - lowX) * (highZ - lowZ) * costs) && !player.isOp()){
			player.sendMessage("Your balance is too low.");
			return null;
		}
		ProtectedRegion region = regionCreator();
		if(regionCreator() == null){
			player.sendMessage("Plot creation failed");
			return null ;
		}
		
		return region;		
	}
	
	private ProtectedRegion regionCreator() {
		
		List<String> citys = Mcc.plugin.getYamlHandler().getCityNames(); //Filling List of Strings with Region names.
		
		ProtectedRegion region = null;
		
		for(String city: citys) {
			
			if(checkForRegions(player, name, city)){
				region = saveRegion();
			}
		}
		
		return region;
		
	}
	
	
	public ProtectedCuboidRegion saveRegion() {//creates and returns a Cuboid region and saves it to RegionManager
		
		com.sk89q.worldedit.BlockVector pos1 = 
				new com.sk89q.worldedit.BlockVector(position1.getBlockX(), position1.getBlockY(), position1.getBlockZ());
		com.sk89q.worldedit.BlockVector pos2 =
				new com.sk89q.worldedit.BlockVector(position2.getBlockX(), position2.getBlockY(), position2.getBlockZ());
		
		player.sendMessage("" + name + position1.toString() + position2.toString());
		
		ProtectedCuboidRegion newPlotRegion = new ProtectedCuboidRegion(name, pos1, pos2);
		
		RegionManager regionManager =  Mcc.worldGuard.getRegionManager(player.getWorld());
		regionManager.addRegion(newPlotRegion);
		newPlotRegion = (ProtectedCuboidRegion) regionManager.getRegion(name);
		
		newPlotRegion.setPriority(2);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			player.sendMessage(ChatColor.RED + "Critical World Guard error! Notify Funonly");
		}
		
		return newPlotRegion;
	}
	
	private boolean checkForRegions(Player player, String plotName, String cityRegionName) {
		
		Vector position1;
		Vector position2;
		position1 = PlayerCords.getPlayerCords(player.getName())[0];
		position2 = PlayerCords.getPlayerCords(player.getName())[1];
		
		RegionManager regionManager = Mcc.worldGuard.getRegionManager(player.getWorld());
		if(!regionManager.hasRegion(cityRegionName)){
			player.sendMessage(cityRegionName + " is non exsistant");
			return false;
		}
		
		ProtectedRegion cityRegion = regionManager.getRegion(cityRegionName);
		if ((!cityRegion.contains(position1.getBlockX(), position1.getBlockY(), position1.getBlockZ())) &&
			(!cityRegion.contains(position2.getBlockX(), position2.getBlockY(), position2.getBlockZ()))){
			player.sendMessage("you need to be in the city region");
			return false;
		}
		
		com.sk89q.worldedit.BlockVector pos1 = 
				new com.sk89q.worldedit.BlockVector(position1.getBlockX(), position1.getBlockY(), position1.getBlockZ());
		com.sk89q.worldedit.BlockVector pos2 =
				new com.sk89q.worldedit.BlockVector(position2.getBlockX(), position2.getBlockY(), position2.getBlockZ());
		
		ProtectedCuboidRegion newPlotRegion = new ProtectedCuboidRegion(plotName, pos1, pos2);
		
		List<ProtectedRegion> regionsInsideCity = getRegionsInRegion(cityRegion, player.getWorld());
		for(ProtectedRegion region: regionsInsideCity){
			if(newPlotRegion.containsAny(region.getPoints())){
				player.sendMessage("you cannot overlap with other plots!");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the regions inside the given region
	 * 
	 * @param inputRegion
	 * @param world
	 * @return List of the regions inside inputRegion
	 */
	private static List<ProtectedRegion> getRegionsInRegion(ProtectedRegion inputRegion, World world) {
		
		List<ProtectedRegion> regionsInsideRegion = new ArrayList<ProtectedRegion>();
		
		RegionManager regionManager = Mcc.worldGuard.getRegionManager(world);
		List<ProtectedRegion> regionList = new ArrayList<ProtectedRegion>(regionManager.getRegions().values());
		for(ProtectedRegion region: regionList){
			if(inputRegion.containsAny(region.getPoints())){
				regionsInsideRegion.add(region);
			}
		}
				
		return regionsInsideRegion;
	}
	
	/*
	 * Instantates the plot object stores it and returns it
	 */
	public Plot createResidentPlot(String string, World world, List<String> stringList, CityPlot cityPlot){
		Plot plot = new ResidencePlot(string, cityPlot, stringList);
		PlotHandler handle = Mcc.plugin.getPlotHander(player.getWorld());
 		handle.setPlot(plot.getName(), plot);
		return plot;
	}
	
	/*
	 * Returns the CityPlot that the region is in. If the region is not in any cityPlot
	 * it will return null.
	 */
	private CityPlot getCityPlot(ProtectedRegion region){
		List<String> citys = Mcc.plugin.getYamlHandler().getCityNames();
		PlotHandler plotHandler = Mcc.plugin.getPlotHander(player.getWorld());
		CityPlot cityPlot = null;
		
		for(String city : citys){
			cityPlot  = (CityPlot)plotHandler.getPlot(city);
			if(cityPlot.contains(region)){
				return cityPlot;
			}
		}
		return cityPlot;
	}
}
