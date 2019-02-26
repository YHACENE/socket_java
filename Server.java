import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
   public static void main(String[] args)
   {
     final ServerSocket serveurSocket  ;
     final Socket clientSocket ;
     final BufferedReader in;
     final PrintWriter out;
     final Scanner sc = new Scanner(System.in);

     if (args.length != 1)
      System.out.println("Introduire le port de communication");

     System.out.println("Waiting for connection ...");
     try
     {
       serveurSocket = new ServerSocket(Integer.parseInt(args[0]));
       clientSocket = serveurSocket.accept();
       out = new PrintWriter(clientSocket.getOutputStream());
       in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));

       Thread send = new Thread(new Runnable()
       {
          String msg;
          @Override
          public void run()
          {
             while(true)
             {
                msg = sc.nextLine();
                out.println(msg);
                out.flush();
             }
          }
       });

       send.start();

       Thread receive = new Thread(new Runnable()
       {
          String msg ;
          @Override
          public void run()
          {
             try
             {
                msg = in.readLine();

                while(msg!=null)
                {
                   System.out.println("Client : " + msg);
                   msg = in.readLine();
                }

                System.out.println("Client déconecté");
                out.close();
                clientSocket.close();
                serveurSocket.close();
             } catch (IOException e)
             {
                  e.printStackTrace();
             }
         }
      });
      receive.start();
      }catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
