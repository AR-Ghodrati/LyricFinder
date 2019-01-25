
package Helpers.Official;

import Models.Lyrics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class MetalArchives {

    public static final String domain = "metal-archives.com";

    public static Lyrics fromMetaData(String artist, String title) {
        String baseURL = "http://www.metal-archives.com/search/ajax-advanced/searching/songs/?bandName=%s&songTitle=%s&releaseType[]=1&exactSongMatch=1&exactBandMatch=1";
        String urlArtist = artist.replaceAll("\\s","+");
        String urlTitle = title.replaceAll("\\s","+");
        String url;
        String text;
        try {
            String response = getUrlAsString(String.format(baseURL, urlArtist, urlTitle));
            JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();
            JsonArray track = jsonResponse.getAsJsonArray("aaData").get(0).getAsJsonArray();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < track.size(); i++)
                builder.append(track.get(i).getAsString());
            Document trackDocument = Jsoup.parse(builder.toString());
            url = trackDocument.getElementsByTag("a").get(1).attr("href");
            String id = trackDocument.getElementsByClass("viewLyrics").get(0).id().substring(11);
            text = Jsoup.connect("http://www.metal-archives.com/release/ajax-view-lyrics/id/" + id)
                    .get().body().html();
        } catch (JsonSyntaxException e) {
            return new Lyrics(Lyrics.NO_RESULT);
        } catch (IOException e) {
            return new Lyrics(Lyrics.ERROR);
        }
        Lyrics lyrics = new Lyrics(Lyrics.POSITIVE_RESULT);
        lyrics.setArtist(artist);
        lyrics.setTitle(title);
        lyrics.setText(text);
        lyrics.setSource(domain);
        lyrics.setURL(url);

        return lyrics;
    }

    public static Lyrics fromURL(String url, String artist, String title){
        // TODO: support metal-archives URL
        return new Lyrics(Lyrics.NO_RESULT);
    }
    private static String getUrlAsString(String URl){
        try {
            return IOUtils.toString(URI.create(URl), StandardCharsets.UTF_8);
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
    }

}
