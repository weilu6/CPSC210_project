package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Order class represents a order created by a customer.
 * It record customer's name, books ordered with numbers, total price,
 * and a completed status.
 * We use two arraylist: one for book information, one for corresponding ordered numbers.
 */


public class Order {
    private String customerName;
    private ArrayList<Book> orderList;
    private ArrayList<Integer> numberList;
    private double totalPrice;
    private boolean status;

    // EFFECTS: create an order with customer's name, 
    // initially with 0 books and 0.0 total price, completed status is false.
    public Order(String name) {
        customerName = name;
        orderList = new ArrayList<Book>();
        numberList = new ArrayList<Integer>();
        totalPrice = 0.0;
        status = false;
    }

    // REQUIRES: 0< num < book.getNumber()
    // MODIFIES: this, book
    // EFFECTS: add num books into the order list, decrease the book in stock, increase the sales by num
    // if the book already exists, increase its number by num;
    // update the total price.
    public void addBooks(Book book, int num) {
        if (orderList.contains(book)) {
            int index = orderList.indexOf(book);
            int originalNum = numberList.get(index);
            numberList.set(index, originalNum + num);
        } else {
            orderList.add(book);
            numberList.add(num);
        }
        book.decreaseNum(num);
        book.increaseSales(num);
        totalPrice += book.getPrice() * num;
        EventLog.getInstance().logEvent(
            new Event(num + " Book(s): " + book.getName() + " are ordered by " + customerName));
    }

    // REQUIRES: num > 0
    // MODIFIES: this, book
    // EFFECTS: remove num books from the order list,
    // once the number of ordered books reduced to 0,
    // remove it from the order list;
    // update the total price.
    // increase the number of book in stock, decrease its sales
    // if the book does not exist, do nothing
    public void removeBooks(Book book, int num) {
        if (orderList.contains(book)) {
            int index = orderList.indexOf(book);
            int originalNum = numberList.get(index);
            if (originalNum <= num) {
                orderList.remove(book);
                numberList.remove(index);
                totalPrice -= originalNum * book.getPrice();
                book.increaseNum(originalNum);
                book.decreaseSales(originalNum);
                EventLog.getInstance().logEvent(
                    new Event(originalNum + " Book(s): " 
                        + book.getName() + " are removed from the order by " + customerName));
            } else {
                numberList.set(index, originalNum - num);
                totalPrice -= num * book.getPrice();
                book.increaseNum(num);
                book.decreaseSales(num);
                EventLog.getInstance().logEvent(
                    new Event(num + " Book(s): " 
                        + book.getName() + " are removed from the order by " + customerName));
            }
        }
    }

    // REQUIRES: the length of the given books and nums should be the same
    // MODIFIES: this
    // EFFECTS: set the orderList and numList without changing the books in the warehouse
    // when loading data from file
    public void setList(ArrayList<Book> books, ArrayList<Integer> nums) {
        orderList = books;
        numberList = nums;
    }

    // REQUIRES: price >= 0
    // MODIFIES: this
    // EFFECTS: set the total price of the order with the given price
    // when loading data from file
    public void setTotal(double price) {
        totalPrice = price;
    }

    // MODIFIES: this
    // EFFECTS: complete the order by setting status as true.
    public void complete() {
        status = true;
    }


    public String getName() {
        return customerName;
    }

    public ArrayList<Book> getOrderList() {
        return orderList;
    }

    public ArrayList<Integer> getNumberList() {
        return numberList;
    }
    
    public double getTotalPrice() {
        EventLog.getInstance().logEvent(
            new Event("Viewed " + customerName + "'s total price of ordered book: " + totalPrice + "$"));
        return totalPrice;
    }

    public boolean getStatus() {
        return status;
    }

    // reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: return the Order as json
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("customerName", customerName);
        json.put("orderList", booksToJson());
        json.put("numList", numsToJson());
        json.put("totalPrice", totalPrice);
        json.put("status", status);
        return json;
    }

    // EFFECTS: returns books as a JSON array
    private JSONArray booksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Book b : orderList) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns numList as a JSON array
    private JSONArray numsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (int i : numberList) {
            jsonArray.put(i);
        }
        return jsonArray;
    }
}
