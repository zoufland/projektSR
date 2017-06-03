import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import javax.xml.soap.Text;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {

    @FXML public TextField id;
    @FXML public TextField serverIpAddress;
    @FXML public TextField serverPort;
    @FXML public TextField timestamp;
    @FXML public TextField oczekiwanie;
    @FXML public TextArea czySK;
    @FXML public TextField czasPobytuWSK;
    @FXML public TextArea Logi;


    public void zarejestruj(ActionEvent actionEvent) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        //System.out.println(Integer.parseInt(id.getText()));
        Client client = new Client();
        boolean czyZarejestrowany = Client.zarejestruj(Integer.parseInt(id.getText()), serverIpAddress.getText() + ":" + serverPort.getText());
        if (czyZarejestrowany == true)
        {
            Logi.appendText("Pomyślnie zarejestrowano się w serwerze. \n");
            long starttime = System.currentTimeMillis();
            executor.execute(()->{
                while(System.currentTimeMillis() - starttime < 25000)
                {

                }
                LinkedList<String> adresyIP = Client.zwrocListeKlientow();
                Logi.appendText("Lista klientów otrzymana od serwera: \n");
                for (String klient: adresyIP) {
                    Logi.appendText(klient + "\n");
                }
                executor.execute(()->
                {
                    while(true)
                    {
                        try {
                            String idWylaczonego = Client.rejestrWlasny.odbierzIDWylaczonego();
                            //System.out.println(idWylaczonego);
                            Thread.sleep(500);
                            if(idWylaczonego != null)
                            {
                                Logi.appendText("Wyłączono klienta " + idWylaczonego + ".\n");
                                Thread.sleep(5000);
                                Logi.appendText("Aktualna lista klientów: " + Client.zwrocListeKlientow() + ".\n");
                            }
                            idWylaczonego = null;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

            });

        }
        else Logi.appendText("Wystąpił błąd podczas rejestrowania się na serwerze. \n");

    }

    public void zadajSK(ActionEvent actionEvent) {
        boolean czyWszedl = Client.zadajSK(timestamp.getText(), oczekiwanie.getText());
        System.out.println("Czy wszedłem do sekcji krytycznej? " + czyWszedl);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        if (czyWszedl == true)
        {
            czySK.setText("SK");
            Logi.appendText("Wszedłem do sekcji krytycznej. \n");
        }
        else Logi.appendText("Nie wszedłem do sekcji krytycznej. \n");
        long starttime = System.currentTimeMillis();

        executor.execute(() -> {
            while(System.currentTimeMillis() - starttime < Integer.parseInt(czasPobytuWSK.getText())*1000)
            {

            }
            czySK.setText("");

            //czyWszedl = false;
            try {
                RejestrKlient rejestrZdalny = (RejestrKlient) Naming.lookup("rmi://localhost:1100/RejestrKlient");
                rejestrZdalny.wyzerujTimestamp();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        czyWszedl = false;



    }



    public void wylaczKlienta(ActionEvent actionEvent) {
        int idWylaczonego = Client.wylaczKlienta();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Logi.appendText("Wyłączono klienta " + idWylaczonego + ".\nAktualna lista klientów: " + Client.zwrocListeKlientow() + ".\n");
    }
}
