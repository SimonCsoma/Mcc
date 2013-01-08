package org.mccity.mcc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Invisable {
	
	public static List<Player> InvPlayers =  new ArrayList<Player>();
	
	public static List<Player> getInvPlayers(){
		if (InvPlayers == null){
			return null;
		}
		return InvPlayers;
	}
	
	public static void addPlayer(Object player){
		InvPlayers.add((Player)player);
	}
	public static boolean isInList(Player player){
		
		if (player == null)
			return false;
		
		if (InvPlayers.isEmpty()){
			return false;
		}
		if (InvPlayers.contains(player)){
			return true;
		}

		return false;
	}
	public static void removeFromList(Object player){
		InvPlayers.remove((Player) player);
	}


}
