import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OrderActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private PrintWriter out;
    private Semaphore s;

    OrderActor(Semaphore s) {

        this.s = s;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    if(orderBook(s)) {
                        Message msg = new Message("result", "order submitted");
                        getSender().tell(msg, getSelf());
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


    private boolean orderBook(String bookTitle) throws Exception {
        s.down();
        try{
            FileWriter fw = new FileWriter("src\\orders", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out2 = new PrintWriter(bw);
            out2.println(bookTitle);
            bw.close();
            fw.close();
            System.out.println("Order submitted");
            return true;
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            s.up();
        }
       return false;
    }

}
