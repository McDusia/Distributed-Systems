package Client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Client {

    public static void main(String[] args) throws Exception {

        // config
        File configFile = new File("client.conf");
        Config config = ConfigFactory.parseFile(configFile);

        // create actor system & actors
        final ActorSystem system = ActorSystem.create("client_system", config);
        final ActorRef clientActor = system.actorOf(Props.create(ClientActor.class), "clientActor");

        // interaction
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Started. Commands: 'search <bookTitle>', 'order <bookTitle>', 'stream <bookTitle>'");

        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }
            String type = line.substring(0, line.indexOf(' '));
            String bookTitle = line.substring(line.indexOf(' ') + 1);
            Message msg = new Message(type, bookTitle);
            clientActor.tell(msg, null);
        }

        system.terminate();
    }

}
