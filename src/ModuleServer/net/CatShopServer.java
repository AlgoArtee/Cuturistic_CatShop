package ModuleServer.net;

//import ModuleServer.net.ClientRequestProcessor;

import ModuleCommon.IShop;
import ModuleServer.domain.Shop;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  @author Chachulski, Mathea
 */
public class CatShopServer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        IShop shop = new Shop("Shop");

        ServerSocket ss = new ServerSocket(1399);
        System.out.println("Server is running and waits for incoming requests!");

        while(true) {
            Socket s = ss.accept(); // clientSocket

            Runnable c = new ClientRequestProcessor(s, shop);

            // Parallele Abarbeitung des Clients starten
            Thread t = new Thread(c);
            t.start();

            //System.out.println(t.getName());

            System.err.println("Client connected!");
        }
    }

}
