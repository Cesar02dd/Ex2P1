import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainHTTPServerThreadTest {

    MainHTTPServerThread server;

    @BeforeEach
    public void setup(){
        server = new MainHTTPServerThread(8080, 1);
    }

    @DisplayName("Test que verifica a funcionalidade do servidor para muitos pedidos")
    @ParameterizedTest
    @CsvSource({"http://localhost:8888/testing.html, ./server/testing.html","http://localhost:8888/404.html, ./server/404.html"})
    void run(String urlTest, String path) {
        URL url = null;
        try {
            url = new URL(urlTest);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            connection.connect();
            assertEquals( 200,connection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
            String content = "";
            try {
                content += br.readLine();
                String test;
                while ((test =  br.readLine( ))!=null) {
                    content += "\n";
                    content += test;
                }
            } catch ( Exception e ) {
                e.printStackTrace( );
            }

            content = content.strip();
            assertEquals(server.readFile(path), content);
            connection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}