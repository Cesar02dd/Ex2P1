import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *<b> Class Binary File </b>
 *<p>
 *     This Class implements a way to read each requested html files by returning its content
 * </p>
 */

public class BinaryFile extends Thread{



    /**
     *<h2> Global variables </h2>
     *
     * @param locks Hashmap that assigns a lock to each file.
     * @param content Has the content necessary to show the client
     * @param server_root default directory/path for server
     * @param file_name file requested
     * @param backup arraylist that checks if the last requested file is the same as the current one
     * @param counter counts the number of incoming requests
     *
     */

    private final HashMap<String, ReentrantLock> locks = new HashMap<>();
    private ArrayList<byte[]> content;
    private String server_root;
    private String file_name;
    private Semaphore semaphore = new Semaphore(1);

    /**
     *  Constructor for Binary File
     * @param server_root default directory/path for server
     * @param file_name file requested
     * @param content Has the content necessary to show the client
     */

    public BinaryFile(String server_root, String file_name, ArrayList<byte[]> content) {
        this.server_root = server_root;
        this.file_name = file_name;
        this.content = content;
    }

    /**
     * Important method which locks threads in case there's another one trying to access the same file
     * on the HashMap. If different threads access different files then each of them will acquire both a permit
     * and a lock for said files.
     * Reads a server document and returns it as an array of bytes
     *
     * @return  <code>byte[]</code> with the html document at <code>path</code>
     */

    private byte[] readBinaryFile(String file_name){

        if(locks.containsKey(file_name)){
            locks.get(file_name).lock();
            try{
                byte[] teste = contentBinaryFile();
                locks.get(file_name).unlock();
                return teste;

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            try {
                semaphore.acquire();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(!locks.containsKey(file_name)){
                locks.put(file_name, new ReentrantLock());
            }
            semaphore.release();
            return readBinaryFile(file_name);
        }
        return null;
    }

    /**
     * <p>
     *     This method reads the requested file on a certain path and stores its data in <code>content</code>
     * </p>
     * @return content of a file
     */

    public byte[] contentBinaryFile(){
        String[] tokens = file_name.split(" ");
        String route = tokens[1];
        String path = server_root + route;

        byte[] content = new byte[0];
        File f= new File(path);
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream("pa-web-server/"+f);
                content = fileInputStream.readAllBytes();
            }
            catch(FileNotFoundException e){
                try {
                    String rota = path_index(route);
                    File recebe_emergencia = ficheiro_emergencia(server_root, rota);
                    FileInputStream fileInputStream = new FileInputStream("pa-web-server/"+recebe_emergencia);
                    content = fileInputStream.readAllBytes();
                }
                catch(FileNotFoundException ex){
                    File recebe_emergencia = ficheiro_emergencia(server_root, "/404.html");
                    FileInputStream fileInputStream = new FileInputStream("pa-web-server/"+recebe_emergencia);
                    content = fileInputStream.readAllBytes();
                }
            }
            return content;

        }catch(Exception e){
            e.printStackTrace();
            return content;
        }
    }

    public void run(){
        content.add(readBinaryFile(file_name));
    }


    /**
     *
     * @param server server root directory
     * @param route path to the requested file
     * @return Joined path of <code>server</code> and <code>route</code>
     */

    public File ficheiro_emergencia(String server,String route) {
        File emergencia = new File(server+route);
        System.out.println(emergencia);
        return emergencia;
    }

    /**
     *
     * <p>
     *     Creates an emergency path for index.html on the current folder
     *     by constructing a StringBuilder which constructs a String and
     *     appends the required index.html to the last position.
     *     This is possible due to route being split in sections according
     *     the regex which in this case is /
     * </p>
     *
     * @param route route of the file in a directory/server
     * @return Newly constructed path to the index.html
     */

    private String path_index(String route) {
        String[] token_for_index = route.split("(?=[/])");
        StringBuilder construct_emergency = new StringBuilder();
        for (int i = 0; i < token_for_index.length; i++) {
            if (i == token_for_index.length - 1) {
                construct_emergency.append("/index.html");
            } else {
                construct_emergency.append(token_for_index[i]);
            }
        }
        System.out.println(construct_emergency);
        return construct_emergency.toString();
    }
}

