import Client.Message;
import akka.Done;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.stream.scaladsl.Sink;
import akka.util.ByteString;
import scala.concurrent.duration.Duration;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class StreamActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    private final Materializer materializer = ActorMaterializer.create(context());

    ActorRef client;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, msg -> {
                    
                    String bookTitle = msg.getMessage();

                    log.info("stream request: "+bookTitle);
                    String path = "resources/Books/"+bookTitle;
                    final Path file = Paths.get(path);
                    msg.setClient(getSender());
                    client = getSender();

                    FileIO.fromPath(file)
                            .via(Framing.delimiter(ByteString.fromString("\n"), 1024))
                            .map(ByteString::utf8String)
                            .map((s -> {
                                 String r = (String) s;
                                 Message respond = new Message("respond",r);
                                 respond.setClient(client);
                                return respond;
                            }))
                            .throttle(1, Duration.create(1, TimeUnit.SECONDS), 1, ThrottleMode.shaping())
                            .runWith(Sink.actorRef(client, new Message("response", "The End")), materializer);


                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


}
