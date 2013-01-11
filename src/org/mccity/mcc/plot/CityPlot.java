package org.mccity.mcc.plot;

import java.util.Date;
import java.util.List;

import org.bukkit.World;
import org.mccity.mcc.Mcc;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CityPlot extends Plot{
	
	public boolean contains(ProtectedRegion region){
		
		RegionManager regionManger = Mcc.worldGuard.getRegionManager(world);
		List<BlockVector2D> vecs = region.getPoints();
		for(String cityRegion : regionIds){
			if(regionManger.getRegion(cityRegion).containsAny(vecs));
			return true;
		}		
		return false;
	}

	public CityPlot(String name, World world, List<String> regionIds) {
		super(name, world, regionIds);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isResetable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExpandable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setExpireDate(long newExpiredate) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean resetPlot() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isExpired() {
		
		Date date = new Date();
		long currentDate = date.getTime();
		
		if(currentDate > this.expiredate){
			return true;
		}
		
		return false;
	}

}
