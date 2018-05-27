import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StreamActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private PrintWriter out;

    StreamActor() {
        try{
            FileWriter fw = new FileWriter("src\\orders", true);
            BufferedWriter bw = new BufferedWriter(fw);
            this.out = new PrintWriter(bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    if(orderBook(s)) {
                        getSender().tell("order submitted", getSelf());
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


    private boolean orderBook(String bookTitle) throws Exception {
        out.println(bookTitle);
        return true;
    }
}
