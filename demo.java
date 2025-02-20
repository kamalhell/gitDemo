import java.util.Scanner;

public class demo {

                public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                System.out.print("Enter the angle in degrees: ");
                double degrees = scanner.nextDouble();
                double radians = Math.toRadians(degrees);
                double sine = Math.sin(radians);
                double cosine = Math.cos(radians);
                double tangent = Math.tan(radians);

                System.out.println("Sine of " + degrees + " degrees: " + sine);
                System.out.println("Cosine of " + degrees + " degrees: " + cosine);
                System.out.println("Tangent of " + degrees + " degrees: " + tangent);
                scanner.close();
            }
        }
