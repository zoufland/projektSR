import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.rmi.RemoteException;

/**
 * Created by Zoufland on 01.06.2017.
 */
public class RejestrImpl extends UnicastRemoteObject implements RejestrKlient{

    public LocalTime znacznikCzasuWlasny;
    public String coUsunac;
    public boolean czyUsunac = false;

    protected RejestrImpl() throws RemoteException {
    }

    public String doUsuniecia(String id)
    {
        coUsunac = id;
        czyUsunac = true;
        return id;
    }

    public String odbierzID()
    {
        if (czyUsunac == true)
        {
            czyUsunac = false;
            return coUsunac;
        }
        else return null;
    }

    public boolean sprawdzTimestamp (LocalTime timestampZadania)
    {
        if(znacznikCzasuWlasny == null)
        {
            return true;
        }
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

    public void wyzerujTimestamp()
    {
        znacznikCzasuWlasny = null;
    }
}
