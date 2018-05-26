package Search;

import Search.SearchWorker;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class SearchActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {

                   if(s.startsWith("result")) {
                       System.out.println("Result in Search.SearchActor");
                       if(!s.contains("Failed")){
                           getSender().tell(s, getSelf()); //odesłanie odpowiedzi do klienta?
                       }
                    } else {

                        System.out.println("Search.SearchActor "+ s);
                        context().child("worker_1").get().forward(s, getContext());   //zlecenie zadania
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
