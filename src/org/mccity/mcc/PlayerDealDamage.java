package org.mccity.mcc;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerDealDamage extends BukkitRunnable {
	
	public Player player;
	World world;
	
	public PlayerDealDamage(Player player){
		this.player = player;
	}

	@Override
	public void run() {
		if(player.getHealth() <= 0){
			player.setHealth(0);
			cancel();
		}
		player.setHealth(player.getHealth() - 1);
		return;
	}
}
