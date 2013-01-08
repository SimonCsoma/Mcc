package org.mccity.mcc.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.mccity.mcc.Mcc;

public class PlotPrompt extends FixedSetPrompt implements Prompt {
	
	public CommandSender sender;
	public Mcc mcc;
	
	public PlotPrompt(CommandSender sender){
		this.sender = sender;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return ChatColor.GREEN + "What can i do for you in the category plots?\n" +
				ChatColor.GOLD + 
				"Share Plots\n" +
				"Buy\n" +
				"Sell\n" +
				"Exit";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String input) {		
		input = input.toLowerCase();
		if (input.contains("share") && input.contains("plot"))
			return new SharePlotPrompt(sender);
		if (input.contains("buy")){
			return new BuyPrompt();			
		}if (input.contains("sell")){
			return new SellPrompt();
		}else{
			return new Succes("Have a nice day!");
		}
	}
	protected boolean isInputValid(ConversationContext context, String input){
		
		input = input.toLowerCase();
		
		if (input.contains("buy") || input.contains("sell") || input.contains("exit")){
			return true;
		}
		if (input.contains("share") && input.contains("plot")){
			return true;
		}
		
		return false;
	}

}
