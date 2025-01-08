# Online Bookstore Application

## Proposal

### Introduction

This project is to design an **online bookstore application**. With the popularity of the internet, the demand for online shopping is growing. This application will help *customers* make orders and help *staffs* complete orders and manage the warehouse.

Things you can do as a *customer*:
- Login in with your name
- search books by names and categories.
- view details of each book in the store.
- add/remove available books in your order and view total price.
- see the best sellers list.

Things you can do as a *staff*:
- login in as staff.
- add/remove books in the warehouse.
- set the details of books in the store.
- see and operate on the orders from customers.


### **Why** do I design this application?

First, when I ordered books from UBC bookstore online, I found there're no rankings or recommendations of books. Hence, I want to improve this in my application. Second, it is interesting to design a application for two types of users. Third, for physical stores, this application may increase their sales. In addition, online bookstore is more convenient for customers to choose and check out.

## User Stories

- As a customer, I want to be able to add a *book* to my *order*.
- As a customer, I want to be able to view the list of books in my *order*.
- As a customer, I want to be able to remove a *book* from my *order*.
- As a customer, I want to be able to see the total price of books in my *order*.
- As a customer, I want to be able to view a *book* in detail.
- As a customer, I want to be able to view a list of *books* ranking by sales.
- As a staff, I want to be able to add/remove a type of *book* to the *warehouse*.
- As a staff, I want to be able to view the list of books in the *warehouse*.
- As a staff, I want be able to set details (*category*,*number*,*description*,etc)
 of *book* in the *warehouse*.
- As a staff, I want be able to view the *orders* made by customers.
- As a staff, I want be able to complete the *orders* made by customers.

### Phase 2

- As a user, when I select to quit the bookstore application, I want to be reminded to save the changes to the bookstore and have a option to do so.
- As a user, when I start the bookstore application, I want to be given an option to load a bookstore from file.

## Instructions for End User

- You can generate "adding multiple Xs to a Y" by:
    1. As a customer, select "add books to order", then enter book name, press "return" on your keyboard; then enter numbers, press "return" on your keyboard; the added books will show on the panel.
    2. As a staff, select "add books to store", then enter the information as the instruction (using "/" to separate),
 press "return" on your keyboard; the books in the warehouse will update.
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by:
    1. As a customer, select "remove books from order", then enter book name, press "return" on your keyboard; then enter numbers, press "return" on your keyboard; the books in order will update on the panel.
    2. As a staff, select "set the price of a book", enter the information as required, press "return" on your keyboard; the price of the book will be updated.
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by:
    1. As a customer, select "view total price", the total price in the order will show on a panel.
    2. As a staff, select "set the description of a book", enter the information as required, press "return" on your keyboard; the description of the book in the warehouse will be updated.
- You can locate my visual component by selecting "quit", choose whether to save the data, and click the "Leave the bookstore" button, a "goodbye" picture will show.
- You can save the state of my application by selecting "quit", choose "Yes" to save the data.
- You can reload the state of my application by before choose status, choose "load data from file" button.

## Phase 4: Task 2  
### sample 1 (customer)
Tue Nov 26 23:26:00 PST 2024
Real Analysis is added to the store

Tue Nov 26 23:26:00 PST 2024
CPSC210 Notes is added to the store

Tue Nov 26 23:26:00 PST 2024
PrideAndPrejudice is added to the store

Tue Nov 26 23:26:00 PST 2024
test1 is added to the store

Tue Nov 26 23:26:00 PST 2024
test2 is added to the store

Tue Nov 26 23:26:27 PST 2024
2 Book(s): Real Analysis are ordered by Mike

Tue Nov 26 23:26:37 PST 2024
1 Book(s): Real Analysis are removed from the order by Mike

Tue Nov 26 23:26:52 PST 2024
2 Book(s): CPSC210 Notes are ordered by Mike

Tue Nov 26 23:26:54 PST 2024
Viewed Mike's total price of ordered book: 160.0$

### sample 2 (staff)
Tue Nov 26 23:30:14 PST 2024
Real Analysis is added to the store

Tue Nov 26 23:30:14 PST 2024
CPSC210 Notes is added to the store

Tue Nov 26 23:30:14 PST 2024
PrideAndPrejudice is added to the store

Tue Nov 26 23:30:14 PST 2024
test1 is added to the store

Tue Nov 26 23:30:14 PST 2024
test2 is added to the store

Tue Nov 26 23:30:53 PST 2024
Phase4Instruction is added to the store

Tue Nov 26 23:31:16 PST 2024
The price of PrideAndPrejudice is set to: 70.0$

Tue Nov 26 23:31:39 PST 2024
The description of Real Analysis is set to: this is a challenging math book

## Phase 4: Task 3
- I may rename the *BookCollection* class as *WareHouse*, and add an abstract class called *BookCollection*, then make *WareHouse* and *Order* to extend this new abstract class. The **original** *BookCollection* class acts as the warehouse of the bookstore, so the name is not accurate. Since both **original** *BookCollection* and *Order* has a list of books as their field, there are duplicated codes in both class. Move these codes to a Superclass will improve the coupling.

- I'd like to create a new *Orders* class and add it to the field of *BookStore* to replace the list of *Order*. Some *Order* related methods are currently defined in *BookStore*, which violates the single responsibility principle and limits further development. I would also split the GUI frames and panels from the *BookStore* class as new classes for cohesion. The *BookStore* class works for the console and GUI, so the relationships between methods are not neat. After refactoring, the coupling will be improved.