
public class Semaphore {
    private boolean busy;

    public Semaphore() {
        busy = false;
    }

    synchronized public void down() {
        while(busy) {
            try {
                wait();
            } catch(InterruptedException e) {}
        }
        busy = true;
        notify();
    }

    synchronized  public void up() {
        busy = false;
        notify();
    }

}
