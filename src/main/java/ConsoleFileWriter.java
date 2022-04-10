import java.io.FileWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

/**
 *<b> Class ConsoleFileWriter </b>
 *<p>
 *     This Class will receive the console file in which the server
 *     writes each request information.
 * </p>
 */


public class ConsoleFileWriter extends Thread{

    /**
     *<h2> Global variables </h2>
     *
     * @param client {@link Socket} person accessing to a file currently
     * @param file_name accessed file
     * @param fileWriter {@link FileWriter} object that enables writing on the file
     * @param lock {@link Lock} object lock that prevents anyone from writing on the file
     *
     */

    private Socket client;
    private String file_name;
    private FileWriter fileWriter;
    private final Lock lock = new ReentrantLock();

    /**
     * Constructor for ConsoleFileWriter
     * @param client  {@link Socket} person accessing to a file currently
     * @param file_name accessed file
     * @param fileWriter {@link FileWriter} object that enables writing on the file
     */
    public ConsoleFileWriter(Socket client, String file_name, FileWriter fileWriter) {
        this.client = client;
        this.file_name = file_name;
        this.fileWriter = fileWriter;
    }

    private void write(){
        lock.lock();
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String[] tokens_log = file_name.split(" ");
            fileWriter.write(dtf.format(now) + "-Method:" +tokens_log[0]+ "-Route:"+tokens_log[1] + "-" + client.getInetAddress() + "\r\n" );
            fileWriter.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void run(){
        write();
    }

}
