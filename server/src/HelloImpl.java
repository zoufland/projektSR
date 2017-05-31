import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/**
 * Created by Zoufland on 18.05.2017.
 */
public class HelloImpl extends UnicastRemoteObject implements Hello {

    public String adresIP;
   public String coUsunac;
   public boolean czyUsunac = false;
    public AddressTracker tracker;
    public LinkedList<String> listaAdresow;
    public LinkedList<String> znacznikiCzasu = new LinkedList<String>();
    public LinkedList<String> listaOkejek;/* = new LinkedList<String>();*/
    public String timestamp;

    protected HelloImpl(AddressTracker tracker) throws RemoteException {
        this.tracker = tracker;
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

    public String sayHello() {
        return "Hello, world!";
    }

    public String przeslijAdres(String message) {
        String[] czesci = message.split(" ");
        this.tracker.idKlienta = czesci[0];
        this.tracker.adres = czesci[1];
        return czesci[0];
    }

    public String odbierzAdres()
    {
        return adresIP;
    }

    public LinkedList<String> przeslijListe(LinkedList<String> lista)
    {
        listaAdresow = lista;
        return lista;
    }

    public LinkedList<String> odbierzListe()
    {
        return listaAdresow;
    }

    public String przeslijTimestamp(String timestamp)
    {
        this.timestamp = timestamp;

        //znacznikiCzasu.add(timestamp);
        return timestamp;
    }

    public String odbierzTimestamp()
    {
        return this.timestamp;
    }

    public void klientWszedl(String id)
    {
        for (String znacznik:znacznikiCzasu
             ) {
            if (znacznik.startsWith(id)) znacznikiCzasu.remove(znacznikiCzasu.indexOf(znacznik));
        }
    }



    public void przeslijOkejke(String idDocelowy)
    {
        listaOkejek.add(idDocelowy + " " + "OK");
    }

    public LinkedList<String> zwrocListeOkejek()
    {
        return listaOkejek;
    }

    public void wyczyscOkejkiOID(String idDocelowy)
    {
        for (String okejka: listaOkejek) {
            if (okejka.startsWith(idDocelowy)) listaOkejek.remove(listaOkejek.indexOf(okejka));
        }
    }
}
