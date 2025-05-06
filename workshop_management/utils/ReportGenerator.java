package utils;

import models.Task;

import java.io.*;
import java.util.*;

public class ReportGenerator {

    // Generate task report and save it to a file
    public void generateTaskReport(List<Task> tasks) {
        // Custom quicksort instead of built-in sort
        quickSort(tasks, 0, tasks.size() - 1);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/task_report.txt"))) {
            for (Task task : tasks) {
                writer.write(task.getTaskId() + ": " + task.getDescription() + " - " + task.getStatus() + " (Priority " + task.getPriority() + ")");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Custom QuickSort implementation to sort tasks by priority
    private void quickSort(List<Task> tasks, int low, int high) {
        if (low < high) {
            // Partition the array and get the pivot position
            int pivotIndex = partition(tasks, low, high);
            
            // Recursively sort elements before and after pivot
            quickSort(tasks, low, pivotIndex - 1);
            quickSort(tasks, pivotIndex + 1, high);
        }
    }
    
    private int partition(List<Task> tasks, int low, int high) {
        // Using the last element as pivot
        Task pivot = tasks.get(high);
        int i = low - 1; // Index of smaller element
        
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (tasks.get(j).getPriority() <= pivot.getPriority()) {
                i++;
                
                // Swap tasks[i] and tasks[j]
                Task temp = tasks.get(i);
                tasks.set(i, tasks.get(j));
                tasks.set(j, temp);
            }
        }
        
        // Swap tasks[i+1] and tasks[high] (pivot)
        Task temp = tasks.get(i + 1);
        tasks.set(i + 1, tasks.get(high));
        tasks.set(high, temp);
        
        return i + 1;
    }
}
