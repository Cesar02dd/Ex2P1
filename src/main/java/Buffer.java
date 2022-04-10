import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.concurrent.Semaphore;

/**
 *<b> Class Buffer </b>
 *<p>
 *     This Class implements the bounded buffer using shared memory between producer and consumer
 *     In this shared memory, the requests of the clients will be treated.
 *     Both the consumer and the producer will have their respective method to get and put the request
 *
 * </p>
 */

public class Buffer {

    /**
     * Global variables
     *
     *  @param request Ordered list of clients request.
     *  @param semaphoreConsumer variable to control synchronization.
     *  @param semaphoreProducer variable to control synchronization.
     *
     *  <p>
     *     Note: It uses two semaphores to regulate the producer and consumer threads,
     *     ensuring that each call to put() is followed by a corresponding call get().
     *     At the beginning the semaphore of the producer is 1 so, producer can get the permit to produce
     *     And the semaphore of the consumer created is 0 so, that consumer could wait for permit to consume.
     *  </p>
     */

    private final LinkedHashMap<String, Socket> request = new LinkedHashMap<>();
    Semaphore semaphoreConsumer = new Semaphore(0);
    Semaphore semaphoreProducer = new Semaphore(1);
    private final MainConsumer mainConsumer;

    /**
     * Constructor for Buffer
     * @param mainConsumer receives the {@link MainConsumer} thread
     */
    public Buffer (MainConsumer mainConsumer) {
        this.mainConsumer = mainConsumer;

    }

    /**
     * Gets the LinkedHashMap
     * @return LinkedHashMap with current its current values
     */
    public LinkedHashMap<String, Socket> getRequest() {
        return request;
    }

    /**
     * Functions
     * <p>
     *     public LinkedHashMap (String, socket) get(): It´s created a linkedHashMap where
     *     the client request will be save there. With the semaphoreConsumer.acquire()
     *     Consumer will get permit and start consuming, after consuming it calls semaphoreProducer.release().
     *     So, that producer could get the permit to produce.
     *
     *     In the finally, the variable client e fileName made a casting and gets
     *     the last element of the list. client e fileName made a casting
     *     because the LinkedHashMap receives a key-value in this case is String-Socket.

     * </p>
     * @return The LinkedHashMap with its current values
     */

    public LinkedHashMap<String, Socket> get(){
        String fileName;
        Socket client;
        LinkedHashMap<String, Socket> listOfValues = new LinkedHashMap<>();

        try{
            semaphoreConsumer.acquire();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            if(this.request.size() != 0){
                client = (Socket) (this.request.values().toArray()[this.request.size() - 1]);
                fileName = (String) (this.request.keySet().toArray()[this.request.size() - 1]);
                this.request.remove(this.request.keySet().toArray()[this.request.size() -1]);

                System.out.println("Got: " + this.request);
                listOfValues.put(fileName, client);
            }

            semaphoreProducer.release();

        }
        return listOfValues;
    }

    /**
     * <p>
     *      void put (String request, Socket client): Producer gets permit by calling semaphoreProducer.acquire()
     *      and starts producing, after producing it calls semaphoreConsumer.release().
     *      So, that consumer could get the  permit to consume.
     * </p>
     *
     *      @param request request of the client
     *      @param client client who enter in the website
     */

    public void put (String request, Socket client){
        try{
            semaphoreProducer.acquire();

        } catch (InterruptedException e){
            e.printStackTrace();
        }

        this.request.put(request, client);
        System.out.println("Buffer" + this.request.values());
        System.out.println("Put: " + request);
        semaphoreConsumer.release();
        notifyThreads();
    }

    /**
     * Runs the {@link MainConsumer} thread
     */
    public void notifyThreads(){
        mainConsumer.run();
    }
}
