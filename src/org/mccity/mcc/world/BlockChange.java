package org.mccity.mcc.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockChange {
	
	public static void foo(){
		
	}
	
	public static void changeBlocks(Vector position1, Vector position2, int blockId, World world, Player player){
		
		int i, j, k;
		Location block = position1.toLocation(world);
		for(i = position2.getBlockX(); i < position1.getBlockX() +1; i++){
			block.setX(i);
			for(j = position2.getBlockY(); j < position1.getBlockY() +1; j++){
				block.setY(j);
				for(k = position2.getBlockZ(); k < position1.getBlockZ() +1; k++){
					block.setZ(k);
					
					if (block.getBlock().getTypeId() == 54){
						Chest chest = (Chest)block.getBlock().getState();
						chest.getInventory().clear();
					}
					if (block.getBlock().getTypeId() == 61 || block.getBlock().getTypeId() == 62) {
						Furnace chest = (Furnace)block.getBlock().getState();
						chest.getInventory().clear();
					}
					if (block.getBlock().getTypeId() == 117){
						BrewingStand chest = (BrewingStand)block.getBlock().getState();
						chest.getInventory().clear();
					}
					if (block.getBlock().getTypeId() == 23){
						Dispenser chest = (Dispenser)block.getBlock().getState();
						chest.getInventory().clear();
					}
					block.getBlock().setTypeId(blockId);

				}
			}
		}
	}
}









