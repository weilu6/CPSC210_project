package persistence;

import model.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;


public class TestJsonReader extends TestJson{
    
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            BookCollection bc = reader.readCollection();
            ArrayList<Order> orders = reader.readOrders(); 
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyStore() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyStore.json");
        try {
            BookCollection bc = reader.readCollection();
            assertEquals(0, bc.getSize());
            ArrayList<Order> orders = reader.readOrders();
            assertEquals(0, orders.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralStore() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralStore.json");
        try {
            BookCollection bc = reader.readCollection();
            assertEquals(3, bc.getSize());
            checkBook(bc.getBookByIndex(0),
            "testBook1",5, "This is test book 1", "cate1", 3, 1);
            checkBook(bc.getBookByIndex(1),
            "testBook2",10, "This is test book 2", "cate2", 4, 0);
            checkBook(bc.getBookByIndex(2),
            "testBook3",15, "This is test book 3", "cate3", 5, 10);

            ArrayList<Order> orders = reader.readOrders();
            assertEquals(2, orders.size());
            checkOrder(orders.get(0), "testCustomer1", 2, 40, false);
            ArrayList<Book> orderList = orders.get(0).getOrderList();
            ArrayList<Integer> numList = orders.get(0).getNumberList();
            checkBook(orderList.get(0),
            "testBook1",5, "This is test book 1", "cate1", 3, 1);
            checkBook(orderList.get(1),
            "testBook2",10, "This is test book 2", "cate2", 4, 0);
            assertEquals(2,numList.get(0));
            assertEquals(3,numList.get(1));

            checkOrder(orders.get(1), "testCustomer2", 1, 15, true);
            orderList = orders.get(1).getOrderList();
            numList = orders.get(1).getNumberList();
            checkBook(orderList.get(0),
            "testBook3",15, "This is test book 3", "cate3", 5, 10);
            assertEquals(1,numList.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
