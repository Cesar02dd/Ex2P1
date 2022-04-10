import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <b> Class MainHTTPServer Thread </b>
 */

public class MainHTTPServerThread extends Thread {


    /**
     *<h2> Global variables </h2>
     *
     * @param server
     * @param client each client accessing the server
     * @param port port in which the server listens
     * @param counter_client number of people accessing the server
     *
     */


    private DataInputStream in;
    private ServerSocket server;
    private Socket client;
    private int port;
    private static int counter_client = 0;
    private int option;

    /**
     * Constrcutor for MainHTTPThread
     * @param port port in which the server listens
     * @param option determines the method that will resolver Server-Client requests handling
     */
    public MainHTTPServerThread(int port, int option) {
        this.port = port;
        this.option = option;
    }


    /**
     * Reads an HTML documents and returns it as string
     *
     * @param path  path of the file
     * @return  String with the html document at <code>path</code>
     */

    public String readFile (String path) {
        System.out.println( ">>> Reading the file" );
        File original = new File( path);
        Scanner reader = null;
        String content = "";
        try {
            reader = new Scanner( original );
            while ( reader.hasNextLine( ) ) {
                String input = reader.nextLine( );
                if ( content.isEmpty( ) ) {
                    content = input;
                } else {
                    content = content + "\n" + input;
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
            return "";
        }
        System.out.println( ">>> Done reading the file" );
        return content;
    }


    /**
     * <b>Pa-web-server</b>
     * <p>
     * Main cycle of the server, it creates the {@link ServerSocket} at the specified port,
     * and then it creates a new {@link Socket}
     * for each new request
     *
     * A new object type was introduced to receive the configurations of the server: {@link Properties}
     * which makes it possible to receive each configuration by keywords and apply them to the {@link ServerSocket}
     * mentioned above.
     *
     * A {@link FileWriter} object was also created so that the server would be able to write each new
     * request on the console.log file.
     *
     * For this project 2 methods of handling incoming requests were implemented :
     * <ul>
     *     <li>
     *          Task-Pool Method {@link TaskPool}
     *     </li>
     *     <li>
     *         Producer-Consumer Method: a {@link Producer} , a {@link Consumer} and a {@link Buffer}
     *     </li>
     * </ul>
     *
     * <p>
     * To refactor with:
     * </p>
     * <ul>
     *     <li>loading the server configurations from the server.config file</li>
     *     <li>Introduce parallelism to handle the requests</li>
     *     <li>Introduce parallelism to handle the documents</li>
     *     <li>Parse the request according as necessary for the implementation</li>
     *     <li>...</li>
     * </ul>
     *
     */
    @Override
    public void run(){
        try {
//            String server_root = "server"; // codigo ja submetido sem config file - defines the server root
//            server = new ServerSocket(port); // creates new server object on specified port

            Properties prop = new Properties(); // creates new property object to read server config file
            String fileName = "pa-web-server/server/server.config";
            try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
            } catch (FileNotFoundException ex) {
                System.out.println("File does not contain anything"); // FileNotFoundException catch is optional and can be collapsed
            } catch (IOException ex) {
                System.out.println("File does not exist");
            }

            String server_root = prop.getProperty("server.root");
            server = new ServerSocket(Integer.parseInt(prop.getProperty("server.port")));

            System.out.println("Started Server Socket");
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            System.out.println("Server Port from config : " + prop.getProperty("server.port"));
            System.out.println("Server Port from string conversion  : " + server.getLocalPort());
            System.out.println("Número de requests máximos: " + prop.getProperty("server.maximum.requests"));

//          File file = new File("server/server_client_aceptances/console.log"); previously used
            FileWriter fileWriter = new FileWriter("pa-web-server/server/server_client_aceptances/console.log", false); // able to create new file to write the console logs

            ExecutorService pool = Executors.newFixedThreadPool(15);

            //Producer-Consumer
            MainConsumer mainConsumer = new MainConsumer(server_root, fileWriter);
            Buffer buffer = new Buffer(mainConsumer);
            mainConsumer.setBuffer(buffer);
            mainConsumer.createConsumers(15);

            while(true){

                client = server.accept();
                String file_name;

                System.out.println();
                System.out.println("Debug: got new client " + client.toString());

                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                file_name = br.readLine();

                System.out.println("Cliente: " + client);
                System.out.println(file_name);

                try{
                    switch (option) {
                        case 1:
                            System.out.println(" ************   Task Pool Method    ************");
                            pool.execute(new TaskPool(client, server, server_root, counter_client, fileWriter, file_name));
                            break;
                        case 2:
                            System.out.println(" ************   Producer - Consumer Method    ************");
                            Producer p1 = new Producer(buffer, counter_client, file_name, client);
                            p1.start();
                            break;
                        default:
                            System.out.println("");
                    }
                    counter_client = counter_client + 1;
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error no MainHTTPServer");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
