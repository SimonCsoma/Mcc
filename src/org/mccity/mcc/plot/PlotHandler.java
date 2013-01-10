package org.mccity.mcc.plot;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mccity.mcc.YamlHandler;
import org.mccity.mcc.Mcc;

public class PlotHandler extends YamlHandler{
	
	public Mcc plugin;
	private Map<String, Plot> plots;
	
	public PlotHandler(Mcc plugin, World world) {
		super(plugin);
		plots = loadPlots(world);
	}
	
	public Plot getPlotForPosition(Vector vector){
		
		for(Plot plot : plots.values()){
			if(plot.contains(vector))
				return plot;
		}
		return null;
	}
	
	public Plot getPlayersPlot(Player player){
		
		Location currentPlayerLocation = player.getLocation();
		
		for(Plot plot : plots.values()){
			if (plot.isOwner(player)){
				if(plot.contains(currentPlayerLocation.toVector())){
					return plot;
				}
			}			
		}
		
		return null;
	}
	public Plot getPlot(String name){
		if (plots.containsKey(name)){
			return 	plots.get(name);
		}else{
			return null;
		}	
	}
	
	public boolean setPlot(String nameOfPlot, Plot plot){
		if ( plot!= null){
			plots.put(nameOfPlot, plot);
			return true;	
		}else{
			return false;
		}
		
	}
		
	
}
	
