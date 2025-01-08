package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBook {
    private Book testBook; 
    private String name = "Mathematical Analysis";
    private double price = 100;
    private String description = "introduction of real analysis";
    private String category = "textbook";
    private int num = 5;
    private int sales = 22;

    @BeforeEach
    void runBefore() {
        testBook = new Book(name, price, description, category, num, sales);
    }

    @Test
    void testConstructor() {
        assertEquals("Mathematical Analysis", testBook.getName());
        assertEquals(100, testBook.getPrice(), 0.01);
        assertEquals("introduction of real analysis", testBook.getDescription());
        assertEquals("textbook",testBook.getCategory());
        assertEquals(5,testBook.getNumber());
        assertEquals(22, testBook.getSales());
    }

    @Test
    void testIncreaseNum() {
        testBook.increaseNum(3);
        assertEquals(8,testBook.getNumber());
        testBook.increaseNum(6);
        assertEquals(14,testBook.getNumber());
    }

    @Test
    void testIncreaseSales() {
        testBook.increaseSales(1);
        assertEquals(23,testBook.getSales());
        testBook.increaseSales(2);
        assertEquals(25,testBook.getSales());
    }

    @Test
    void testDecreaseSales() {
        testBook.decreaseSales(1);
        assertEquals(21,testBook.getSales());
        testBook.decreaseSales(2);
        assertEquals(19,testBook.getSales());
    }

    @Test
    void testDecreaseNum() {
        testBook.decreaseNum(3);
        assertEquals(2,testBook.getNumber());
        testBook.decreaseNum(2);
        assertEquals(0,testBook.getNumber());
        testBook.decreaseNum(1);
        assertEquals(0,testBook.getNumber());
    }

    @Test
    void testSetPrice() {
        testBook.setPrice(50.0);
        assertEquals(50.0,testBook.getPrice(),0.01);
        testBook.setPrice(150.0);
        assertEquals(150.0,testBook.getPrice(),0.01);
    }

    @Test
    void testSetDescription() {
        testBook.setDescription("advanced book");
        assertEquals("advanced book", testBook.getDescription());
        testBook.setDescription("on sale");
        assertEquals("on sale", testBook.getDescription());
    }

    @Test
    void testSetCategory() {
        testBook.setCategory("novel");
        assertEquals("novel", testBook.getCategory());
        testBook.setCategory("dictionary");
        assertEquals("dictionary", testBook.getCategory());
    }
}