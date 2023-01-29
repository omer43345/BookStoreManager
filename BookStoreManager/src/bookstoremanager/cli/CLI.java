package bookstoremanager.cli;

import bookstoremanager.dal.BookStoreManagerFileDao;
import bookstoremanager.entity.Book;
import bookstoremanager.entity.BookCustomer;
import bookstoremanager.entity.Entity;
import bookstoremanager.service.Managable;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Managable bookStoreService = context.getBean("BookstoreService", Managable.class);

        // constants for user codes
        final int SHOW = 1;
        final int GET = 2;
        final int ADD = 3;
        final int UPDATE = 4;
        final int DELETE = 5;
        final int EXIT = 0;

        String welcomeMsg = "Hello, Welcome to Omer's, Eilam's and Koush's book store manager";
        System.out.println(welcomeMsg);
        Scanner scanner = new Scanner(System.in);
        int choice = -1;


        while (choice != 0) {
            // print options to the user
            System.out.println("Press 1 to see entity list.");
            System.out.println("Press 2 to see a specific entity.");
            System.out.println("Press 3 to add entity to the file");
            System.out.println("Press 4 to update existing entity.");
            System.out.println("Press 5 to delete entity from the file");
            System.out.println("Press 0 to exit the program");
            System.out.println("Enter here your choice: ");
            // getting user input
            choice = scanner.nextInt();
            int entityClassId = -1;
            int id = -1;
            Entity e = null;
            try {
                switch (choice) {
                    case SHOW:

                        // get the id of the class from the user
                        System.out.println("Enter entity:\n0 to book\n1 to book customer");
                        entityClassId = scanner.nextInt();
                        if (entityClassId < 0 || entityClassId > 2) {
                            System.out.println("You have to choose from the given options");
                        }
                        // print all entities
                        if (bookStoreService.getAll(entityClassId) != null) {
                            for (Entity entity : bookStoreService.getAll(entityClassId)) {
                                System.out.println(entity);
                            }
                        }

                        break;
                    case GET:
                        // returns an entity
                        System.out.println("Enter entity:\n0 to book\n1 to book customer");
                        entityClassId = scanner.nextInt();
                        System.out.println("Enter entity id: ");
                        id = scanner.nextInt();
                        switch (entityClassId) {
                            case BookStoreManagerFileDao.bookClassId:
                                System.out.println(bookStoreService.get(id, Book.class));
                                break;
                            case BookStoreManagerFileDao.bookCustomerClassId:
                                System.out.println(bookStoreService.get(id, BookCustomer.class));
                                break;
                            default:
                                System.out.println("You have to enter valid choice.");
                                break;
                        }
                        break;
                    case ADD:
                        //adds new entity
                        System.out.println("Enter entity:\n0 to book\n1 to book customer");
                        entityClassId = scanner.nextInt();
                        switch (entityClassId) {
                            case BookStoreManagerFileDao.bookClassId:
                                System.out.println("Enter book name: ");
                                String bookName = scanner.next();
                                System.out.println("Enter the writer of the book: ");
                                String bookWriter = scanner.next();
                                System.out.println("Enter the publisher of the book: ");
                                String bookPublisher = scanner.next();
                                System.out.println("Enter the price of the book: ");
                                double bookPrice = scanner.nextDouble();
                                System.out.println("Enter the discount on the book: ");
                                double bookDiscount = scanner.nextDouble();
                                e = new Book(bookName, bookWriter, bookPublisher, bookPrice, bookDiscount);
                                break;
                            case BookStoreManagerFileDao.bookCustomerClassId:
                                System.out.println("Enter your name: ");
                                String customerName = scanner.next();
                                System.out.println("Enter how much money you have: ");
                                double money = scanner.nextDouble();
                                e = new BookCustomer(customerName, money);
                                break;
                            default:
                                System.out.println("You have to enter valid choice.");
                                break;
                        }
                        bookStoreService.save(e);
                        break;
                    case UPDATE:
                        //update entity
                        System.out.println("Enter entity:\n0 to update book price or discount\n1 to change how much money you have, to buy book or become a member");
                        entityClassId = scanner.nextInt();

                        switch (entityClassId) {
                            case BookStoreManagerFileDao.bookClassId:
                                System.out.println("Enter book id: ");
                                id = scanner.nextInt();
                                Book updatedBook = (Book) bookStoreService.get(id, Book.class);
                                System.out.println("Enter updated price: ");
                                double updatedPrice = scanner.nextDouble();
                                System.out.println("Enter updated discount: ");
                                double updatedDiscount = scanner.nextDouble();
                                updatedBook.setPrice(updatedPrice);
                                updatedBook.setDiscount(updatedDiscount);
                                bookStoreService.update(updatedBook);
                                System.out.println("book updated!");
                                e = updatedBook;
                                break;
                            case BookStoreManagerFileDao.bookCustomerClassId:
                                System.out.println("Enter book customer id: ");
                                id = scanner.nextInt();
                                BookCustomer updatedCustomer = (BookCustomer) bookStoreService.get(id, BookCustomer.class);
                                System.out.println("Enter 1 to buy book\nEnter 2 to become a member for 50$\nEnter 3 to change how much money you have");
                                entityClassId = scanner.nextInt();
                                switch (entityClassId) {
                                    case 1:
                                        System.out.println("Enter book id: ");
                                        int bookId = scanner.nextInt();
                                        Book customerBook = (Book) bookStoreService.get(bookId, Book.class);
                                        updatedCustomer.buyItem(customerBook);
                                        break;
                                    case 2:
                                        updatedCustomer.becomeMember();
                                        break;
                                    case 3:
                                        // change money
                                        System.out.println("Enter how much money you have: ");
                                        double money = scanner.nextDouble();
                                        updatedCustomer.setMoney(money);
                                        break;
                                    default:
                                        System.out.println("You have to enter valid choice.");
                                        break;
                                }
                                // update customer
                                bookStoreService.update(updatedCustomer);
                                e = updatedCustomer;
                                break;
                            default:
                                System.out.println("You have to enter valid choice.");
                                break;
                        }

                        bookStoreService.update(e);
                        break;
                    case DELETE:
                        //Deletes a given entity
                        System.out.println("Enter entity:\n0 to book\n1 to book customer");
                        entityClassId = scanner.nextInt();

                        switch (entityClassId) {
                            case BookStoreManagerFileDao.bookClassId:
                                System.out.println("Enter book id: ");
                                id = scanner.nextInt();
                                bookStoreService.delete(id, Book.class);
                                break;
                            case BookStoreManagerFileDao.bookCustomerClassId:
                                System.out.println("Enter book customer id: ");
                                id = scanner.nextInt();
                                bookStoreService.delete(id, BookCustomer.class);
                                break;
                            default:
                                System.out.println("You have to enter valid choice.");
                                break;
                        }
                        break;
                    case EXIT:
                        //exits the program
                        System.out.println("Exiting...");
                        context.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("You have to enter valid choice.");
                        break;

                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

