package cback.commands;

import cback.MovieBot;
import cback.MovieRoles;
import cback.Util;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandAMuteAdd implements Command {
    @Override
    public String getName() {
        return "amute";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "!amute @user";
    }

    @Override
    public String getDescription() {
        return "Silently mutes a user, does not add log";
    }

    @Override
    public List<String> getPermissions() {
        return Arrays.asList(MovieRoles.ADMIN.id);
    }

    @Override
    public void execute(MovieBot bot, IDiscordClient client, String[] args, IGuild guild, IMessage message, boolean isPrivate) {
        if (message.getAuthor().getRolesForGuild(guild).contains(guild.getRoleByID(MovieRoles.ADMIN.id))) {


            if (args.length == 1) {
                String user = args[0];
                Pattern pattern = Pattern.compile("^<@!?(\\d+)>");
                Matcher matcher = pattern.matcher(user);
                if (matcher.find()) {
                    String u = matcher.group(1);
                    IUser userInput = guild.getUserByID(u);
                    if (message.getAuthor().getID().equals(u)) {
                        Util.sendPrivateMessage(message.getAuthor(), "You probably shouldn't mute yourself");
                    } else {
                        try {
                            userInput.addRole(guild.getRoleByID("231269949635559424"));

                            Util.sendPrivateMessage(message.getAuthor(), userInput.getDisplayName(guild) + " has been muted");

                            List<String> mutedUsers = bot.getConfigManager().getConfigArray("muted");
                            if (!mutedUsers.contains(u)) {
                                mutedUsers.add(u);
                                bot.getConfigManager().setConfigValue("muted", mutedUsers);
                            }

                            Util.deleteMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Util.sendPrivateMessage(message.getAuthor(), "Invalid arguments. Usage: ``!mute @user``");
            }
        }
    }

}
