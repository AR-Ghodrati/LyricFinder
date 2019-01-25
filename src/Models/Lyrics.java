package Models;


import java.io.*;
import java.util.Objects;

public class Lyrics implements Serializable {

    public static final int NO_RESULT = -2;
    public static final int NEGATIVE_RESULT = -1;
    public static final int POSITIVE_RESULT = 1;
    public static final int ERROR = -3;
    public static final int SEARCH_ITEM = 2;
    private final int mFlag;
    private String mTitle;
    private String mArtist;
    private String mOriginalTitle;
    private String mOriginalArtist;
    private String mSourceUrl;
    private String mCoverURL;
    private String mLyrics;
    private String mSource;
    private boolean mLRC = false;

    public Lyrics(int flag) {
        this.mFlag = flag;
    }

    public static Lyrics fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        if (data == null)
            return null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Lyrics) is.readObject();
    }

    public String getTrack() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getOriginalTrack() {
        if (mOriginalTitle != null)
            return mOriginalTitle;
        else
            return mTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    public String getOriginalArtist() {
        if (mOriginalArtist != null)
            return mOriginalArtist;
        else
            return mArtist;
    }

    public void setOriginalArtist(String originalArtist) {
        this.mOriginalArtist = originalArtist;
    }

    public String getURL() {
        return mSourceUrl;
    }

    public void setURL(String uRL) {
        this.mSourceUrl = uRL;
    }

    public String getCoverURL() {
        return mCoverURL;
    }

    public void setCoverURL(String coverURL) {
        this.mCoverURL = coverURL;
    }

    public String getText() {
        return mLyrics;
    }

    public void setText(String lyrics) {
        this.mLyrics = lyrics;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public int getFlag() {
        return mFlag;
    }

    public boolean isLRC() {
        return this.mLRC;
    }

    public void setLRC(boolean LRC) {
        this.mLRC = LRC;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.close();
        } finally {
            bos.close();
        }
        return bos.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lyrics)) return false;
        Lyrics lyrics = (Lyrics) o;
        return mLRC == lyrics.mLRC &&
                mFlag == lyrics.mFlag &&
                Objects.equals(mTitle, lyrics.mTitle) &&
                Objects.equals(mArtist, lyrics.mArtist) &&
                Objects.equals(mOriginalTitle, lyrics.mOriginalTitle) &&
                Objects.equals(mOriginalArtist, lyrics.mOriginalArtist) &&
                Objects.equals(mSourceUrl, lyrics.mSourceUrl) &&
                Objects.equals(mCoverURL, lyrics.mCoverURL) &&
                Objects.equals(mLyrics, lyrics.mLyrics) &&
                Objects.equals(mSource, lyrics.mSource);
    }

    @Override
    public int hashCode() {
        // Potential issue with the Birthday Paradox when we hash over 50k lyrics
        return this.getURL() != null ? this.getURL().hashCode() :
                (""+this.getOriginalArtist()+this.getOriginalTrack()+this.getSource()).hashCode();
    }

    @Override
    public String toString() {
        return "Lyrics{" +
                "mTitle='" + mTitle + '\'' +
                ", mArtist='" + mArtist + '\'' +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mOriginalArtist='" + mOriginalArtist + '\'' +
                ", mSourceUrl='" + mSourceUrl + '\'' +
                ", mCoverURL='" + mCoverURL + '\'' +
                ", mLyrics='" + mLyrics + '\'' +
                ", mSource='" + mSource + '\'' +
                ", mLRC=" + mLRC +
                ", mFlag=" + mFlag +
                '}';
    }

    public interface Callback {
        void onLyricsDownloaded(Lyrics lyrics);
    }
}
