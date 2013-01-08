package org.mccity.mcc.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.mccity.mcc.Mcc;

public class SharePlotPrompt extends StringPrompt implements Prompt {
	
	public CommandSender sender;
	public Mcc mcc;

	public SharePlotPrompt(CommandSender sender ) {
		this.sender = sender;
	}

	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg1) {
		
		arg1 = arg1.toLowerCase();
		if (arg1.contains("exit")){
			return new Succes("Have a nice day!");
		}
		if (!arg1.contains(" ")){
			return new Succes(ChatColor.RED + "Wrong input");
		}
		int place = arg1.indexOf(" ");
		String [] args = new String[2];
		args[0] = arg1.substring(0, place);
		args[1] = arg1.substring(place + 1);
		
		mcc.mccCommands.sharePlot(mcc.getPlayer(sender));
		
		return new Succes("Succes");
		
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		
		return ChatColor.GREEN + "You can share your plots with your friends!\n" +
				"This means he will be able to build on your plot\n" +
				"and open unlocked chests on you plot" +
				"To share a plot enter a the plotnumber of the plot\n" +
				"you want to share and the player name\n" +
				"You can also use: \n" +  
				ChatColor.GOLD +
				"/shareplot [plotnumber] [player]\n"+ ChatColor.GREEN +
				"Type" + ChatColor.GOLD + " Exit " + ChatColor.GREEN + "to leave the menu";
	}

	
}
