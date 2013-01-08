package org.mccity.mcc.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.mccity.mcc.Mcc;

public class IntroPrompt extends FixedSetPrompt implements Prompt{
	
	public CommandSender sender;
	public Mcc mcc;


	public IntroPrompt(CommandSender sender) {
		this.sender = sender;
	}

	@Override
	public String getPromptText(ConversationContext contect) {
		return ChatColor.GREEN + "This is the mcc menu\n" +
				"what can i help you with today?\n" + ChatColor.GOLD +
				"Plots\n" +
				"Shops\n" +
				"Corporations\n" +
				"Exit\n" + ChatColor.GREEN +
				"Enter the word of your choise";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String input) {
		input = input.toLowerCase();

		if (input.contains("plots")){
			return new PlotPrompt(sender);
		}
		if (input.contains("shops")){
			return new ShopPrompt(sender);
		}
		if (input.contains("corporations")){
			return new CorpPrompt();

		}
		else{
			return new Succes("Have a nice day!");
		}
	}
	@Override
	protected boolean isInputValid(ConversationContext context, String input){
		input = input.toLowerCase();
		if (input.contains("plots") ||input.contains("shops") || input.contains("corporations") || input.contains ("exit")){
			return true;
		}
		return false;
	}

}


