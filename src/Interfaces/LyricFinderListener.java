package Interfaces;

import Models.LyricSaver;
import Models.Track;

public interface LyricFinderListener {
    void OnFound(LyricSaver lyricSaver);
    void OnNotFound(Track track);
}
