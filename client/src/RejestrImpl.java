import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.rmi.RemoteException;

/**
 * Created by Zoufland on 01.06.2017.
 */
public class RejestrImpl extends UnicastRemoteObject implements RejestrKlient{

    public LocalTime znacznikCzasuWlasny;

    protected RejestrImpl() throws RemoteException {
    }

    public boolean sprawdzTimestamp (LocalTime timestampZadania)
    {
        if (timestampZadania.compareTo(znacznikCzasuWlasny) < 0)
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    public void przeslijTimestamp(LocalTime timestamp)
    {
        znacznikCzasuWlasny = timestamp;
    }
}
