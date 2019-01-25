package Models;

import java.io.Serializable;
import java.util.Objects;

public class Track implements Serializable {

    private String TrackID ;
    private String TrackName ;
    private String ArtistIDs ;
    private String AlbumID ;
    private String AlbumName ;
    private String ArtistNames ;
    private int DurationMS ;

    public Track(String TrackName,String ArtistNames){
        this.ArtistNames=ArtistNames;
        this.TrackName=TrackName;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Track)) return false;
        Track track = (Track) o;
        return Objects.equals(TrackID, track.TrackID) &&
                Objects.equals(TrackName, track.TrackName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TrackID, TrackName, ArtistIDs, AlbumID, AlbumName, ArtistNames, DurationMS);
    }

    @Override
    public String toString() {
        return "Track{" +
                "TrackID='" + TrackID + '\'' +
                ", TrackName='" + TrackName + '\'' +
                ", ArtistIDs='" + ArtistIDs + '\'' +
                ", AlbumID='" + AlbumID + '\'' +
                ", AlbumName='" + AlbumName + '\'' +
                ", ArtistNames='" + ArtistNames + '\'' +
                ", DurationMS=" + DurationMS +
                '}';
    }

    public String getTrackID() {
        return TrackID;
    }

    public void setTrackID(String trackID) {
        TrackID = trackID;
    }

    public String getTrackName() {
        return TrackName;
    }

    public void setTrackName(String trackName) {
        TrackName = trackName;
    }

    public String getArtistIDs() {
        return ArtistIDs;
    }

    public void setArtistIDs(String artistIDs) {
        ArtistIDs = artistIDs;
    }

    public String getAlbumID() {
        return AlbumID;
    }

    public void setAlbumID(String albumID) {
        AlbumID = albumID;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public String getArtistNames() {
        return ArtistNames;
    }

    public void setArtistNames(String artistNames) {
        ArtistNames = artistNames;
    }

    public int getDurationMS() {
        return DurationMS;
    }

    public void setDurationMS(int durationMS) {
        DurationMS = durationMS;
    }
}
