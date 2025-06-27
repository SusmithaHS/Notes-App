import java.io.*;
import java.util.Scanner;
public class Notes{
    private static final String FILE_NAME = "notes.txt";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n Notes App Menu");
            System.out.println("1. Write a Note");
            System.out.println("2. Read Notes");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    writeNote(scanner);
                    break;
                case 2:
                    readNotes();
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting Notes App.");
                    break;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
        scanner.close();
    }
    private static void writeNote(Scanner scanner) {
        System.out.print("Enter your note: ");
        String note = scanner.nextLine();
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(note + "\n");
            System.out.println("Note saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing note: " + e.getMessage());
        }
    }
    private static void readNotes() {
        System.out.println("\n Your Notes ");
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("- " + line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No notes found. Create a note first.");
        } catch (IOException e) {
            System.out.println("Error reading notes: " + e.getMessage());
        }
    }
}
