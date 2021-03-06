package cback.commands;

import cback.MovieBot;
import cback.MovieRoles;
import cback.Util;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;
import java.util.List;

public class CommandStaffSummon implements Command {
    @Override
    public String getName() {
        return "staff";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("summon");
    }

    @Override
    public String getSyntax() {
        return "!staff";
    }

    @Override
    public String getDescription() {
        return "Requests moderators to come to the channel you are in, permission can be taken away";
    }

    @Override
    public List<String> getPermissions() {
        return null;
    }

    @Override
    public void execute(MovieBot bot, IDiscordClient client, String[] args, IGuild guild, IMessage message, boolean isPrivate) {
        if (!bot.getConfigManager().getConfigArray("cantsummon").contains(message.getAuthor().getID())) {
            Util.botLog(message);

            IChannel staff_hq = client.getChannelByID("256249393622024202");
            List<IUser> mods = Util.getUsersByRole(MovieRoles.MOD.id);

            StringBuilder modMentions = new StringBuilder();
            for (IUser u : mods) {
                modMentions.append(" ").append(u.mention());
            }

            Util.sendMessage(message.getChannel(), "Staff have been notified and will come shortly.");
            Util.sendMessage(staff_hq, message.getAuthor().mention() + " has requested staff in " + message.getChannel().mention() + "\n" + modMentions.toString());

            Util.deleteMessage(message);
        }
    }
}
