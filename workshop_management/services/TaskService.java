package services;

import models.Task;
import models.Employee;
import utils.TaskHistoryLinkedList;

import java.io.*;
import java.util.*;

public class TaskService {
    private Map<String, Task> tasks = new HashMap<>();
    private final String FILE_PATH = "data/tasks.txt";
    private EmployeeService employeeService;
    private TaskHistoryLinkedList taskHistory;
    private int nextTaskId = 1;

    public TaskService() {
        this.employeeService = new EmployeeService();
        this.taskHistory = new TaskHistoryLinkedList();
    }
    
    // Generate next task ID
    private String generateTaskId() {
        // Find the largest existing ID to ensure we don't have collisions
        for (String taskId : tasks.keySet()) {
            try {
                int idNum = Integer.parseInt(taskId);
                if (idNum >= nextTaskId) {
                    nextTaskId = idNum + 1;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
        
        String newId = String.valueOf(nextTaskId);
        nextTaskId++;
        return newId;
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

    // Assign task with auto-generated ID
    public void assignTask(String description, String assignedTo, int priority, EmployeeService employeeService) {
        // Check if employee exists
        Employee employee = employeeService.getEmployeeById(assignedTo);
        
        if (employee == null) {
            System.out.println("ERROR: Cannot assign task. Employee with ID '" + assignedTo + "' does not exist.");
            return;
        }
        
        // Generate a new task ID
        String taskId = generateTaskId();
        
        // Create the task with the generated ID
        Task task = new Task(taskId, description, assignedTo, Task.STATUS_TODO, priority);
        
        tasks.put(taskId, task);
        
        // Record in history linked list
        taskHistory.addHistory(taskId, "CREATION", "N/A", 
                              "Assigned to " + assignedTo + " with priority " + priority);
        
        saveTasks();
        System.out.println("Task '" + taskId + "' (auto-generated) has been assigned to " + employee.getName() + 
                          " (ID: " + assignedTo + ") successfully!");
    }
    
    // Keep the original method for compatibility with existing code
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
        
        // Record in history linked list
        taskHistory.addHistory(task.getTaskId(), "CREATION", "N/A", 
                              "Assigned to " + employeeId + " with priority " + task.getPriority());
        
        saveTasks();
        System.out.println("Task '" + task.getTaskId() + "' has been assigned to " + employee.getName() + 
                          " (ID: " + employeeId + ") successfully!");
    }
    
    // Enhanced update task status with history tracking
    public void updateTaskStatus(String taskId, String newStatus) {
        Task task = tasks.get(taskId);
        if (task != null) {
            String oldStatus = task.getStatus();
            
            // Only record and update if status is actually changing
            if (!oldStatus.equals(newStatus)) {
                task.setStatus(newStatus);
                
                // Record in history linked list
                taskHistory.addHistory(taskId, "STATUS_CHANGE", oldStatus, newStatus);
                
                saveTasks();
                System.out.println("Task '" + taskId + "' status updated from '" + oldStatus + 
                                  "' to '" + newStatus + "' successfully!");
            } else {
                System.out.println("Task '" + taskId + "' already has status '" + newStatus + "'.");
            }
        } else {
            System.out.println("Task with ID '" + taskId + "' not found.");
        }
    }
    
    // Manager methods for task status manipulation
    public void markTaskAsCompleted(String taskId) {
        updateTaskStatus(taskId, Task.STATUS_COMPLETED);
    }
    
    public void markTaskAsTodo(String taskId) {
        updateTaskStatus(taskId, Task.STATUS_TODO);
    }
    
    public void markTaskAsInProgress(String taskId) {
        updateTaskStatus(taskId, Task.STATUS_IN_PROGRESS);
    }

    // View task history
    public void viewTaskHistory(String taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Task with ID '" + taskId + "' not found.");
            return;
        }
        taskHistory.printTaskHistory(taskId);
    }
    
    // View recent history across all tasks
    public void viewRecentHistory(int limit) {
        taskHistory.printFullHistory(limit);
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
    
    // Get sorted tasks by priority (required for binary search)
    public List<Task> getSortedTasksByPriority() {
        List<Task> taskList = getAllTasks();
        taskList.sort(Comparator.comparingInt(Task::getPriority));
        return taskList;
    }
    
    // Binary search to find tasks with a given priority
    public List<Task> findTasksByPriority(int priority) {
        List<Task> sortedTasks = getSortedTasksByPriority();
        List<Task> result = new ArrayList<>();
        
        // If no tasks, return empty list
        if (sortedTasks.isEmpty()) {
            return result;
        }
        
        // Perform binary search to find first occurrence
        int index = binarySearch(sortedTasks, priority);
        
        // If priority not found, return empty list
        if (index == -1) {
            return result;
        }
        
        // Find all tasks with the given priority (there may be multiple)
        // First go backward to find the first occurrence
        int firstIndex = index;
        while (firstIndex > 0 && sortedTasks.get(firstIndex - 1).getPriority() == priority) {
            firstIndex--;
        }
        
        // Then collect all tasks with the same priority
        for (int i = firstIndex; i < sortedTasks.size() && sortedTasks.get(i).getPriority() == priority; i++) {
            result.add(sortedTasks.get(i));
        }
        
        return result;
    }
    
    // Binary search algorithm implementation
    private int binarySearch(List<Task> tasks, int targetPriority) {
        int left = 0;
        int right = tasks.size() - 1;
        
        // Keep track of result index
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midPriority = tasks.get(mid).getPriority();
            
            if (midPriority == targetPriority) {
                // Found a match, but continue search to find the first occurrence
                result = mid;
                right = mid - 1;  // Continue search in the left half
            } else if (midPriority < targetPriority) {
                left = mid + 1;  // Search in the right half
            } else {
                right = mid - 1;  // Search in the left half
            }
        }
        
        return result;
    }
    
    // Print tasks with a specific priority using binary search
    public void printTasksByPriority(int priority, EmployeeService employeeService) {
        List<Task> matchingTasks = findTasksByPriority(priority);
        
        if (matchingTasks.isEmpty()) {
            System.out.println("No tasks found with priority " + priority + ".");
        } else {
            System.out.println("\n===== Tasks with Priority " + priority + " =====");
            System.out.println("-------------------------");
            for (Task task : matchingTasks) {
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
}
