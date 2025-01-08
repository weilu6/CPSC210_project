package persistence;

import org.json.JSONArray;
// referencing JsonWriter from the demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
import org.json.JSONObject;
import model.*;
import java.io.*;
import java.util.ArrayList;

// writes JSON representation of bookStore to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representations to file
    public void write(BookCollection bc, ArrayList<Order> orders) {
        JSONObject json = bc.toJson();
        json.toString(TAB);
        json.put("orders", ordersToJson(orders));
        saveToFile(json.toString(TAB));
    }


    // MODIFIES: this
    // EFFECTS: writes JSON representation of BookCollection to file
    // public void writeCollection(BookCollection bc) {
    //     JSONObject json = bc.toJson();
    // }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of BookCollection to file
    // public void writeOrders(ArrayList<Order> orders) {
    //     JSONObject json = new JSONObject();
    //     json.put("orders", ordersToJson(orders));
    // }

    // EFFECTS: returns orders in this workroom as a JSON array
    private JSONArray ordersToJson(ArrayList<Order> orders) {
        JSONArray jsonArray = new JSONArray();
        for (Order o : orders) {
            jsonArray.put(o.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
