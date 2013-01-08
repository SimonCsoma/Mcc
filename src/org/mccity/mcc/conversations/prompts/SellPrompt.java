package org.mccity.mcc.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class SellPrompt extends MessagePrompt implements Prompt {

	@Override
	public String getPromptText(ConversationContext arg0) {
		return ChatColor.GREEN + 
				"Coming soon!";
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext arg0) {
		return new Succes("Have a nice day!");
	}


}