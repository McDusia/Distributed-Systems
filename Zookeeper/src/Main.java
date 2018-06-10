import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Scanner;

import com.google.common.collect.Ordering;

public class Main implements Watcher, AutoCloseable {

    private static final String znode = "/znode_testowy";
    private final String connectionString;
    private final String[] exec;
    private final ZooKeeper zk;
    private ProgramManager programManager;

    public Main(String connectionString, String[] exec) throws IOException {
        this.connectionString = connectionString;
        this.exec = exec;
        this.zk = new ZooKeeper(connectionString, 3000, this);
        this.programManager = new ProgramManager(exec);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err
                    .println("USAGE: Executor connectionString program [args ...]");
            System.exit(2);
        }
        String connectionString = args[0];
        String exec[] = new String[args.length - 1];
        System.arraycopy(args, 1, exec, 0, exec.length);
        try (Main main = new Main(connectionString, exec)) {
            main.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void run() throws KeeperException, InterruptedException {
        addWatcher();
        Scanner scanner = new Scanner(System.in);
        while (!Thread.interrupted()) {
            System.out.print("$");
            String cmd = scanner.nextLine();
            if (cmd.isEmpty()) {
                continue;
            }
            if (cmd.startsWith("ls")) {
                ls();
            } else {
                System.out.println("Hit 'ls' to print zNode tree");
            }
        }
    }

    private void addWatcher() throws KeeperException, InterruptedException {
        //we have to get data to add watcher
        try {
            zk.getChildren(znode, true);
        } catch (KeeperException.NoNodeException e) {
            zk.exists(znode, true);
        }
    }

    void ls() {
        visitChildren(znode, (znode, tabsQnt) -> {
            while(tabsQnt>0){
                System.out.print("    ");
                tabsQnt--;
            }
            System.out.println(znode);
        }, 0);
    }

    private void visitChildren(String znode, ChildVisitor childVisitor, int tabsQnt) {
        childVisitor.visit(znode, tabsQnt);
        try {
            zk.getChildren(znode, false).stream().sorted(Ordering.natural()).
                    forEach((z) -> {
                        String path = znode + "/" + z;
                        visitChildren(path, childVisitor, tabsQnt+1);
                    });
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            System.out.println(watchedEvent);
            if(!znode.equals(watchedEvent.getPath())){
                addWatcher();
                return;
            }
            switch (watchedEvent.getType()) {
                case NodeCreated:
                    programManager.start();
                    zk.getChildren(znode, true);
                    break;
                case NodeDeleted :
                    programManager.stop();
                    zk.exists(znode, true);
                    break;
                case NodeChildrenChanged:
                    int childrenQnt = zk.getChildren(znode, true).size();
                    System.out.println("Children quantity changed, current quantity: " + childrenQnt);
                    break;
                default: addWatcher();
                    break;
            }
        } catch (KeeperException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        programManager.close();
        zk.close();
    }
}
