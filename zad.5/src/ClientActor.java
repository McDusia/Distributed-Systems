import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class ClientActor extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        String bookstorePath = "akka.tcp://bookstore_system@127.0.0.1:3552/user/bookstoreActor";
        System.out.println(getSelf().path());
        return receiveBuilder()
                .match(Message.class, s -> {
                    Message msg = s;
                    String type = s.getType();
                    String bookTitle = s.getMessage();
                    System.out.println(type+" "+bookTitle);
                    if(type.equals("search") | type.equals("order") | type.equals("stream")) {
                        System.out.println("clientActor received: "+ s);
                        getContext().actorSelection(bookstorePath).tell(msg, getSelf());
                    } else {
                        System.out.println("clientActor received: "+ s);
                    }

                })
                .matchAny(o -> {
                    log.info("received unknown message");}
                )
                .build();
    }
}
