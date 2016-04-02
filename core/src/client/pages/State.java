package client.pages;

import client.component.basicComponents.Button;
import client.events.ActionEvent;
import client.events.executables.internalChanges.updatePageExecutables.ExecuteChangePage;
import client.internalExceptions.NoExecutableException;
import client.pageStorage.Pages;
import client.pages.pageInternal.inputController.InputController;
import client.singletons.MainBatch;
import client.singletons.StateManager;
import client.stateInterfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tools.utilities.Utils;

import java.util.List;
import java.util.Map;

/**
 * This is the superclass of all States.
 *
 * All pages should extend this.
 */
public abstract class State implements Updatable, Drawable, Disposable, ActionMonitor {
    public static final String SHELLINPUT = "shell";
    public static final String MOVEABLEINPUT = "moveable";
    protected Stage stage;



    @Override
    public void update(float dt) {
        stage.act();
    }


    /**
     * This clears the stage of all actors. The main purpose of this
     * is for the input multiplexer to work properly.
     */
    public void clearStage(){
        stage.clear();
    }

    private Map<String, InputController> controllers;



    /**
     * This is the serviceList will store all components
     * inside the given state.
     */
    private List<Actor> components;

    /**
     * This method will initialize all values as required within state
     */
    protected void init(){
        components = Utils.newList();
        controllers = Utils.newMap();
        controllers.put(SHELLINPUT, new InputController());
        controllers.put(MOVEABLEINPUT, new InputController());
        stage = new Stage();

    }

    /**
     * This method will add a component to the components.
     *
     * This will also add all Performables to the performable list in the static
     * input controller.
     * @param c the component to be added
     */
    public void add(Actor c){
        if (c instanceof Performable)
            controllers.get(SHELLINPUT).add((Performable) c);
        components.add(c);
    }

    /**
     * NO ONE ELSE EVER USE THIS METHOD PLSPSLSPLSLSLPSPLSPLPSPLSSPLSPLSLPSL
     * THIS METHOD IS CALLED. ULTIMATE
     * SPAGETTHI.
     * NEVER EVER EVER EVER EVER EVER USE THIS!!!!!!!!!!!!!!!!!!!!!!
     * OK? never ever ever ever ever ever everever ever everever ever everever ever everever ever everever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * ever ever ever
     * Use this fucking method.
     * @param c thing.
     */
    @Deprecated
    public void remove(Performable c){
        components.remove(c);
        controllers.get(SHELLINPUT).remove(c);
    }
    /**
     * Create and add the buttons for the bottom bar.
     */
    protected void setBottomBar(){
        Button homeButton = new Button(this);
        homeButton.setBounds(0, 0, 210, 117);
        homeButton.setExecutable(new ExecuteChangePage(Pages.HOME));
        add(homeButton);

        Button diaryButton = new Button(this);
        diaryButton.setBounds(210, 0, 180, 117);
        diaryButton.setExecutable(new ExecuteChangePage(Pages.DIARY1));
        add(diaryButton);

        Button friendsButton = new Button(this);
        friendsButton.setBounds(390, 0, 160, 117);
        friendsButton.setExecutable(new ExecuteChangePage(Pages.FRIENDS1));
        add(friendsButton);

        Button toolsButton = new Button(this);
        toolsButton.setBounds(550, 0, 200, 117);
        toolsButton.setExecutable(new ExecuteChangePage(Pages.MYPROFILE));
        add(toolsButton);
    }

    protected ImageButton createImageButton(String imagePath, Executable e, int x, int y, int width, int height ){
        Image image = new Image(new Texture(imagePath + StateManager.M + ".png"));
        ImageButton imageButton = new ImageButton(image.getDrawable());
        imageButton.setBounds(x * StateManager.M, y * StateManager.M, width * StateManager.M, height * StateManager.M);
        final Executable executable = e;
        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                executable.execute();
            }
        });

        return imageButton;
    }

    protected void addImageButton(String imagePath, Executable e, int x, int y, int width, int height){
        stage.addActor(createImageButton(imagePath, e, x, y, width, height));
    }

    /**
     * This will return a deep copy of the stored component list
     * @return A deep copy of the components.
     */
    public List<Actor> getComponents(){

        List<Actor> deepCopy = Utils.newList();
        for (Actor i : components){
            deepCopy.add(i);
        }

        return deepCopy;
    }

    /**
     * This method will draw everything.
     */
    public void draw(){
        if (stage.getActors().size != 0) {
            stage.draw();
        }
        for (Actor actor : components){
            actor.draw(MainBatch.getInstance(), 1);
        }
    }



    @Override
    public void actionPerformed(ActionEvent e){
        try {
            e.getSource().getExecutable().execute();
        } catch (NoExecutableException ex){
            System.out.println(ex.getMessage());
        }
    }

    public abstract void reset();


    public InputController getInputController(String FIXEDINPUT){
        return controllers.get(FIXEDINPUT);
    }



    public Stage getStage() {
        return stage;
    }




}

