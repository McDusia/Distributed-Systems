

import Client.Message;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;


import java.io.*;

public class SearchWorker extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private File file;
    private String path;

    SearchWorker(String path) {
        this.path = path;
        this.file = new File(path);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, s -> {
                    String bookTitle = s.getMessage();
                    String result = search(bookTitle);
                    //------ send back to parent
                    ActorRef client = s.getClient();
                    Message msg = new Message("result", result);
                    msg.setClient(client);

                    if(!result.contains("Exception")){
                        getContext().parent().forward(msg, getContext());
                        getSender().tell(msg, getSelf());
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private String search(String request){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null) {
                if(line.startsWith("\""+request)) {
                    return line;
                }
            } return "Failed";
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound ex");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("IOException ex");
            e.printStackTrace();
        }
        return "Exception";
    }
}
