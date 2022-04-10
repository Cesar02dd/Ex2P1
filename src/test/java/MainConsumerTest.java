import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import static org.junit.jupiter.api.Assertions.*;

class MainConsumerTest {

    FileWriter fileWriter;
    MainConsumer mainConsumer;

    @BeforeEach
    void setUp() {
        try {
            fileWriter = new FileWriter("../pa-web-server/server/server_client_aceptances/console.log", false);
        }catch (Exception e){
            e.printStackTrace();
        }
        mainConsumer = new MainConsumer("server", fileWriter);
    }

    @Test
    void createConsumers() {
        mainConsumer.createConsumers(15);
        assertEquals(15, mainConsumer.getConsumers().size());
    }
}