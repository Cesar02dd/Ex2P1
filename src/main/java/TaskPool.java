import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *<b> Class TaskPool </b>
 *<p>
 *     First Method used to handle client requests on a server
 * </p>
 */

public class TaskPool extends Thread{

    private Socket client;
    private ServerSocket server;
    private String server_root;
    private int counter_client;
    private FileWriter fileWriter;
    private String file_name;

    /**
     * Constructor for TaskPool
     * @param client {@link Socket} person accessing to a file currently
     * @param server {@link ServerSocket} server socket
     * @param server_root default directory/path for server
     * @param counter_client number of clients currently accessing a file
     * @param fileWriter {@link FileWriter} object that enables writing on the file
     * @param file_name file requested
     */
    public TaskPool(Socket client, ServerSocket server, String server_root, int counter_client, FileWriter fileWriter, String file_name) {
        this.client = client;
        this.server = server;
        this.server_root = server_root;
        this.counter_client = counter_client;
        this.fileWriter = fileWriter;
        this.file_name = file_name;
    }

    private void instructions(){
        try{
            ArrayList<byte[]> content = new ArrayList<>();
            ConsoleFileWriter consoleFileWriter = new ConsoleFileWriter(client, file_name,fileWriter);
            BinaryFile readBinaryFile = new BinaryFile(server_root, file_name, content);
            ShowPage showHTML = new ShowPage(client, file_name,server_root,content);

            consoleFileWriter.run();
            readBinaryFile.run();
            readBinaryFile.join();
            showHTML.run();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void run(){
        instructions();
    }
}
