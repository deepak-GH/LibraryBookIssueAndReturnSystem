package internship.task2.ctc.entity;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

@Entity
public class TheBook {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int bookId;	
	
	private int bookCountInLib;
	private String title;
	private String author;
	private String genre;
	private Date issuedDate;
	private Date returnDate;
	private double fineAmt;	
	
	@ManyToMany
	@JoinColumn
	private Collection<Borrower> borrowerList = new ArrayList<>();
	
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}	
		
	public int getBookCountInLib() {
		return bookCountInLib;
	}
	public void setBookCountInLib(int bookCountInLib) {
		this.bookCountInLib = bookCountInLib;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public Date getIssuedDate() {
		return issuedDate;
	}
	
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	
	public Date getReturnDate() {
		return returnDate;
	}
	
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	public double getFineAmt() {
		return fineAmt;
	}
	
	public void setFineAmt(double fineAmt) {
		this.fineAmt = fineAmt;
	}
	
	public Collection<Borrower> getBorrowerList() {
		return borrowerList;
	}
	public void setBorrowerList(ArrayList<Borrower> borrowerList) {
		this.borrowerList = borrowerList;
	}
		
}
