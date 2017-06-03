import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalTime;
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
    private static LocalTime znacznikCzasu;
    public static RejestrKlient rejestrWlasny;

    public static boolean zarejestruj(Integer id, String adresIPSerwera) {
        Hello hello;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            adresyIP = new LinkedList<String>();
            IPSerwera = adresIPSerwera;
            idKlienta = id;
            hello = (Hello) Naming.lookup("rmi://" + IPSerwera + "/Hello"); //połączenie z rejestrem RMI
            registry = LocateRegistry.createRegistry(1100);

            //AddressTracker tracker = new AddressTracker();


            String message = InetAddress.getLocalHost().toString(); // pobranie adresu IP
            String[] czesci = message.split("/"); //usunięcie z pobranego stringa nazwy komputera
            Pair idIP = new Pair(id, czesci[1]); //zapisanie pary id:IP

            hello.przeslijAdres(idIP.id.toString() + " " + idIP.adresIP); //przesłanie adresów i ID klienta do serwera
            System.out.println(message);
            System.out.println("Przesłanie adresu powiodło się.");

            rejestrWlasny = new RejestrImpl();
            try {
                Naming.rebind("rmi://localhost:1100/RejestrKlient", rejestrWlasny);
                System.out.println("Utworzyłem rejestr.");
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            }

            executor.execute(()-> {
                while (!done) { //odebranie listy adresów IP od serwera
                    try {
                        if (hello.odbierzListe() != null) {

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
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                String idDoUsuniecia= "";

                                try {
                                    idDoUsuniecia = rejestrWlasny.odbierzID();

                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }


                                if (idDoUsuniecia != null) {
                                    for (String IP : adresyIP
                                            ) {
                                        if (IP.startsWith(idDoUsuniecia)) adresyIP.remove(adresyIP.indexOf(IP));
                                    }
                                    System.out.println(idDoUsuniecia);
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
            return false;
        }
        return true;
    }

    public static boolean zadajSK(String timestamp, String timeout) {
        znacznikCzasu = LocalTime.parse(timestamp);
        try {
            rejestrWlasny.przeslijTimestamp(znacznikCzasu);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //Hello hello;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        czyWszedl = false;
        try {
            //hello = (Hello) Naming.lookup("rmi://" + IPSerwera + "/Hello");
            //hello.przeslijTimestamp(timestamp);
            System.out.println(timeout);
            executor.execute(() -> {
                long startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < (Integer.parseInt(timeout)*1000))
                {
                    try {
                        int liczbaOkejek = 0;
                        for (String klient: adresyIP) {
                            String[] czesci = klient.split(" ");
                            //System.out.println(czesci[1]);
                            RejestrKlient rejestrZdalny = (RejestrKlient) Naming.lookup("rmi://" + czesci[1] + ":1100/RejestrKlient");
                            if (rejestrZdalny.sprawdzTimestamp(znacznikCzasu) == true)
                            {
                                liczbaOkejek++;
                            }
                        }

                        if (liczbaOkejek == adresyIP.size()-1)
                        {
                            //System.out.println(liczbaOkejek + " " + (adresyIP.size()-1));
                            czyWszedl = true;

                        }
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
            /*if (czyWszedl == true)
            {
                hello.klientWszedl(idKlienta.toString());
            }
            else
            {
                System.out.println("Nie wszedłem do SK");
            }*/


        }
         catch (InterruptedException e) {
            e.printStackTrace();
        }
        return czyWszedl;
    }

    public static int wylaczKlienta()
    {
        //Hello hello;

        try {
            //RejestrKlient rejestr = (RejestrKlient) Naming.lookup("rmi://localhost:1100/RejestrKlient");
            for (String klient:adresyIP) {
                String[] czesci = klient.split(" ");
                //System.out.println(czesci[1]);
                RejestrKlient rejestrZdalny = (RejestrKlient) Naming.lookup("rmi://" + czesci[1] + ":1100/RejestrKlient");
                rejestrZdalny.doUsuniecia(idKlienta.toString());
            }



        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return idKlienta;
    }

    public static LinkedList<String> zwrocListeKlientow()
    {
        return adresyIP;
    }
}