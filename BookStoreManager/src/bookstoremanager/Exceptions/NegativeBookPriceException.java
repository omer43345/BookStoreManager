package bookstoremanager.Exceptions;

public class NegativeBookPriceException extends Exception{
	public NegativeBookPriceException(String msg) {
		super(msg);
	}
}
