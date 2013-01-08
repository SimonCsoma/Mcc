package org.mccity.mcc.conversations.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class Succes extends MessagePrompt implements Prompt {
	
	public String error;

	public Succes(String error) {
		this.error = error;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return error;
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext arg0) {
		
		return END_OF_CONVERSATION;
	}

}
