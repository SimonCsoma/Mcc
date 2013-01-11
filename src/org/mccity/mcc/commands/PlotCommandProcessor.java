package org.mccity.mcc.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mccity.mcc.Mcc;
import org.mccity.mcc.plot.Plot;
import org.mccity.mcc.plot.PlotCreator;
import org.mccity.mcc.plot.PlotHandler;

public class PlotCommandProcessor {
	
	final private static List<String> subCommands = Arrays.asList("claim", "share", "buy", "sell", "unshare", "create", "extend");
	
	public static void argumentParsing(String[] args, Player player){
		
		String subCommand = null;
		String[][] parameter = new String[args.length - 1][];
		int x = 0;
		
		for(String string : args){
			if(string.contains(":") || !(x >= args.length)){
				parameter[x] = string.split(":");
				x++;
			} else if (subCommands.contains(string)){
				subCommand = string;
			} else{
				player.sendMessage(ChatColor.DARK_RED +  "No such command in /plot.\nUse /mcc for help.");
				return;
			}
		}
		if(subCommand == null){
			player.sendMessage("For help with the command /plot use /mcc.");
			return;
		}
		
		PlotHandler plotHandler =  Mcc.plugin.getPlotHander(player.getWorld());
		/*
		 * player is the sender of the command - type CommandSender or Player
		 * p: is the plot number - type plot
		 * u: the player to be added - type string
		 * 
		 */
		switch(subCommand){
		
			/*
			 *  If you want to claim a plot you need either to stand on the plot you want to claim
			 *  or you need specify a plot number with p:<plot-number> in your command
			 *  this is filtered by the command processor on validness
			 */
		
			case "claim": 
				switch(parameter.length - 1){
					case 0:
						// if you do not give any extra parameters you must be standing on the plot
						Mcc.plugin.mccCommands.claimPlot(player);
						return;
					case 1:
						// if you give a plot number it is retrieved from the plothandler
						Plot plot = plotHandler.getPlot(parameter[0][1]);
						if ( plot != null) {
							// The plot-number is valid and the claiming can proceed
							Mcc.plugin.mccCommands.claimPlot(player,plot);
						}else{
							// The plot-number is invalid and the process is terminated
							player.sendMessage("The plot you entered was not valid.");
						}
					default:
						// Rest is caught here and terminated
						player.sendMessage("Wrong arguments inputed");
						return;	
				}
			/*
			 * Share plot is prepared here.
			 * It can either have 0, 1 or 2 parameters
			 * No parameters means the player is standing on the plot to be shared with a player and
			 * the player need to hit the player to share with.
			 * 1 parameter can either be the the plot the player wants to share or the player
			 * which are respectively p: or u: 
			 * 2 parameters which is the plot to be shared and the player to be shared with.	
			 */
			case "share":
				switch(parameter.length - 1){
					case 0:
						Mcc.plugin.mccCommands.sharePlot(player);
						return;
					case 1:
						if (parameter[0][0].toLowerCase() == "p"){	//This happens when p is sent
							Plot plot = plotHandler.getPlot(parameter[0][1]);
							if (plot !=null){
								Mcc.plugin.mccCommands.sharePlot(player, plot);//(Player, Plot)
								return;
							}else{
								player.sendMessage("The plot you entered was not valid.");
								return;
							}
						}else if (parameter[0][0].toLowerCase() == "u"){ //This happens when u is sent
							Mcc.plugin.mccCommands.sharePlot(player, parameter[0][1]); //(Player, String)
							return;
						}else{
							player.sendMessage("Wrong arguments inputed");
							return;
						}
					case 2:
						if (parameter[0][0].toLowerCase() == "p" && parameter [1][0].toLowerCase() == "u"){ //This will happen when p is sent first and u second
							Plot plot = plotHandler.getPlot(parameter[0][1]);
							if (plot != null){
								if(plot.isOwner(player)){
									Mcc.plugin.mccCommands.sharePlot(player, plot, parameter[1][1]);//(Player,Plot,String)
								} else{
									player.sendMessage("You are not the owner of the plot you enterd");
								}
								return;
							}else{
								player.sendMessage("The plot you entered was not valid.");
								return;
							}
						}else if (parameter[0][0].toLowerCase() == "u" && parameter [1][0].toLowerCase() == "p"){
							Plot plot = plotHandler.getPlot(parameter[1][1]);
							if (plot != null){
								if(!plot.isOwner(player)){
									player.sendMessage("You are not the owner of this plot");
									return;
								}
								Mcc.plugin.mccCommands.sharePlot(player, plot, parameter[0][1]);//(Player,Plot,String)
								return;
							}else{
								player.sendMessage("The plot you entered was not valid.");
								return;
							}
						}else{
							player.sendMessage("Wrong arguments inputed");
							return;	
						}
					default:
						player.sendMessage("Wrong arguments inputed");
						return;					
					}

			case "unshare":
				switch (parameter.length){
				case 0:
					Mcc.plugin.mccCommands.unsharePlot(player);
					return;
				case 1:
					if (parameter[0][0].toLowerCase() == "p"){	//This happens when p is sent
						Plot plot = plotHandler.getPlot(parameter[0][1]);
						if (plot !=null){
							Mcc.plugin.mccCommands.unsharePlot(player, plot);//(Player, Plot)
							return;
						}else{
							player.sendMessage("The plot you entered was not valid.");
							return;
						}
					}else if (parameter[0][0].toLowerCase() == "u"){ //This happens when u is sent
						Mcc.plugin.mccCommands.unsharePlot(player, parameter[0][1]); //(Player, String)
						return;
					}else{
						player.sendMessage("Wrong arguments inputed");
						return;
					}					
				case 2:
					if (parameter[0][0].toLowerCase() == "p" && parameter [1][0].toLowerCase() == "u"){ //This will happen when p is sent first and u second
						Plot plot = plotHandler.getPlot(parameter[0][1]);
						if (plot != null){
							if(plot.isOwner(player)){
								Mcc.plugin.mccCommands.unsharePlot(player, plot, parameter[1][1]);//(Player,Plot,String)
							} else{
								player.sendMessage("You are not the owner of the plot you enterd");
							}
							return;
						}else{
							player.sendMessage("The plot you entered was not valid.");
							return;
						}
					}else if (parameter[0][0].toLowerCase() == "u" && parameter [1][0].toLowerCase() == "p"){
						Plot plot = plotHandler.getPlot(parameter[1][1]);
						if (plot != null){
							if(!plot.isOwner(player)){
								player.sendMessage("You are not the owner of this plot");
								return;
							}
							Mcc.plugin.mccCommands.unsharePlot(player, plot, parameter[0][1]);//(Player,Plot,String)
							return;
						}else{
							player.sendMessage("The plot you entered was not valid.");
							return;
						}
					}else{
						player.sendMessage("Wrong arguments inputed");
						return;	
					}
				default:
					player.sendMessage("Wrong arguments inputed");
					return;					
				}
			
			case "buy":
				player.sendMessage("Not implemented yet");
				return;
			case "sell":
				player.sendMessage("Not implemented yet");
				return;
			case "expand":
				player.sendMessage("Not implemented yet");
				return;
			case "create":
				PlotCreator plotCreator = new PlotCreator(player);
				plotCreator.argumentOrginazer(parameter[0]);
				return;
			default:
				player.sendMessage("Wrong arguments inputed");
				return;	
		}		
	}

}
