import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <b> Class MainConsumer </b>
 */

public class MainConsumer implements Runnable {

    /**
     * Global variables
     *
     *  @param consumers arraylist that contains each {@link Consumer}.
     *  @param server_root default directory/path for server.
     *  @param FileWriter {@link FileWriter} object that enables writing on the file
     *  @param buffer {@link Buffer} shared memory where the consumer can comunicated with the producer.
     *  @param lock {@link Lock} object lock that prevents anyone from writing on the file
     *
     */
    private ArrayList<Consumer> consumers;
    private String server_root;
    private FileWriter fileWriter;
    private Buffer buffer;
    private final Lock lock = new ReentrantLock();
    private static int nConsumersCreated = 0;

    /**
     *
     * @param server_root default directory/path for server.
     * @param fileWriter object that enables writing on the file.
     */
    public MainConsumer(String server_root, FileWriter fileWriter) {
        this.fileWriter = fileWriter;
        this.server_root = server_root;
        this.buffer = buffer;
        this.consumers = new ArrayList<>();

    }

    /**
     *
     * @return gets the {@link Consumer} Arraylist
     */
    public ArrayList<Consumer> getConsumers() {
        return consumers;
    }

    /**
     *
     * @param buffer sets this buffer to the designed one
     */
    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     *
     */
    public void listenRequest(){
        boolean flag = true;
        while (flag) {
            for (Consumer c : consumers) {
                if (!c.getIsWorking()) {
                    c.start();
                    consumers.remove(c);
                    createConsumers(1);
                    flag = false;
                    break;
                }
            }
        }
    }

    /**
     *
     * @param nConsumers Creates a number of {@link Consumer} threads.
     */
    public void createConsumers(int nConsumers){
        for (int i = 0; i < nConsumers; i++){
            consumers.add(new Consumer(buffer, server_root, fileWriter, nConsumersCreated));
            nConsumersCreated++;
        }
    }

    @Override
    public void run() {
        lock.lock();
        try{
            listenRequest();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
