package org.mccity.mcc.plot;

import java.util.Date;
import java.util.List;

import org.bukkit.World;

public class CityPlot extends Plot{

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
