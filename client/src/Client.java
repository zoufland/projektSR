import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Zoufland on 18.05.2017.
 */
public class Client {

    public static LinkedList<String> adresyIP;
    public static String IPSerwera;
    public static Integer idKlienta;

    public static boolean czyWszedl = false;
    static boolean done = false;
    private static Registry registry;
    private static String znacznikCzasu;

    public static void zarejestruj(Integer id, String adresIPSerwera) {
        Hello hello;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            adresyIP = new LinkedList<String>();
            IPSerwera = adresIPSerwera;
            idKlienta = id;
            hello = (Hello) Naming.lookup("rmi://" + IPSerwera + "/Hello"); //połączenie z rejestrem RMI
            registry = LocateRegistry.createRegistry(1099);

            //AddressTracker tracker = new AddressTracker();


            String message = InetAddress.getLocalHost().toString(); // pobranie adresu IP
            String[] czesci = message.split("/"); //usunięcie z pobranego stringa nazwy komputera
            Pair idIP = new Pair(id, czesci[1]); //zapisanie pary id:IP

            hello.przeslijAdres(idIP.id.toString() + " " + idIP.adresIP); //przesłanie adresów i ID klienta do serwera
            System.out.println(message);
            System.out.println("Przesłanie adresu powiodło się.");


            executor.execute(()-> {
                while (!done) { //odebranie listy adresów IP od serwera
                    try {
                        if (hello.odbierzListe() != null) {
                            System.out.println("Jestem w if ");
                            adresyIP = hello.odbierzListe();
                            for (String ip : adresyIP) {
                                System.out.println(ip);
                            }
                            done = true;
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                   // Thread.sleep(1000);
                }
                executor.execute(() -> {
                            boolean flaga = false;
                            while (true) {
                                String idDoUsuniecia= "";
                                //System.out.println("Jestem w tasku");
                                try {
                                    idDoUsuniecia = hello.odbierzID();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }


                                if (idDoUsuniecia != null) {
                                    for (String IP : adresyIP
                                            ) {
                                        if (IP.startsWith(idDoUsuniecia)) adresyIP.remove(adresyIP.indexOf(IP));
                                    }
                                    System.out.println("Usunąłem z listy. Obecna lista: ");
                                    for (String ip :adresyIP
                                            ) {
                                        System.out.println(ip);
                                    }
                                    idDoUsuniecia = null;
                                }
                            }
                        }

                );
            });






            //System.out.println(hello.sayHello());
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static boolean zadajSK(String timestamp, String timeout) {
        znacznikCzasu = timestamp;
        Hello hello;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        czyWszedl = false;
        try {
            hello = (Hello) Naming.lookup("rmi://" + IPSerwera + "/Hello");
            hello.przeslijTimestamp(timestamp);
            System.out.println(timeout);
            executor.execute(() -> {
                long startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < (Integer.parseInt(timeout)*1000))
                {
                    try {
                        int liczbaOkejek = 0;
                        for (String klient: adresyIP) {
                            String[] czesci = klient.split(" ");
                            Hello rejestrZdalny = (Hello) Naming.lookup("rmi://" + czesci[1] + "/Hello");
                        }

                        /*if (hello.zwrocListeOkejek() != null)
                        {

                            LinkedList<String> listaOkejek = hello.zwrocListeOkejek();
                            for (String okejka:listaOkejek
                                 ) {
                                if (okejka.startsWith(idKlienta.toString())) liczbaOkejek++;
                            }
                        }
                        if (liczbaOkejek == adresyIP.size()-1)
                        {
                            //System.out.println(liczbaOkejek + " " + (adresyIP.size()-1));
                            czyWszedl = true;

                        }*/
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

            });
            Thread.sleep(Integer.parseInt(timeout)*1001);
            executor.shutdownNow();
            System.out.println(czyWszedl);
            if (czyWszedl == true)
            {
                hello.klientWszedl(idKlienta.toString());
            }
            else
            {
                System.out.println("Nie wszedłem do SK");
            }


        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return czyWszedl;
    }

    public static void wylaczKlienta()
    {
        Hello hello;

        try {
            hello = (Hello) Naming.lookup("rmi://" + IPSerwera + "/Hello");
            hello.doUsuniecia(idKlienta.toString());


        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}