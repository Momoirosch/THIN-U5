public class Main {
    static boolean shouldExit = false;
    static final int CHOICE_P  = 1;
    static final int CHOICE_PO = 2;
    static final int CHOICE_POT = 3;

    static P   p   = new P();
    static PO  po  = new PO();
    static POT pot = new POT();

    static Programm currentProgramm = null;
    static boolean showDetails = false;

    public static void main(String[] args) {
        // Get the boolean input for details
        int details = getIntInputBetween("Do you want to see details? (1 for yes, 0 for no)", 0, 1);
        showDetails = details == 1;

        while (!shouldExit) {
            // Choose the program to run
            chooseProgramm();
            // Get the integer input
            int x = getIntInputBetween("Please enter an integer x:", 0, Integer.MAX_VALUE);

            // Run the chosen program with the input
            int lengthFound = currentProgramm.run(x, showDetails);
            // Print the result
            System.out.println("Max length found: " + lengthFound);

            // Ask if the user wants to exit
            shouldExit();
        }
    }

    private static int getIntInputBetween(String prompt, int min, int max) {
        // Ask the user for an integer input between min and max
        System.out.println(prompt);
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int input = scanner.nextInt();
        // Check if the input is valid
        while (input < min || input > max) {
            System.out.println("Invalid input. Please enter an integer between " + min + " and " + max + ":");
            input = scanner.nextInt();
        }
        return input;
    }

    private static void chooseProgramm() {
        int choice = getIntInputBetween(
                "Which program would you like to run? (1 = P, 2 = P Optimized, 3 = P Optimized Threaded)",
                CHOICE_P, CHOICE_POT
        );
        if (choice == CHOICE_P) {
            System.out.println("You chose P");
            currentProgramm = p;
        } else if (choice == CHOICE_PO) {
            System.out.println("You chose PO");
            currentProgramm = po;
        } else if (choice == CHOICE_POT) {
            System.out.println("You chose POT");
            currentProgramm = pot;
        }
    }

    static private void shouldExit() {
        if (getIntInputBetween("Should exit? (-1 for yes, 0 for no)", -1, 0) == -1) {
            shouldExit = true;
            System.exit(0);
        }
    }
}
