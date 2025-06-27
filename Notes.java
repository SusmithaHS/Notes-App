import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
public class Notes {
    private static final String FILE_NAME = "notes.txt";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    static class Note {
        String title;
        List<String> content;
        String timestamp;
        Note(String title, List<String> content, String timestamp) {
            this.title = title;
            this.content = content;
            this.timestamp = timestamp;
        }
    }
    public static void main(String[] args) {
        loadNotesFromFile();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n--- Notes Manager ---");
            System.out.println("1. Add Note");
            System.out.println("2. View Notes");
            System.out.println("3. Delete Note by Title");
            System.out.println("4. Edit Note by Title");
            System.out.println("5. Search Notes by Keyword");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1 -> addNote(scanner);
                case 2 -> viewNotes();
                case 3 -> deleteNote(scanner);
                case 4 -> editNote(scanner);
                case 5 -> searchNotes(scanner);
                case 6 -> {
                    saveNotesToFile();
                    System.out.println("Exiting... Notes saved.");
                }
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 6);
        scanner.close();
    }
    private static void addNote(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        if (notes.containsKey(title)) {
            System.out.println("Title already exists. Try editing it instead.");
            return;
        }
        System.out.println("Enter content (enter 'END' in a new line to finish):");
        List<String> content = new ArrayList<>();
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) break;
            content.add(line);
        }
        String timestamp = sdf.format(new Date());
        notes.put(title, new Note(title, content, timestamp));
        System.out.println("Note saved.");
    }
    private static void viewNotes() {
        if (notes.isEmpty()) {
            System.out.println("No notes found.");
            return;
        }
        System.out.println("\n--- All Notes ---");
        for (Note note : notes.values()) {
            System.out.println("\nTitle: " + note.title);
            System.out.println("Created: " + note.timestamp);
            for (String line : note.content) {
                System.out.println("- " + line);
            }
        }
    }
    private static void deleteNote(Scanner scanner) {
        System.out.print("Enter title to delete: ");
        String title = scanner.nextLine();
        if (notes.remove(title) != null) {
            System.out.println("Note deleted.");
        } else {
            System.out.println("Title not found.");
        }
    }
    private static void editNote(Scanner scanner) {
        System.out.print("Enter title to edit: ");
        String title = scanner.nextLine();
        if (!notes.containsKey(title)) {
            System.out.println("Note not found.");
            return;
        }
        Note existingNote = notes.get(title);
        System.out.println("Existing content:");
        for (String line : existingNote.content) {
            System.out.println("- " + line);
        }
        System.out.println("Enter new content to append (enter 'END' to finish):");
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) break;
            existingNote.content.add(line);
        }
        existingNote.timestamp = sdf.format(new Date());
        System.out.println("Note updated (appended).");
    }
    private static void searchNotes(Scanner scanner) {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Note note : notes.values()) {
            if (note.title.toLowerCase().contains(keyword) ||
                    note.content.stream().anyMatch(line -> line.toLowerCase().contains(keyword))) {
                found = true;
                System.out.println("\nTitle: " + note.title);
                System.out.println("Created: " + note.timestamp);
                for (String line : note.content) {
                    System.out.println("- " + line);
                }
            }
        }
        if (!found) {
            System.out.println("No matching notes found.");
        }
    }
    private static void loadNotesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            String currentTitle = null;
            String timestamp = null;
            List<String> content = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("##")) {
                    if (currentTitle != null) {
                        notes.put(currentTitle, new Note(currentTitle, new ArrayList<>(content), timestamp));
                    }
                    currentTitle = line.substring(2).trim();
                    content.clear();
                } else if (line.startsWith("@@")) {
                    timestamp = line.substring(2).trim();
                } else {
                    content.add(line);
                }
            }
            if (currentTitle != null) {
                notes.put(currentTitle, new Note(currentTitle, content, timestamp));
            }
        } catch (IOException e) {
        }
    }
    private static void saveNotesToFile() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Note note : notes.values()) {
                writer.write("## " + note.title + "\n");
                writer.write("@@ " + note.timestamp + "\n");
                for (String line : note.content) {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving notes: " + e.getMessage());
        }
    }
}
