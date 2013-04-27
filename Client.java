import java.io.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
class Client 
{
   public static void main(String args[])
	{
	
      	try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("mushroom.dat");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=1;
			
			InetAddress host = InetAddress.getLocalHost();
            Socket socket = new Socket(host.getHostName(), 4444);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			String msg;
			StringBuffer str=new StringBuffer();
			//Read File Line By Line
			while ((strLine = br.readLine()) != null){
				// Print the content on the console
				System.out.println("Sending transaction "+i+" to server application ");
				System.out.println (strLine+"\n");
				str.append(strLine+"\n");
				//System.out.println(str);
				if(strLine.equals("no")){
					oos.writeObject("no");
					str=new StringBuffer();
				}
				if(i%8==0){
					oos.writeObject(str.toString());
					str=new StringBuffer();
					while(!((String)ois.readObject()).equals("yes")){
						break;
				}
			}
			i++;
			}
			//Close the input stream
			in.close();
			oos.close();
		}
		catch (UnknownHostException e) {
            	System.out.println("The End");
        	} catch (IOException e) {
            	
        	} 
		catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
	}
}

