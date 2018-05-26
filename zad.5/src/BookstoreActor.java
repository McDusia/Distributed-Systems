import Search.SearchActor;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BookstoreActor  extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        String path = "akka.tcp://bookstore_system@127.0.0.1:3552/user/bookstoreActor";
        String clientPath = "akka.tcp://client_system@127.0.0.1:2552/user/clientActor";
        return receiveBuilder()
                .match(Message.class, s -> {
                    Message msg = s;
                    String type = s.getType();
                    String bookTitle = s.getMessage();

                    if(type.equals("result")) {
                        getContext().actorSelection(clientPath).tell(s, getSelf());
                    }
                    else if(type.equals("search")){
                        System.out.println("TU");
                        context().child("search").get().forward(bookTitle, getContext());
                    }
                    else if(type.equals("order")){
                        context().child("order").get().tell(bookTitle, getSelf());
                    }
                    else if(type.equals("stream")){
                        context().child("stream").get().tell(bookTitle, getSelf());
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

}
