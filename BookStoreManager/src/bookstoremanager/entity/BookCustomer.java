package bookstoremanager.entity;

import bookstoremanager.Exceptions.AlreadyAMemberException;
import bookstoremanager.Exceptions.InvalidItemExcption;
import bookstoremanager.Exceptions.MoneyNotEnoughException;
import bookstoremanager.Exceptions.NegativeCustomerMoneyException;

import java.util.ArrayList;
import java.util.List;

public class BookCustomer extends Customer {

    private List<Book> books;

    public BookCustomer(String name, double money) throws NegativeCustomerMoneyException {
        super(name, money);
        books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }


    // function to become a member that cost 50 dollars
    public void becomeMember() throws AlreadyAMemberException, MoneyNotEnoughException {
        if (!Membership) {
            if (this.money >= 50) {
                this.money -= 50;
                this.Membership = true;
            } else {
                throw new MoneyNotEnoughException("You do not have enough money to become a member");
            }
        } else {
            throw new AlreadyAMemberException("You are already a member");
        }
    }

    @Override
    public void buyItem(Object item) throws InvalidItemExcption, MoneyNotEnoughException {
        if (item.getClass() != Book.class)
            throw new InvalidItemExcption("Book Customer can not buy this item");
        Book book = (Book) item;
        double updatedMoney;
        if (this.Membership) {
            updatedMoney = (money + credits) - book.getValue();
            credits += book.getValue() * 0.1;
        } else {
            updatedMoney = money - book.getValue();
        }
        if (updatedMoney < 0) {
            throw new MoneyNotEnoughException("You do not have enough money to buy this book\n" + book.getBook());
        }
        books.add(book);
        money = updatedMoney;

    }


    @Override
    public String toString() {
        StringBuilder books = new StringBuilder();
        this.books.forEach(book -> books.append("\n" + book.toString()));
        String allBooks = books.toString();
        return "BookCustomer [" + super.toString() + "\nbooks: " + allBooks + "]";
    }


    @Override
    public int getId() {
        return super.getCustomerId();
    }

    @Override
    public double getValue() {
        return super.money;
    }

}
