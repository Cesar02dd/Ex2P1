import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * /**
 *  *<b> Class ShowPage </b>
 *  *<p>
 *  *     This Class shows a client his requested file.
 *  * </p>
 *  */
public class ShowPage extends Thread{
    private String file_name;
    private String server_root;
    private ArrayList<byte[]> content;
    private Socket client;

    /**
     * Constructor for ShowPage
     * @param client {@link Socket} person accessing to a file currently
     * @param file_name accessed file
     * @param server_root default directory/path for server
     * @param content Has the content necessary to show the client
     */
    public ShowPage(Socket client,String file_name, String server_root, ArrayList<byte[]> content) {
        this.client = client;
        this.file_name = file_name;
        this.server_root = server_root;
        this.content = content;

    }
    private void showHTML(){
        try{
            String[] tokens = file_name.split(" ");
            String route = tokens[1];

            System.out.println(server_root+route);

            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
            clientOutput.write(("ContentType: text/html\r\n").getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write(content.get(0));
            clientOutput.write("\r\n\r\n".getBytes());
            clientOutput.flush();
            client.close();
        }
        catch (Exception e ){
            e.printStackTrace();
        }

    }

    public void run(){
        showHTML();
    }
}
