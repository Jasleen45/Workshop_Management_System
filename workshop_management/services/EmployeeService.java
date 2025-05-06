package services;

import models.Employee;

import java.io.*;
import java.util.*;

public class EmployeeService {
    private Map<String, Employee> employees = new HashMap<>();
    private final String FILE_PATH = "data/employees.txt";
    private int nextEmployeeId = 1;
    
    // Create data directory if it doesn't exist
    private void ensureDataDirectoryExists() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
        } catch (Exception e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    // Generate next employee ID
    private String generateEmployeeId() {
        // Find the largest existing ID to ensure we don't have collisions
        for (String empId : employees.keySet()) {
            try {
                int idNum = Integer.parseInt(empId);
                if (idNum >= nextEmployeeId) {
                    nextEmployeeId = idNum + 1;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
        
        String newId = String.valueOf(nextEmployeeId);
        nextEmployeeId++;
        return newId;
    }

    // Load employees from file
    public void loadEmployees() {
        ensureDataDirectoryExists();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            return;
        }
        
        // Clear existing employees to prevent duplicates on reload
        employees.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] data = line.split(",");
                if (data.length >= 3) {
                    try {
                        Employee employee = new Employee(data[0], data[1], data[2]);
                        employees.put(data[0], employee);
                    } catch (Exception e) {
                        System.err.println("Error parsing employee at line " + lineCount + ": " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid employee data at line " + lineCount + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading employees: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save employees to file
    public void saveEmployees() {
        ensureDataDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Employee employee : employees.values()) {
                writer.write(employee.getEmpId() + "," + employee.getName() + "," + employee.getRole());
                writer.newLine();
            }
            System.out.println("Successfully saved " + employees.size() + " employees.");
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add employee with auto-generated ID
    public void addEmployee(String name, String role) {
        String empId = generateEmployeeId();
        
        System.out.println("\n===== ADDING EMPLOYEE =====");
        System.out.println("ID: " + empId + " (auto-generated)");
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
        
        Employee employee = new Employee(empId, name, role);
        employees.put(empId, employee);
        saveEmployees(); // Save changes to file immediately
        
        System.out.println("\n===== SUCCESS =====");
        System.out.println("Employee " + name + " (ID: " + empId + ") has been added successfully!");
        System.out.println("Total employees: " + employees.size());
    }
    
    // Keep the original method for compatibility with existing code
    public void addEmployee(String empId, String name, String role) {
        System.out.println("\n===== ADDING EMPLOYEE =====");
        System.out.println("ID: " + empId);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
        
        // Check if employee ID already exists
        if (employees.containsKey(empId)) {
            System.out.println("\n===== ERROR =====");
            System.out.println("Employee ID '" + empId + "' is already in use.");
            System.out.println("Please use a unique employee ID.");
            return;
        }
        
        Employee employee = new Employee(empId, name, role);
        employees.put(empId, employee);
        saveEmployees(); // Save changes to file immediately
        
        System.out.println("\n===== SUCCESS =====");
        System.out.println("Employee " + name + " (ID: " + empId + ") has been added successfully!");
        System.out.println("Total employees: " + employees.size());
    }
    
    // Update employee information
    public void updateEmployee(String empId, String newName, String newRole) {
        Employee employee = employees.get(empId);
        if (employee != null) {
            String oldName = employee.getName();
            String oldRole = employee.getRole();
            
            employee.setName(newName);
            employee.setRole(newRole);
            saveEmployees(); // Save changes to file immediately
            
            System.out.println("SUCCESS: Employee information updated:");
            System.out.println("  - ID: " + empId);
            System.out.println("  - Name: " + oldName + " → " + newName);
            System.out.println("  - Role: " + oldRole + " → " + newRole);
        } else {
            System.out.println("ERROR: Employee with ID '" + empId + "' not found.");
        }
    }
    
    // Remove employee
    public void removeEmployee(String empId) {
        Employee removed = employees.remove(empId);
        if (removed != null) {
            saveEmployees(); // Save changes to file immediately
            System.out.println("SUCCESS: Employee " + removed.getName() + " (ID: " + empId + ") has been removed successfully!");
        } else {
            System.out.println("ERROR: Employee with ID '" + empId + "' not found.");
        }
    }
    
    // Get employee by ID
    public Employee getEmployeeById(String empId) {
        Employee employee = employees.get(empId);
        return employee;
    }

    // Check if employee exists
    public boolean employeeExists(String empId) {
        return employees.containsKey(empId);
    }

    // View all employees
    public void viewEmployees() {
        System.out.println("\n===== EMPLOYEE LIST =====");
        if (employees.isEmpty()) {
            System.out.println("No employees found. The employee database is empty.");
        } else {
            System.out.println("Total employees: " + employees.size());
            System.out.println("-------------------------");
            for (Employee employee : employees.values()) {
                System.out.println(employee);
            }
            System.out.println("-------------------------");
        }
    }
}
