package internship.task2.ctc.service;

import java.util.List;
import internship.task2.ctc.entity.Borrower;
import internship.task2.ctc.entity.TheBook;

public interface LibraryServiceFeatures {
	
	public int searchBookByName(String bookName);
	public List<TheBook> allAvailableBooks();
	public String decideIssueAction(int borrowerId);
	public boolean isValidUser(Borrower borrower);
	public void calculatePenalty(Borrower borrower);
	public void saveBorrower(Borrower borrower);
	public TheBook getBookById(int bookId);
	public void addBook(TheBook book);
	public Borrower getBorrowerById(int uid);
	public void issueConfirmed(int uid, int bid);
	public void returningBook(int uid, int bid);

}
