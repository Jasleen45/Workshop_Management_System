package services;

import models.Task;
import models.Employee;

import java.io.*;
import java.util.*;

public class TaskService {
    private Map<String, Task> tasks = new HashMap<>();
    private final String FILE_PATH = "data/tasks.txt";
    private EmployeeService employeeService;

    public TaskService() {
        this.employeeService = new EmployeeService();
    }

    // Load tasks from file
    public void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                tasks.put(data[0], new Task(data[0], data[1], data[2], data[3], Integer.parseInt(data[4])));
            }
            // Remove debug message
            // System.out.println("Successfully loaded " + tasks.size() + " tasks from file.");
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save tasks to file
    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task task : tasks.values()) {
                writer.write(task.getTaskId() + "," + task.getDescription() + "," + 
                             task.getAssignedTo() + "," + task.getStatus() + "," + task.getPriority());
                writer.newLine();
            }
            System.out.println("Tasks saved successfully. Total tasks: " + tasks.size());
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Assign task
    public void assignTask(Task task, EmployeeService employeeService) {
        // Check if employee exists
        String employeeId = task.getAssignedTo();
        Employee employee = employeeService.getEmployeeById(employeeId);
        
        if (employee == null) {
            System.out.println("ERROR: Cannot assign task. Employee with ID '" + employeeId + "' does not exist.");
            return;
        }
        
        // Check if task ID is already in use
        if (tasks.containsKey(task.getTaskId())) {
            System.out.println("ERROR: Task ID '" + task.getTaskId() + "' is already in use. Please use a unique ID.");
            return;
        }
        
        tasks.put(task.getTaskId(), task);
        saveTasks(); // Save immediately after assigning
        System.out.println("Task '" + task.getTaskId() + "' has been assigned to " + employee.getName() + 
                          " (ID: " + employeeId + ") successfully!");
    }
    
    // Update task status
    public void updateTaskStatus(String taskId, String newStatus) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setStatus(newStatus);
            saveTasks(); // Save immediately after updating
            System.out.println("Task '" + taskId + "' status updated to '" + newStatus + "' successfully!");
        } else {
            System.out.println("Task with ID '" + taskId + "' not found.");
        }
    }

    // View all tasks
    public void printTasks(EmployeeService employeeService) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("\n===== Task List =====");
            System.out.println("-------------------------");
            for (Task task : tasks.values()) {
                // Get employee name for the assigned employee ID
                String empId = task.getAssignedTo();
                String empName = "Unknown";
                
                Employee employee = employeeService.getEmployeeById(empId);
                if (employee != null) {
                    empName = employee.getName();
                }
                
                System.out.println("ID: " + task.getTaskId());
                System.out.println("Description: " + task.getDescription());
                System.out.println("Assigned to: " + empName + " (ID: " + empId + ")");
                System.out.println("Status: " + task.getStatus());
                System.out.println("Priority: " + task.getPriority());
                System.out.println("-------------------------");
            }
        }
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
}
