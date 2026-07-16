import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Manages the course list and all core operations.
public class CourseManager {
    private final List<Course> courses;

    public CourseManager() {
        courses = new ArrayList<>();
    }

    public int getCourseCount() {
        return courses.size();
    }

    public void addCourse(String code, String title, int units) throws IllegalArgumentException {
        Course newCourse = new Course(code, title, units);

        for (Course course : courses) {
            if (course.getCode().equals(newCourse.getCode())) {
                throw new IllegalArgumentException(
                        "Course with code '" + newCourse.getCode() + "' already exists.");
            }
        }

        courses.add(newCourse);
    }

    public Course searchByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return recursiveSearch(code.trim().toUpperCase(Locale.ROOT), 0);
    }

    // Base case: return null when the end of the list is reached.
    private Course recursiveSearch(String code, int index) {
        if (index >= courses.size()) {
            return null;
        }
        if (courses.get(index).getCode().equals(code)) {
            return courses.get(index);
        }
        return recursiveSearch(code, index + 1);
    }

    public long computeTotalUnits() {
        return recursiveSumUnits(courses.size() - 1);
    }

    // Base case: return 0 when index goes below the first course.
    private long recursiveSumUnits(int index) {
        if (index < 0) {
            return 0L;
        }
        return courses.get(index).getUnits() + recursiveSumUnits(index - 1);
    }

    public void displayAll() {
        if (courses.isEmpty()) {
            System.out.println("No courses recorded yet.");
            return;
        }

        System.out.println();
        System.out.println("+------------+--------------------------------+------+");
        System.out.println("| Code       | Title                          | Units|");
        System.out.println("+------------+--------------------------------+------+");

        for (Course course : courses) {
            System.out.println(course);
        }

        System.out.println("+------------+--------------------------------+------+");
        System.out.println("Total courses: " + courses.size());
    }

    // Saves courses in CODE|TITLE|UNITS format.
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Course course : courses) {
                writer.write(course.toFileLine());
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new IOException("Failed to save courses to '" + filename + "': " + ex.getMessage(), ex);
        }
    }

    // Loads into a temporary list so invalid files do not erase current data.
    public void loadFromFile(String filename) throws IOException {
        List<Course> loadedCourses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    Course course = Course.fromFileLine(line);
                    if (containsCode(loadedCourses, course.getCode())) {
                        throw new IllegalArgumentException(
                                "Duplicate course code '" + course.getCode() + "'.");
                    }
                    loadedCourses.add(course);
                } catch (IllegalArgumentException ex) {
                    throw new IOException("Error on line " + lineNumber + ": " + ex.getMessage(), ex);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("File '" + filename + "' not found.");
        }

        courses.clear();
        courses.addAll(loadedCourses);
    }

    private static boolean containsCode(List<Course> courseList, String code) {
        for (Course course : courseList) {
            if (course.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

}
