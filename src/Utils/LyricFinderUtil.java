package Utils;

import Helpers.Official.*;
import Models.Lyrics;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Models.Lyrics.NO_RESULT;

public class LyricFinderUtil {

    public static Lyrics getLyric(String ArtistName, String TrackName){

        try {
            Lyrics lyrics=new Lyrics(NO_RESULT);
            lyrics=Genius.Find(ArtistName,TrackName);
           // System.out.println(lyrics);
            if(IsFound(lyrics)) {
                return NormalizeLyric(lyrics);
            }
            else {
                lyrics=Genius.Find2(ArtistName,TrackName);
                if(IsFound(lyrics)) {
                    return NormalizeLyric(lyrics);
                }
                else
                return getLyric(ArtistName,TrackName,1);
            }
        }
        catch (Exception e){
            return new Lyrics(NO_RESULT);
        }

    }

    private static Lyrics getLyric(String ArtistName, String TrackName, int Helper){
        Lyrics lyrics=new Lyrics(Lyrics.NO_RESULT);

        if(Helper>8){
            return new Lyrics(Lyrics.NO_RESULT);
        }
        else {
            switch (Helper){
                case 1:
                    lyrics= LyricWiki.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                case 2:
                    lyrics= AZLyrics.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                    else getLyric(ArtistName,TrackName,3);
                    break;
                case 3:
                    lyrics= Bollywood.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                    else getLyric(ArtistName,TrackName,4);
                    break;
                case 4:
                    lyrics= LyricsMania.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                    else getLyric(ArtistName,TrackName,5);
                    break;
                case 5:
                    lyrics= Lololyrics.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                    else getLyric(ArtistName,TrackName,6);
                    break;
                case 6:
                    lyrics= PLyrics.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                    else getLyric(ArtistName,TrackName,7);
                    break;
                case 7:
                    lyrics= UrbanLyrics.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                    else getLyric(ArtistName,TrackName,8);
                    break;
                case 8:
                    lyrics= JLyric.Find(ArtistName,TrackName);
                    if(IsFound(lyrics)){
                        return NormalizeLyric(lyrics);
                    }
                    else getLyric(ArtistName,TrackName,9);
                    break;
                    default:getLyric(ArtistName,TrackName,85);
            }
        }
        return lyrics;
    }

    private static Lyrics NormalizeLyric(Lyrics lyrics){
        String ly= RemoveSingersName(Normalizer(LineSeparator(
                lyrics.getText())));
        lyrics.setText(ly);
        return lyrics;
    }

    private static boolean IsFound(Lyrics lyrics){
        switch (lyrics.getFlag()){
            case Lyrics.NO_RESULT:
            case Lyrics.ERROR:
            case Lyrics.NEGATIVE_RESULT:
            case Lyrics.SEARCH_ITEM:
                return false;
            case Lyrics.POSITIVE_RESULT:return true;
            default:return false;
        }
    }


    private static String LineSeparator(String html){
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

    private static String Normalizer(String lyric) {
        return Jsoup.parse(lyric).text();
    }
}
