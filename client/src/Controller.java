import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import javax.xml.soap.Text;
import java.net.InetAddress;
import java.rmi.Naming;

public class Controller {

    @FXML public TextField id;
    @FXML public TextField serverIpAddress;
    @FXML public TextField serverPort;
    @FXML public TextField timestamp;
    @FXML public TextField oczekiwanie;
    @FXML public TextArea czySK;
    @FXML public TextField czasPobytuWSK;


    public void zarejestruj(ActionEvent actionEvent) {
        System.out.println("test");
        //System.out.println(Integer.parseInt(id.getText()));
        Client client = new Client();
        Client.zarejestruj(Integer.parseInt(id.getText()), serverIpAddress.getText() + ":" + serverPort.getText());


    }

    public void zadajSK(ActionEvent actionEvent) {
        boolean czyWszedl = Client.zadajSK(timestamp.getText(), oczekiwanie.getText());
        System.out.println("Czy wszedłem do sekcji krytycznej? " + czyWszedl);
        if (czyWszedl == true) czySK.setText("SK");
        long starttime = System.currentTimeMillis();

        while(System.currentTimeMillis() - starttime < Integer.parseInt(czasPobytuWSK.getText())*1000)
        {

        }
        czySK.setText("");
        czyWszedl = false;
    }



    public void wylaczKlienta(ActionEvent actionEvent) {
        Client.wylaczKlienta();
    }
}
