package org.mccity.mcc.plot;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mccity.mcc.world.BlockChange;

public class CreatePhysicalPlot {
	
	public static void CreatePlot(Vector position1, Vector position2, int height, int depth, World world, Player player) {
		
		int bedrockstart = -2;
		int layer1 = 0;
		int layer2 = 4;
		int layer3 = 7;
		int layer4 = 2;
		
		Vector temp1;
		Vector temp2;
		
		temp2 = new Vector(position2.getBlockX(), position2.getBlockY(), position2.getBlockZ());
		temp2.setY(temp2.getBlockY() + depth);
		player.sendMessage(temp2.toString() + " " + position1.toString());
		BlockChange.changeBlocks(position1, temp2, layer1, world, player);
		
		temp1 = new Vector(position1.getBlockX(), temp2.getBlockY(), position1.getBlockZ());
		temp2 = new Vector(position2.getBlockX(), position2.getBlockY(), position2.getBlockZ());

		temp2.setY(temp2.getBlockY() + depth + bedrockstart);
		player.sendMessage(temp2.toString() + " " + temp1.toString());
		BlockChange.changeBlocks(temp1, temp2, layer2, world, player);
		
		temp1.setY(temp2.getBlockY());
		temp2 = new Vector(position2.getBlockX(), position2.getBlockY(), position2.getBlockZ());
		player.sendMessage(temp2.toString() + " " + temp1.toString());
		BlockChange.changeBlocks(temp1, temp2, layer3, world, player);
		
		temp1 = new Vector(position1.getBlockX(), position1.getBlockY(), position1.getBlockZ());
		temp2 = new Vector(position2.getBlockX(), position2.getBlockY(), position2.getBlockZ());
		
		temp1.setX(temp1.getBlockX() - 1);
		temp1.setY(temp1.getBlockY() - height);
		temp1.setZ(temp1.getBlockZ() - 1);
		
		temp2.setX(temp2.getBlockX() + 1);
		temp2.setY(temp2.getBlockY() + 1);
		temp2.setZ(temp2.getBlockZ() + 1);
		
		player.sendMessage(temp2.toString() + " " + temp1.toString());
		BlockChange.changeBlocks(temp1, temp2, layer4, world, player);
		
		return ;
	}
	
	/**
	 * Return a proper set Vectors
	 * The 0th position being the vector with all the high values
	 * and the 1st position being the vector with all the low values
	 * and the y coordinate being added to 25 high an 7 low from
	 * the avarge of the 2 input y coordinates.
	 */
	
	public static Vector[] getProperVectors(Vector position1, Vector position2, int heigth, int depth) {
		Vector[] properVector = new Vector[2];
		int[] x = new int[2];
		int[] y = new int[2];
		int[] z = new int[2];
		int tempy1, tempy2;
		tempy1 = getMax(position1.getBlockY(), position2.getBlockY());
		tempy2 = getMin(position1.getBlockY(), position2.getBlockY());
		if((tempy1 - tempy2) % 2 == 0) {
			y[0] = tempy2 + ((tempy1 - tempy2)/ 2);
		} else {
			y[0] = tempy2 + ((tempy1 - tempy2 + 1)/ 2);
		}
		
		y[1] = y[0] - depth;
		y[0] = y[0] + heigth;
		
		x[0] = getMax(position1.getBlockX(), position2.getBlockX());
		x[1] = getMin(position1.getBlockX(), position2.getBlockX());
		
		z[0] = getMax(position1.getBlockZ(), position2.getBlockZ());
		z[1] = getMin(position1.getBlockZ(), position2.getBlockZ());	
		
		properVector[0] = new Vector(x[0], y[0], z[0]);
		properVector[1] = new Vector(x[1], y[1], z[1]);
		
		return properVector;
	}

	/**
	 * duh...
	 * @param a
	 * @param b
	 * @return
	 */
	
	public static int getMax(int a, int b){
		if(a > b)
			return a;
		else
			return b;
	}
	
	/**
	 * duh...
	 * @param a
	 * @param b
	 * @return
	 */
	
	public static int getMin(int a, int b){
		if(a < b)
			return a;
		else
			return b;
	}
	
	
	
}
