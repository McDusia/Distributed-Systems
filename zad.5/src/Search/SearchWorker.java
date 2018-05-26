package Search;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.*;

public class SearchWorker extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private File file;
    private String path;

    SearchWorker(String path) {
        this.path = path;
        this.file = new File(path);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    System.out.println("Search.SearchWorker "+ s);
                    String result = search(s);
                    System.out.println("Result in worker"+ result);
                    //odesłanie do rodzica
                    getContext().parent().forward("result"+result, getContext());

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private String search(String request){
        System.out.println("search in Search.SearchWorker");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null) {
                if(line.startsWith("\""+request)) {
                    return line;
                }
            } return "Failed";
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound ex");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("IOException ex");
            e.printStackTrace();
        }
        return "Failed";
    }
}
