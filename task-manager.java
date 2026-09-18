import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;

public class StudentTaskManager {

    public static void main(String[] args) {
        CLIInterface app = new CLIInterface();
        app.run();
    }
}

class Task {
    private String title;
    private String dueDate;
    private String category;
    private String status;
    private String createdAt;

    public Task(String title, String dueDate, String category) {
        this.title = title;
        this.dueDate = dueDate;
        this.category = category;
        this.status = "Pending";
        this.createdAt = LocalDate.now().toString();
    }

    public Task(String title, String dueDate, String category, String status, String createdAt) {
        this.title = title;
        this.dueDate = dueDate;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class TaskManager {
    private final ArrayList<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "tasks_data.txt";
    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    public TaskManager() {
        loadData();
    }

    public void addTask(String title, String dueDate, String category) {
        try {
            Task task = new Task(title, dueDate, category);
            tasks.add(task);
            saveData();

            logger.info("Added task: " + title);
            System.out.println("Task added successfully.");
        } catch (Exception e) {
            logger.warning("Could not add task: " + e.getMessage());
            System.out.println("Something went wrong while adding the task.");
        }
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks found.");
            return;
        }

        System.out.printf("\n%-5s %-20s %-12s %-12s %-15s%n",
                "ID", "Title", "Due Date", "Status", "Category");
        System.out.println("----------------------------------------------------------------");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.printf("%-5d %-20s %-12s %-12s %-15s%n",
                    i + 1,
                    task.getTitle(),
                    task.getDueDate(),
                    task.getStatus(),
                    task.getCategory());
        }
    }

    public void markComplete(int id) {
        try {
            if (id >= 1 && id <= tasks.size()) {
                tasks.get(id - 1).setStatus("Completed");
                saveData();

                logger.info("Completed task ID: " + id);
                System.out.println("Task marked as completed.");
            } else {
                System.out.println("Invalid Task ID.");
            }
        } catch (Exception e) {
            logger.warning("Error completing task: " + e.getMessage());
        }
    }

    public void deleteTask(int id) {
        try {
            if (id >= 1 && id <= tasks.size()) {
                Task task = tasks.remove(id - 1);
                saveData();

                logger.info("Deleted task: " + task.getTitle());
                System.out.println("Task deleted.");
            } else {
                System.out.println("Invalid Task ID.");
            }
        } catch (Exception e) {
            logger.warning("Error deleting task: " + e.getMessage());
        }
    }

    private void saveData() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_NAME))) {
            for (Task task : tasks) {
                // Sanitizing potential pipe entries to prevent data layout breaks
                String cleanTitle = task.getTitle().replace("|", "-");
                String cleanCat = task.getCategory().replace("|", "-");

                writer.write(cleanTitle + "|" +
                        task.getDueDate() + "|" +
                        cleanCat + "|" +
                        task.getStatus() + "|" +
                        task.getCreatedAt());
                writer.newLine();
            }
        } catch (IOException e) {
            logger.warning("Could not save tasks: " + e.getMessage());
        }
    }

    private void loadData() {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    Task task = new Task(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4]
                    );
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            logger.warning("Could not load saved tasks.");
            tasks.clear();
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}

class AnalyticsEngine {

    public static void generateReport(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("\nNot enough data for analytics.");
            return;
        }

        int total = tasks.size();
        int completed = 0;

        for (Task task : tasks) {
            if ("Completed".equalsIgnoreCase(task.getStatus())) {
                completed++;
            }
        }

        int pending = total - completed;
        int completionRate = (completed * 100) / total;

        HashMap<String, Integer> categories = new HashMap<>();

        for (Task task : tasks) {
            String category = task.getCategory();
            categories.put(category, categories.getOrDefault(category, 0) + 1);
        }

        System.out.println("\n--- Productivity Analytics ---");
        System.out.println("Total Tasks: " + total);
        System.out.println("Completed Tasks: " + completed);
        System.out.println("Pending Tasks: " + pending);
        System.out.println("Completion Rate: " + completionRate + "%");

        System.out.println("\nTasks by Category:");
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }
}

class CLIInterface {
    private final TaskManager manager;
    private final Scanner scanner;

    public CLIInterface() {
        manager = new TaskManager();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Student Task Manager ===");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Mark Task Complete");
            System.out.println("4. Delete Task");
            System.out.println("5. View Analytics");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    manager.viewTasks();
                    break;
                case "3":
                    completeTask();
                    break;
                case "4":
                    deleteTask();
                    break;
                case "5":
                    AnalyticsEngine.generateReport(manager.getTasks());
                    break;
                case "6":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter due date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();

        if (title.isEmpty() || date.isEmpty() || category.isEmpty()) {
            System.out.println("Input fields cannot be blank.");
            return;
        }

        manager.addTask(title, date, category);
    }

    private void completeTask() {
        if (manager.getTasks().isEmpty()) {
            System.out.println("\nNo tasks available to mark complete.");
            return;
        }

        manager.viewTasks();
        try {
            System.out.print("Enter Task ID to complete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            manager.markComplete(id);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric ID.");
        }
    }

    private void deleteTask() {
        if (manager.getTasks().isEmpty()) {
            System.out.println("\nNo tasks available to delete.");
            return;
        }

        manager.viewTasks();
        try {
            System.out.print("Enter Task ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            manager.deleteTask(id);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric ID.");
        }
    }
}
