package Helpers.Official;

import Models.Lyrics;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;

import static Models.Lyrics.POSITIVE_RESULT;


public class LyricWiki {


    public static final String domain = "lyrics.wikia.com";
    private static final String baseUrl =
            "http://lyrics.wikia.com/api.php?action=lyrics&fmt=json&func=getSong&artist=%1s&song=%1s";
    private static final String baseAPIUrl =
            "http://lyrics.wikia.com/wikia.php?controller=LyricsApi&method=getSong&artist=%1s&song=%2s";
    private static final String baseSearchUrl =
            "http://lyrics.wikia.com/Special:Search?search=%s&fulltext=Search";


    public static ArrayList<Lyrics> search(String query) {
        ArrayList<Lyrics> results = new ArrayList<>();
        query = query + " song";
        query = Normalizer.normalize(query, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        try {
            URL queryURL = new URL(String.format(baseSearchUrl, URLEncoder.encode(query, "UTF-8")));
            Document searchpage = Jsoup.connect(queryURL.toExternalForm()).timeout(0).get();
            Elements searchResults = searchpage.getElementsByClass("Results");
            if (searchResults.size() >= 1) {
                searchResults = searchResults.get(0).getElementsByClass("result");
                for (Element searchResult : searchResults) {
                    String[] tags = searchResult.getElementsByTag("h1").text().split(":");
                    if (tags.length != 2) continue;
                    String url = searchResult.getElementsByTag("a").attr("href");
                    Lyrics lyrics = new Lyrics(Lyrics.SEARCH_ITEM);
                    lyrics.setArtist(tags[0]);
                    lyrics.setTitle(tags[1]);
                    lyrics.setURL(url);
                    lyrics.setSource(domain);
                    results.add(lyrics);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static Lyrics Find(String artist, String title) {
        if ((artist == null) || (title == null))
            return new Lyrics(Lyrics.ERROR);
        String originalArtist = artist;
        String originalTitle = title;
        String url = null;
        try {
            String encodedArtist = URLEncoder.encode(artist, StandardCharsets.UTF_8);
            String encodedSong = URLEncoder.encode(title, StandardCharsets.UTF_8);

            JsonObject json = new JsonParser().parse(getUrlAsString(new URL(
                    String.format(baseUrl, encodedArtist, encodedSong))).replace("song = ", "")).getAsJsonObject();
            url = URLDecoder.decode(json.get("url").getAsString(), StandardCharsets.UTF_8);
            artist = json.get("artist").getAsString();
            title = json.get("song").getAsString();
            encodedArtist = URLEncoder.encode(artist, StandardCharsets.UTF_8);
            encodedSong = URLEncoder.encode(title, StandardCharsets.UTF_8);
            json = new JsonParser().parse(getUrlAsString
                    (new URL(String.format(baseAPIUrl, encodedArtist, encodedSong)))
            ).getAsJsonObject().get("result").getAsJsonObject();
            Lyrics lyrics = new Lyrics(POSITIVE_RESULT);
            lyrics.setArtist(artist);
            lyrics.setTitle(title);
            lyrics.setText(json.get("lyrics").getAsString().replaceAll("\n", "<br />"));
            lyrics.setURL(url);
            lyrics.setOriginalArtist(originalArtist);
            lyrics.setOriginalTitle(originalTitle);
            lyrics.setSource(domain);
            return lyrics;
        } catch (JsonParseException e) {
            return new Lyrics(Lyrics.NO_RESULT);
        } catch (IOException | IllegalStateException | NullPointerException e) {
            return url == null ? new Lyrics(Lyrics.ERROR) : fromURL(url, originalArtist, originalTitle);
        }
    }

    public static Lyrics fromURL(String url, String artist, String song) {
        if (url.endsWith("action=edit")) {
            return new Lyrics(Lyrics.NO_RESULT);
        }
        String text;
        String originalArtist = artist;
        String originalTitle = song;
        try {
            //url = URLDecoder.decode(url, "utf-8");
            Document lyricsPage = Jsoup.connect(url).get();
            Element lyricbox = lyricsPage.select("div.lyricBox").get(0);
            lyricbox.getElementsByClass("references").remove();
            String lyricsHtml = lyricbox.html();
            final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
            text = Jsoup.clean(lyricsHtml, "", new Whitelist().addTags("br"), outputSettings);
            if (text.contains("&#"))
                text = Parser.unescapeEntities(text, true);
            text = text.replaceAll("\\[\\d]", "").trim();

            String title = lyricsPage.getElementsByTag("title").get(0).text();
            int colon = title.indexOf(':');
            if (artist == null)
                artist = title.substring(0, colon).trim();
            if (song == null) {
                int end = title.lastIndexOf("Lyrics");
                song = title.substring(colon+1, end).trim();
            }
        } catch (IndexOutOfBoundsException | IOException e) {
            return new Lyrics(Lyrics.ERROR);
        }

        artist = URLDecoder.decode(artist, StandardCharsets.UTF_8);
        song = URLDecoder.decode(song, StandardCharsets.UTF_8);
        if (text.contains("Unfortunately, we are not licensed to display the full lyrics for this song at the moment.")
                || text.equals("Instrumental <br />")) {
            Lyrics result = new Lyrics(Lyrics.NEGATIVE_RESULT);
            result.setArtist(artist);
            result.setTitle(song);
            return result;
        } else if (text.equals("") || text.length() < 3)
            return new Lyrics(Lyrics.NO_RESULT);
        else {
            Lyrics lyrics = new Lyrics(POSITIVE_RESULT);
            lyrics.setArtist(artist);
            lyrics.setTitle(song);
            lyrics.setOriginalArtist(originalArtist);
            lyrics.setOriginalTitle(originalTitle);
            lyrics.setText(text);
            lyrics.setSource("LyricsWiki");
            lyrics.setURL(url);
            return lyrics;
        }
    }

    private static String getUrlAsString(URL URl){
        try {
            return IOUtils.toString(URl,StandardCharsets.UTF_8);
        } catch (IOException e) {
            //e.printStackTrace();
            return "";
        }
    }

}