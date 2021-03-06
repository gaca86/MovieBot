package cback.commands;

import cback.MovieBot;
import cback.MovieRoles;
import cback.Util;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHelp implements Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands");
    }

    @Override
    public String getSyntax() {
        return "!help";
    }

    @Override
    public String getDescription() {
        return "Returns a list of commands (you're looking at it right now)";
    }

    @Override
    public List<String> getPermissions() {
        return null;
    }

    @Override
    public void execute(MovieBot bot, IDiscordClient client, String[] args, IGuild guild, IMessage message, boolean isPrivate) {

        EmbedBuilder embed = Util.getEmbed();
        embed.withTitle("Commands:");

        List<String> roles = message.getAuthor().getRolesForGuild(guild).stream().map(role -> role.getID()).collect(Collectors.toList());
        for (Command c : MovieBot.registeredCommands) {

            if (c.getDescription() != null) {

                String aliases = "";
                if (c.getAliases() != null) {
                    aliases = "\n*Aliases:* " + c.getAliases().toString();
                }

                if (c.getPermissions() == null) {
                    embed.appendField(c.getSyntax(), c.getDescription() + aliases, false);
                } else if (!Collections.disjoint(roles, c.getPermissions())) {
                    embed.appendField(c.getSyntax(), c.getDescription() + aliases, false);
                }

            }

        }

        embed.withFooterText("Staff commands excluded for regular users");

        try {
            Util.sendEmbed(message.getAuthor().getOrCreatePMChannel(), embed.withColor(85, 50, 176).build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Util.deleteMessage(message);

    }

}
