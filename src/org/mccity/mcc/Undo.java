package org.mccity.mcc;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Undo {
	
	public Map<String, ArrayList<Location[]>> Location = new HashMap<String,ArrayList<Location[]>>();
	public Map<String, ArrayList<byte[]>> BdataList = new HashMap<String,ArrayList<byte[]>>();
	public Player player;
	
	
	public Location[] getUndo(String playerName){
		if(!Location.containsKey(playerName)){
			return null;
		}
		ArrayList<Location[]> list = Location.get(playerName);
		if (list.isEmpty()){
			return null;
		}
		Location[] retour = list.get(list.size()-1).clone();
		list.remove(list.size()-1);
		return retour;
	}	
	
	public byte[] getByte(String playerName){
		if(!BdataList.containsKey(playerName)){
			return null;
		}
		ArrayList<byte[]> list = BdataList.get(playerName);
		if (list.isEmpty()){
			return null;
		}
		byte[] Bdata = list.get(list.size()-1).clone();
		list.remove(list.size()-1);
		return Bdata;		
	}
	
	public void addUndo(Location[] Locs, byte[] Bdata , String playerName){
		
		if(!Location.containsKey(playerName)){
			Location.put(playerName, new ArrayList<Location[]>());
		}
		Location.get(playerName).add(Locs);	
		if(!BdataList.containsKey(playerName)){
			BdataList.put(playerName, new ArrayList<byte[]>());
		}
		BdataList.get(playerName).add(Bdata);	
	}
}
