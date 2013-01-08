package org.mccity.mcc.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.plugin.Plugin;
import org.mccity.mcc.Mcc;

public class DefaultUser extends StringPrompt implements Prompt {
	
	public CommandSender sender;
	public Plugin Wg;

	public DefaultUser(CommandSender sender) {
		this.sender = sender;
	}

	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg1) {
		arg1.toLowerCase();
		if(arg1.contains("exit")){
			return new Succes("Have a nice day");
		}
		Mcc.plugin.mccCommands.claimPlot(Mcc.plugin.getPlayer(sender));		
		
		return new Succes("");
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return ChatColor.GREEN + "To start on this server you will need to claim a plot\n" +
				"You can claim a plot in 2 ways:\n" +
				"1) You enter a plotnumber of an empty plot\n" +
				"2) You enter exit and use /claimplot [plotnumber]\n";
	}


}
