package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class TestOrder {
    private String testName = "Mitchell";

    private String name1 = "Mathematical Analysis";
    private double price1 = 100;
    private String description1 = "introduction of real analysis";
    private String category1 = "textbook";
    private int num1 = 5;
    private int sales1 = 22;

    private String name2 = "testnovel";
    private double price2 = 5;
    private String description2 = "a novel for test";
    private String category2 = "novel";
    private int num2 = 91;
    private int sales2 = 2;

    private String name3 = "Pride and prejudice";
    private double price3 = 56;
    private String description3 = "the second novel by Jane Austen";
    private String category3 = "novel";
    private int num3 = 45;
    private int sales3 = 122;

    private Book b1;
    private Book b2;
    private Book b3;
    private Order testOrder;

    @BeforeEach
    void runBefore() {
        b1 = new Book(name1, price1, description1, category1, num1, sales1);
        b2 = new Book(name2, price2, description2, category2, num2, sales2);
        b3 = new Book(name3, price3, description3, category3, num3, sales3);
        testOrder = new Order(testName);
    }

    @Test
    void testConstructor() {
        assertEquals("Mitchell", testOrder.getName());
        ArrayList<Book> orderList = testOrder.getOrderList();
        assertEquals(0, orderList.size());
        assertEquals(0.0, testOrder.getTotalPrice(), 0.001);
        assertFalse(testOrder.getStatus());
    }

    @Test
    void testAddBooks() {
        testOrder.addBooks(b1, 1);
        ArrayList<Book> orderList = testOrder.getOrderList();
        ArrayList<Integer> numList = testOrder.getNumberList();
        assertEquals(1, orderList.size());
        assertEquals(1, numList.size());
        assertEquals(orderList.get(0), b1);
        assertEquals(b1.getPrice(), testOrder.getTotalPrice(), 0.001);
        assertEquals(4, b1.getNumber());
        assertEquals(23, b1.getSales());
        
        testOrder.addBooks(b1, 2);
        orderList = testOrder.getOrderList();
        numList = testOrder.getNumberList();
        assertEquals(1, orderList.size());
        assertEquals(1, numList.size());
        assertEquals(orderList.get(0), b1);
        assertEquals(3 * b1.getPrice(), testOrder.getTotalPrice(), 0.001);
        assertEquals(2, b1.getNumber());
        assertEquals(25, b1.getSales());

        testOrder.addBooks(b2, 3);
        orderList = testOrder.getOrderList();
        numList = testOrder.getNumberList();
        assertEquals(2, orderList.size());
        assertEquals(2, numList.size());
        assertEquals(orderList.get(0), b1);
        assertEquals(orderList.get(1), b2);
        double total = 3 * b1.getPrice() + 3 * b2.getPrice();
        assertEquals(total, testOrder.getTotalPrice(), 0.001);
        assertEquals(88, b2.getNumber());
        assertEquals(5, b2.getSales());
    }  

    @Test
    void testRemoveBooks() {
        testOrder.addBooks(b1, 3);
        testOrder.addBooks(b2, 5);
        ArrayList<Book> orderList = testOrder.getOrderList();
        ArrayList<Integer> numList = testOrder.getNumberList();
        assertEquals(2, orderList.size());
        assertEquals(2, numList.size());
        assertEquals(orderList.get(0), b1);
        assertEquals(orderList.get(1), b2);
        double totalPrice = 3 * b1.getPrice() + 5 * b2.getPrice();
        assertEquals(totalPrice, testOrder.getTotalPrice(), 0.001);
        assertEquals(2, b1.getNumber());
        assertEquals(25, b1.getSales());
        assertEquals(86, b2.getNumber());
        assertEquals(7, b2.getSales());
        
        testOrder.removeBooks(b3, 2);
        orderList = testOrder.getOrderList();
        numList = testOrder.getNumberList();
        assertEquals(2, orderList.size());
        assertEquals(2, numList.size());
        assertEquals(orderList.get(0), b1);
        assertEquals(orderList.get(1), b2);
        totalPrice = 3 * b1.getPrice() + 5 * b2.getPrice();
        assertEquals(totalPrice, testOrder.getTotalPrice(), 0.001);
        assertEquals(45, b3.getNumber());
        assertEquals(122, b3.getSales());

        testOrder.removeBooks(b1, 2);
        orderList = testOrder.getOrderList();
        numList = testOrder.getNumberList();
        assertEquals(2, orderList.size());
        assertEquals(2, numList.size());
        assertEquals(orderList.get(0), b1);
        assertEquals(orderList.get(1), b2);
        totalPrice = 1 * b1.getPrice() + 5 * b2.getPrice();
        assertEquals(totalPrice, testOrder.getTotalPrice(), 0.001);
        assertEquals(4, b1.getNumber());
        assertEquals(23, b1.getSales());

        testOrder.removeBooks(b1, 1);
        orderList = testOrder.getOrderList();
        numList = testOrder.getNumberList();
        assertEquals(1, orderList.size());
        assertEquals(1, numList.size());
        assertEquals(orderList.get(0), b2);
        totalPrice = 5 * b2.getPrice();
        assertEquals(totalPrice, testOrder.getTotalPrice(), 0.001);
        assertEquals(5, b1.getNumber());
        assertEquals(22, b1.getSales());

        testOrder.removeBooks(b2, 6);
        orderList = testOrder.getOrderList();
        numList = testOrder.getNumberList();
        assertEquals(0, orderList.size());
        assertEquals(0, numList.size());
        totalPrice = 0.0;
        assertEquals(totalPrice, testOrder.getTotalPrice(), 0.001);
        assertEquals(91, b2.getNumber());
        assertEquals(2, b2.getSales());
    }

    @Test
    void testComplete() {
        assertFalse(testOrder.getStatus());
        testOrder.complete();
        assertTrue(testOrder.getStatus());
        testOrder.complete();
        assertTrue(testOrder.getStatus());
    }

    @Test
    void testSetList() {
        ArrayList<Book> books = new ArrayList<Book>();
        ArrayList<Integer> nums = new ArrayList<Integer>();
        books.add(b1);
        books.add(b3);
        nums.add(2);
        nums.add(3);
        testOrder.setList(books, nums);
        assertEquals(2, testOrder.getOrderList().size());
        assertEquals(2, testOrder.getNumberList().size());
        assertEquals(b1, testOrder.getOrderList().get(0));
        assertEquals(b3, testOrder.getOrderList().get(1));
        assertEquals(2, testOrder.getNumberList().get(0));
        assertEquals(3, testOrder.getNumberList().get(1));
    }

    @Test
    void testSetTotal() {
        testOrder.setTotal(99);
        assertEquals(99, testOrder.getTotalPrice(), 0.01);
    }
}
