package cback.commands;

import cback.MovieBot;
import cback.Util;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

public class CommandCommandList implements Command {
    @Override
    public String getName() {
        return "listcommands";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "!listcommands";
    }

    @Override
    public String getDescription() {
        return "Returns a list of custom commands on the server";
    }

    @Override
    public List<String> getPermissions() {
        return null;
    }

    @Override
    public void execute(MovieBot bot, IDiscordClient client, String[] args, IGuild guild, IMessage message, boolean isPrivate) {
        Util.sendMessage(message.getChannel(), "**Custom Commands**: \n" + bot.getCommandManager().getCommandList());

        Util.botLog(message);
        Util.deleteMessage(message);
    }
}
