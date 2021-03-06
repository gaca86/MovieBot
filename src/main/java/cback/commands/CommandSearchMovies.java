package cback.commands;

import cback.MovieBot;
import cback.Util;
import com.uwetrottmann.trakt5.entities.Movie;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandSearchMovies implements Command {
    @Override
    public String getName() {
        return "movie";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "!movie [movie name]";
    }

    @Override
    public String getDescription() {
        return "Searches and returns information about the desired movie";
    }

    @Override
    public List<String> getPermissions() {
        return null;
    }

    @Override
    public void execute(MovieBot bot, IDiscordClient client, String[] args, IGuild guild, IMessage message, boolean isPrivate) {
        String movieName = Arrays.stream(args).collect(Collectors.joining(" "));
        Movie movieData = bot.getTraktManager().movieSummaryFromName(movieName);
        if (movieData != null) {
            String title = "**" + movieData.title + " (" + Integer.toString(movieData.year) + ")**";
            String overview = movieData.overview;
            String premier = new SimpleDateFormat("MMM dd, yyyy").format(movieData.released.toDate());
            String rating = movieData.certification;
            String runtime = Integer.toString(movieData.runtime);
            String country = movieData.language;
            String homepage = "<https://trakt.tv/movies/" + movieData.ids.slug + ">\n<http://www.imdb.com/title/" + movieData.ids.imdb + ">";

            EmbedBuilder embed = Util.getEmbed(message.getAuthor());

            if (movieData.tagline != null) {
                embed.withTitle(movieData.tagline);
            }

            embed.withTitle(title);
            embed.withDescription(overview);
            embed.appendField("References:", homepage, true);

            embed.appendField("\u200B", "\u200B", false);

            embed.appendField("PREMIERED:", premier, true);
            embed.appendField("RUNTIME:", runtime, true);
            embed.appendField("RATED:", rating, true);
            embed.appendField("LANGUAGE:", country.toUpperCase(), true);
            embed.appendField("GENRES:", String.join(", ", movieData.genres), true);

            Util.sendEmbed(message.getChannel(), embed.build());
        } else {
            Util.sendMessage(message.getChannel(), "Error: Movie not found");
            Util.errorLog(message, "Couldn't find movie: " + movieName);
        }
    }

}
