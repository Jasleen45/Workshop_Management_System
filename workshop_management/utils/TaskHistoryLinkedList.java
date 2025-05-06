package utils;

import models.TaskHistory;
import java.text.SimpleDateFormat;

public class TaskHistoryLinkedList {
    private TaskHistory head;
    private int size;
    
    public TaskHistoryLinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    // Add a new history entry at the beginning (most recent first)
    public void addHistory(String taskId, String action, String previousValue, String newValue) {
        TaskHistory newEntry = new TaskHistory(taskId, action, previousValue, newValue);
        newEntry.setNext(head);
        head = newEntry;
        size++;
    }
    
    // Get history for a specific task
    public void printTaskHistory(String taskId) {
        if (head == null) {
            System.out.println("No history available for task " + taskId);
            return;
        }
        
        System.out.println("\n===== History for Task " + taskId + " =====");
        TaskHistory current = head;
        boolean found = false;
        int count = 0;
        
        while (current != null) {
            if (current.getTaskId().equals(taskId)) {
                found = true;
                count++;
                System.out.println(count + ". " + current.toString());
            }
            current = current.getNext();
        }
        
        if (!found) {
            System.out.println("No history records found for task " + taskId);
        }
    }
    
    // Get full history (all tasks)
    public void printFullHistory(int limit) {
        if (head == null) {
            System.out.println("No task history available.");
            return;
        }
        
        System.out.println("\n===== Recent Task History =====");
        TaskHistory current = head;
        int count = 0;
        
        while (current != null && count < limit) {
            count++;
            System.out.println("Task: " + current.getTaskId() + " - " + current.toString());
            current = current.getNext();
        }
        
        System.out.println("-------------------------");
    }
    
    // Get size of history
    public int size() {
        return size;
    }
}