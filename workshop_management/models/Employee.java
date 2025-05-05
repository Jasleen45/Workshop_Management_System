package models;

public class Employee {
    private String empId;
    private String name;
    private String role;

    public Employee(String empId, String name, String role) {
        if (empId == null || empId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        this.empId = empId;
        this.name = name != null ? name : "";
        this.role = role != null ? role : "";
    }

    // Getters and setters
    public String getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role != null ? role : "";
    }

    @Override
    public String toString() {
        return "ID: " + empId + " - Name: " + name + " - Role: " + role;
    }
}
