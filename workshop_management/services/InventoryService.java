package services;

import models.InventoryItem;

import java.io.*;
import java.util.*;

public class InventoryService {
    private Map<String, InventoryItem> inventory = new HashMap<>();
    private final String FILE_PATH = "data/inventory.txt";

    // Load inventory from file
    public void loadInventory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                inventory.put(data[0], new InventoryItem(data[0], data[1], Integer.parseInt(data[2])));
            }
        } catch (IOException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save inventory to file
    public void saveInventory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (InventoryItem item : inventory.values()) {
                writer.write(item.itemId + "," + item.name + "," + item.quantity);
                writer.newLine();
            }
            System.out.println("Inventory saved successfully. Total items: " + inventory.size());
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Add new inventory item
    public void addItem(String itemId, String name, int quantity) {
        InventoryItem item = new InventoryItem(itemId, name, quantity);
        inventory.put(itemId, item);
        saveInventory(); // Save immediately after adding
        System.out.println("Item '" + name + "' (ID: " + itemId + ") added to inventory successfully!");
    }
    
    // Update item quantity
    public void updateQuantity(String itemId, int newQuantity) {
        InventoryItem item = inventory.get(itemId);
        if (item != null) {
            int oldQuantity = item.quantity;
            item.quantity = newQuantity;
            saveInventory(); // Save immediately after updating
            System.out.println("Item '" + item.name + "' quantity updated from " + oldQuantity + 
                              " to " + newQuantity + " successfully!");
        } else {
            System.out.println("Item with ID '" + itemId + "' not found in inventory.");
        }
    }
    
    // Remove item from inventory
    public void removeItem(String itemId) {
        InventoryItem removed = inventory.remove(itemId);
        if (removed != null) {
            saveInventory(); // Save immediately after removing
            System.out.println("Item '" + removed.name + "' (ID: " + itemId + ") removed from inventory successfully!");
        } else {
            System.out.println("Item with ID '" + itemId + "' not found in inventory.");
        }
    }

    // View all inventory items
    public void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("No inventory items found.");
        } else {
            System.out.println("\n===== Inventory List =====");
            for (InventoryItem item : inventory.values()) {
                System.out.println(item);
            }
        }
    }
}
