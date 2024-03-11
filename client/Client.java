import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client 
{
	public static void main( String[] args)
	{
		try {
			// Create socket to deal with I/O to server
            Socket newsocket = new Socket("localhost", 9100);
			PrintWriter out = new PrintWriter(newsocket.getOutputStream(), true);
        	BufferedReader in = new BufferedReader(new InputStreamReader(newsocket.getInputStream()));
            
			// Check if the user wants to list files or put a file
			if(args[0].equals("list")){
				out.println("list");
				String response = in.readLine();
				String responses = "";
				int numfiles = 0;

				// Read server output
				while(response != null){
					numfiles++;
					responses += "\n" + response;
					response = in.readLine();
				} 

				responses = responses.substring(0, responses.length() - 1); // Remove newline character from string
				System.out.println("Listing " + (numfiles - 1) + " file(s):" + responses);

				in.close();
				out.close();
				newsocket.close();
				System.exit(0);

			} else if(args[0].equals("put")){
				String fname = args[1];
				File NewFile = new File(fname);

				// Check whether the file exists in the client directory
				if(NewFile.exists()){
					out.println("put");
					out.println(fname);

					// Read file contents and send to server
					String FileContents = new String(Files.readAllBytes(Paths.get(fname)));
					out.println(FileContents);

					String response = in.readLine();
					System.out.println(response);
				} else {
					System.out.println("Error: Cannot open local file " + fname + " for reading");
					out.println("error");
				}

				in.close();
				out.close();
				newsocket.close();
				System.exit(0);
			} else {
				System.out.println("Error Invalid command input");

				in.close();
				out.close();
				newsocket.close();
				System.exit(0);
			}
        } catch (IOException e) {
            System.out.println("Error when trying to create socket: " + e.getMessage());
        }
	}
}