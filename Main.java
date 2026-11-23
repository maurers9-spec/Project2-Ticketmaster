import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int option = 0;

        while (option != 4) {
            System.out.println("\nTicketmaster");
            System.out.println("1. Manage shows");
            System.out.println("2. Sell tickets");
            System.out.println("3. Display show report");
            System.out.println("4. Exit");
            option = readInt(input, "Choose an option: ");

            try {
                switch (option) {
                    case 1:
                        manageShow(input);
                        break;
                    case 2:
                        sellTicket(input);
                        break;
                    case 3:
                        showReport(input);
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Please choose an option from 1 to 4.");
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
        input.close();
    }

    public static void manageShow(Scanner input) throws IOException {
        String showName = readName(input, "Enter the show name: ");
        File file = new File(showName + ".txt");

        if (file.exists()) {
            System.out.println("That show already exists.");
            return;
        }

        int numberOfTickets = readInt(input, "How many tickets? ");
        while (numberOfTickets <= 0) {
            numberOfTickets = readInt(input, "Enter a positive number of tickets: ");
        }

        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= numberOfTickets; i++) {
            tickets.add(new Ticket(i));
        }
        saveTickets(file, tickets);
        System.out.println("Show created with " + numberOfTickets + " tickets.");
    }

    public static void sellTicket(Scanner input) throws IOException {
        String showName = readName(input, "Enter the show name: ");
        File file = new File(showName + ".txt");

        if (!file.isFile()) {
            System.out.println("Show not found.");
            return;
        }

        ArrayList<Ticket> tickets = readTickets(file);
        int available = 0;
        for (Ticket ticket : tickets) {
            if (!ticket.isSold()) {
                available++;
            }
        }

        if (available == 0) {
            System.out.println("This show is sold out.");
            return;
        }

        System.out.println("Seat numbers: 1-" + tickets.size());
        System.out.println("Available tickets: " + available);
        while (true) {
            int seat = readInt(input, "Enter the ticket number (0 to cancel): ");
            if (seat == 0) {
                return;
            }
            if (seat < 1 || seat > tickets.size()) {
                System.out.println("Invalid ticket number. Try again.");
            } else {
                Ticket ticket = tickets.get(seat - 1);
                if (ticket.isSold()) {
                    System.out.println("That seat is already sold. Choose another seat.");
                } else {
                    String name = readName(input, "Enter purchaser name: ");
                    ticket.sellTicket(name);
                    saveTickets(file, tickets);
                    System.out.println("Ticket sold!");
                    System.out.println("Confirmation code: " + ticket.getVerificationCode());
                    return;
                }
            }
        }
    }

    public static void showReport(Scanner input) throws IOException {
        String showName = readName(input, "Enter the show name: ");
        File file = new File(showName + ".txt");

        if (!file.isFile()) {
            System.out.println("Show not found.");
            return;
        }

        ArrayList<Ticket> tickets = readTickets(file);
        System.out.println("\nShow Report: " + showName);
        System.out.printf("%-10s %-25s %s%n", "Seat #", "Purchaser", "Confirmation code");
        for (Ticket ticket : tickets) {
            String name = ticket.getPurchaserName();
            if (name.isEmpty()) {
                name = "(Available)";
            }
            System.out.printf("%-10d %-25s %d%n", ticket.getTicketNumber(),
                    name, ticket.getVerificationCode());
        }
    }

    // Each ticket is stored as three lines: seat number, name, and code.
    public static void saveTickets(File file, ArrayList<Ticket> tickets) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Ticket ticket : tickets) {
                writer.println(ticket.getTicketNumber());
                writer.println(ticket.getPurchaserName());
                writer.println(ticket.getVerificationCode());
            }
            if (writer.checkError()) {
                throw new IOException("Could not save the show file.");
            }
        }
    }

    public static ArrayList<Ticket> readTickets(File file) throws IOException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                int number = Integer.parseInt(reader.nextLine());
                String name = reader.nextLine();
                int code = Integer.parseInt(reader.nextLine());
                tickets.add(new Ticket(number, name, code));
            }
        }
        return tickets;
    }

    public static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = input.nextLine();
            try {
                return Integer.parseInt(answer.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    public static String readName(Scanner input, String prompt) {
        System.out.print(prompt);
        String name = input.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Please enter a name: ");
            name = input.nextLine().trim();
        }
        return name;
    }
}
