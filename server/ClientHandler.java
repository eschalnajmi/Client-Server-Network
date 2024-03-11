import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class ClientHandler extends Thread {
	private Socket socket = null;

	public ClientHandler(Socket socket) {
		super("ClientHandler");
		this.socket = socket;
	}

	public void run(){
		try {
			// Create a socket to deal with I/O to client
			Socket clientSocket = this.socket;	
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String task = in.readLine();

			// Get request details
			LocalDate date = LocalDate.now();
			LocalTime time = LocalTime.now();
			String clientIP = clientSocket.getInetAddress().getHostAddress();
			FileWriter fileWriter = new FileWriter("log.txt", true);
			PrintWriter writetolog = new PrintWriter(fileWriter);
					
			if(task.equals("list")){
				File serverFiles = new File("serverFiles");
				File[] filenames = serverFiles.listFiles();
				String files = "";

				//Add request details to log.txt
				writetolog.println(date + "|" + time + "|" + clientIP + "|" +"list");
				writetolog.close();
						
				// List files in serverFiles directory
				if(filenames != null){
					for(File name : filenames){
						if(name.isFile()){
							files += name.getName() + "\n";
						}
					}
					out.println(files);
				} else {
					out.println("No files in serverFiles directory");
				}
			} else if(task.equals("put")){
				String filename = in.readLine();
				String filepath = "serverFiles/" + filename;
				File newFile = new File(filepath);

				// Add request details to log.txt
				writetolog.print(date + "|" + time + "|" + clientIP + "|" +"put\n");
				writetolog.close();


				if(newFile.exists()){
					out.println("Error: Cannot upload file " + filename + "; already exists on server.");
				} else {
					// Create file and write contents to it from client
					newFile.createNewFile();
					PrintWriter writetofile = new PrintWriter(filepath, "UTF-8");

					String content = in.readLine();
					while(in.ready()){
						writetofile.println(content);
						content = in.readLine();
					}
					writetofile.print(content);

					out.println("Uploaded file " + filename);
					writetofile.close();
				}
			} else{
				out.println("Invalid command");
			}

			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error: Could not create new Handler");
		}
	}
}
