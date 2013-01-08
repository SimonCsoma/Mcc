package org.mccity.mcc.plot;

import java.util.ArrayList;
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
	
	public Player player;
	public Mcc plugin;
	int heigth = 25;
	int depth = 6;
	Vector position1 = null;
	Vector position2 = null;
	String name = null;
	
	public PlotCreator(Player player, Mcc plugin){
		this.player = player;
		this.plugin = plugin;
	}
	
	public void argumentOrginazer(String[] args){
				
		for(String arg:args) {
			
			if(arg.toLowerCase().contains("-s")) {
				if(!player.isOp()){
					player.sendMessage("You need to be op to create a starters plot!");
					return;
				}
			} else if( arg.toLowerCase().contains("h:") || arg.toLowerCase().contains("height:")) {
				
				int temp = arg.indexOf(":") + 1;
				heigth = Integer.valueOf((String) arg.subSequence(temp, arg.length()));
				
			} else if( arg.toLowerCase().contains("d:") || arg.toLowerCase().contains("depth:")) {
				
				int temp = arg.indexOf(":") + 1;
				depth = Integer.valueOf((String) arg.subSequence(temp, arg.length()));
				
			} else if ( arg.toLowerCase().contains("n") || arg.toLowerCase().contains("name:")) {
				
				int temp = arg.indexOf(":") + 1;
				name = (String) arg.subSequence(temp, arg.length());
				
			} else {
				player.sendMessage(ChatColor.RED + "The argument: " + arg + "does not exists");
				return;
			}
		}
			
		if (position1 == null && position2 == null) {
			position1 = PlayerCords.getPlayerCords(player.getName())[0];
			position2 = PlayerCords.getPlayerCords(player.getName())[1];
		}
		player.sendMessage(position1.getBlockY() + " 2nd " + position2.getBlockY());
		if(position1 == null || position2 == null) {
			player.sendMessage(ChatColor.RED + "You forgot to select an area");
			return;
		}
		if (name == null){
			player.sendMessage(ChatColor.RED + "You forgot to give a plot name");
		}
		Vector[] temp = CreatePhysicalPlot.getProperVectors(position1, position2, heigth, depth);
		position1 = temp[0];
		position2 = temp[1];
		if(!this.setRegion()){
			return;
		}
		CreatePhysicalPlot.CreatePlot(position1, position2, heigth, depth, player.getWorld(), player);
		registerPlot();
		
	}
	
	public boolean setRegion(){
		
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
			return false;
		}
			
		if(position1.getBlockZ()  < position2.getBlockZ()) {
			lowZ = position1.getBlockZ();
			highZ = position2.getBlockZ();
		} else if (position1.getBlockZ() > position2.getBlockZ()) {
			lowZ = position2.getBlockZ();
			highZ = position1.getBlockZ();
		} else {
			player.sendMessage("The selection is too small to be a plot");
			return false ;
		}
		
		if(!Mcc.eco.has(player.getName(), (highX - lowX) * (highZ - lowZ) * costs) && !player.isOp()){
			player.sendMessage("Your balance is too low.");
			return false;
		}
		if(!regionCreator()){
			player.sendMessage("Plot creation failed");
			return false ;
		}
		
		return true;		
	}
	
	private boolean regionCreator() {
		
		List<String> citys = plugin.getYamlHandler().getCityNames();
		
		for(String city: citys) {
			
			if(checkForRegions(player, name, city)){
				saveRegion();
			}
		}
		
		return true;
		
	}
	public void saveRegion() {
		
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
		
		return;
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
	
	public static List<ProtectedRegion> getRegionsInRegion(ProtectedRegion inputRegion, World world) {
		
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
	
	public void registerPlot(){
		
		//plugin.plots.
		//plugin.plots.set("plots.mcc.mccity", name);
		
	}
}
