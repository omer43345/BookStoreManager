package bookstoremanager.entity;

import bookstoremanager.Exceptions.InvalidDiscountException;
import bookstoremanager.Exceptions.NegativeBookPriceException;

import java.util.Objects;

public class Book extends Entity {
    private static int idCounter = 0;
    private int bookId;
    private double discount;
    private String writer;
    private String publisher;
    private String book;
    private double price;

    public Book(String book, String writer, String publisher, double price, double discount) throws InvalidDiscountException, NegativeBookPriceException {
        if (price < 0) {// invalid discount
            throw new NegativeBookPriceException("Book price can not be a negative number");
        }

        if (discount < 0 || discount > 100) {// invalid discount
            throw new InvalidDiscountException("Invalid discount");
        }
        this.discount = discount;
        this.price = price * ((100 - discount) / 100);
        this.book = book;
        this.writer = writer;
        this.publisher = publisher;
        this.bookId = idCounter;
        idCounter++;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }

    @Override
    public String toString() {
        return "Book\nBook name: " + book + "\nbookId = " + bookId + "\nprice afetr discount of " + discount + "% is = " + price + "\nwriter: " + writer + "\npublisher: " + publisher;
    }

    public void setDiscount(double discountPrecentage) throws InvalidDiscountException {
        // change the price according to the discount
        if (discountPrecentage < 0 || discountPrecentage > 100) {// invalid discount
            throw new InvalidDiscountException("Invalid discount");
        }
        if (this.discount == 0) {
            this.discount = discountPrecentage;
        } else {
            this.discount = 100 - (100 - this.discount) * ((discountPrecentage) / 100);
        }
        this.price *= (100 - discountPrecentage) / 100;
    }

    public void setPrice(double price) throws NegativeBookPriceException {
        if (price < 0) {// invalid discount
            throw new NegativeBookPriceException("Book price can not be a negative number");
        }
        this.price = price;
        this.discount = 0;
    }


    public String getBook() {
        return book;
    }

    @Override
    public int getId() {
        return bookId;
    }

    @Override
    public double getValue() {
        return price;
    }

    public static void initializeCounter(int counter) {
        idCounter = counter;

    }


}
