import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class ServerSocketExample {
    private ServerSocket server;
    private int port = 4444;

    public ServerSocketExample() {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerSocketExample example = new ServerSocketExample();
        example.handleConnection();
    }

    public void handleConnection() {
        System.out.println("Waiting for client message...");

        //
        // The server do a loop here to accept all connection initiated by the
        // client application.
        //
        while (true) {
            try {
                Socket socket = server.accept();
                new ConnectionHandler(socket);
            } catch (IOException e) {
                
				
            }
        }
    }
}

class ConnectionHandler implements Runnable {
    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try
        {
            //
            // Read a message sent by client application
            //
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			String message;
		do{
            message = (String) ois.readObject();
            System.out.println("Pane Received: " + message);
			File f=new File("f1.dat");
			if(!f.exists())
			f.createNewFile();
			Writer output = null;
			output = new BufferedWriter(new FileWriter(f));
			output.write(message);
			output.close();
			System.out.println(f.length());
			Thread.sleep(1000);

			oos.writeObject("yes");
		}while(!message.equals("no"));
            //
            // Send a response information to the client application
            //
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //oos.writeObject("Hi...");

            ois.close();
            //oos.close();
            socket.close();

            System.out.println("Waiting for client message...");
        } catch (IOException e) {
            System.out.println("The End");
			System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("The End");
        }
		catch(Exception e) {
		}
    }
}