package models;

public class Task {
    private String taskId;
    private String description;
    private String assignedTo;
    private String status;
    private int priority;
    
    // Possible status values
    public static final String STATUS_TODO = "To Do";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_COMPLETED = "Completed";

    public Task(String taskId, String description, String assignedTo, String status, int priority) {
        if (taskId == null || taskId.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        
        this.taskId = taskId;
        this.description = description != null ? description : "";
        this.assignedTo = assignedTo != null ? assignedTo : "";
        
        // Validate status
        if (status == null || (!status.equals(STATUS_TODO) && 
                              !status.equals(STATUS_IN_PROGRESS) && 
                              !status.equals(STATUS_COMPLETED))) {
            this.status = STATUS_TODO; // Default to "To Do" if invalid
        } else {
            this.status = status;
        }
        
        // Validate priority (assuming 1-5 scale)
        if (priority < 1) {
            this.priority = 1;
        } else if (priority > 5) {
            this.priority = 5;
        } else {
            this.priority = priority;
        }
    }
    
    // Getters and setters
    public String getTaskId() {
        return taskId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo != null ? assignedTo : "";
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        if (status != null && (status.equals(STATUS_TODO) || 
                              status.equals(STATUS_IN_PROGRESS) || 
                              status.equals(STATUS_COMPLETED))) {
            this.status = status;
        }
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        if (priority < 1) {
            this.priority = 1;
        } else if (priority > 5) {
            this.priority = 5;
        } else {
            this.priority = priority;
        }
    }

    @Override
    public String toString() {
        return taskId + ": " + description + " (Assigned to: " + assignedTo + ") - Status: " + status + " - Priority: " + priority;
    }
}
