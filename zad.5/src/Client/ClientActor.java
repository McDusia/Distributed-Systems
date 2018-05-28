package Client;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class ClientActor extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        String bookstorePath = "akka.tcp://bookstore_system@127.0.0.1:3552/user/bookstoreActor";
        System.out.println(getSelf().path());
        return receiveBuilder()
                .match(Message.class, s -> {
                    String type = s.getType();
                    if(type.equals("search") | type.equals("order") | type.equals("stream")) {
                        getContext().actorSelection(bookstorePath).tell(s, getSelf());
                    } else if (type.equals("result")){
                        System.out.println(s.getMessage());
                    } else if (type.equals("stream")){
                        System.out.println(s.getMessage());
                    } else {
                        System.out.println(s.getMessage());
                    }
                })
                .matchAny(o -> {
                    log.info("received unknown message");
                    System.out.println(o);}
                )
                .build();
    }
}
