import java.io.FileWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *<b> Class Consumer </b>
 *<p>
 *     This Class implements the the Runnable interface to enable thread creation.
 *     In this class consumer is consuming the request from the shared buffer,
 *     one request at a time.
 *
 * </p>
 */

public class Consumer extends Thread{

    /**
     *<h2> Global variables </h2>
     *
     * @param server_root: it´s the server_root
     * @param counter_client: number of clients
     * @param sharedMemory: shared memory where the consumer can be communicated with the producer
     * @param int id: id of the consumer
     * @param fileWriter {@link FileWriter} object that enables writing on the file
     *
     */

    private Buffer sharedMemory;
    private int id;
    private String server_root;
    private FileWriter fileWriter;

    private boolean isWorking;

    /**
     * Constructor for Consumer
     * @param sharedMemory sharedMemory: shared memory where the consumer can comunicated with the producer
     * @param server_root default directory/path for server
     * @param fileWriter object that enables writing on the file
     */
    public Consumer(Buffer sharedMemory, String server_root, FileWriter fileWriter, int id){
        this.sharedMemory = sharedMemory;
        this.server_root = server_root;
        this.fileWriter = fileWriter;
        this.isWorking = false;
        this.id = id;

    }

    /**
     *
     * @return if its working
     */
    public boolean getIsWorking() {
        return isWorking;
    }

    /**
     *
     * @param working set private boolean to true or false
     */
    public void setWorking(boolean working) {
        this.isWorking = working;
    }

    /**
     * <h3> Funtions </h3>
     *
     * <p>
     *     public void printBuffer(): funtion where prints the content of the buffer
     * </p>
     *
     */

    private void printBuffer(){
        for(int i = 0; i < sharedMemory.getRequest().size(); i++) {
            System.out.println("********** printing buffer content **********");
            sharedMemory.get();
            System.out.println("*   " + sharedMemory.getRequest().get(i));
        }
    }

    /**
     * <p>
     *      public void run(): It´s created a linkedHashMap where gets the content of the sharedMemory.
     *      The variable client e fileName made a casting and gets
     *      the last element of the list.
     *
     *      After, it´s created different instance of ArrayList byte[] , ConsoleFileWriter,
     *      ShowPage and BinaryFile when it´s called .run() the instance initialize, in this case
     *      consoleFileWriter and binaryFile will be working at the same time and de consumer
     *      will deal with the requesrt . It´s implemeted a .join() to synchronize
     *
     * </p>
     */


    @Override
    public void run() {
        String fileName;
        Socket client;
        try{
            if(!sharedMemory.getRequest().isEmpty()){

               setWorking(true);
               LinkedHashMap<String, Socket> valuesOfTheList = sharedMemory.get();
               System.out.println("Teste");
               client = (Socket) (valuesOfTheList.values().toArray()[valuesOfTheList.size() - 1]);
               fileName = (String) (valuesOfTheList.keySet().toArray()[valuesOfTheList.size() - 1]);

               System.out.println("Consumer #" + this.id + " got " + fileName + " with the client: " + client);

               ArrayList<byte[]> content = new ArrayList<>();

               ConsoleFileWriter consoleFileWriter = new ConsoleFileWriter(client, fileName, fileWriter);
               ShowPage showPage = new ShowPage(client, fileName, server_root, content);
               BinaryFile binaryFile = new BinaryFile(server_root, fileName, content);

               consoleFileWriter.start();
               binaryFile.start();
               binaryFile.join();
               showPage.start();
               consoleFileWriter.join();
               showPage.join();

               setWorking(false);
            }
            else{
                System.out.println("Consumer " + id + " cannot consumer buffer is empty");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}