package Interfaces;

import Models.LyricSaver;

public interface GUIUpdaterListener {

    void OnUpdateData(LyricSaver lyricSaver);
    void OnUpdateProgress(int progress,int total);

}
