package Utils;

import Models.ArinousFinderSaver;
import Models.ArtistAllLyricsSaver;
import Models.LyricSaver;
import Models.Track;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class LyricSaverUtil {
    private static String AppPath="C:/ArinousLyricFinder/Files";
    private static final String DIR_Path=AppPath+"/"+"SAVED_LYRICS";
    private static final String NOTDOUND_DIR_Path=AppPath+"/"+"NOT_FOUND_LYRICS";
    private static final String FILE_Path=AppPath+"/LYRICS_FOUND_ID.txt";
    private static final String
            APP_Status_Path=AppPath+"/ArinousFinderAppStatus.txt";
    private static final String NOTFOUND_Path=AppPath+"/LYRICS_NOT_FOUND_ID.txt";

    public static void AddLyrics(LyricSaver lyrics){
        File DIR_file=new File(DIR_Path);
        if(!DIR_file.exists()) DIR_file.mkdirs();
        try {
            String FILE_Path=AppPath+"/"+"SAVED_LYRICS/"+
                    GetAllArtistName(lyrics.getTrack().getArtistNames()
                            .split(","))
                    +"_LYRICS.txt";
            File ArtistFile=new File(FILE_Path);
            if(ArtistFile.exists()){
                String data=FileUtils.readFileToString(ArtistFile,"UTF-8");
                if(data!=null && data.length()>0) {
                    ArtistAllLyricsSaver saver=new Gson()
                            .fromJson(data,ArtistAllLyricsSaver.class);
                    saver.AddLyric(lyrics);
                    saver.setOffset(saver.getOffset()+1);
                    //AddLyricFoundID(lyrics);
                    FileUtils.writeStringToFile(ArtistFile, new Gson().toJson(saver)
                            , "UTF-8");
                }
                else {
                    ArtistAllLyricsSaver saver=new ArtistAllLyricsSaver
                            (GetAllArtistName(lyrics.getTrack().getArtistNames()
                            .split(",")),0);

                    saver.AddLyric(lyrics);
                    //AddLyricFoundID(lyrics);
                    FileUtils.writeStringToFile(ArtistFile, new Gson().toJson(saver)
                            , "UTF-8");
                }
            }
            else {
                if(ArtistFile.createNewFile()){
                    System.out.println("New File With Name "+ArtistFile.getName()
                    +" Created!!");
                    ArtistAllLyricsSaver saver=new ArtistAllLyricsSaver
                            (GetAllArtistName(lyrics.getTrack().getArtistNames()
                                    .split(",")),0);

                    //AddLyricFoundID(lyrics);
                    saver.AddLyric(lyrics);
                    FileUtils.writeStringToFile(ArtistFile, new Gson().toJson(saver)
                            , "UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static void AddLyricFoundID(LyricSaver lyrics){
        File File=new File(FILE_Path);
        if(File.exists()){
            try {
                String data=FileUtils.readFileToString(File,"UTF-8");
                if(data!=null && data.length()>0){
                    List<String>ids=new Gson().fromJson(data
                            , new TypeToken<List<String>>() {
                            }.getType());
                    if(!ids.contains(lyrics.getTrack().getTrackID()))
                        ids.add(lyrics.getTrack().getTrackID());

                    FileUtils.writeStringToFile(File, new Gson().toJson(ids)
                            , "UTF-8");
                    ids.clear();
                }
                else {
                    List<String>ids=new ArrayList<>(1);
                    ids.add(lyrics.getTrack().getTrackID());
                    FileUtils.writeStringToFile(File, new Gson().toJson(ids)
                            , "UTF-8");
                }
            } catch (IOException e) {
            }
        }
        else {
            try {
                if(File.createNewFile()){
                    List<String>ids=new ArrayList<>(1);
                    ids.add(lyrics.getTrack().getTrackID());
                    FileUtils.writeStringToFile(File, new Gson().toJson(ids)
                            , "UTF-8");
                }
            } catch (IOException e) {
            }
        }
    }

    public static List<String> getAllTracksFoundID(){
        File File=new File(DIR_Path);
        List<String>IDS=new ArrayList<>();
        if(File.isDirectory()){

            File[]Files=File.listFiles();

            if (Files != null) {
                for (File f:Files) {
                    try {
                        String data=FileUtils.readFileToString
                                (f, Charset.forName("UTF-8"));

                        if(isJSONValid(data)) {
                            ArtistAllLyricsSaver saver = new Gson()
                                    .fromJson(data, ArtistAllLyricsSaver.class);

                            for (LyricSaver saver1 : saver.getLyrics()) {
                                if (!IDS.contains(saver1.getTrack().getTrackID()))
                                    IDS.add(saver1.getTrack().getTrackID());
                            }
                            System.out.println("Worked Found Lyric!!");
                        }
                        else System.err.println("Json For "+f.getName()+" file " +
                                "is Not Valid!!");

                    } catch (Exception e) {
                        System.err.println("Json For "+f.getName()+" file " +
                                "is Not Valid!!");
                    }
                }
                return IDS;
            }
        }
        return null;
    }

    public static List<String> getAllTracksIDNotFound() {
        File File = new File(NOTDOUND_DIR_Path);
        List<String>IDS=new ArrayList<>();
        if(File.isDirectory()) {
            File[]files=File.listFiles();

            if (files != null) {
                for (File f:files) {
                    try {
                        String data = FileUtils.readFileToString
                                (f, "UTF-8");
                        if (data != null && data.length() > 0) {

                            if (isJSONValid(data)) {
                                List<Track> tracks = new Gson().fromJson(data,
                                        new TypeToken<List<Track>>() {
                                        }.getType());

                                for (Track t : tracks) {
                                    if (!IDS.contains(t.getTrackID()))
                                        IDS.add(t.getTrackID());
                                }
                            }
                            System.out.println("Worked NotFound Track!!");
                        }
                        else System.err.println("Json For "+f.getName()+" file " +
                                "is Not Valid!!");
                    } catch (Exception e) {
                        System.err.println("Json For "+f.getName()+" file " +
                                "is Not Valid!!");
                    }
                }
                return IDS;
            }
        }
        return null;
    }

        public static void AddNotFound(Track track){
        File DIR_file=new File(DIR_Path);
        File File=new File(NOTFOUND_Path);
        AddNotFoundData(track);
        if(!DIR_file.exists()) DIR_file.mkdirs();
        try {
            String data=FileUtils.readFileToString(File,"UTF-8");
            if(data!=null && data.length()>0){
                List<String>lyricss=new Gson().fromJson(data
                        ,new TypeToken<List<String>>(){}.getType());
                if(!lyricss.contains(track.getTrackID())) {
                    lyricss.add(track.getTrackID());
                    FileUtils.writeStringToFile(File, new Gson().toJson(lyricss)
                            , "UTF-8");

                    System.out.println("LyricNOTFound Saved!!");
                    lyricss.clear();

                }
                else System.out.println("LyricNOTFound Exist!!");
            }
            else {
                List<String>lyricss=new ArrayList<>(1);
                lyricss.add(track.getTrackID());
                FileUtils.writeStringToFile(File,new Gson().toJson(lyricss)
                        ,"UTF-8");
                System.out.println("LyricNOTFound Saved!!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

       private static void AddNotFoundData(Track track){
        File File=new File(NOTDOUND_DIR_Path);
        if(!File.exists()) File.mkdirs();
        try {
            String FILE_Path=NOTDOUND_DIR_Path+"/"+
                    GetAllArtistName(track.getArtistNames()
                            .split(","))
                    +"_NOT_FOUND_TRACK_METADATA.txt";
            File ArtistFile=new File(FILE_Path);
            if(ArtistFile.exists()){
                String data=FileUtils.readFileToString(ArtistFile,"UTF-8");
                if(data!=null && data.length()>0) {
                    List<Track>tracks=new Gson().fromJson(data,new TypeToken<
                            List<Track>>(){}.getType());
                    if(!tracks.contains(track)) {
                        tracks.add(track);
                        FileUtils.writeStringToFile(
                                ArtistFile, new Gson().toJson(tracks)
                                , "UTF-8");
                    }
                    tracks.clear();
                }
                else {
                    List<Track>tracks=new ArrayList<>(1);
                    tracks.add(track);
                    FileUtils.writeStringToFile(ArtistFile, new Gson().toJson(tracks)
                            , "UTF-8");
                }
            }
            else {
                if(ArtistFile.createNewFile()){
                    System.out.println("New File With Name "+ArtistFile.getName()
                            +" Created!!");
                    List<Track>tracks=new ArrayList<>(1);
                    tracks.add(track);
                    FileUtils.writeStringToFile(ArtistFile, new Gson().toJson(tracks)
                            , "UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SaveLastOffset(int OffSet){

        File file=new File(APP_Status_Path);
        String data= null;
        try {
            File DIR_file=new File(DIR_Path);
            if(!DIR_file.exists()) DIR_file.mkdirs();
            if(file.exists()){
                String s=FileUtils.readFileToString(file,"UTF-8");
                if (s != null && s.length() > 0) {
                    ArinousFinderSaver saver=new Gson()
                            .fromJson(s,ArinousFinderSaver.class);
                    saver.setAbsOffset(OffSet);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                    ,"UTF-8");
                }
                else {
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setAbsOffset(OffSet);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }

            }
            else {
                if(file.createNewFile()){
                    System.out.println("Created New File With Name "+file.getName());
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setAbsOffset(OffSet);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void SaveFaLyricsFound(int FaLyricsFound){

        File file=new File(APP_Status_Path);
        String data= null;
        try {
            File DIR_file=new File(DIR_Path);
            if(!DIR_file.exists()) DIR_file.mkdirs();
            if(file.exists()){
                String s=FileUtils.readFileToString(file,"UTF-8");
                if (s != null && s.length() > 0) {
                    ArinousFinderSaver saver=new Gson()
                            .fromJson(s,ArinousFinderSaver.class);
                    saver.setFaLyricsFound(FaLyricsFound);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }
                else {
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setFaLyricsFound(FaLyricsFound);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }

            }
            else {
                if(file.createNewFile()){
                    System.out.println("Created New File With Name "+file.getName());
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setFaLyricsFound(FaLyricsFound);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void SaveFoundLyrics(int FoundLyrics){

        File file=new File(APP_Status_Path);
        String data= null;
        try {
            File DIR_file=new File(DIR_Path);
            if(!DIR_file.exists()) DIR_file.mkdirs();
            if(file.exists()){
                String s=FileUtils.readFileToString(file,"UTF-8");
                if (s != null && s.length() > 0) {
                    ArinousFinderSaver saver=new Gson()
                            .fromJson(s,ArinousFinderSaver.class);
                    saver.setFoundLyrics(FoundLyrics);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }
                else {
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setFoundLyrics(FoundLyrics);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }

            }
            else {
                if(file.createNewFile()){
                    System.out.println("Created New File With Name "+file.getName());
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setFoundLyrics(FoundLyrics);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void SaveNotFoundLyrics(int NotFoundLyrics){

        File file=new File(APP_Status_Path);
        String data= null;
        try {
            File DIR_file=new File(DIR_Path);
            if(!DIR_file.exists()) DIR_file.mkdirs();
            if(file.exists()){
                String s=FileUtils.readFileToString(file,"UTF-8");
                if (s != null && s.length() > 0) {
                    ArinousFinderSaver saver=new Gson()
                            .fromJson(s,ArinousFinderSaver.class);
                    saver.setNotFoundLyrics(NotFoundLyrics);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }
                else {
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setNotFoundLyrics(NotFoundLyrics);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }

            }
            else {
                if(file.createNewFile()){
                    System.out.println("Created New File With Name "+file.getName());
                    ArinousFinderSaver saver=new ArinousFinderSaver();
                    saver.setNotFoundLyrics(NotFoundLyrics);
                    FileUtils.writeStringToFile(file,new Gson().toJson(saver)
                            ,"UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArinousFinderSaver getAppData(){
        File file=new File(APP_Status_Path);
        try {
            if(file.exists()) {
                String s = FileUtils.readFileToString(file, "UTF-8");
                if (s != null && s.length() > 0) {
                    return new Gson()
                            .fromJson(s, ArinousFinderSaver.class);

                }
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }



    private static String GetAllArtistName(String[] names){
        StringBuilder s= new StringBuilder();
        for (String name:names) {
            s.append("__").append(name.trim().replace(" ", "_")
                    .replace("-", "_"));
        }

        if(s.length()>2)
        return s.toString().substring(2);
        else return s.toString();
    }

    private static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
