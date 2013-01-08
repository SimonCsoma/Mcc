package org.mccity.mcc.plot;

import java.util.Date;
import java.util.List;

public class CorporationPlot extends Plot{

	protected CityPlot city;

	public CorporationPlot(String name, CityPlot city, List<String> regionIds) {
		super(name, city.world, regionIds);
		this.city = city;
		
	}

	@Override
	public void setExpireDate(long newExpiredate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isExpandable() {
		// TODO Auto-generated method stub
		return false;
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
