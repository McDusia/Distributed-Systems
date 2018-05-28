
import Client.Message;
import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;

public class BookstoreActor  extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        String clientPath = "akka.tcp://client_system@127.0.0.1:2552/user/clientActor";
        return receiveBuilder()
                .match(Message.class, s -> {
                    String type = s.getType();
                    String bookTitle = s.getMessage();

                    if(type.equals("result")) {
                        getContext().actorSelection(clientPath).tell(s, getSelf());
                    }
                    else if(type.equals("search")){
                        System.out.println("search request");
                        context().child("search").get().forward(s, getContext());
                    }
                    else if(type.equals("order")){
                        System.out.println("order request");
                        context().child("order").get().forward(bookTitle, getContext());
                    }
                    else if(type.equals("stream")){
                        System.out.println("stream request in Bookstore");

                        context().child("stream").get().forward(s, getContext());
                    }
                })
                .matchAny(o -> {
                    log.info("received unknown message");

                })
                .build();
    }

    @Override
    public void preStart() throws Exception {

        Props props3 = Props.create(OrderActor.class, new Semaphore());
        context().actorOf(Props.create(SearchActor.class), "search");
        context().actorOf(props3, "order");
        context().actorOf(Props.create(StreamActor.class), "stream");
    }

    private static SupervisorStrategy oneForOneStrategy
            = new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.
            matchAny(o -> resume()).
            build());
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return oneForOneStrategy;
    }

}
