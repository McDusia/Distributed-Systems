import org.slf4j.Logger;
import java.io.IOException;
import static org.slf4j.LoggerFactory.getLogger;

public class ProgramManager implements AutoCloseable{

    private final String[] exec;
    private Process process = null;
    private static final Logger LOG = getLogger(ProgramManager.class);

    ProgramManager(String[] exec){
        this.exec = exec;
    }

    void start() throws IOException {
        LOG.info("Starting program");
        if(process != null){
            stop();
        }
        process = Runtime.getRuntime().exec(exec);
    }

    void stop(){
        LOG.info("Stop program");
        if(process != null){
            if(process.isAlive()){
                process.destroy();
            }
            process = null;
        }
    }

    @Override
    public void close() throws Exception {
        stop();
    }
}
