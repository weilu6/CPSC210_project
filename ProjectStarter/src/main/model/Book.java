package model;

import org.json.JSONObject;

/**
 * Book class represents a specific book
 * it has name, price, a short description,
 * a category, numbers and sales
 */

public class Book {
    private String name;
    private double price;
    private String description;
    private String category;
    private int number;
    private int sales; 


    // REQUIRES: num >= 0, price > 0, sales >= 0
    // EFFECTS: construct a book with given 
    // name, price, short description, a category,
    // numbers and sales
    public Book(String name, double price, String description, String category, int num, int sales) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.number = num;
        this.sales = sales;
    }

    // REQUIRES: num > 0
    // MODIFIES: this
    // EFFECTS: increase the number of the books by num
    public void increaseNum(int num) {
        this.number += num;
    }

    // REQUIRES: num > 0
    // MODIFIES: this
    // EFFECTS: increase the sales of the books by num
    public void increaseSales(int num) {
        this.sales += num;
    }

    // REQUIRES: num > 0
    // MODIFIES: this
    // EFFECTS: increase the sales of the books by num
    public void decreaseSales(int num) {
        this.sales -= num;
    }

    // REQUIRES: num > 0
    // MODIFIES: this
    // EFFECTS: decrease the number of the books by num, if
    // the rest number of books is less than 0, then set
    // the number as 0.
    public void decreaseNum(int num) {
        this.number -= num;
        if (this.number < 0) {
            this.number = 0;
        }
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price; 
    }

    public String getDescription() {
        return description;
    }

    public int getNumber() {
        return number;
    }

    public String getCategory() {
        return category;
    }

    public int getSales() {
        return sales;
    }

    // reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: return the book as json
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("price", price);
        json.put("description", description);
        json.put("category", category);
        json.put("number", number);
        json.put("sales", sales);
        return json;
    }
}
