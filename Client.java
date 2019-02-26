import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
   public static void main(String[] args)
   {
      final Socket clientSocket;
      final BufferedReader in;
      final PrintWriter out;
      final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier

      try
      {
         if (args.length != 2)
            System.out.println("Intruidre l'adresse et le port de communication");

         clientSocket = new Socket(args[0], Integer.parseInt(args[1]));

         out = new PrintWriter(clientSocket.getOutputStream());
         in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

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
            String msg;
            @Override
            public void run()
            {
               try
               {
                 msg = in.readLine();
                 while(msg != null)
                 {
                    System.out.println("Serveur : " + msg);
                    msg = in.readLine();
                 }
                 System.out.println("Serveur déconecté");
                 out.close();
                 clientSocket.close();
               } catch (IOException e)
               {
                   e.printStackTrace();
               }
            }
        });
        receive.start();
      } catch (IOException e)
      {
           e.printStackTrace();
      }
  }
}
