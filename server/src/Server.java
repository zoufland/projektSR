import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Zoufland on 18.05.2017.
 */
public class Server{

    static LinkedList<String> adresyIP;
    private static Registry registry;

    public static void main(String args[]) {
        try {
            adresyIP = new LinkedList<String>();
            AddressTracker tracker = new AddressTracker();
            Hello hello = new HelloImpl(tracker);
            registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/Hello", hello);

            String adres = "";
            String idKlienta;
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 20000)
            {
                    do {
                        if (tracker.getAdres() != adres) {
                            if ((tracker.getAdres()) != null) {
                                adres = tracker.getAdres();
                                idKlienta = tracker.getIdKlienta();
                                System.out.println("Zgłosił się klient: " + idKlienta + " " + adres);
                                adresyIP.add(idKlienta + " " + adres);
                                System.out.println("Zawartość listy IP: ");
                                for (String ip : adresyIP) {
                                    System.out.println(ip);
                                }
                                //done = true;
                            }
                        }
                        Thread.sleep(1000);
                    } while (tracker.getAdres() != adres);


                Thread.sleep(1000);

            }
            System.out.println("Serwer zakończył pracę.");
            hello.przeslijListe(adresyIP);

        } catch (RemoteException | MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
