package bookstoremanager.service;

import bookstoremanager.Exceptions.AlreadySavedEntityException;
import bookstoremanager.Exceptions.BellowAlowedInitialPriceException;
import bookstoremanager.Exceptions.TooManyBooksCustomer;
import bookstoremanager.dal.BookStoreManagerFileDao;
import bookstoremanager.entity.Book;
import bookstoremanager.entity.BookCustomer;
import bookstoremanager.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:params.properties")
public class BookstoreService implements Managable {
    // injected from xml
    @Autowired
    private BookStoreManagerFileDao dao;

    // injected properties from file
    @Value("${Managable.max.books}")
    private int maxBooks;

    @Value("${Managable.min.price.book}")
    private double minPriceBook;

    public void setDao(BookStoreManagerFileDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Entity> getAll(int id) throws Exception {
        return dao.getAll(id);
    }

    @Override
    public void save(Entity t) throws Exception {
        // Checked not saved before

        for (int i = 0; i <= BookStoreManagerFileDao.bookCustomerClassId; i++) {
            if (getAll(i) != null) {
                for (Entity e : getAll(i)) {
                    if (e.equals(t)) {
                        throw new AlreadySavedEntityException(String.format("%s already saved.", t.toString()));
                    }
                }

            }
        }
        // check if bookCustomer above max books for bookCustomer
        if (t instanceof BookCustomer) {
            BookCustomer bookCustomer = (BookCustomer) t;
            if (bookCustomer.getBooks().size() > maxBooks) {
                throw new TooManyBooksCustomer("can't save. The max books for bookCustomer is: " + maxBooks);
            }
        }
        // check min book initial price
        if (t instanceof Book) {
            Book book = (Book) t;
            if (book.getValue() < minPriceBook) {
                throw new BellowAlowedInitialPriceException(
                        "The book is bellow allowed initial price: " + minPriceBook);
            }
        }
        dao.save(t);
    }

    @Override
    public void update(Entity t) throws Exception {
        // check if bookCustomer above max books for bookCustomer
        if (t instanceof BookCustomer) {
            BookCustomer bookCustomer = (BookCustomer) t;
            if (bookCustomer.getBooks().size() > maxBooks) {
                throw new TooManyBooksCustomer("can't save. The max books for bookCustomer is: " + maxBooks);
            }
        }
        dao.update(t);
    }

    @Override
    public void delete(int id, Class c) throws Exception {
        dao.delete(id, c);
    }

    @Override
    public Entity get(int idm, Class c) throws Exception {
        return dao.get(idm, c);
    }

    // when the container is up
    public void onStartMethod() throws Exception {
        System.out.println("Hello fellow user!");
        try {
            dao.Initiate();
            printAllEntities();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // when the container is down
    public void onEndMethod() throws Exception {
        System.out.println("Goodbye! thank you for using our system!");
        printAllEntities();

    }

    // prints all the entities
    private void printAllEntities() throws Exception {
        List<Entity> BooksList = getAll(0);
        List<Entity> BooksCustomerList = getAll(1);
        System.out.println("here is the list of entities in the system: ");
        if (BooksList != null) {
            System.out.println("Books: ");
            for (Object book : BooksList) {
                System.out.println("\n"+((Book) book).toString()+"\n");
            }
            System.out.println("\n\n");
        }

        if (BooksCustomerList != null) {
            System.out.println("BookCustomers: ");
            for (Object bookCustomer : BooksCustomerList) {
                System.out.println("\n"+((BookCustomer) bookCustomer).toString()+"\n");
            }
            System.out.println("\n\n");
        }
    }
}
