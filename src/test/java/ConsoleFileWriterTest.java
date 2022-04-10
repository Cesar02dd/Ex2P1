import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleFileWriterTest {

    @BeforeEach
    public  void setup() throws IOException {
//        ServerSocket server = new ServerSocket(8887);
//        Socket client = new Socket("localhost", 8887);

//        server =
//        PrintStream out = new PrintStream(client.getOutputStream());
////        //BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\cesar\\.m2\\Documents\\Universidad\\3Ano\\PA\\TP7\\-PA-ProjetosGrupo2\\pa-web-server\\server\\testing.html")));
//        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//
//        String path = "/testing.html";
//        out.println("GET " + path + " HTTP/1.1" );
//        out.println();
//
//        System.out.println("Teste");
//        String line = in.readLine();
//        System.out.println(line);

        // Close our streams
//        in.close();
//        out.close();
//        client.close();


    }
    @Test
    void run() {
        assertEquals("test", "test");
    }
}