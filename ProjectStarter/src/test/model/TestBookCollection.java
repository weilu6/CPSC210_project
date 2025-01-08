package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class TestBookCollection {

    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private BookCollection testCollection;
    private String name1 = "Mathematical Analysis";
    private double price1 = 100;
    private String description1 = "introduction of real analysis";
    private String category1 = "textbook";
    private int num1 = 5;
    private int sales1 = 22;

    private double price2 = 30;
    private String description2 = "course notes for CPSC210";
    private String category2 = "notes";
    private int num2 = 10;
    private int sales2 = 53;

    private String name3 = "Pride and prejudice";
    private double price3 = 56;
    private String description3 = "the second novel by Jane Austen";
    private String category3 = "novel";
    private int num3 = 45;
    private int sales3 = 122;

    private String name4 = "testnovel";
    private double price4 = 5;
    private String description4 = "a novel for test";
    private String category4 = "novel";
    private int num4 = 91;
    private int sales4 = 2;

    @BeforeEach
    void runBefore() {
        b1 = new Book(name1, price1, description1, category1, num1, sales1);
        b2 = new Book(name1, price2, description2, category2, num2, sales2);
        b3 = new Book(name3, price3, description3, category3, num3, sales3);
        b4 = new Book(name4, price4, description4, category4, num4, sales4);

        testCollection = new BookCollection();
    }

    @Test
    void testConstructor() {
        assertEquals(0,testCollection.getSize());
    }

    @Test
    void testAddBook() {
        testCollection.addBook(b1);
        assertEquals(1, testCollection.getSize());
        assertEquals(b1, testCollection.getBookByIndex(0));
        testCollection.addBook(b3);
        assertEquals(2, testCollection.getSize());
        assertEquals(b3, testCollection.getBookByIndex(1));
        testCollection.addBook(b2);
        assertEquals(2, testCollection.getSize());
        assertEquals(b2, testCollection.getBookByIndex(0));
    }

    @Test
    void testRemoveBook() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        assertEquals(2,testCollection.getSize());
        testCollection.removeBook(b1.getName());
        assertEquals(1,testCollection.getSize());
        assertEquals(b3,testCollection.getBookByIndex(0));
        testCollection.removeBook(b1.getName());
        assertEquals(1,testCollection.getSize());
        assertEquals(b3,testCollection.getBookByIndex(0));
        testCollection.removeBook(b3.getName());
        assertEquals(0,testCollection.getSize());
    }

    @Test
    void testViewBooks() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        ArrayList<String> booklist = testCollection.viewBooks();
        assertEquals(2, booklist.size());
        assertEquals(b1.getName(),booklist.get(0));
        assertEquals(b3.getName(),booklist.get(1));
    }

    @Test
    void testSearchByName() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        int index = testCollection.searchByName(b1.getName());
        assertEquals(0,index);
        index = testCollection.searchByName(b3.getName());
        assertEquals(1,index);
        index = testCollection.searchByName("nonexist");
        assertEquals(-1, index);
    }

    @Test
    void testSearchByCategory() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        testCollection.addBook(b4);
        ArrayList<Integer> indices = testCollection.searchByCategory("textbook");
        assertEquals(1, indices.size());
        assertEquals(0, indices.get(0));
        indices = testCollection.searchByCategory("novel");
        assertEquals(2, indices.size());
        assertEquals(1, indices.get(0));
        assertEquals(2, indices.get(1));
        indices = testCollection.searchByCategory("none");
        assertEquals(0,indices.size());

    }


    @Test
    void testSumPrice() {
        testCollection.addBook(b1);
        double sumPrice = b1.getPrice() * b1.getNumber();
        assertEquals(sumPrice, testCollection.sumPrice(), 0.01);
        testCollection.addBook(b3);
        sumPrice += b3.getPrice() * b3.getNumber();
        assertEquals(sumPrice, testCollection.sumPrice(), 0.01);
    }

    @Test
    void testIncreaseNumOfBook() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        testCollection.increaseNumOfBook(b1.getName(), 5);
        assertEquals(10, b1.getNumber());
        testCollection.increaseNumOfBook(b1.getName(), 10);
        assertEquals(20, b1.getNumber());
        testCollection.increaseNumOfBook("none", 10);
        assertEquals(20, b1.getNumber());
    }

    @Test
    void testDecreaseNumOfBook() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        testCollection.decreaseNumOfBook(b1.getName(), 1);
        assertEquals(4, b1.getNumber());
        testCollection.decreaseNumOfBook(b1.getName(), 2);
        assertEquals(2, b1.getNumber());
        testCollection.decreaseNumOfBook("none", 5);
        assertEquals(2, b1.getNumber());
        assertEquals(45, b3.getNumber());
        testCollection.decreaseNumOfBook(b1.getName(), 3);
        assertEquals(0, b1.getNumber());
    }

    @Test
    void testSetPriceOfBook() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        testCollection.setPriceOfBook(b1.getName(), 10.0);
        assertEquals(10.0, b1.getPrice(),0.01);
        testCollection.setPriceOfBook("none", 0.0);
        assertEquals(10.0, b1.getPrice(),0.01);
        assertEquals(56.0, b3.getPrice(),0.01);
    }

    @Test
    void testSetCategoryOfBook() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        testCollection.setCategoryOfBook(b1.getName(), "notes");
        assertEquals("notes", b1.getCategory());
        testCollection.setCategoryOfBook("none", "text");
        assertEquals("notes", b1.getCategory());
        assertEquals("novel", b3.getCategory());
    }

    @Test
    void testSetDescriptionOfBook() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        testCollection.setDescriptionOfBook(b1.getName(), "test1");
        assertEquals("test1", b1.getDescription());
        testCollection.setDescriptionOfBook("none", "test2");
        assertEquals("test1", b1.getDescription());
        assertEquals("the second novel by Jane Austen", b3.getDescription());
    }

    @Test
    void testViewPopularBooks() {
        testCollection.addBook(b1);
        testCollection.addBook(b3);
        testCollection.addBook(b4);
        ArrayList<String> booklist = testCollection.viewPopularBooks(3);
        assertEquals(3, booklist.size());
        assertEquals(b3.getName(),booklist.get(0));
        assertEquals(b1.getName(),booklist.get(1));
        assertEquals(b4.getName(),booklist.get(2));
        booklist = testCollection.viewPopularBooks(2);
        assertEquals(2, booklist.size());
        assertEquals(b3.getName(),booklist.get(0));
        assertEquals(b1.getName(),booklist.get(1));
    }
}
