package persistence;

// reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TestJsonWriter extends TestJson {
    
    @Test
    void testWriterInvalidFile() {
        try {
            BookCollection bc = new BookCollection();
            ArrayList<Order> orders = new ArrayList<Order>();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyStore() {
        try {
            BookCollection bc = new BookCollection();
            ArrayList<Order> orders = new ArrayList<Order>();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyStore.json");
            writer.open();
            writer.write(bc, orders);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyStore.json");
            bc = reader.readCollection();
            assertEquals(0, bc.getSize());
            orders = reader.readOrders();
            assertEquals(0, orders.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralStore() {
        try {
            Book b1 = new Book("test1", 10.1, "desc1", "cate1", 2, 0);
            Book b2 = new Book("test2", 10.2, "desc2", "cate2", 3, 1);
            Book b3 = new Book("test3", 10.3, "desc3", "cate3", 4, 2);
            BookCollection bc = new BookCollection();
            bc.addBook(b1);
            bc.addBook(b2);
            bc.addBook(b3);

            Order o1 = new Order("order1");
            Order o2 = new Order("order2");
            o1.setTotal(20.2);
            o2.setTotal(41.1);
            ArrayList<Order> orders = new ArrayList<Order>();
            ArrayList<Book> orderList = new ArrayList<Book>();
            ArrayList<Integer> numList = new ArrayList<Integer>();
            orderList.add(b1);
            numList.add(2);
            o1.setList(orderList, numList);
            orders.add(o1);
            
            orderList = new ArrayList<Book>();
            numList = new ArrayList<Integer>();
            orderList.add(b2);
            orderList.add(b3);
            numList.add(1);
            numList.add(3);
            o2.setList(orderList, numList);
            o2.complete();
            orders.add(o2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralStore.json");
            writer.open();
            writer.write(bc, orders);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralStore.json");
            bc = reader.readCollection();
            orders = reader.readOrders();

            assertEquals(3, bc.getSize());
            checkBook(bc.getBookByIndex(0),
            "test1",10.1, "desc1", "cate1", 2, 0);
            checkBook(bc.getBookByIndex(1),
            "test2",10.2, "desc2", "cate2", 3, 1);
            checkBook(bc.getBookByIndex(2),
            "test3",10.3, "desc3", "cate3", 4, 2);

            assertEquals(2, orders.size());
            checkOrder(orders.get(0), "order1", 1, 20.2, false);
            checkOrder(orders.get(1), "order2", 2, 41.1, true);
            checkBook(orders.get(0).getOrderList().get(0),
             "test1", 10.1, "desc1", "cate1", 2, 0);
            assertEquals(2, orders.get(0).getNumberList().get(0));

            checkBook(orders.get(1).getOrderList().get(0),
             "test2", 10.2, "desc2", "cate2", 3, 1);
            assertEquals(1, orders.get(1).getNumberList().get(0));
            checkBook(orders.get(1).getOrderList().get(1),
             "test3", 10.3, "desc3", "cate3", 4, 2);
            assertEquals(3, orders.get(1).getNumberList().get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
