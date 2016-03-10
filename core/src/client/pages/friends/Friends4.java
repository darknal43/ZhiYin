package client.pages.friends;

import client.component.basicComponents.Button;
import client.component.basicComponents.Image;
import client.events.ActionEvent;
import client.pages.State;
import client.stateInterfaces.ActionMonitor;

public class Friends4 extends State implements ActionMonitor{

    //private ServiceList<> friends;



    public void init(){
        super.init();

        //friends = new ServiceList<>();

        Image background = new Image("Friends -4.png");
        background.setBounds(0, 0, 750, 1350);

        Button messageButton = new Button(this);
        messageButton.setBounds(0, 0, 650, 100);

        Button sendButton = new Button(this);
        sendButton.setBounds(650, 0, 100, 100);

        this.components.add(background);
        this.components.add(messageButton);
        this.components.add(sendButton);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}