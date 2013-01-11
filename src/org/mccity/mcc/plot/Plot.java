package org.mccity.mcc.plot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mccity.mcc.Mcc;

import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public abstract class Plot {
	
	protected String city = null;
	protected String name = null;
	
	protected World world;
	
	protected List<String> regionIds;
	protected List<Player> members;
	protected List<Player> owners;	
	
	protected boolean resetable;
	
	protected long expiredate;
	
	/*
	 * Never set name, world, or region to null.  Never , ever , ever !!!!
	 */	
	public Plot(String name,World world, List<String> region) {
		
		if(region == null || name == null || world == null){
			throw new NullPointerException();
		}

		this.name = name;
		regionIds = region;
		this.world = world;

	}
	
	protected List<ProtectedRegion> getRegion(){
		
		List<ProtectedRegion> regions = new ArrayList<ProtectedRegion>();
		
		RegionManager regionManager = Mcc.worldGuard.getRegionManager(world);
		for(String id : regionIds){
			regions.add(regionManager.getRegion(id));
		}
		return regions;
	}
	
	public String getName(){
		return name;
	}
	
	private boolean setRegion(List<ProtectedRegion> regions){
		
		RegionManager regionManager = Mcc.worldGuard.getRegionManager(world);
		Map<String, ProtectedRegion> officialRegion = regionManager.getRegions();
		for(ProtectedRegion region : regions){
			String id = region.getId();
			officialRegion.put(id, region);
		}
		regionManager.setRegions(officialRegion);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean hasOwner(){
		
		return !owners.isEmpty();
	}

	public List<Player> getOwners(){
		return owners;
	}
	
	public boolean removeOwner(Player player){
		
		if(!isOwner(player))
			return false;
		List<ProtectedRegion> regions = getRegion();
		for(ProtectedRegion region :regions){
			DefaultDomain domain = region.getOwners();
			domain.removePlayer(player.getName());
			region.setOwners(domain);
		}
		setRegion(regions);
		owners.remove(player);
		return true;
	}
	
	public boolean removeMember(Player player){
		if(!isMember(player))
			return false;
		List<ProtectedRegion> regions = getRegion();
		for(ProtectedRegion region : regions){
			DefaultDomain domain = region.getMembers();
			domain.removePlayer(player.getName());
			region.setMembers(domain);
		}
		setRegion(regions);
		members.remove(player.getName());
		return true;
	}
	
	public boolean isOwner(Player player){
		return owners.contains(player);
	}
	
	public boolean isMember(Player player){
		return members.contains(player);
	}
	
	public List<Player> getmembers(){
		return members;
	}
	
	public boolean isResetable(){
		return resetable;
	}
	
	public double getExpireDate(){
		return expiredate;
	}
	
	public boolean contains(Vector vector){
		List<ProtectedRegion> regions = getRegion();
		for (ProtectedRegion region : regions){
			if(region.contains(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ())){
				return true;
			}
		}
		return false;
	}
	abstract public void setExpireDate(long newExpiredate);
	
	abstract public boolean isExpandable();
	
	public boolean addOwner(Player player){
		List<ProtectedRegion> regions = getRegion();
		for (ProtectedRegion region : regions){
			if(region.isOwner(player.getName())){
				return false;
			}
			DefaultDomain defaultDomain = region.getOwners();
			defaultDomain.addPlayer(player.getName());
			region.setOwners(defaultDomain);
		}
		setRegion(regions);
		owners.add(player);
		return true;
	}
	
	public boolean addMember(Player player){
						
		List<ProtectedRegion> regions = getRegion();
		for (ProtectedRegion protectedRegion : regions){
			if(protectedRegion.isMember(player.getName())){
				return false;
			}
			DefaultDomain defaultDomain = protectedRegion.getMembers();
			defaultDomain.addPlayer(player.getName());
			protectedRegion.setMembers(defaultDomain);
		}
		members.add(player);
		setRegion(regions);
		return true;
	}
	
	abstract public boolean resetPlot();
	
	public abstract boolean isExpired();
		
	public boolean addRegion(ProtectedRegion regionToAdd){
		List<ProtectedRegion> regions = getRegion();
		regions.add(regionToAdd);
		return setRegion(regions);
	}
}
