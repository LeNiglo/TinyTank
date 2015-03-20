package com.lefrantguillaume.networkComponent;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 12/03/2015.
 */
public class NetworkMessage extends Observable {
    private List<MessageModel> messages;

    public NetworkMessage() {
        this.messages = new ArrayList<MessageModel>();
    }

    public boolean add(MessageModel item) {
        this.messages.add(item);
        setChanged();
        notifyObservers(true);
        return true;
    }

    public List<MessageModel> getMessages() {
        return this.messages;
    }

    public int size() {
        return this.messages.size();
    }

    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

    public MessageModel get(int index) {
        return this.messages.get(index);
    }

    public void remove(int index) {
        this.messages.remove(index);
    }
}
