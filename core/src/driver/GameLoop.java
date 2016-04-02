package driver;

import client.pageStorage.Pages;
import client.pages.pageInternal.serverClientInteractions.TalkerFactory;
import client.pages.pageInternal.serverClientInteractions.VeryBeginningInitializer;
import client.singletons.MainBatch;
import client.singletons.StateManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import server.model.media.MMusic;
import tools.AudioTools.AudioCreator;

import java.nio.ByteBuffer;

public class GameLoop extends ApplicationAdapter {
	private StateManager stateManager;
	private SpriteBatch spriteBatch;

	public static boolean ISPUSHING = true;

	public static final int WIDTH = 750;

	public static final int HEIGHT = 1334;

    private static OrthographicCamera primary;

	@Override
	public void create() {

		stateManager = StateManager.getInstance();
		Pages.initLogin();
        stateManager.changeState(Pages.LOGIN);
		spriteBatch = MainBatch.getInstance();
        primary = new OrthographicCamera(WIDTH*StateManager.M, HEIGHT*StateManager.M);
        primary.translate(primary.viewportWidth / 2, primary.viewportHeight / 2);
        primary.update();
		AudioCreator.initializeAllWindows();

    }

	@Override
	public void render () {



		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        spriteBatch.setProjectionMatrix(primary.combined);




		spriteBatch.begin();

		stateManager.draw();
		stateManager.update(Gdx.graphics.getDeltaTime());
		spriteBatch.end();


	}

    @Override
    public void dispose() {
        stateManager.dispose();
    }

}
