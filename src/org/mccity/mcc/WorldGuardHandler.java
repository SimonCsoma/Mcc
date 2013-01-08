package org.mccity.mcc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHandler {

	
	public static List<Player> getMembers(String id, World world){
		
		RegionManager regionManager = Mcc.worldGuard.getRegionManager(world);
		return getMembers(regionManager.getRegion(id));
	}
	
	public static List<Player> getMembers(ProtectedRegion region){
		
		List<Player> players = new ArrayList<Player>();
		
		for (String stringplayer :region.getMembers().getPlayers()){
			players.add(Bukkit.getPlayer(stringplayer));
		}
		
		return players;
	}
	
	public static List<Player> getOwners(String id, World world){
		
			RegionManager regionManager = Mcc.worldGuard.getRegionManager(world);
			return getOwners(regionManager.getRegion(id));
	}
	
		public static List<Player> getOwners(ProtectedRegion region){
		
			List<Player> players = new ArrayList<Player>();
		
			for (String stringplayer :region.getOwners().getPlayers()){
				players.add(Bukkit.getPlayer(stringplayer));
			}
		
			return players;
	}
}
