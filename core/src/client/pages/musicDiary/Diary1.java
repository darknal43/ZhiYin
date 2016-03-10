package client.pages.musicDiary;

import client.component.basicComponents.Image;
import client.events.ActionEvent;
import client.pages.State;
import driver.GameLoop;

/**
 * This is the first music diary page as given in the
 * art assets folder.
 *
 * Created by Hongyu Wang on 3/9/2016.
 */
public class Diary1 extends State {
    public void init() {
        super.init();
        Image background = new Image("Diary - 1.png");
        background.setBounds(0, 0, GameLoop.WIDTH, GameLoop.HEIGHT);
        components.add(background);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void dispose() {

    }



    @Override
    public void update(float dt) {

    }
}
