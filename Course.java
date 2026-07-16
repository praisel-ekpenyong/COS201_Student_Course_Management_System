import java.util.Locale;

// Represents a validated, immutable course record.
public class Course {
    private static final String FILE_DELIMITER = "|";

    private final String code;
    private final String title;
    private final int units;

    public Course(String code, String title, int units) {
        this.code = normalizeRequired(code, "Course code").toUpperCase(Locale.ROOT);
        this.title = normalizeRequired(title, "Course title");

        if (this.code.contains(FILE_DELIMITER) || this.title.contains(FILE_DELIMITER)) {
            throw new IllegalArgumentException("Course code and title cannot contain '|'.");
        }
        if (units <= 0) {
            throw new IllegalArgumentException("Units must be a positive integer.");
        }

        this.units = units;
    }

    private static String normalizeRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public int getUnits() {
        return units;
    }

    public String toFileLine() {
        return code + "|" + title + "|" + units;
    }

    public static Course fromFileLine(String line) throws IllegalArgumentException {
        if (line == null) {
            throw new IllegalArgumentException("Course record cannot be null.");
        }

        String[] parts = line.split("\\|", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid course record: " + line);
        }

        try {
            int units = Integer.parseInt(parts[2].trim());
            return new Course(parts[0], parts[1], units);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid units in record: " + line, ex);
        }
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-30s | %3d |", code, title, units);
    }
}