package Model;

import java.util.ArrayList;
import java.util.UUID;

public class Chat {
    String id;
    ArrayList<String> userNames;
    ArrayList<String> messages;
    public Chat(){
        id= UUID.randomUUID().toString();
        userNames= new ArrayList<>();
        messages= new ArrayList<>();
    }

    public Chat(String id, ArrayList<String> userNames, ArrayList<String> messages) {
        this.id = id;
        this.userNames = userNames;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }
}
