package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import model.*;

public class TestJson {
    protected void checkBook(Book b, String name, double price, String des, String cat, int n, int sales) {
        assertEquals(name, b.getName());
        assertEquals(price, b.getPrice(), 0.01);
        assertEquals(des, b.getDescription());
        assertEquals(cat, b.getCategory());
        assertEquals(n, b.getNumber());
        assertEquals(sales, b.getSales());
    }

    protected void checkOrder(Order o, String name, int num, double total, boolean status) {
        assertEquals(name, o.getName());
        assertEquals(num, o.getNumberList().size());
        assertEquals(num, o.getOrderList().size());
        assertEquals(total, o.getTotalPrice(), 0.01);
        assertEquals(status, o.getStatus());
    }
}
