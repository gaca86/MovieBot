package cback;

import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.*;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.IdType;
import com.uwetrottmann.trakt5.enums.Type;
import retrofit2.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TraktManager {

    private TraktV2 trakt;
    private MovieBot bot;

    public TraktManager(MovieBot bot) {
        this.bot = bot;

        Optional<String> traktToken = bot.getConfigManager().getTokenValue("traktToken");
        if (!traktToken.isPresent()) {
            System.out.println("-------------------------------------");
            System.out.println("Insert your Trakt token in the config.");
            System.out.println("Exiting......");
            System.out.println("-------------------------------------");
            System.exit(0);
            return;
        }
        trakt = new TraktV2(traktToken.get());

    }

    public String getShowTitle(String imdbID) {
        try {
            Response<List<SearchResult>> search = trakt.search().idLookup(IdType.IMDB, imdbID, 1, 1).execute();
            if (search.isSuccessful() && !search.body().isEmpty()) {
                return search.body().get(0).show.title;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Show showSearch(String showName) {
        try {
            Response<List<SearchResult>> search = trakt.search().textQuery(showName, Type.SHOW, null, 1, 1).execute();
            if (search.isSuccessful() && !search.body().isEmpty()) {
                return search.body().get(0).show;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Show showIDSearch(String imdbID) {
        try {
            Response<List<SearchResult>> search = trakt.search().idLookup(IdType.IMDB, imdbID, 1, 1).execute();
            if (search.isSuccessful() && !search.body().isEmpty()) {
                Response<Show> show = trakt.shows().summary(imdbID, Extended.FULL).execute();
                if (show.isSuccessful()) {
                    return show.body();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Show showSummary(String imdbID) {
        try {
            Response<Show> show = trakt.shows().summary(imdbID, Extended.FULL).execute();
            if (show.isSuccessful()) {
                return show.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Show showSummaryFromName(String showName) {
        try {
            Response<List<SearchResult>> search = trakt.search().textQuery(showName, Type.SHOW, null, 1, 1).execute();
            if (search.isSuccessful() && !search.body().isEmpty()) {
                Response<Show> show = trakt.shows().summary(search.body().get(0).show.ids.imdb, Extended.FULL).execute();
                if (show.isSuccessful()) {
                    return show.body();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Movie movieSummaryFromName(String movieName) {
        try {
            Response<List<SearchResult>> search = trakt.search().textQuery(movieName, Type.MOVIE, null, 1, 1).execute();
            if (search.isSuccessful() && !search.body().isEmpty()) {
                Response<Movie> movie = trakt.movies().summary(search.body().get(0).movie.ids.imdb, Extended.FULL).execute();
                if (movie.isSuccessful()) {
                    return movie.body();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Person personSummaryFromName(String personName) {
        try {
            Response<List<SearchResult>> search = trakt.search().textQuery(personName, Type.PERSON, null, 1, 1).execute();
            if (search.isSuccessful() && !search.body().isEmpty()) {
                Response<Person> person = trakt.people().summary(search.body().get(0).person.ids.imdb, Extended.FULL).execute();
                if (person.isSuccessful()) {
                    return person.body();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}