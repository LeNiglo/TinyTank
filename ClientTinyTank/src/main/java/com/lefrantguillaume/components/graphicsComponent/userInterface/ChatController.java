package com.lefrantguillaume.components.graphicsComponent.userInterface;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.MessageChat;
import de.lessvoid.nifty.controls.ListBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 20/06/2015.
 */
public class ChatController {
    private List<Tuple<String, String, String>> messages;

    public ChatController(){
        this.messages = new ArrayList<>();
        this.messages.add(new Tuple<>("6416541", "toto", "salut"));
        this.messages.add(new Tuple<>("6416541", "dimi", "hi"));
        this.messages.add(new Tuple<>("6416541", "toto", "voila quoi ça marche"));
    }

    // FUNCTIONS
    public void addAllElement(ListBox list){
        if (list != null) {
            for (Tuple<String, String, String> values : messages) {
                list.addItem(values.getV2() + ": " + values.getV3());
            }
        } else {
            Debug.debug("list = null");
        }
    }

    public void addMessage(MessageChat message){
        this.messages.add(new Tuple<>(message.getId(), message.getPseudo(), message.getMessage()));
    }
}
