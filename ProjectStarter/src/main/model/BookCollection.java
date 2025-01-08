package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * BookCollection class represents a list of books.
 * For customers, a cart is a collection of books;
 * For staffs, their warehouse is a collection of books.
 */

public class BookCollection {

    private ArrayList<Book> books;

    // EFFECTS: create a list of books,
    // initialized as empty arraylist.
    public BookCollection() {
        books = new ArrayList<Book>();
    }

    // MODIFIES: this
    // EFFECTS: add a book to the list if the book is not
    // in the list; otherwise, update the book
    public void addBook(Book book) {
        if (this.viewBooks().contains(book.getName())) {
            int index = this.searchByName(book.getName());
            books.set(index, book);
        } else {
            books.add(book);
            EventLog.getInstance().logEvent(new Event(book.getName() + " is added to the store"));
        }
    }

    // MODIFIES: this
    // EFFECTS: given book name, remove the book from the list;
    // If the book is not in the list, do noting
    public void removeBook(String name) {
        if (this.viewBooks().contains(name)) {
            int index = this.searchByName(name);
            books.remove(index);
        }
    }

    // EFFECTS: return a list of book names in the collection
    public ArrayList<String> viewBooks() {
        ArrayList<String> bookNames = new ArrayList<String>();
        for (Book next : books) {
            String name = next.getName();
            bookNames.add(name);
        }
        // EventLog.getInstance().logEvent(new Event("Books in the store are displayed"));
        return bookNames;
    }

    // EFFECTS: search a book by name and return the index of the book;
    // if not exist, return -1
    public int searchByName(String name) {
        int n = books.size();
        for (int i = 0; i < n; i++) {
            Book book = books.get(i);
            if (name.equals(book.getName())) {
                return i;
            }
        }
        return -1;
    }

    // EFFECTS: search a book by category and return the indices of books;
    // if not exist, return empty arraylist
    public ArrayList<Integer> searchByCategory(String category) {
        ArrayList<Integer> indices = new ArrayList<Integer>();
        int n = books.size();
        for (int i = 0; i < n; i++) {
            Book book = books.get(i);
            if (category.equals(book.getCategory())) {
                indices.add(i);
            }
        }
        return indices;
    }

    // EFFECTS: return the total price of the books, considering book numbers
    public double sumPrice() {
        double total = 0.0;
        for (Book next : books) {
            total += next.getPrice() * next.getNumber();
        }
        return total;
    }

    // REQUIRES: num > 0
    // MODIFIES: this, book
    // EFFECTS: given book name, if the book is in the collection,
    // increase the book by the given num; otherwise, do nothing
    public void increaseNumOfBook(String name, int num) {
        int index = this.searchByName(name);
        if (index > -1) {
            Book book = books.get(index);
            book.increaseNum(num); 
        }
    }

    // REQUIRES: num > 0
    // MODIFIES: this, book
    // EFFECTS: given book name, if the book is in the collection,
    // decrease the book by the given num, decrease at most to zero;
    // otherwise, do nothing
    public void decreaseNumOfBook(String name, int num) {
        int index = this.searchByName(name);
        if (index > -1) {
            Book book = books.get(index);
            book.decreaseNum(num); 
        }
    }

    // MODIFIES: this, book
    // EFFECTS: given book name, if the book is in the collection,
    // set the price as the given one;
    // otherwise, do nothing
    public void setPriceOfBook(String name, double price) {
        int index = this.searchByName(name);
        if (index > -1) {
            Book book = books.get(index);
            book.setPrice(price);
            EventLog.getInstance().logEvent(
                new Event("The price of " + name + " is set to: " + price + "$"));
        }
    }

    // MODIFIES: this, book
    // EFFECTS: given book name, if the book is in the collection,
    // set the category as the given one;
    // otherwise, do nothing
    public void setCategoryOfBook(String name, String category) {
        int index = this.searchByName(name);
        if (index > -1) {
            Book book = books.get(index);
            book.setCategory(category); 
        }
    }

    // MODIFIES: this, book
    // EFFECTS: given book name, if the book is in the collection,
    // set the description as the given one;
    // otherwise, do nothing
    public void setDescriptionOfBook(String name, String description) {
        int index = this.searchByName(name);
        if (index > -1) {
            Book book = books.get(index);
            book.setDescription(description); 
            EventLog.getInstance().logEvent(
                new Event("The description of " + name + " is set to: " + description));
        }
    }

    //REQUIRES: 0< num <= size of the BookCollection
    //EFFECTS: return a list of book names with the given size 
    // from the most popular
    // to the least popular by sales
    public ArrayList<String> viewPopularBooks(int num) {
        books.sort((b1,b2) -> b2.getSales() - b1.getSales());
        ArrayList<String> bookNames = new ArrayList<String>();
        for (int i = 0; i < num; i++) {
            String name = books.get(i).getName();
            bookNames.add(name);
        }
        return bookNames;
    }

    public Book getBookByIndex(int i) {
        return books.get(i);
    }

    public int getSize() {
        return books.size();
    }

    // referencing https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    // EFFECTS: return JSONObject representing the bookcollection
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("wareHouse", booksToJson());
        return json;
    }

    // EFFECTS: returns books as a JSON array
    private JSONArray booksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Book b : books) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }
}
