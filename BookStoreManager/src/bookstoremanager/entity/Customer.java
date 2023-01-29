package bookstoremanager.entity;

import bookstoremanager.Exceptions.AlreadyAMemberException;
import bookstoremanager.Exceptions.InvalidItemExcption;
import bookstoremanager.Exceptions.MoneyNotEnoughException;
import bookstoremanager.Exceptions.NegativeCustomerMoneyException;

import java.util.Objects;

public abstract class Customer extends Entity {
    protected static int idCounter = 0;
    protected int customerId;
    protected String name;
    protected double money;

    protected Boolean Membership;

    protected double credits = 0;


    public Customer(String name, double money) throws NegativeCustomerMoneyException {
        if (money < 0) {
            throw new NegativeCustomerMoneyException("Customer can not have negative money");
        }
        this.name = name;
        this.money = money;
        customerId = idCounter;
        idCounter++;
        this.Membership = false;
    }

    public abstract void buyItem(Object item) throws InvalidItemExcption, MoneyNotEnoughException;

    public abstract void becomeMember() throws AlreadyAMemberException, MoneyNotEnoughException;

    public static void initializeCounter(int counter) {
        idCounter = counter;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", name=" + name + ", money=" + money + ", Membership=" + Membership + ", credits=" + credits + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, money, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        return customerId == other.customerId;
    }


}
