import com.sun.org.apache.regexp.internal.RE;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * Created by Zoufland on 18.05.2017.
 */
public interface Hello extends Remote {
    String sayHello() throws RemoteException;

    String przeslijAdres(String wiadomosc) throws RemoteException;

    String odbierzAdres() throws RemoteException;

    LinkedList<String> przeslijListe(LinkedList<String> lista) throws RemoteException;

    LinkedList<String> odbierzListe() throws RemoteException;

    String przeslijTimestamp(String timestamp) throws RemoteException;

    String odbierzTimestamp() throws RemoteException;

    String doUsuniecia(String id) throws RemoteException;

    String odbierzID() throws RemoteException;

    void wyczyscOkejkiOID(String idDocelowy) throws RemoteException;

    LinkedList<String> zwrocListeOkejek() throws RemoteException;

    void przeslijOkejke(String idDocelowy) throws RemoteException;

    void klientWszedl(String id) throws RemoteException;
}
