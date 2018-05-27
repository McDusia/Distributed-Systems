package Client;

import akka.actor.ActorRef;

import java.io.Serializable;

public class Message implements Serializable {
    private String type;
    private String message;
    private ActorRef client;

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClient(ActorRef client) {
        this.client = client;
    }

    public ActorRef getClient() {
        return client;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

}
