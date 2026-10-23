package tp3;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServeurCalculatrice {
    private static final int PORT = 1234;
    private static final AtomicInteger compteurClients = new AtomicInteger(0);

    // Ressource partagée : compteur global d'opérations traitées.
    private static int compteurOperations = 0;

    private static synchronized int incrementerCompteurOperations() {
        return ++compteurOperations;
    }

    public static void main(String[] args) {
        try (ServerSocket serveur = new ServerSocket(PORT)) {
            System.out.println("Serveur calculatrice multi-thread démarré sur le port " + PORT);

            while (true) {
                Socket socket = serveur.accept();
                int numeroClient = compteurClients.incrementAndGet();
                System.out.println("Client n°" + numeroClient + " connecté : "
                        + socket.getRemoteSocketAddress());
                new Thread(new ClientProcessCalculatrice(socket, numeroClient),
                        "ClientProcess-" + numeroClient).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }

    static int operationTraitee() {
        int nouveauTotal = incrementerCompteurOperations();
        System.out.println("Nombre total d'opérations traitées : " + nouveauTotal);
        return nouveauTotal;
    }
}

class ClientProcessCalculatrice implements Runnable {
    private final Socket socket;
    private final int numeroClient;
    private final Calculatrice calculatrice = new Calculatrice();

    ClientProcessCalculatrice(Socket socket, int numeroClient) {
        this.socket = socket;
        this.numeroClient = numeroClient;
    }

    @Override
    public void run() {
        // Créer et flusher ObjectOutputStream avant ObjectInputStream des deux côtés
        // évite le blocage pendant l'échange des en-têtes de sérialisation.
        try (Socket client = socket;
             ObjectOutputStream sortie = new ObjectOutputStream(client.getOutputStream())) {
            sortie.flush();
            try (ObjectInputStream entree = new ObjectInputStream(client.getInputStream())) {
                sortie.writeObject("Bienvenue, client n°" + numeroClient);
                sortie.flush();

                while (true) {
                    Object objet = entree.readObject();
                    if (!(objet instanceof OperationRequest)) {
                        sortie.writeObject(new OperationResponse(false,
                                "Requête invalide.", 0, 0));
                        sortie.flush();
                        continue;
                    }

                    OperationRequest requete = (OperationRequest) objet;
                    try {
                        double resultat = calculatrice.calculer(
                                requete.getOperation(), requete.getA(), requete.getB());
                        int numeroOperation = ServeurCalculatrice.operationTraitee();
                        sortie.writeObject(new OperationResponse(true,
                                "Résultat calculé avec succès.", resultat, numeroOperation));
                    } catch (IllegalArgumentException | ArithmeticException ex) {
                        sortie.writeObject(new OperationResponse(false, ex.getMessage(), 0, 0));
                    }
                    sortie.flush();
                }
            }
        } catch (EOFException e) {
            System.out.println("Client n°" + numeroClient + " déconnecté.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur avec le client n°" + numeroClient + " : " + e.getMessage());
        }
    }
}
