package client.events.executables.internalChanges.conversation;

import client.pages.friends.Friends2;
import client.pages.friends.boxes.MessageBox;
import client.pages.pageInternal.modelStorage.ModelStorage;
import client.pages.pageInternal.modelStorage.ModelStorageFactory;
import client.stateInterfaces.Executable;
import client.stateInterfaces.Pushable;
import server.model.media.MText;
import server.model.social.MConversation;
import server.model.social.MMessage;
import server.services.factories.MessageManagerFactory;
import server.services.factories.TextManagerFactory;

import java.util.List;

/**
 * Created by Kevin Zheng on 2016-03-24.
 */
public class ExecuteSendMessage implements Executable{
    private Friends2 friend2;

    public ExecuteSendMessage(Friends2 friend2){
        this.friend2 = friend2;
    }

    @Override
    public void execute() {
        ModelStorage ms = ModelStorageFactory.createModelStorage();

        MConversation conversation = ms.getModel(friend2.getConversation());

        if(conversation == null){
            ms.requestModelFromServer(friend2.getConversation());
            return;
        }

        String userText = friend2.getMessage();

        friend2.addMessage(new MessageBox(userText, 1));
    }
}
