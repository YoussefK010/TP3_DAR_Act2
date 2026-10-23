package tp3;

public class Calculatrice {
    public double calculer(String operation, double a, double b) {
        switch (operation.toLowerCase()) {
            case "addition":
            case "somme":
            case "+":
                return a + b;
            case "soustraction":
            case "-":
                return a - b;
            case "multiplication":
            case "*":
                return a * b;
            case "division":
            case "/":
                if (b == 0) throw new ArithmeticException("Division par zéro impossible.");
                return a / b;
            default:
                throw new IllegalArgumentException("Opération inconnue : " + operation);
        }
    }
}
