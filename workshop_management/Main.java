import services.*;
import models.*;
import utils.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize services and other components
        Scanner scanner = new Scanner(System.in);
        EmployeeService employeeService = new EmployeeService();
        TaskService taskService = new TaskService();
        InventoryService inventoryService = new InventoryService();
        ReportGenerator reportGenerator = new ReportGenerator();
        
        // Load data from files
        employeeService.loadEmployees();
        taskService.loadTasks();
        inventoryService.loadInventory();
        
        // Login system
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println("\n===== Workshop Management System Login =====");
            System.out.println("(Enter 'exit' as username to quit the application)");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            if (username.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the system...");
                return;
            }
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            isLoggedIn = Authentication.authenticate(username, password);
            
            if (isLoggedIn) {
                System.out.println("Login successful! Welcome, " + 
                                  username + " (" + Authentication.getCurrentUserRole() + ")");
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
        
        while (true) {
            System.out.println("\n===== Workshop Management System =====");
            System.out.println("Current user: " + Authentication.getCurrentUserRole());
            System.out.println("=======================================");
            
            // Print menu items
            System.out.println("1. Add Employee " + 
                              (Authentication.isAdmin() ? "" : "(Admin only)"));
            System.out.println("2. Assign Task " + 
                              (Authentication.hasManagerAccess() ? "" : "(Admin/Manager only)"));
            System.out.println("3. View Tasks");
            System.out.println("4. View Employees");
            System.out.println("5. View Inventory");
            System.out.println("6. Edit Inventory " + 
                              (Authentication.hasManagerAccess() ? "" : "(Admin/Manager only)"));
            System.out.println("7. Generate Task Report");
            System.out.println("8. Search Tasks by Priority (Binary Search)");
            System.out.println("9. Update Task Status " + 
                              (Authentication.isAdminOrManager() ? "" : "(Admin/Manager only)"));
            System.out.println("10. View Task History " + 
                              (Authentication.isAdminOrManager() ? "" : "(Admin/Manager only)"));
            System.out.println("11. Logout");
            System.out.println("12. Exit");
            
            System.out.println("=======================================");
            System.out.print("Enter your choice: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input! Please enter a number.");
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                continue;
            }
            
            switch (choice) {
                case 1: // Add Employee (Admin only)
                    if (Authentication.isAdmin()) {
                        System.out.println("\n===== Add Employee =====");
                        System.out.println("(Enter '0' at any prompt to go back to main menu)");
                        
                        System.out.print("Enter employee name: ");
                        String name = scanner.nextLine();
                        if (name.equals("0")) {
                            System.out.println("Operation canceled. Returning to main menu...");
                            break;
                        }
                        
                        System.out.print("Enter employee role (Admin/Worker/Manager): ");
                        String role = scanner.nextLine();
                        if (role.equals("0")) {
                            System.out.println("Operation canceled. Returning to main menu...");
                            break;
                        }
                        
                        // Use the new method that automatically generates an ID
                        employeeService.addEmployee(name, role);
                    } else {
                        System.out.println("ACCESS DENIED: Only Administrators can add employees.");
                    }
                    
                    // Add a pause so user can see the confirmation message
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;

                case 2: // Assign Task (Admin/Manager only)
                    if (Authentication.hasManagerAccess()) {
                        System.out.println("\n===== Assign Task =====");
                        System.out.println("(Enter '0' at any prompt to go back to main menu)");
                        
                        // Show all employees first so the user can see the available employee IDs
                        employeeService.viewEmployees();
                        
                        System.out.print("\nEnter Task description: ");
                        String taskDesc = scanner.nextLine();
                        if (taskDesc.equals("0")) {
                            System.out.println("Operation canceled. Returning to main menu...");
                            break;
                        }
                        
                        System.out.print("Enter Employee ID to assign the task: ");
                        String assignedTo = scanner.nextLine();
                        if (assignedTo.equals("0")) {
                            System.out.println("Operation canceled. Returning to main menu...");
                            break;
                        }
                        
                        System.out.print("Enter Task priority (1=High, 2=Medium, 3=Low) or 0 to cancel: ");
                        int priority;
                        try {
                            priority = scanner.nextInt();
                            scanner.nextLine();  // Consume newline
                            if (priority == 0) {
                                System.out.println("Operation canceled. Returning to main menu...");
                                break;
                            }
                        } catch (InputMismatchException e) {
                            scanner.nextLine(); // Clear invalid input
                            System.out.println("Invalid priority. Operation canceled.");
                            break;
                        }
                        
                        // Use the new method that automatically generates a task ID
                        taskService.assignTask(taskDesc, assignedTo, priority, employeeService);
                    } else {
                        System.out.println("ACCESS DENIED: Only Administrators and Managers can assign tasks.");
                    }
                    
                    // Add a pause so user can see the confirmation message
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;

                case 3: // View Tasks (All roles)
                    // Use the improved printTasks method that shows employee names
                    taskService.printTasks(employeeService);
                    
                    // Add a pause after viewing
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;

                case 4: // View Employees (All roles)
                    employeeService.viewEmployees();
                    
                    // Add a pause after viewing
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;

                case 5: // View Inventory (All roles)
                    inventoryService.viewInventory();
                    
                    // Add a pause after viewing
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                    
                case 6: // Edit Inventory (Admin/Manager only)
                    if (Authentication.hasManagerAccess()) {
                        boolean returnToMainMenu = false;
                        
                        while (!returnToMainMenu) {
                            System.out.println("\n===== Edit Inventory =====");
                            System.out.println("1. Add New Item");
                            System.out.println("2. Update Item Quantity");
                            System.out.println("3. Remove Item");
                            System.out.println("4. Back to Main Menu");
                            System.out.print("Enter your choice: ");
                            
                            int inventoryChoice;
                            try {
                                inventoryChoice = scanner.nextInt();
                                scanner.nextLine();  // Consume newline
                            } catch (InputMismatchException e) {
                                scanner.nextLine(); // Clear invalid input
                                System.out.println("Invalid choice. Please enter a number.");
                                continue;
                            }
                            
                            switch (inventoryChoice) {
                                case 1: // Add new item
                                    System.out.println("(Enter '0' at any prompt to go back)");
                                    System.out.print("Enter Item ID: ");
                                    String itemId = scanner.nextLine();
                                    if (itemId.equals("0")) {
                                        System.out.println("Operation canceled.");
                                        continue;
                                    }
                                    
                                    System.out.print("Enter Item Name: ");
                                    String itemName = scanner.nextLine();
                                    if (itemName.equals("0")) {
                                        System.out.println("Operation canceled.");
                                        continue;
                                    }
                                    
                                    System.out.print("Enter Quantity or 0 to cancel: ");
                                    int quantity;
                                    try {
                                        quantity = scanner.nextInt();
                                        scanner.nextLine();  // Consume newline
                                        if (quantity == 0) {
                                            System.out.println("Operation canceled.");
                                            continue;
                                        }
                                    } catch (InputMismatchException e) {
                                        scanner.nextLine(); // Clear invalid input
                                        System.out.println("Invalid quantity. Operation canceled.");
                                        continue;
                                    }
                                    
                                    inventoryService.addItem(itemId, itemName, quantity);
                                    System.out.println("\nPress Enter to continue...");
                                    scanner.nextLine();
                                    break;
                                
                                case 2: // Update quantity
                                    System.out.println("(Enter '0' at any prompt to go back)");
                                    System.out.print("Enter Item ID: ");
                                    String updateId = scanner.nextLine();
                                    if (updateId.equals("0")) {
                                        System.out.println("Operation canceled.");
                                        continue;
                                    }
                                    
                                    System.out.print("Enter New Quantity or 0 to cancel: ");
                                    int newQuantity;
                                    try {
                                        newQuantity = scanner.nextInt();
                                        scanner.nextLine();  // Consume newline
                                        if (newQuantity == 0) {
                                            System.out.println("Operation canceled.");
                                            continue;
                                        }
                                    } catch (InputMismatchException e) {
                                        scanner.nextLine(); // Clear invalid input
                                        System.out.println("Invalid quantity. Operation canceled.");
                                        continue;
                                    }
                                    
                                    inventoryService.updateQuantity(updateId, newQuantity);
                                    System.out.println("\nPress Enter to continue...");
                                    scanner.nextLine();
                                    break;
                                
                                case 3: // Remove item
                                    System.out.println("(Enter '0' to go back)");
                                    System.out.print("Enter Item ID to remove: ");
                                    String removeId = scanner.nextLine();
                                    if (removeId.equals("0")) {
                                        System.out.println("Operation canceled.");
                                        continue;
                                    }
                                    
                                    inventoryService.removeItem(removeId);
                                    System.out.println("\nPress Enter to continue...");
                                    scanner.nextLine();
                                    break;
                                
                                case 4: // Back to main menu
                                    returnToMainMenu = true;
                                    break;
                                
                                default:
                                    System.out.println("Invalid choice! Please try again.");
                                    System.out.println("\nPress Enter to continue...");
                                    scanner.nextLine();
                            }
                        }
                    } else {
                        System.out.println("ACCESS DENIED: Only Administrators and Managers can edit inventory.");
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                    }
                    break;

                case 7: // Generate Task Report (All roles)
                    System.out.println("\n===== Generate Task Report =====");
                    System.out.print("Generate report? (Y/N): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("N")) {
                        System.out.println("Report generation canceled.");
                        break;
                    }
                    
                    List<Task> tasks = taskService.getAllTasks();
                    reportGenerator.generateTaskReport(tasks);
                    System.out.println("Task report generated and saved to task_report.txt");
                    
                    // Add a pause so user can see the confirmation message
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                
                case 8: // Search Tasks by Priority using Binary Search (All roles)
                    System.out.println("\n===== Search Tasks by Priority (Binary Search) =====");
                    System.out.print("Enter priority to search (1=High, 2=Medium, 3=Low): ");
                    int searchPriority;
                    try {
                        searchPriority = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                    } catch (InputMismatchException e) {
                        scanner.nextLine(); // Clear invalid input
                        System.out.println("Invalid priority. Operation canceled.");
                        break;
                    }
                    
                    // Use binary search to find and display tasks with the given priority
                    taskService.printTasksByPriority(searchPriority, employeeService);
                    
                    // Add a pause so user can see the results
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                    
                case 9: // Update Task Status (Admin/Manager only)
                    if (Authentication.isAdminOrManager()) {
                        System.out.println("\n===== Update Task Status =====");
                        
                        // Show all tasks first so the user can see the available task IDs
                        System.out.println("\nAvailable tasks:");
                        taskService.printTasks(employeeService);
                        
                        System.out.print("\nEnter task ID to update: ");
                        String updateTaskId = scanner.nextLine();
                        
                        System.out.println("\nSelect new status:");
                        System.out.println("1. To Do");
                        System.out.println("2. In Progress");
                        System.out.println("3. Completed");
                        System.out.print("Enter your choice: ");
                        
                        int statusChoice = 0;
                        try {
                            statusChoice = scanner.nextInt();
                            scanner.nextLine();  // Consume newline
                        } catch (InputMismatchException e) {
                            scanner.nextLine(); // Clear invalid input
                            System.out.println("Invalid choice. Operation canceled.");
                            break;
                        }
                        
                        switch (statusChoice) {
                            case 1:
                                taskService.markTaskAsTodo(updateTaskId);
                                break;
                            case 2:
                                taskService.markTaskAsInProgress(updateTaskId);
                                break;
                            case 3:
                                taskService.markTaskAsCompleted(updateTaskId);
                                break;
                            default:
                                System.out.println("Invalid status choice.");
                        }
                        
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                    } else {
                        System.out.println("ACCESS DENIED: Only Administrators and Managers can update task status.");
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                    }
                    break;
                    
                case 10: // View Task History (Admin/Manager only)
                    if (Authentication.isAdminOrManager()) {
                        System.out.println("\n===== View Task History =====");
                        System.out.print("Enter task ID (or 'all' to see recent history): ");
                        String historyTaskId = scanner.nextLine();
                        
                        if (historyTaskId.equalsIgnoreCase("all")) {
                            taskService.viewRecentHistory(10); // Show last 10 task history entries
                        } else {
                            taskService.viewTaskHistory(historyTaskId);
                        }
                        
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                    } else {
                        System.out.println("ACCESS DENIED: Only Administrators and Managers can view task history.");
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                    }
                    break;
                    
                case 11: // Logout
                    System.out.print("Are you sure you want to logout? (Y/N): ");
                    String logoutConfirm = scanner.nextLine();
                    if (!logoutConfirm.equalsIgnoreCase("Y")) {
                        System.out.println("Logout canceled.");
                        break;
                    }
                    
                    Authentication.logout();
                    System.out.println("You have been logged out successfully.");
                    
                    // Return to login screen
                    isLoggedIn = false;
                    while (!isLoggedIn) {
                        System.out.println("\n===== Workshop Management System Login =====");
                        System.out.println("(Enter 'exit' as username to quit the application)");
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        
                        if (username.equalsIgnoreCase("exit")) {
                            System.out.println("Exiting the system...");
                            return;
                        }
                        
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        
                        isLoggedIn = Authentication.authenticate(username, password);
                        
                        if (isLoggedIn) {
                            System.out.println("Login successful! Welcome, " + 
                                              username + " (" + Authentication.getCurrentUserRole() + ")");
                        } else {
                            System.out.println("Invalid username or password. Please try again.");
                        }
                    }
                    break;

                case 12: // Exit
                    System.out.print("Are you sure you want to exit? All unsaved changes will be saved. (Y/N): ");
                    String exitConfirm = scanner.nextLine();
                    if (!exitConfirm.equalsIgnoreCase("Y")) {
                        System.out.println("Exit canceled.");
                        break;
                    }
                    
                    System.out.println("Exiting the system...");
                    employeeService.saveEmployees();
                    taskService.saveTasks();
                    inventoryService.saveInventory();
                    System.out.println("All changes have been saved. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
                    
                    // Add a pause after error message
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
            }
        }
    }
}
