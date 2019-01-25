import Interfaces.LyricFinderListener;
import Models.LyricSaver;
import Models.Track;
import Utils.LyricHandler;


public class Test {

    public static void main(String[] args) {


        LyricHandler.Find(new Track("God Is A Woman","Ariana Grande")
                , new LyricFinderListener() {

            @Override
            public void OnFound(LyricSaver lyricSaver) {
                System.out.println("Found -> "+lyricSaver);
            }

            @Override
            public void OnNotFound(Track track) {
                System.out.println("NotFound -> "+track);
            }
        });

    }



}
