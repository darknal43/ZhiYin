package client.pages.pageInternal.serverClientInteractions;

import server.model.media.MMusic;
import server.model.media.MText;
import server.model.soundCloud.MBand;
import tools.utilities.Utils;

import java.util.List;

/**
 * Created by Kevin Zheng on 2016-03-22.
 */
public class ArtistTalker extends Talkers {

    private MBand artist;

    //--Interface Fields
    private String name;
    private List<MMusic> musicList;

    //Getters and Setters
    public String getName() {
        return name;
    }

    public List<MMusic> getMusicList() {
        return musicList;
    }

    /*------------------------------------------------------------------------*/

    @Deprecated
    @Override
    public void init() {

    }

    public void init(MBand artist){
        this.artist = artist;
    }


    /*------------------------------------------------------------------------*/

    @Override
    public void pull() {
        modelStorage.requestModelFromServer(MText.class.getName(), artist.getName());

        for(long key: artist.getSongs()){
            modelStorage.requestModelFromServer(MMusic.class.getName(), key);
        }
    }

    @Override
    public void push() {
        return;
    }

    @Override
    public boolean isUpdated() {
        if(name == null)
            return false;

        if(musicList == null)
            return false;

        for(MMusic music: musicList)
            if(music == null)
                return false;

        return true;
    }

    @Override
    public void update(float dt) {
        MText bandName = modelStorage.<MText>getModel(artist.getName());

        if(bandName != null)
            name = bandName.getText();

        List<MMusic> newMusicList = Utils.<MMusic>newList();

        for(long key: artist.getSongs()){
            newMusicList.add(modelStorage.getModel(key));
        }
    }
}
