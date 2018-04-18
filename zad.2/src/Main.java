
import java.io.*;

public class Main {

    public static void main(String[] args) {

        BufferedReader br = null;
        DistributedMap map = new DistributedMap("channel_0");
        map.put("giraffe", "George");
        //map.put("bird", "Mike");

        System.out.println("Map state at the beginning: \n" + map.toString());

        try {
            Thread.sleep(3600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Enter something : \n");
                String input = br.readLine();
                if ("q".equals(input)) {
                    System.out.println("Exit!");
                    System.exit(0);
                }
                System.out.println("-----------\n");

                MessageParser parser = new MessageParser();
                parser.parseOperation(input.split(" "), map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
