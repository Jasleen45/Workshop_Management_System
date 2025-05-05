package models;

public class InventoryItem {
    public String itemId;
    public String name;
    public int quantity;

    public InventoryItem(String itemId, String name, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item ID: " + itemId + " - Name: " + name + " - Quantity: " + quantity;
    }
}
