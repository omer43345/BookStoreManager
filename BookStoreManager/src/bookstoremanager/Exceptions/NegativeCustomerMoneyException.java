package bookstoremanager.Exceptions;

public class NegativeCustomerMoneyException extends Exception{
	public NegativeCustomerMoneyException(String msg) {
		super(msg);
	}
}
