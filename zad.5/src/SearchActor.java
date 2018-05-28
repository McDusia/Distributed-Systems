

import Client.Message;
import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.resume;
import static akka.pattern.PatternsCS.ask;


public class SearchActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef client;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, s -> {

                    String type = s.getType();
                    String msg = s.getMessage();

                    if(type.equals("result")) {
                        if(!msg.contains("Failed")){
                            ActorRef client  = s.getClient();
                            client.tell(s, getSelf()); //send respond to client
                        }
                    } else {
                        Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));
                        client = getSender();
                        s.setClient(client);
                        CompletableFuture<Object> future1 =
                                ask(context().child("worker_1").get(), s, t).toCompletableFuture();

                        CompletableFuture<Object> future2 =
                                ask(context().child("worker_2").get(), s, t).toCompletableFuture();

                        CompletableFuture<String> transformed =
                                CompletableFuture.allOf(future1, future2)
                                .thenApply(v -> {
                                    Message r1 = (Message) future1.join();
                                    Message r2 = (Message) future2.join();

                                    if(r1.getMessage().contains("Failed") && r2.getMessage().contains("Failed")){
                                        ActorRef client = r1.getClient();
                                        Message respond = new Message("Failure", "No such book");
                                        client.tell(respond, getSelf());
                                    }
                                    return "";
                                })
                                .exceptionally(v -> {
                                    System.out.println("Exceptionally");
                                    if(!future1.isCompletedExceptionally()) {
                                        Message r1 = (Message) future1.join();
                                        String r = r1.getMessage();
                                        if(r.contains("Failed")){
                                            Message respond = new Message("Failure", "Problem with database");
                                            client.tell(respond, getSelf());
                                        }
                                    }
                                    else if(!future2.isCompletedExceptionally()) {
                                        Message r1 = (Message) future2.join();
                                        String r = r1.getMessage();
                                        if(r.contains("Failed")){
                                            Message respond = new Message("Failure", "Problem with database");
                                            client.tell(respond, getSelf());
                                        }
                                    }
                                    if(future1.isCompletedExceptionally() && future2.isCompletedExceptionally()){
                                        Message respond = new Message("Failure", "Problem with database");
                                        client.tell(respond, getSelf());
                                    }
                                    return "";
                                });
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    @Override
    public void preStart() throws Exception {
        String path1 = "resources\\DB_1";
        String path2 = "resources\\DB_2";
        context().actorOf(Props.create(SearchWorker.class, path1), "worker_1");
        context().actorOf(Props.create(SearchWorker.class, path2), "worker_2");
    }

    private static SupervisorStrategy oneForOneStrategy
            = new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.
            matchAny(o -> stop()).
            build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return oneForOneStrategy;
    }


}
