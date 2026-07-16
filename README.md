# Student Course Management System

A Java console application for the COS 201 Mid-Semester Lab Assessment at MIVA Open University. It helps students record and manage semester courses: add courses, view them, search by code, compute total credit units, and save or load the list from a text file.

## Project Layout

```text
course_management_system/
|-- Course.java
|-- CourseManager.java
|-- Main.java
|-- courses.txt
|-- Course_Managment_System_Report.docx
|-- screenshot.png
`-- README.md
```

## How to Compile and Run

Open a terminal in `course_management_system`. Running from this folder ensures that the default `courses.txt` path refers to the supplied sample file.

```bash
javac *.java
java Main
```

Java 11 or later is required.

## File Format and Validation

`courses.txt` stores one course per line, with fields separated by `|`:

```text
COS201|Programming I|3
MTH101|Mathematics|4
```

The following rules apply:

- Course codes and titles must be non-empty.
- Course codes are trimmed, converted to uppercase, and must be unique.
- Credit units must be positive whole numbers.
- Course codes and titles cannot contain the `|` delimiter.
- Loading is transactional. If any record is invalid, the existing in-memory list is preserved.

## Menu Options

1. Add Course
2. View All Courses
3. Search Course by Code
4. Compute Total Units
5. Save to File
6. Load from File
7. Exit Program

## Design Notes

- **Classes**: `Course` represents one validated, immutable record; `CourseManager` owns the collection and operations; `Main` provides the console interface.
- **Recursion**: `runMenu()` redisplays the menu, `recursiveSearch()` searches by course code, and `recursiveSumUnits()` computes the total credit units.
- **Loops**: input validation, course display, duplicate checking, and file reading/writing.
- **Exception handling**: `NumberFormatException`, `IllegalArgumentException`, `FileNotFoundException`, and `IOException` match the exceptions described in the report.
- **String processing**: `trim()` and locale-neutral `toUpperCase(Locale.ROOT)` normalize course codes.
- **File handling**: `FileReader` and `FileWriter` read and write the course list, while `FileNotFoundException` handles missing files.