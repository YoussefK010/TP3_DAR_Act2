package tp3;

import java.io.Serializable;

public class OperationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String operation;
    private final double a;
    private final double b;

    public OperationRequest(String operation, double a, double b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public String getOperation() { return operation; }
    public double getA() { return a; }
    public double getB() { return b; }
}
