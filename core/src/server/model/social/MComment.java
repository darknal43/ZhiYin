package server.model.social;

import server.model.structureModels.ServerModel;

import java.util.List;

/**
 * Created by Kevin Zheng on 2016-03-02.
 */
public class MComment extends ServerModel {
    private long text;
    private List<Long> likes;
    private List<Long> Music;
    private List<Long> Images;
    private List<Long> Audio;
    private List<Long> comments;
    private long timeStamp;

    public long getText() {
        return text;
    }

    public void setText(long text) {
        this.text = text;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    public List<Long> getMusic() {
        return Music;
    }

    public void setMusic(List<Long> music) {
        Music = music;
    }

    public List<Long> getImages() {
        return Images;
    }

    public void setImages(List<Long> image) {
        Images = image;
    }

    public List<Long> getAudio() {
        return Audio;
    }

    public void setAudio(List<Long> audio) {
        Audio = audio;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }
}