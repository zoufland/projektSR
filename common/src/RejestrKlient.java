import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

/**
 * Created by Zoufland on 01.06.2017.
 */
public interface RejestrKlient extends Remote {

    public boolean sprawdzTimestamp (LocalTime timestamp) throws RemoteException;

    public void przeslijTimestamp(LocalTime timestamp) throws RemoteException;

}
