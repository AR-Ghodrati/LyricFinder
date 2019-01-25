package Helpers.UnOfficial;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParslyricsHelper {

    public static String Find(String URL){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            Document doc = Jsoup.connect(URL).userAgent("Mozilla/5.0").get();
            Elements results = doc.select("p[style=\"text-align: center;\"]");
            for (Element e:results) {
                String lyric=RemoveSingersName(Normalizer(LineSeprator(e.toString())));
                stringBuilder.append(lyric);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return "NOT_FOUND";
        }
    }

    private static String LineSeprator(String html){
        return html.replaceAll("<[^>]*>","/n");
    }

    private static List<String> ExtractSingersName(String lyric){
        //[Ariana Grande:] for example
        Matcher matcher = Pattern.compile("\\[([^]]+)").matcher(lyric);

        List<String> tags = new ArrayList<>();

        int pos = -1;
        while (matcher.find(pos+1)){
            pos = matcher.start();
            tags.add(matcher.group(1));
        }

        return tags;
    }

    private static String RemoveSingersName(String lyric){
        String ret= lyric;

        for (String singer:ExtractSingersName(lyric)) {
            ret=ret.replace("["+singer+"]","");
        }
        return ret;
    }

    private static String Normalizer(String lyric){
        return Jsoup.parse(lyric).text();
    }
}
