import java.io.*;
import java.util.concurrent.Callable;


public class DB_Reader implements Callable<String>{
    private File file;
    private String path;
    private String request;

    DB_Reader(String path, String request) {
        this.path = path;
        this.file = new File(path);
        this.request = request;
    }

    @Override
    public String call() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null) {
                System.out.println(path +": "+ line);
                if(line.startsWith("\""+request)) {
                    return "Founded " + line;
                }
            }
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
