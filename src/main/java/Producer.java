import java.net.Socket;

/**
 *<b> Class Producer </b>
 *<p>
 *     This Class implements the the Runnable interface to enable thread creation.
 *     In this class producers produces the request, where it´s puts into
 *     the buffer (sharedMemory between consumer and producer),
 *     and keeps repeating this process as long as it´s necessary.
 *
 * </p>
 */

public class Producer extends Thread {

    /**
     * Global variables & Constructor
     *
     * @param String fileName:
     * @param Socket client: person accessing to a file currently
     * @param Buffer sharedMemory: shared memory where the producer can comunicated with the consumer.
     * @param int idProducer: id of the producer.
     *
     */

    private String fileName;
    private Socket client;
    private Buffer sharedMemory;
    private int id;

    /**
     *  Constructor for Producer
     * @param sharedMemory {@link Buffer} shared memory where the producer can comunicated with the consumer.
     * @param idProducer number of produced requests
     * @param fileName file requested
     * @param client {@link Socket} person accessing to a file currently
     */
    public Producer (Buffer sharedMemory, int idProducer, String fileName, Socket client){
        this.sharedMemory = sharedMemory;
        this.id = idProducer;
        this.client = client;
        this.fileName = fileName;
    }

    /**
     * Functions
     *
     * <p>
     *     public void run(): producer produces the request, where it´s puts into
     *     the buffer (sharedMemory between consumer and producer),
     *     and keeps repeating this process as long as it´s necessary.
     * </p>
     *
     */

    @Override
    public void run() {
        sharedMemory.put(fileName, client);
        System.out.println("Producer #" + this.id + " put: " + fileName + " with the client: " + client);
            try {
                sleep((int)(Math.random() * 100));
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
}