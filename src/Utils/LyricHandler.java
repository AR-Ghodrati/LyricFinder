package Utils;

import Interfaces.LyricFinderListener;
import Models.LyricSaver;
import Models.Lyrics;
import Models.Track;

public class LyricHandler {

    public static void Find(Track track, LyricFinderListener lyricFinderListener){

        Thread thread=new Thread(new LyricFinderAsync(track,lyricFinderListener));
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private static LyricSaver Convert(Lyrics lyrics, Track track){
        return new LyricSaver(lyrics.getText(),lyrics.getSource(),track);
    }

    private static class LyricFinderAsync implements Runnable{

        private Track track;
        private LyricFinderListener lyricFinderListener;

        public LyricFinderAsync(Track track,LyricFinderListener
                                lyricFinderListener){
            this.track=track;
            this.lyricFinderListener=lyricFinderListener;
        }
        @Override
        public void run() {


                Lyrics lyrics = LyricFinderUtil.getLyric(
                        track.getArtistNames().split(",")[0]
                        , track.getTrackName());


                if (lyrics.getFlag() == Lyrics.POSITIVE_RESULT) {
                    lyricFinderListener.OnFound(Convert(lyrics, track));
                }
                else{
                    lyricFinderListener.OnNotFound(track);
                }
            }


    }
}
