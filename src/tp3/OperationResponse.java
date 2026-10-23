package tp3;

import java.io.Serializable;

public class OperationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean succes;
    private final String message;
    private final double resultat;
    private final int numeroOperation;

    public OperationResponse(boolean succes, String message, double resultat, int numeroOperation) {
        this.succes = succes;
        this.message = message;
        this.resultat = resultat;
        this.numeroOperation = numeroOperation;
    }

    public boolean isSucces() { return succes; }
    public String getMessage() { return message; }
    public double getResultat() { return resultat; }
    public int getNumeroOperation() { return numeroOperation; }
}
