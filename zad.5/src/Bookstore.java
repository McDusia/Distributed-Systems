import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Bookstore {

    public static void main(String[] args) throws Exception {

        File configFile = new File("server.conf");
        Config config = ConfigFactory.parseFile(configFile);
        // create actor system & actors
        final ActorSystem system = ActorSystem.create("bookstore_system", config);
        final ActorRef bookstoreActor = system.actorOf(Props.create(BookstoreActor.class), "bookstoreActor");

        System.out.println("------ Started. enter 'q' to quit ------");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }
        }

        // finish
        system.terminate();
    }
}
