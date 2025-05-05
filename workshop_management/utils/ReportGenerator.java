package utils;

import models.Task;

import java.io.*;
import java.util.*;

public class ReportGenerator {

    // Generate task report and save it to a file
    public void generateTaskReport(List<Task> tasks) {
        tasks.sort(Comparator.comparingInt(task -> task.priority)); // Sort tasks by priority
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/task_report.txt"))) {
            for (Task task : tasks) {
                writer.write(task.taskId + ": " + task.description + " - " + task.status + " (Priority " + task.priority + ")");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
