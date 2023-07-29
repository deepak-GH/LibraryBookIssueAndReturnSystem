package internship.task2.ctc.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import internship.task2.ctc.entity.Borrower;
import internship.task2.ctc.entity.TheBook;
import internship.task2.ctc.repository.BookRepository;
import internship.task2.ctc.repository.BorrowerRepository;

@Service
public class LibraryService implements LibraryServiceFeatures{
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BorrowerRepository borrowerRepository;
	
	public LibraryService(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
		super();
		this.bookRepository = bookRepository;
		this.borrowerRepository = borrowerRepository;
	}

	@Override
	public int searchBookByName(String bookName) {
		
		List<TheBook> allBooks= bookRepository.findAll();
		for(TheBook book:allBooks) {
			if(bookName.trim().equalsIgnoreCase(book.getTitle().trim())) {
				return  book.getBookId();
			}
		}
		return -99;
	}

	@Override
	public List<TheBook> allAvailableBooks() {
		
		List<TheBook> availBooks=new ArrayList<>();
		
		List<TheBook> allBooks= bookRepository.findAll();
		for(TheBook book:allBooks) {
			if(book.getBookCountInLib()>0)
				availBooks.add(book);
		}		
		return availBooks;
	}

	@Override
	public String decideIssueAction(int borrowerId) {		
		
		int booksIssued = borrowerRepository.getReferenceById(borrowerId).getBorrowedBooks().size() ;					
		if(booksIssued>=3)
			return "Your total book limit exceeded.";
		else
			return "APPROVED";
	}
	
	@Override
	public boolean isValidUser(Borrower b) {
		
		for(Borrower b1 : borrowerRepository.findAll()) {
			if((b1.getBorrowerId() == b.getBorrowerId()) && 
					(b1.getName().equalsIgnoreCase(b.getName())) &&
					(b1.getEmail().equalsIgnoreCase(b.getEmail()))
					)
				return true;
		}
		return false;
	}

	@Override
	public void saveBorrower(Borrower borrower) {		
		borrowerRepository.save(borrower);		
	}

	@Override
	public TheBook getBookById(int bookId) {		
		return bookRepository.getReferenceById(bookId);
	}

	@Override
	public void addBook(TheBook book) {
		
		int flag=0;		
		for(TheBook b: bookRepository.findAll()) {
			if( (book.getTitle().equalsIgnoreCase(b.getTitle()))	&&
				(book.getAuthor().equalsIgnoreCase(b.getAuthor()))  ){
				
				int newCount = book.getBookCountInLib() + b.getBookCountInLib();
				b.setBookCountInLib(newCount);
				bookRepository.save(b);
				flag=-1;
				break;				
			}
		}
		
		if(flag== -1)
			return;
		else
			bookRepository.save(book);	
	}

	@Override
	public Borrower getBorrowerById(int uid) {
		
		return borrowerRepository.getReferenceById(uid);
	}

	@Override
	public void issueConfirmed(int uid, int bid) {
		
		TheBook book = bookRepository.getReferenceById(bid);
		Borrower user = borrowerRepository.getReferenceById(uid);
		
        Date today = new Date();
              
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        Date updatedDate = calendar.getTime();
       
		if(book.getBookCountInLib()>0) {
			user.getBorrowedBooks().add(book);
			book.getBorrowerList().add(user);
			
			book.setBookCountInLib(book.getBookCountInLib()-1);
			book.setIssuedDate(today);
			book.setReturnDate(updatedDate);
			
			bookRepository.save(book);
			borrowerRepository.save(user);
		}		
	}

	@Override
	public void calculatePenalty(Borrower borrower) {
		
		for(TheBook book : borrower.getBorrowedBooks()) {
			
			Calendar returnCalendar = Calendar.getInstance();
			Calendar todayCalendar = Calendar.getInstance();				
			todayCalendar.setTime(new Date());
			returnCalendar.setTime(book.getReturnDate());
			int comparisonResult = todayCalendar.compareTo(returnCalendar);
	        if (comparisonResult > 0) {
	        	long returnTimeInMillis = returnCalendar.getTimeInMillis();
	            long todayTimeInMillis = todayCalendar.getTimeInMillis();
	            long millisecondsPerDay = 24 * 60 * 60 * 1000;
	            long daysDifference = Math.abs((returnTimeInMillis - todayTimeInMillis) / millisecondsPerDay);
	            
	            book.setFineAmt(daysDifference * 5.0);	
	        }				
		}	
		borrowerRepository.save(borrower);
	}

	@Override
	public void returningBook(int uid, int bid) {
		
		TheBook book = bookRepository.getReferenceById(bid);
		Borrower user = borrowerRepository.getReferenceById(uid);
		
		user.getBorrowedBooks().remove(book);
		book.getBorrowerList().remove(user);	
		
		book.setBookCountInLib(book.getBookCountInLib()+1);
		book.setIssuedDate(null);
		book.setReturnDate(null);
		
		bookRepository.save(book);
		borrowerRepository.save(user);
	}
	
	
}
