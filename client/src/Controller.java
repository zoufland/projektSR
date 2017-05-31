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


    public void zarejestruj(ActionEvent actionEvent) {
        System.out.println("test");
        //System.out.println(Integer.parseInt(id.getText()));
        Client.zarejestruj(Integer.parseInt(id.getText()), serverIpAddress.getText() + ":" + serverPort.getText());


    }

    public void zadajSK(ActionEvent actionEvent) {
        boolean czyWszedl = Client.zadajSK(timestamp.getText(), oczekiwanie.getText());
        if (czyWszedl == true) czySK.setText("SK");
    }

    public void wylaczKlienta(ActionEvent actionEvent) {
        Client.wylaczKlienta();
    }
}
