package cback.commands;

import cback.MovieBot;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Arrays;
import java.util.List;

public class CommandSuggest implements Command {
    @Override
    public String getName() {
        return "suggest";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("idea","suggestion");
    }

    @Override
    public void execute(MovieBot bot, IDiscordClient client, String[] args, IGuild guild, IMessage message, boolean isPrivate) {
        if (message.getChannel().getID().equals("256491839870337024") || message.getChannel().getID().equals("214763749867913216") || message.getChannel().getID().equals("256491839870337024")) {
            try {
                message.getChannel().pin(client.getMessageByID(message.getID()));
            } catch (Exception e) {
            }
        }
    }

}
