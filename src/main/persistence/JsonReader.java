package persistence;

// referencing JsonReader.java from the given demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
import model.Book;
import model.BookCollection;
import model.Order;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.json.*;

// represents a reader that reads BookCollection from JSON data from file
public class JsonReader {
    private String source;

    // EFFECTS: construct reader to read source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads BookCollection from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BookCollection readCollection() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBookCollection(jsonObject);
    }

    // EFFECTS: reads orders from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<Order> readOrders() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseOrders(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses BookCollection from JSON object and returns it
    private BookCollection parseBookCollection(JSONObject jsonObject) {
        BookCollection bc = new BookCollection();
        addBooks(bc, jsonObject);
        return bc;
    }

    // MODIFIES: bc
    // EFFECTS: parses books from JSON object and adds them to BookCollection
    private void addBooks(BookCollection bc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("wareHouse");
        for (Object json : jsonArray) {
            JSONObject nextBook = (JSONObject) json;
            bc.addBook(returnBook(nextBook));
        }
    }

    // EFFECTS: parses book from JSON object and return it
    private Book returnBook(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double price = jsonObject.getDouble("price");
        String desc = jsonObject.getString("description");
        String cate = jsonObject.getString("category");
        int num = jsonObject.getInt("number");
        int sales = jsonObject.getInt("sales");
        Book b = new Book(name, price, desc, cate, num, sales);
        return b;
    }

    // EFFECTS: parses Orders from JSON object and returns it
    private ArrayList<Order> parseOrders(JSONObject jsonObject) {
        ArrayList<Order> orders = new ArrayList<Order>();
        JSONArray jsonArray = jsonObject.getJSONArray("orders");
        for (Object json : jsonArray) {
            JSONObject nextOrder = (JSONObject) json;
            addOrder(orders, nextOrder);
        }
        return orders;
    }


    // MODIFIES: orders
    // EFFECTS: parses order from JSON object and adds it to BookCollection
    private void addOrder(ArrayList<Order> orders, JSONObject jsonObject) {
        String customerName = jsonObject.getString("customerName");
        ArrayList<Book> orderList = new ArrayList<Book>();
        ArrayList<Integer> numList = new ArrayList<Integer>();
        double total = jsonObject.getDouble("totalPrice");
        boolean status = jsonObject.getBoolean("status");

        JSONArray jsonOrderArray = jsonObject.getJSONArray("orderList");
        for (Object json : jsonOrderArray) {
            JSONObject nextBook = (JSONObject) json;
            orderList.add(returnBook(nextBook));
        }

        JSONArray jsonNumArray = jsonObject.getJSONArray("numList");
        for (int i = 0; i < jsonNumArray.length(); i++) {
            numList.add(jsonNumArray.getInt(i));
        }

        Order order = new Order(customerName);
        if (status) {
            order.complete();
        }
        order.setList(orderList, numList);
        order.setTotal(total);
        orders.add(order);
    }
}
