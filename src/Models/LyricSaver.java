package Models;

import java.io.Serializable;
import java.util.Objects;

public class LyricSaver implements Serializable {

    private String LyricTxt;
    private boolean HasFaLyric; // if true ENLyric is null
    private String SourceFound;
    private Track track;

    public LyricSaver(String lyricTxt, String sourceFound, Track track) {
        LyricTxt = lyricTxt;
        HasFaLyric = isProbablyArabic(lyricTxt);
        SourceFound = sourceFound;
        this.track = track;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LyricSaver)) return false;
        LyricSaver sender = (LyricSaver) o;
        return this.track.getTrackID().equals(sender.getTrack().getTrackID())
                && this.track.getTrackName().equals(sender.getTrack().getTrackName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(LyricTxt, HasFaLyric, SourceFound, track);
    }

    @Override
    public String toString() {
        return "LyricSaver{" +
                "LyricTxt='" + LyricTxt + '\'' +
                ", HasFaLyric=" + HasFaLyric +
                ", SourceFound='" + SourceFound + '\'' +
                ", track=" + track +
                '}';
    }

    public String getLyricTxt() {
        return LyricTxt;
    }

    public void setLyricTxt(String lyricTxt) {
        LyricTxt = lyricTxt;
    }

    public boolean isHasFaLyric() {
        return HasFaLyric;
    }

    public void setHasFaLyric(boolean hasFaLyric) {
        HasFaLyric = hasFaLyric;
    }

    public String getSourceFound() {
        return SourceFound;
    }

    public void setSourceFound(String sourceFound) {
        SourceFound = sourceFound;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    private boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
}
