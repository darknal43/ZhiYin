package client.events.executables.internalChanges.conversation;

import client.pages.friends.Friends2;
import client.pages.friends.boxes.MessageBox;
import tools.serverTools.databases.LocalDatabase;
import tools.serverTools.databases.LocalDatabaseFactory;
import client.stateInterfaces.Executable;
import server.model.media.MText;
import server.model.social.MConversation;
import server.model.social.MMessage;
import server.services.factories.MessageManagerFactory;
import server.services.factories.TextManagerFactory;

import java.util.List;

/**
 * Created by Kevin Zheng on 2016-03-24.
 */
public class ExecuteSendMessage implements Executable {
    private Friends2 friend2;

    public ExecuteSendMessage(Friends2 friend2){
        this.friend2 = friend2;
    }

    @Override
    public void execute() {
        LocalDatabase localDatabase = LocalDatabaseFactory.createModelStorage();

        MConversation conversation = localDatabase.getModel(friend2.getConversation());

        if(conversation == null){
            localDatabase.requestModelFromServer(friend2.getConversation());
            return;
        }

        List<Long> messageKeys = conversation.getMessageList();

        String userText = friend2.getMessage();

        MText text = TextManagerFactory.createTextManager().createText(userText, 0);

        MMessage message = MessageManagerFactory.createMessageManager().createMessage(text.getKey(), System.currentTimeMillis(), localDatabase.getMainUser().getKey());

        messageKeys.add(message.getKey());

        friend2.addMessage(new MessageBox(userText, 1));

        friend2.getMessageKeys().add(message.getKey());

        localDatabase.pushModel(text);
        localDatabase.pushModel(message);
        localDatabase.pushModel(conversation);
    }
}
