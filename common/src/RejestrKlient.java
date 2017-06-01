import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

/**
 * Created by Zoufland on 01.06.2017.
 */
public interface RejestrKlient extends Remote {

    boolean sprawdzTimestamp (LocalTime timestamp) throws RemoteException;

    void przeslijTimestamp(LocalTime timestamp) throws RemoteException;

    void wyzerujTimestamp() throws RemoteException;

    String doUsuniecia(String id) throws RemoteException;

    String odbierzID() throws RemoteException;

}
