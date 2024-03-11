import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server 
{
	public static void main( String[] args ) throws IOException
	{
		// Create a thread pool to handle maximum 20 clients
		ExecutorService executor = Executors.newFixedThreadPool(20);
		// Create the log file to track client requests
		File log = new File("log.txt");
		log.createNewFile();

		try{
			// Create a server socket to listen for incoming client connections
			ServerSocket serverSocket = null;

			try{
				serverSocket = new ServerSocket(9100);
			} catch(IOException e){
				System.err.println("Could not listen on port");
				System.exit(-1);
			}

			while(true){
				// Accept incoming client connections
				Socket clientSocket = serverSocket.accept();
				executor.submit(new ClientHandler(clientSocket));
			}
		} catch (IOException e) {
			System.out.println("Error: Could not listen on port 9100");
		}
	}
}

