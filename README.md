# LyricFinder

# Java Application To Find Music Lyric With TrackName & ArtistName(s).

Helpers Used in Program :

1-Genius  
2-AZLyrics   
3-LyricWiki     
4-LyricsMania     
5-UrbanLyrics   
6-JLyric    
7-Lololyrics    
&& ...


# You Can Find Lyric With TrackName & ArtistName(s) For Example: 

TrackName=God Is A Woman & ArtistName=Ariana Grande

```java
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
```
   
# And Result is:
```
Found -> LyricSaver{LyricTxt='You, you love it how I move you/nYou love it how I touch you, my one/nWhen all is said and done/nYou'll believe God is a woman/nAnd I, I feel it after midnight/nA feelin' that you can't fight, my one/nIt lingers when we're done/nYou'll believe God is a woman/n/nI don't wanna waste no time, yeah/nYou ain't got a one-track mind, yeah/nHave it any way you like, yeah/nAnd I can tell that you know I know how I want it/nAin't nobody else can relate/nBoy, I like that you ain't afraid/nBaby, lay me down and let's pray/nI'm tellin' you the way I like it, how I want it/n/n(Yeah) And I can be all the things you told me not to be/n(Yeah) When you try to come for me, I keep on flourishing/n(Yeah) And he see the universe when I'm the company/nIt's all in me/n/nYou, you love it how I move you/nYou love it how I touch you, my one/nWhen all is said and done/nYou'll believe God is a woman/nAnd I, I feel it after midnight/nA feelin' that you can't fight, my one/nIt lingers when we're done/nYou'll believe God is a woman/n/n(Yeah)/nI tell you all the things you should know/nSo baby, take my hands, save your soul/nWe can make it last, take it slow, hmm/nAnd I can tell that you know I know how I want it, yeah/nBut you're different from the rest/nAnd boy, if you confess, you might get blessed/nSee if you deserve what comes next/nI'm tellin' you the way I like it, how I want it/n/n(Yeah) And I can be all the things you told me not to be/n(Yeah) When you try to come for me, I keep on flourishing/n(Yeah) And he see the universe when I'm the company/nIt's all in me/n/nYou, you love it how I move you/nYou love it how I touch you, my one/nWhen all is said and done/nYou'll believe God is a woman/nAnd I, I feel it after midnight/nA feelin' that you can't fight, my one/nIt lingers when we're done/nYou'll believe God is a woman/n/nYeah, yeah/n(God is a woman)/nYeah, yeah/n(God is a woman, yeah)/nMy one/n(One)/nWhen all is said and done/nYou'll believe God is a woman/nYou'll believe God/n(God is a woman)/nOh, yeah/n(God is a woman, yeah)/n(One)/nIt lingers when we're done/nYou'll believe God is a woman', HasFaLyric=false, SourceFound='Genius', track=Track{TrackID='null', TrackName='God Is A Woman', ArtistIDs='null', AlbumID='null', AlbumName='null', ArtistNames='Ariana Grande', DurationMS=0}}

```


