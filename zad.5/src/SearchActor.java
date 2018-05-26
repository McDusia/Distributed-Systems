

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.concurrent.CompletableFuture;


public class SearchActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, s -> {
                    String type = s.getType();
                    String msg = s.getMessage();
                    if(type.equals("result")) {
                        if(!msg.contains("Failed")){
                            getSender().tell(s, getSelf()); //send respond to client
                        }
                    } else {
                        //CompletableFuture<Message> future1 = ask(context().child("worker_1").get())
                        context().child("worker_1").get().forward(s, getContext());   //commission to workers
                        context().child("worker_2").get().forward(s, getContext());
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    @Override
    public void preStart() throws Exception {
        String path1 = "src\\DB_1";
        String path2 = "src\\DB_2";
        context().actorOf(Props.create(SearchWorker.class, path1), "worker_1");
        context().actorOf(Props.create(SearchWorker.class, path2), "worker_2");
    }




}
