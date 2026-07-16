import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

// Console-based entry point for the Student Course Management System.
public class Main {
    private static final String DEFAULT_FILENAME = "courses.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final CourseManager manager = new CourseManager();

    public static void main(String[] args) {
        printBanner();
        runMenu(0);
        scanner.close();
    }

    // Recursively displays the menu until the user chooses to exit.
    private static void runMenu(int depth) {
        if (depth > 500) {
            System.out.println("\nSession limit reached. Goodbye!");
            return;
        }

        printMenu();
        int choice = getValidChoice();

        if (handleChoice(choice)) {
            System.out.println();
            runMenu(depth + 1);
        }
    }

    private static boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                handleAddCourse();
                return true;
            case 2:
                handleViewCourses();
                return true;
            case 3:
                handleSearchCourse();
                return true;
            case 4:
                handleComputeUnits();
                return true;
            case 5:
                handleSave();
                return true;
            case 6:
                handleLoad();
                return true;
            case 7:
                System.out.println("\nThank you for using the Course Management System. Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice. Please choose 1 - 7.");
                return true;
        }
    }

    private static void printBanner() {
        System.out.println("=============================================");
        System.out.println("   STUDENT COURSE MANAGEMENT SYSTEM         ");
        System.out.println("=============================================");
        System.out.println();
    }

    private static void printMenu() {
        System.out.println("----------------- MENU -----------------");
        System.out.println(" 1. Add Course");
        System.out.println(" 2. View All Courses");
        System.out.println(" 3. Search Course by Code");
        System.out.println(" 4. Compute Total Units");
        System.out.println(" 5. Save to File");
        System.out.println(" 6. Load from File");
        System.out.println(" 7. Exit Program");
        System.out.println("----------------------------------------");
    }

    // Validates menu input using a loop and exception handling.
    private static int getValidChoice() {
        while (true) {
            System.out.print("\nEnter your choice (1-7): ");
            String input = scanner.nextLine().trim();


            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 7) {
                    return choice;
                }
                System.out.println("Error: Please enter a number between 1 and 7.");
            } catch (NumberFormatException ex) {
                System.out.println("Error: Invalid input. Please enter a valid number.");
            }
        }
    }

    private static String getNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Error: Input cannot be empty. Please try again.");
        }
    }

    private static int getPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: Value cannot be empty.");
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Error: Units must be greater than zero.");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Error: Please enter a valid whole number.");
            }
        }
    }

    private static void handleAddCourse() {
        System.out.println("\n--- Add Course ---");
        try {
            String code = getNonEmptyString("Enter course code (e.g., COS201): ");
            String title = getNonEmptyString("Enter course title: ");
            int units = getPositiveInt("Enter credit units: ");
            manager.addCourse(code, title, units);
            System.out.println("\nCourse '" + code.toUpperCase(Locale.ROOT) + "' added successfully!");
        } catch (IllegalArgumentException ex) {
            System.out.println("\nError: " + ex.getMessage());
        }
    }

    private static void handleViewCourses() {
        System.out.println("\n--- All Courses ---");
        manager.displayAll();
    }

    private static void handleSearchCourse() {
        System.out.println("\n--- Search Course by Code ---");
        String code = getNonEmptyString("Enter course code to search: ");
        Course result = manager.searchByCode(code);

        if (result != null) {
            System.out.println("\nCourse found:\n  " + result);
        } else {
            System.out.println("\nNo course found with code '" + code.toUpperCase(Locale.ROOT) + "'.");
        }
    }

    private static void handleComputeUnits() {
        System.out.println("\n--- Total Credit Units ---");
        long total = manager.computeTotalUnits();
        int count = manager.getCourseCount();

        if (count == 0) {
            System.out.println("No courses recorded. Total units: 0");
        } else {
            System.out.println("Total courses: " + count);
            System.out.println("Total credit units: " + total);
        }
    }

    private static void handleSave() {
        System.out.println("\n--- Save to File ---");
        System.out.print("Enter filename [" + DEFAULT_FILENAME + "]: ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = DEFAULT_FILENAME;
        }

        try {
            manager.saveToFile(filename);
            System.out.println("\nCourses saved successfully to '" + filename + "'.");
        } catch (IOException ex) {
            System.out.println("\nError: " + ex.getMessage());
        }
    }

    private static void handleLoad() {
        System.out.println("\n--- Load from File ---");
        System.out.print("Enter filename [" + DEFAULT_FILENAME + "]: ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = DEFAULT_FILENAME;
        }

        try {
            manager.loadFromFile(filename);
            System.out.println("\nCourses loaded successfully from '" + filename + "'.");
            System.out.println("Loaded " + manager.getCourseCount() + " course(s).");
        } catch (IOException ex) {
            System.out.println("\nError: " + ex.getMessage());
        }
    }
}