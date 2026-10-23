package tp3;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientCalculatrice {
    public static void main(String[] args) {
        String hote = args.length > 0 ? args[0] : "localhost";
        int port = 1234;

        try (Socket socket = new Socket(hote, port);
             ObjectOutputStream sortie = new ObjectOutputStream(socket.getOutputStream())) {
            sortie.flush();
            try (ObjectInputStream entree = new ObjectInputStream(socket.getInputStream());
                 Scanner clavier = new Scanner(System.in)) {

                System.out.println(entree.readObject());
                while (true) {
                    System.out.print("Opération (addition, soustraction, multiplication, division ou quit) : ");
                    String operation = clavier.nextLine().trim();
                    if ("quit".equalsIgnoreCase(operation)) break;

                    System.out.print("a = ");
                    double a = Double.parseDouble(clavier.nextLine());
                    System.out.print("b = ");
                    double b = Double.parseDouble(clavier.nextLine());

                    sortie.writeObject(new OperationRequest(operation, a, b));
                    sortie.flush();

                    OperationResponse reponse = (OperationResponse) entree.readObject();
                    if (reponse.isSucces()) {
                        System.out.println("Résultat : " + reponse.getResultat());
                        System.out.println("Numéro de l'opération traitée par le serveur : "
                                + reponse.getNumeroOperation());
                    } else {
                        System.out.println("Erreur : " + reponse.getMessage());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}
