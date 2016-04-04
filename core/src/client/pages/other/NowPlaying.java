package client.pages.other;

import client.events.executables.internalChanges.TestExecutable;
import client.events.executables.internalChanges.schmoferMusicExecutable.ExecuteMoveSlider;
import client.events.executables.internalChanges.schmoferMusicExecutable.ExecutePlayMusic;
import client.events.executables.internalChanges.schmoferMusicExecutable.ExecuteSetTime;
import client.events.executables.internalChanges.updatePageExecutables.ExecuteToTempState;
import client.pages.State;
import client.stateInterfaces.Executable;
import tools.serverTools.databases.LocalDatabase;
import tools.serverTools.databases.LocalDatabaseFactory;
import client.singletons.SkinSingleton;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import server.model.media.MImage;
import server.model.media.MMusic;
import tools.AudioTools.AudioManager;

/**
 * This is the now playing page as given in the
 * art assets folder.
 *
 * Picture co-ords: TopLeft: 49, 193
 *                  BottomRight:  706, 847
 *
 * Created by Hongyu Wang on 3/9/2016.
 */
public class NowPlaying extends NowPlayingShell {
    private ExecuteMoveSlider executeMoveSlider;
    private State previousState;
    private Slider slider;
    private boolean verbose;
    private Label totalTime;
    private Label currentTime;
    private long iterations;



    private MMusic post;

    private ImageButton pauseButton;
    private ImageButton playButton;
    private ImageButton create;
    private ImageButton showComments;
    private Image filter;
    private Image picture;

    public NowPlaying(State previousState, MMusic post){
        this.previousState = previousState;
        verbose = false;
        this.post = post;
        init();
    }



    protected void initAlbumArt(){
        LocalDatabase localDatabase = LocalDatabaseFactory.createLocalDatabase();
        
        MImage image = localDatabase.getModel(post.getAlbumArt());

        byte [] bytes = image.getImage();

        Pixmap px = new Pixmap(bytes, 0, bytes.length);

        Texture albumArt = new Texture(px);
        px.dispose();

        filter = new Image(new Texture("Filter.png"));

        filter.setBounds((50) * M, (1160 - 655) * M, (655) * M, (655) * M);

        picture = new Image(albumArt);
        filter.setColor(1, 1, 1, 0.2F);

        picture.setBounds((50) * M, (1160 - 655) * M, (655) * M, (655) * M);

        stage.addActor(picture);

    }


    @Override
    protected void init() {
        super.init();

        TestExecutable rewindEx = new TestExecutable("rewind");
        addImageButton("NowPlaying/Rewind@", rewindEx, 170, 246, 53, 46);

        TestExecutable forwardEx = new TestExecutable("forward");
        addImageButton("NowPlaying/Forward@", forwardEx, 535, 246, 53, 46);

        initAlbumArt();
        ExecuteToTempState backEx = new ExecuteToTempState(previousState);
        addImageButton("NowPlaying/Back@", backEx, 0, 1217, 117, 117);



        TestExecutable pauseEx = new TestExecutable("pause");
        pauseButton = createImageButton("NowPlaying/Pause@", pauseEx, 288, 177, 180, 180);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause();
                new ExecutePlayMusic().execute();
            }
        });

        TestExecutable playEx = new TestExecutable("play");
        playButton = createImageButton("NowPlaying/Play@", playEx, 288, 177, 180, 180);
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                play();
                new ExecutePlayMusic().execute();
            }
        });

        stage.addActor(playButton);

        ExecuteToTempState commentEx = new ExecuteToTempState(new Comment(this, post));
        addImageButton("NowPlaying/Comment@", commentEx, 0, 0, 230, 117);

        ExecuteToTempState secEx = new ExecuteToTempState(new Sec1(this, post));
        addImageButton("NowPlaying/1S@", secEx, 230, 0, 290, 117);

        initializeSlider();

        addMusicLabels();

        initializeComments();
    }

    private void initializeComments(){
        Executable addCommentEx = new TestExecutable("fuckJerryXu");
        create = createImageButton("NowPlaying/AddComment@", addCommentEx, 537, 1063, 51, 51);

        Executable showCommentsEx = new Executable(){
            @Override
            public void execute() {

                setUpScrollingComments();
                verbose = !verbose;

            }
        };

        filter = new Image(new Texture("Filter.png"));
        showComments = createImageButton("NowPlaying/ShowComments@", showCommentsEx, 607, 1063, 51, 51);
        stage.addActor(showComments);

    }

    private void setUpScrollingComments() {
        if (!verbose) {
            stage.addActor(create);
            showComments.remove();
            picture.remove();
            stage.addActor(filter);
            stage.addActor(showComments);
            stage.addActor(create);
        } else{
            filter.remove();
            create.remove();
            stage.addActor(picture);
        }
    }

    private void pause(){
        pauseButton.remove();
        stage.addActor(playButton);
    }

    private void play(){
        playButton.remove();
        stage.addActor(pauseButton);
    }



    @Override
    public void dispose() {


    }

    @Override
    public void update(float dt){
        super.update(dt);
        if (iterations%5 == 0)
            executeMoveSlider.execute();
        if (os == MAC)
            updateMusicLabels(AudioManager.getTime());
        iterations ++;
    }

    public void initializeSlider(){
        slider = new Slider(0, 100, 1, false, SkinSingleton.getInstance());
        Table table = new Table();
        table.add(slider).minWidth(660*M);
        table.setBounds(45 * M, 400 * M, 660 * M, 10);




        slider.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new ExecuteSetTime(slider).execute();
            }
        });
        executeMoveSlider = new ExecuteMoveSlider(slider);

        stage.addActor(table);
    }


    private void addMusicLabels(){
        currentTime = new Label("0:00", SkinSingleton.getInstance());
        totalTime = new Label("", SkinSingleton.getInstance());
        currentTime.setBounds(60 * M, 420 * M, currentTime.getPrefWidth(), currentTime.getPrefHeight());
        totalTime.setBounds((WIDTH - 100 - totalTime.getPrefWidth()) * M, 420 * M, currentTime.getPrefWidth(), currentTime.getPrefHeight());
        stage.addActor(totalTime);
        stage.addActor(currentTime);
    }

    public static String convertToMinutes(double timeInSeconds){
        int time = (int)timeInSeconds;

        int minutes = time/60;
        int seconds = time%60;
        if (seconds < 10)
            return minutes+":0"+seconds;
        return minutes+":"+seconds;
    }

    public void updateMusicLabels(double time){
        currentTime.setText(convertToMinutes(time));

        String total = convertToMinutes(AudioManager.trackLength());
        totalTime.setText(total);
    }



}
