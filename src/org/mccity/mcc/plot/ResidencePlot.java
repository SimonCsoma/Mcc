package org.mccity.mcc.plot;

import java.util.Date;
import java.util.List;


public class ResidencePlot extends Plot{

	protected CityPlot city;

	public ResidencePlot(String name, CityPlot city, List<String> regionIds) {
		super(name, city.world, regionIds);
		this.city = city;
		
	}


	@Override
	public void setExpireDate(long newExpiredate) {
		if(isResetable()){
			expiredate = newExpiredate;
		}
	}

	@Override
	public boolean isExpandable() {
		return true;
	}


	@Override
	public boolean resetPlot() {
		if(!isResetable())
			return false;
		if(!isExpired())
			return false;
		
		
		
		return true;
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
