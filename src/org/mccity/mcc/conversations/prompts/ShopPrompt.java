package org.mccity.mcc.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.plugin.Plugin;

public class ShopPrompt extends FixedSetPrompt implements Prompt {
	
	public CommandSender sender;
	public Plugin Wg;

	public ShopPrompt(CommandSender sender) {
		this.sender = sender;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return ChatColor.GREEN + "Renting a plot will cost you *** blockies a week\n" +
				"Warning:\n" +  ChatColor.RED + 
				"If you are inactive on the server for over 2 weeks or do not pay rent\n" +
				"You will lose your shop plot with all the blocks and items on it!\n" +
				ChatColor.GREEN + "If you want to leave this menu type " + ChatColor.GOLD +
				"Exit\n" + ChatColor.GREEN + "If you want to continue enter the shop plot number:";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
		arg1.toLowerCase();
		if (arg1.contains("exit")){
			return new Succes("Have a nice day!");
		}
		
		String Error = "not implemented yet";

		return new Succes(Error);

	}
	protected boolean isInputValid(ConversationContext context, String input){
		return true;
	}
}
