import java.util.Scanner;

public class EnergyData {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        GetUserInput(scanner);
    }

    private static void GetUserInput(Scanner scanner) {
        while (true) {
            System.out.println("\nEnergy option");
            System.out.println("1. Add data");
            System.out.println("2. Average consumption");
            System.out.println("3. Average generation");
            System.out.println("4. Quit");

            System.out.print("Wählen Sie eine Option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    AddData(scanner);
                    break;

                case 2:
                    GetAverageConsumtion(scanner);
                    break;

                case 3:
                    GetAverageGeneration(scanner);
                    break;

                case 4:
                    return;

                default:
                    System.out.println("invalid Number!");
                    break;
            }
        }
    }

    public static void AddData(Scanner scanner) {
        int meter_id = GetMeterId(scanner);
        System.out.println("consumed kwh:");
        double consumed = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("generated kwH:");
        double generated = scanner.nextDouble();
        scanner.nextLine();
        DB_Manager.AddEnergyData(meter_id, consumed, generated);
    }

    public static void GetAverageConsumtion(Scanner scanner)
    {       
        int meter_id = GetMeterId(scanner);
        double average = DB_Manager.GetAverageConsumtion(meter_id);
        System.out.printf("Average power consumtion: %.2f kwh %n", average);
    }

    public static void GetAverageGeneration(Scanner scanner)
    {
        int meter_id = GetMeterId(scanner);
        double average = DB_Manager.GetAverageGeneration(meter_id);
        System.out.printf("Average power generation: %.2f kwh %n", average);
    }

    public static int GetMeterId(Scanner scanner)
    {
        System.out.println("MeterId:");
        int meter_id = scanner.nextInt();
        scanner.nextLine();
        return meter_id;
    }
}
