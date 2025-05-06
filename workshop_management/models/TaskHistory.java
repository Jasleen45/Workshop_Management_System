package models;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TaskHistory {
    private String taskId;
    private String action;
    private String previousValue;
    private String newValue;
    private Date timestamp;
    private TaskHistory next;
    
    public TaskHistory(String taskId, String action, String previousValue, String newValue) {
        this.taskId = taskId;
        this.action = action;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.timestamp = new Date();
        this.next = null;
    }
    
    // Getters and setters
    public String getTaskId() { return taskId; }
    public String getAction() { return action; }
    public String getPreviousValue() { return previousValue; }
    public String getNewValue() { return newValue; }
    public Date getTimestamp() { return timestamp; }
    public TaskHistory getNext() { return next; }
    public void setNext(TaskHistory next) { this.next = next; }
    
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Use a formatted presentation with clear labels
        return action + ": Changed from '" + previousValue + "' to '" + newValue + "' at " + dateFormat.format(timestamp);
    }
}