package ui;

import java.util.Scanner;
import model.Book;
import model.BookCollection;
import model.EventLog;
import model.Order;
import model.Event;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// this class is referencing TellerApp.java

/**
 * Start the application,
 * login as customer or staff
 * As a customer, search book by name or category, get a recommendation list, view all books in the store,
 * add books to your order, remove books from your order, view total price, 
 * and view details of each book in the store.
 * 
 * As a staff, you can manage the warehouse by adding/removing books,
 * you can view and operate on orders made by customers, and
 * set details of each book.
 * 
 * This application has memory if you don't entirely quit (i.e you can relogin as different people)
 * As a staff, you will be able to see orders made by previous users
 * As a customer, you will see the updated infomation in the bookstore.
 * 
 * Added GUI, users can use the GUI to use this application.
 */

// Book store application
public class BookStore {
    private static final String JSON_STORE = "./data/bookStore.json";
    private BookCollection wareHouse;
    private String userName;
    private ArrayList<Order> orders;
    private Order order;
    private Scanner input;
    private boolean isCustomer;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JFrame jf;
    private JPanel jp1;
    private JPanel jp2;
    private JPanel jp3;
    private JPanel jp4;
    private JPanel jp5;



    // run the book store application
    public BookStore() throws FileNotFoundException {
        runBookStore();
    }

    // MODIFIES: this
    // EFFECTS: initialize the GUI
    private void initGUI() {
        jf = new JFrame("BookStore");
        jf.setSize(500, 800);
        jf.setLocation(500, 300);
        jf.setLayout(new GridLayout(5,1));
        jp1 = new JPanel();
        jp1.setSize(500, 300);
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp5 = new JPanel();
        jf.add(jp1);
        jf.add(jp2);
        jf.add(jp3);
        jf.add(jp4);
        jf.add(jp5);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // MODIFIES: this
    // EFFECTS: process user input
    private void runBookStore() {
        boolean running;
        boolean backToLogin = true;
        
        orders = new ArrayList<Order>();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        wareHouse = new BookCollection();
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");

        // init();
        displayGUI();

        load();

        while (backToLogin) {
            running = true;
            login();

            while (running) {
                if (isCustomer == true) {
                    running = customerRun(running);

                } else {
                    running = staffRun(running);
                }
            }
            backToLogin = relogin();
        }
        save();
        System.out.println("\nGoodbye, have a good day!");
    }

    // MODIFIES: this
    // EFFECTS: display GUI
    private void displayGUI() {
        initGUI();
        // showBooksLabel();
        loadDataPanel();
        chooseStatusPanel();
    }

    // MODIFIES: this
    // EFFECTS: GUI to choose to be a customer or a staff
    private void chooseStatusPanel() {
        JLabel statusLabel = new JLabel("Your status?");
        JRadioButton customerButton = new JRadioButton("customer");
        JRadioButton staffButton = new JRadioButton("staff");
        groupButtons(customerButton, staffButton);
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCustomer = true;
                customerGUI();
                customerButton.setEnabled(false);
                staffButton.setEnabled(false);
            }
        });
        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCustomer = false;
                staffGUI();
                customerButton.setEnabled(false);
                staffButton.setEnabled(false);
            }
        });
        updateChooseStatusPanel(statusLabel, customerButton, staffButton);
    }

    // EFFECTS: help method to group JradioButton
    private void groupButtons(JRadioButton button1, JRadioButton button2) {
        ButtonGroup group = new ButtonGroup();
        group.add(button1);
        group.add(button2);
    }

    // MODIFIES: this
    // EFFECTS: helper method to update chooseStatusPanel
    private void updateChooseStatusPanel(JLabel statusLabel, JRadioButton customerButton, JRadioButton staffButton) {
        jp2.add(statusLabel);
        jp2.add(customerButton);
        jp2.add(staffButton);
        jp2.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: when user choose to be a staff, show the GUI
    private void staffGUI() {
        showOrdersFromCustomer();
        JComboBox<String> staffBox = getStaffBox();
        jp3.setLayout(new FlowLayout());
        staffBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = staffBox.getSelectedItem().toString();
                if (selected.equals("add book to store")) {
                    addBookToStorePanel();
                } else if (selected.equals("set the price of a book")) {
                    setPricePanel();
                } else if (selected.equals("set the description of a book")) {
                    setDescriptionPanel();
                } else if (selected.equals("quit")) {
                    staffBox.setEditable(false);
                    quitPanel();
                }
            }
        });        
        jp3.add(staffBox);
        jp3.updateUI();
    }

    // EFFECTS: initialize the JComboBox for staff
    private JComboBox<String> getStaffBox() {
        JComboBox<String> staffBox = new JComboBox<>();
        staffBox.addItem("--select options from below--");
        staffBox.addItem("add book to store");
        staffBox.addItem("set the price of a book");
        staffBox.addItem("set the description of a book");
        staffBox.addItem("quit");
        return staffBox;
    }

    // MODIFIES: this
    // EFFECTS: GUI to add a book to the warehouse
    private void addBookToStorePanel() {
        JLabel addBookLabel = new JLabel("enter: name/price/description/category/number");
        JTextField addBookTxt = new JTextField(40);
        addBookTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookInfo = addBookTxt.getText();
                String[] stringList = bookInfo.split("/");
                double price = Double.parseDouble(stringList[1]);
                int number = Integer.parseInt(stringList[4]);
                Book b = new Book(stringList[0], price, stringList[2], stringList[3], number, 0);
                wareHouse.addBook(b);
                showBooksLabel();
            }
        });
        jp2.removeAll();
        jp2.repaint();
        jp2.add(addBookLabel);
        jp2.add(addBookTxt);
        jp2.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: GUI to set the price of a book
    private void setPricePanel() {
        JLabel setPriceLabel = new JLabel("enter: book name/new price");
        JTextField setPriceTxt = new JTextField(30);
        setPriceTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookInfo = setPriceTxt.getText();
                String[] stringList = bookInfo.split("/");
                double price = Double.parseDouble(stringList[1]);
                wareHouse.setPriceOfBook(stringList[0], price);
                setPriceLabel.setText("The price of " + stringList[0] + " is " + price + "$ now");
            }
        });
        jp2.removeAll();
        jp2.repaint();
        jp2.add(setPriceLabel);
        jp2.add(setPriceTxt);
        jp2.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: GUI to set the description of a book
    private void setDescriptionPanel() {
        JLabel setDescLabel = new JLabel("enter: book name/new description");
        JTextField setDescTxt = new JTextField(40);
        setDescTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookInfo = setDescTxt.getText();
                String[] stringList = bookInfo.split("/");
                wareHouse.setDescriptionOfBook(stringList[0], stringList[1]);
                setDescLabel.setText("The description is updated");
            }
        });
        jp2.removeAll();
        jp2.repaint();
        jp2.add(setDescLabel);
        jp2.add(setDescTxt);
        jp2.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: show names of customers who ordered
    private void showOrdersFromCustomer() {
        ArrayList<String> stringList = new ArrayList<>();
        for (Order o : orders) {
            stringList.add(o.getName());
        }
        JLabel ordersLabel = new JLabel("<html>Names of customers who ordered books: " 
                + listToString(stringList) + "<html>");
        jp4.removeAll();
        jp4.repaint();
        jp4.add(ordersLabel);
        jp4.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: when user choose to be a customer, show the GUI
    private void customerGUI() {
        JLabel nameLabel = new JLabel("What's your name?");
        JTextField nameTxt = new JTextField(10);
        jp3.setLayout(new FlowLayout());
        nameTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName = nameTxt.getText();
                nameLabel.setText("Hi, " + userName);
                order = new Order(userName);
                nameTxt.setEnabled(false);
            }
        });
        JComboBox<String> customerBox = getCustomerBox();
        jp3.add(nameLabel);
        jp3.add(nameTxt);
        jp3.add(customerBox);
        jp3.updateUI();
    }

    // EFFECTS: add actions to the customer JComboBox
    private JComboBox<String> getCustomerBox() {
        JComboBox<String> customerBox = layoutCustomerBox();
        customerBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = customerBox.getSelectedItem().toString();
                if (selected.equals("add books to order")) {
                    addBookPanel();
                } else if (selected.equals("remove books from order")) {
                    removeBookPanel();
                } else if (selected.equals("view total price")) {
                    viewTotalPricePanel();
                } else if (selected.equals("quit")) {
                    customerBox.setEditable(false);
                    orders.add(order);
                    quitPanel();
                }
            }
        });
        return customerBox;
    }

    // EFFECTS: helper method to initialize JComboBox for customer
    private JComboBox<String> layoutCustomerBox() {
        JComboBox<String> customerBox = new JComboBox<>();
        customerBox.addItem("--select options from below--");
        customerBox.addItem("add books to order");
        customerBox.addItem("remove books from order");
        customerBox.addItem("view total price");
        customerBox.addItem("quit");
        return customerBox;
    }

    // MODIFIES: this
    // EFFECTS: panel for to quit and save
    private void quitPanel() {
        jp5.removeAll();
        jp5.repaint();
        JLabel quitLabel = new JLabel("Do you want to save the store?");
        JButton quitButton = new JButton("Leave the bookstore");
        JRadioButton yesButton = new JRadioButton("Yes");
        JRadioButton noButton = new JRadioButton("No");
        groupButtons(yesButton, noButton);
        addActionsForQuit(quitLabel, quitButton, yesButton, noButton);
        jp5.add(quitLabel);
        jp5.add(yesButton);
        jp5.add(noButton);
        jp5.updateUI();

    }

    // MODIFIES: this
    // EFFECTS: added actions to the buttons in quitPanel
    private void addActionsForQuit(JLabel quitLabel, JButton quitB, JRadioButton yesB, JRadioButton noB) {
        addActionsForSave(quitLabel, quitB, yesB, noB);
        quitB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                jf = new JFrame("GoodBye");
                jf.setLocation(500, 300);
                ImageIcon image = new ImageIcon("./images/goodbye.jpg");
                JLabel imageLabel = new JLabel(image);
                jf.add(imageLabel);
                jf.setSize(image.getIconWidth(), image.getIconHeight());
                jf.setVisible(true);
                printLog(EventLog.getInstance());
            }    
        });
    }

    private void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString() + "\n");
        }
    }

    // MODIFIES: this
    // EFFECRS: add actions to the save option button
    private void addActionsForSave(JLabel quitLabel, JButton quitB, JRadioButton yesB, JRadioButton noB) {
        yesB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBookStore();
                yesB.setEnabled(false);
                noB.setEnabled(false);
                quitLabel.setText("Successfully saved!");
                jp5.add(quitB);
            }
        });
        noB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yesB.setEnabled(false);
                noB.setEnabled(false);
                quitLabel.setText("Not saved!");
                jp5.add(quitB);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: panel to view total price in the order
    private void viewTotalPricePanel() {
        jp5.removeAll();
        jp5.repaint();
        double total = order.getTotalPrice();
        JLabel totalPricLabel = new JLabel("Total Price is: " + total);
        jp5.add(totalPricLabel);
        jp5.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: panel for remove books from order
    private void removeBookPanel() {
        JLabel bookNameLabel = new JLabel("Book name?");
        JLabel numberLabel = new JLabel("How many?");
        JTextField bookNameTextField = new JTextField(20);
        JTextField numberField = new JTextField(4);
        JButton removeButton = new JButton("Remove");

        addActionsToRemoveBooks(bookNameLabel, numberLabel, bookNameTextField, numberField, removeButton);
        repaintPanelForEditOrder(bookNameLabel, numberLabel, bookNameTextField, numberField, removeButton);
    }

    // MODOFIES: this
    // EFFECTS: add actions textfield and button in removeBookPanel
    private void addActionsToRemoveBooks(JLabel bookNameLabel, JLabel numberLabel, JTextField bookNameTextField,
            JTextField numberField, JButton removeButton) {
        bookNameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookNameLabel.setText(bookNameTextField.getText());
            }
        });
        numberField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberLabel.setText(numberField.getText());
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Book b : order.getOrderList()) {
                    if (b.getName().equals(bookNameLabel.getText())) {
                        order.removeBooks(b, Integer.parseInt(numberLabel.getText()));
                        showOrderPanel();
                    }
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: update panel2 to edit order
    private void repaintPanelForEditOrder(JLabel bookNameLabel, JLabel numberLabel, JTextField bookNameTextField,
            JTextField numberField, JButton editButton) {
        jp2.removeAll();
        jp2.repaint();
        jp2.add(bookNameLabel);
        jp2.add(bookNameTextField);
        jp2.add(numberLabel);
        jp2.add(numberField);
        jp2.add(editButton);
        jp2.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: panel for adding books to order
    private void addBookPanel() {
        JLabel bookNameLabel = new JLabel("Book name?");
        JLabel numberLabel = new JLabel("How many?");
        JTextField bookNameTextField = new JTextField(20);
        JTextField numberField = new JTextField(4);
        JButton addButton = new JButton("Add");

        addActionsToAddBookPanel(bookNameLabel, numberLabel, bookNameTextField, numberField, addButton);
        repaintPanelForEditOrder(bookNameLabel, numberLabel, bookNameTextField, numberField, addButton);
    }

    // MODIFIES: this
    // EFFECTS: add actions to add books to order
    private void addActionsToAddBookPanel(JLabel bookNameLabel, JLabel numberLabel, JTextField bookNameTextField,
            JTextField numberField, JButton addButton) {
        bookNameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookNameLabel.setText(bookNameTextField.getText());
            }
        });
        numberField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberLabel.setText(numberField.getText());
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = wareHouse.searchByName(bookNameLabel.getText());
                if (index != -1) {
                    Book b = wareHouse.getBookByIndex(index);
                    order.addBooks(b, Integer.parseInt(numberLabel.getText()));
                    showOrderPanel();
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: display the books ordered
    private void showOrderPanel() {
        jp4.removeAll();
        jp4.repaint();
        String orderList = "";
        ArrayList<Book> books = order.getOrderList();
        ArrayList<Integer> nums = order.getNumberList();
        for (int i = 0; i < order.getOrderList().size(); i++) {
            orderList = orderList + "<br>" + books.get(i).getName() + ":" + nums.get(i);
        }
        JLabel ordersLabel = new JLabel("<html>We have ordered: " + "<br>" + orderList + "<html>");
        jp4.add(ordersLabel);
        jp4.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: GUI to choose whether to load file
    private void loadDataPanel() {
        JLabel loadLabel = new JLabel("How to load data? ");
        JRadioButton load = new JRadioButton("load data from file");
        JRadioButton noLoad = new JRadioButton("go without loading");
        groupButtons(load, noLoad);
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBookStore();
                showBooksLabel();
                load.setEnabled(false);
                noLoad.setEnabled(false);
            }
        });
        noLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
                showBooksLabel();
                load.setEnabled(false);
                noLoad.setEnabled(false);
            }
        });
        updateChooseStatusPanel(loadLabel, load, noLoad);
    }

    // MODIFIES: this
    // EFFECTS: label to show books in the warehouse
    private void showBooksLabel() {
        JLabel bookLabel = new JLabel("<html>Books in the warehouse: " 
                + listToString(wareHouse.viewBooks()) + "<html>");
        jp1.removeAll();
        jp1.repaint();
        jp1.add(bookLabel);
        jp1.updateUI();
    }

    // EFFECTS: helper method to convert list to a string
    private String listToString(ArrayList<String> list) {
        String stringToReturn = "";
        for (String s : list) {
            stringToReturn = stringToReturn + "<br>" + s;
        }
        return stringToReturn;
    }

    // EFFECTS: save the warehouse and orders to file
    private void save() {
        System.out.println("\nDo you want to save the bookstore to file before leave?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");
        String command = input.next();
        command = command.toLowerCase();
        if (command.equals("y")) {
            saveBookStore();
        } else if (command.equals("n")) {
            System.out.println("leaving...without saving");
        } else {
            System.out.println("invalid input, try again");
            save();
        }
    }

    // MODIFIES: this
    // EEFECTS: load the bookstore from file
    private void load() {
        System.out.println("\nDo you want to load warehouse and orders from file?:");
        System.out.println("\ty -> load warehouse and orders from file");
        System.out.println("\tn -> going to the store...");
        String command = input.next();
        if (command.equals("y")) {
            loadBookStore();
        } else {
            init();
        }
    }

    // MODIFIES: this
    // EFFECTS: run the application in staff interface
    private boolean staffRun(boolean running) {
        String command;
        displayStaffMenu();
        System.out.println("q -> quit");
        command = input.next();

        if (command.equals("q")) {
            running = false;
        } else {
            processStaffCommand(command);
        }
        return running;
    }

    // MODIFIES: this
    // EFFECTS: run the application in customer interface
    private boolean customerRun(boolean running) {
        String command;
        displayCustomerMenu();
        System.out.println("q -> quit");
        command = input.next();

        if (command.equals("q")) {
            orders.add(order);
            running = false;
        } else {
            processCustomerCommand(command);
        }
        return running;
    }
    

    // EFFECTS: login as customer or staff with name
    private void login() {
        System.out.println("\nLogin as:");
        System.out.println("\tc -> customer");
        System.out.println("\ts -> staff");

        String command = input.next();
        command = command.toLowerCase();

        if (command.equals("c")) {
            System.out.println("\n what's your name?");
            command = input.next();
            userName = command;
            isCustomer = true;
            order = new Order(userName);
        } else {
            isCustomer = false;
        }
    }

    // MODIFIES: this
    // EFFECTS: load book store from file
    public void loadBookStore() {
        try {
            wareHouse = jsonReader.readCollection();
            orders = jsonReader.readOrders();
            System.out.println("Loaded a book store from: " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the warehouse and orders to file
    private void saveBookStore() {
        try {
            jsonWriter.open();
            jsonWriter.write(wareHouse, orders);
            jsonWriter.close();
            System.out.println("Saved the book store to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: go back to login surface or not
    private boolean relogin() {
        System.out.println("\nDo you want to go back to the login surface?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");
        String command = input.next();

        if (command.equals("n")) {
            System.out.println("... going to leave the store");
            return false;
        } else if (command.equals("y")) {
            System.out.println("... going back to the login");
            return true;
        } else {
            System.out.println("invalid input, try again.");
            return relogin();
        }
        
    }

    // EFFECTS: initialize the warehouse
    private void init() {
        String description1 = "introduction of real analysis";
        String description2 = "course notes for CPSC210";
        String description3 = "the second novel by Jane Austen";

        Book b1 = new Book("Real Analysis", 100, description1, "textbook", 5, 22);
        Book b2 = new Book("CPSC210 Notes", 30, description2, "notes", 10, 53);
        Book b3 = new Book("PrideAndPrejudice", 56, description3, "novel", 45, 100);

        // wareHouse = new BookCollection();

        wareHouse.addBook(b1);
        wareHouse.addBook(b2);
        wareHouse.addBook(b3);

        // input = new Scanner(System.in);
        // input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: display options for customers
    private void displayCustomerMenu() {
        System.out.println("\noptions about warehouse, Select from:");
        System.out.println("\ts -> search books");
        System.out.println("\trecommendation -> view recommendation list");
        System.out.println("\tall -> view all books in the bookstore");
        System.out.println("\noptions about order, Select from:");
        System.out.println("\tedit -> edit your order");
        System.out.println("\torder -> view books ordered");
        System.out.println("\ttotal -> view total price of the order");
        System.out.println("\noptions about book, Select from:");
        System.out.println("\tprice -> view the price of a book ");
        System.out.println("\tdes -> view the description of a book ");
        System.out.println("\tcate -> view the category of a book ");
        System.out.println("\tstock -> view the number of a book in the warehouse");
    }

    // EFFECTS: display options for staffs
    private void displayStaffMenu() {
        System.out.println("\noptions about warehouse, Select from:");
        System.out.println("\tadd -> add a new book");
        System.out.println("\trem -> remove a book");
        System.out.println("\tall -> view all books in the bookstore");
        System.out.println("\noptions about order, Select from:");
        System.out.println("\torders -> view customers' names");
        System.out.println("\topr -> operate an order");
        System.out.println("\noptions about book, Select from:");
        System.out.println("\tprice -> set the price of a book ");
        System.out.println("\tdes -> set the description of a book ");
        System.out.println("\tcate -> set the category of a book ");
        System.out.println("\tstock -> set the number of a book in the warehouse");
    }

    // MODIFIES: this
    // EFFECTS: process customer command
    private void processCustomerCommand(String command) {
        if (command.equals("s")) {
            searchBooks();
        } else if (command.equals("recommendation")) {
            recommend();
        } else if (command.equals("all")) {
            viewBooks();
        } else if (command.equals("edit")) {
            editOrder();
        } else if (command.equals("order")) {
            viewOrder();
        } else if (command.equals("total")) {
            totalPrice();
        } else if (command.equals("price")) {
            bookPrice();
        } else if (command.equals("des")) {
            bookDescription();
        } else if (command.equals("cate")) {
            bookCategory();
        } else if (command.equals("stock")) {
            bookInStock();
        } else {
            System.out.println("invalid input, try again.");
        }
    }

    // EFFECTS: helper method: search books
    private void searchBooks() {
        System.out.println("n -> search book by name");
        System.out.println("c -> search book by category");
        String command = input.next();
        if (command.equals("n")) {
            searchBookByName();
        } else if (command.equals("c")) {
            searchBookByCategory();
        } else {
            System.out.println("invalid input, try again");
        }
    }

    // EFFECTS: helper method: add/remove the books in order
    private void editOrder() {
        System.out.println("\tadd -> add books to order");
        System.out.println("\tremove -> remove books from order");
        String command = input.next();
        if (command.equals("add")) {
            addBookInOrder();
        } else if (command.equals("remove")) {
            removeBookInOrder();
        } else {
            System.out.println("invalid input, try again");
        }
    }

    // EFFECTS: input a book's name to check if it's in the book store
    private void searchBookByName() {
        System.out.println("Please enter the book name: ");
        String bookName = input.next();
        if (wareHouse.searchByName(bookName) == -1) {
            System.out.println(bookName + "does not exist.");
        } else {
            System.out.println(bookName + " is in our store.");
        }
    }

    // EFFECTS: input a category of book then print out all books with that category
    private void searchBookByCategory() {
        System.out.println("Please enter the book category: ");
        String bookCate = input.next();
        ArrayList<Integer> indices = wareHouse.searchByCategory(bookCate);
        if (indices.size() == 0) {
            System.out.println("No such books in our store.");
        } else {
            System.out.println("Books are as listed: ");
            for (int next : indices) {
                Book b = wareHouse.getBookByIndex(next);
                System.out.println(b.getName());
            }
        }
    }

    // EFFECTS: get a list popular books with size n
    private void recommend() {
        System.out.println("\nPlease enter the size of your recommendation list: ");
        System.out.println("\tthe number must be less than " + wareHouse.getSize());
        int n = input.nextInt();
        ArrayList<String> bookList = wareHouse.viewPopularBooks(n);
        System.out.println("these are the most popular books: " + bookList);
    }

    // EFFECTS: print a list of book names in the warehouse
    private void viewBooks() {
        ArrayList<String> bookList = wareHouse.viewBooks();
        System.out.println("We have books in our store as following: " + bookList);
    }

    // MODIFIES: this
    // EFFECTS: given book name, add n books to order
    private void addBookInOrder() {
        System.out.println("What book do you want to order?");
        String bookName = input.next();
        System.out.println("How many do you want to order?");
        int n = input.nextInt();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book in stock");
        } else {
            Book b = wareHouse.getBookByIndex(index);
            if (n > b.getNumber()) {
                System.out.println("We have only " + b.getNumber() + "copies in stock");
                System.out.println("so you have added " + b.getNumber() + "books in your order.");
                order.addBooks(b, b.getNumber());
            } else {
                order.addBooks(b, n);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: given book name, remove n books from the order
    private void removeBookInOrder() {
        System.out.println("What book do you want to remove from your order?");
        String bookName = input.next();
        System.out.println("How many books do you want to remove?");
        int n = input.nextInt();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            Book b = wareHouse.getBookByIndex(index);
            order.removeBooks(b, n);
        }  
    }

    // EFFECTS: view books in an order
    private void viewOrder() {
        System.out.println("\nHere are your ordered books: ");
        ArrayList<Book> orderList = order.getOrderList();
        for (Book b : orderList) {
            System.out.println(b.getName());
        }
    }

    // EFFECTS: view the total price of the order
    private void totalPrice() {
        System.out.println("\nFor this order, you need to pay " + order.getTotalPrice() + "$");
    }

    // EFFECTS: given book name, view the price per book
    private void bookPrice() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            Book b = wareHouse.getBookByIndex(index);
            System.out.println("The price for " + bookName + " is " + b.getPrice() + "$");
        }

    }

    // EFFECTS: given book name, view the description
    private void bookDescription() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            Book b = wareHouse.getBookByIndex(index);
            System.out.println("The description of " + bookName + " is: ");
            System.out.println(b.getDescription());
        }
    }

    // EFFECTS: given book name, view the category
    private void bookCategory() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            Book b = wareHouse.getBookByIndex(index);
            System.out.println("The category of " + bookName + " is: ");
            System.out.println(b.getDescription());
        }
        
    }

    // EFFECTS: given book name, view the number of the book in the warehouse
    private void bookInStock() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            Book b = wareHouse.getBookByIndex(index);
            System.out.println("The available number of " + bookName + " is: ");
            System.out.println(b.getNumber());
        }
    }

    // EFFECTS: process staff command
    private void processStaffCommand(String command) {
        if (command.equals("add")) {
            addBookToWarehouse();
        } else if (command.equals("rem")) {
            removeBookFromWarehouse();
        } else if (command.equals("all")) {
            viewBooks();;
        } else if (command.equals("orders")) {
            viewOrders();
        } else if (command.equals("opr")) {
            operateOrder();
        } else if (command.equals("price")) {
            setBookPrice();
        } else if (command.equals("des")) {
            setBookDescription();
        } else if (command.equals("cate")) {
            setBookCategory();
        } else if (command.equals("stock")) {
            setBookInStock();
        } else {
            System.out.println("invalid input, try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: add a new book to the warehouse
    private void addBookToWarehouse() {
        System.out.println("Please enter the book name: ");
        String bookName = input.next();
        System.out.println("Please enter the book price: ");
        double price = input.nextDouble();
        System.out.println("Please enter the description: ");
        String description = input.next();
        System.out.println("Please enter the book category: ");
        String category = input.next();
        System.out.println("Please enter the avaible number of books: ");
        int number = input.nextInt();
        System.out.println("Book sales is initialized as 0.");
        int sales = 0;
        Book b = new Book(bookName, price, description, category, number, sales);
        wareHouse.addBook(b);
    }

    // MODIFIES: this
    // EFFECTS: remove a book from the warehouse
    private void removeBookFromWarehouse() {
        System.out.println("\tWhich book do you want to remove?");
        viewBooks();
        String bookName = input.next();
        wareHouse.removeBook(bookName);
        System.out.println("\tNow we have these books left: ");
        viewBooks();
    }

    // EFFECTS: print out a list of customers' names who made orders
    private void viewOrders() {
        if (orders.size() == 0) {
            System.out.println("No orders yet.");
        } else {
            System.out.println("\tWe have customers as following: ");
            for (Order o : orders) {
                System.out.println(o.getName());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: operate on the first order that hasn't been completed
    // view order details and set status
    private void operateOrder() {
        boolean areOrdersCompleted = true;
        if (orders.size() == 0) {
            System.out.println("No orders yet.");
        } else {
            for (Order order : orders) {
                if (order.getStatus() == false) {
                    areOrdersCompleted = false;
                    System.out.println(order.getName() + "'s order hasn't been completed.");
                    System.out.println("\nThe details are as following: ");
                    ArrayList<Book> books = order.getOrderList();
                    ArrayList<Integer> numbers = order.getNumberList();
                    int size = books.size();
                    printOutBooks(books, numbers, size);
                    System.out.println("Do you want to complete this order? (Enter y/n)");
                    String command = input.next();
                    areOrdersCompleted = checkOrdersStatus(areOrdersCompleted, order, command); 
                }
            }
            if (areOrdersCompleted == true) {
                System.out.println("All orders are completed.");
            }
        }
    }

    // EFFECTS: if orders that have been seen are completed, return true
    private boolean checkOrdersStatus(boolean areOrdersCompleted, Order order, String command) {
        if (command.equals("y")) {
            order.complete();
            areOrdersCompleted = true;
        }
        return areOrdersCompleted;
    }

    // EFFECTS: helper function: print each book name: number in turn
    private void printOutBooks(ArrayList<Book> books, ArrayList<Integer> numbers, int size) {
        for (int i = 0; i < size; i++) {
            Book b = books.get(i);
            System.out.println(b.getName() + " : " + numbers.get(i));
        }
    }

    // MODIFIES: this
    // EFFECTS: given book name, set the price of the book
    private void setBookPrice() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            System.out.println("How much? ($)");
            double price = input.nextDouble();
            Book b = wareHouse.getBookByIndex(index);
            double originalPrice = b.getPrice();
            b.setPrice(price);
            System.out.println("the price of " + bookName + " from " + originalPrice + " to " + b.getPrice() + "$");
        }
    }

    // MODIFIES: this
    // EFFECTS: given book name, set the description of the book
    private void setBookDescription() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            System.out.println("New description: ");
            Book b = wareHouse.getBookByIndex(index);
            String desc = input.next();
            b.setDescription(desc);;
            System.out.println("the new description is: " + b.getDescription());
        }
    }

    // MODIFIES: this
    // EFFECTS: given book name, set the category of the book
    private void setBookCategory() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            System.out.println("New category is: ");
            Book b = wareHouse.getBookByIndex(index);
            String cate = input.next();
            b.setCategory(cate);
            System.out.println("the new category is: " + b.getCategory());
        }
    }

    // MODIFIES: this
    // EFFECTS: given book name, set the number of books in stock
    private void setBookInStock() {
        System.out.println("What book do you want to select?");
        String bookName = input.next();
        int index = wareHouse.searchByName(bookName);
        if (index == -1) {
            System.out.println("No such book");
        } else {
            Book b = wareHouse.getBookByIndex(index);
            System.out.println("enter the increase number (negative for decrease)");
            int n = input.nextInt();
            if (n > 0) {
                b.increaseNum(n);
            } else if (n < 0 && b.getNumber() + n < 0) {
                b.decreaseNum(-n);
                System.out.println("The number has decreased to zero");
            } else {
                b.decreaseNum(-n);
            }
        }
    }
}
