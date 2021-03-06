package cback.commands;

import cback.MovieBot;
import cback.MovieRoles;
import cback.Util;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandPurge implements Command {

    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("prune");
    }

    @Override
    public String getSyntax() {
        return "!prune @user [number?]";
    }

    @Override
    public String getDescription() {
        return "Prunes a user's messages - if no number is specified then it will purge all messages";
    }

    @Override
    public List<String> getPermissions() {
        return Arrays.asList(MovieRoles.MOD.id, MovieRoles.ADMIN.id);
    }

    @Override
    public void execute(MovieBot bot, IDiscordClient client, String[] args, IGuild guild, IMessage message, boolean isPrivate) {
        List<IRole> userRoles = message.getAuthor().getRolesForGuild(guild);
        if (userRoles.contains(guild.getRoleByID(MovieRoles.ADMIN.id)) || userRoles.contains(guild.getRoleByID(MovieRoles.MOD.id))) {
            Util.botLog(message);

            if (args.length >= 1) {

                String numberArg = args[0];

                int maxDeletions = 0;
                IUser userToDelete;

                if (StringUtils.isNumeric(numberArg)) {
                    try {
                        maxDeletions = Integer.parseInt(numberArg);
                        if (maxDeletions <= 0) {
                            Util.deleteMessage(message);
                            Util.sendMessage(message.getChannel(), "Invalid number \"" + numberArg + "\".");
                            return;
                        }
                    } catch (NumberFormatException e) {
                    }
                }

                if (args.length >= 2) { //user specified
                    userToDelete = Util.getUserFromMentionArg(args[1]);
                    if (userToDelete == null) {
                        Util.deleteMessage(message);
                        Util.sendMessage(message.getChannel(), "Invalid user \"" + args[1] + "\".");
                        return;
                    }
                } else {
                    userToDelete = null;
                    if (!userRoles.contains(guild.getRoleByID(MovieRoles.ADMIN.id))) {
                        //Must be admin to purge all without entering user
                        Util.deleteMessage(message);
                        Util.sendMessage(message.getChannel(), "You must specify a user.");
                        return;
                    }
                }

                if (userToDelete != null) { //this is a prune

                    List<IMessage> toDelete = message.getChannel().getMessages().stream()
                            .filter(msg -> msg.getAuthor().equals(userToDelete) && !msg.equals(message))
                            .limit(maxDeletions)
                            .collect(Collectors.toList());

                    Util.bulkDelete(message.getChannel(), toDelete);
                    Util.sendLog(message, userToDelete.getDisplayName(guild) + "'s messages have been pruned in " + message.getChannel().getName() + ".");

                } else { //this is a purge

                    List<IMessage> toDelete = message.getChannel().getMessages().stream()
                            .filter(msg -> !msg.equals(message))
                            .limit(maxDeletions)
                            .collect(Collectors.toList());

                    Util.bulkDelete(message.getChannel(), toDelete);
                    Util.sendLog(message, numberArg + " messages have been purged in " + message.getChannel().getName() + ".");

                }

            } else {
                Util.deleteMessage(message);
                Util.sendMessage(message.getChannel(), "Invalid arguments. Usage: ``!prune <#> @user``");
                return;
            }

            Util.deleteBufferedMessage(message);
        }
    }

}
