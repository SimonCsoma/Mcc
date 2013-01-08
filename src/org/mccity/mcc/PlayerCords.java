package org.mccity.mcc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

public class PlayerCords{
	
	public static List<Vector> PlayerCords = new ArrayList<Vector>();	
	public static List<String> PlayerName = new ArrayList<String>();
	
	
	/**
	 * Returns the players set coordinates with the shovel.
	 * 
	 * @param player
	 * @return Vector[]
	 */
	public static Vector[] getPlayerCords(String player) {
		if (!PlayerName.contains(player))
			return null;
		int index = PlayerName.indexOf(player);
		Vector[] Vecs = new Vector[2];
		if (PlayerCords.get(index * 2 + 1) == null || PlayerCords.get(index * 2) == null)
			return null;
		Vecs[0] = PlayerCords.get(index * 2);
		Vecs[1] = PlayerCords.get(index * 2 + 1);
		return Vecs;
	}
	
	/**
	 * Used by the Listener to set the player coordinates
	 * @param player
	 * @param cord
	 * @param vecNum
	 */
	public static void setPlayerCords(String player, Vector cord, boolean vecNum) {
		if (!PlayerName.contains(player)){
			PlayerName.add(player);
		}
		int index = PlayerName.indexOf(player);
		if(vecNum){
			PlayerCords.add(index*2, cord);
		}
		else{
			PlayerCords.add(index*2+1, cord);
		}
		
		
	}

}
