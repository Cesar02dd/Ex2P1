/**
 *  <b> Class Main </b>
 *
 *  <p>
 *      {@link MainHTTPServerThread} thread is created here with a default port and a default
 *      number of requests. It waits for it to die.
 *
 *  </p>
 */
public class Main {

    /**
     * Main class of the program, only minimal changes should be added to this method
     * @param args main
     */

    public static void main(String[] args){
        MainHTTPServerThread s = new MainHTTPServerThread(8888, 2);
        s.start();
        try {
            s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
