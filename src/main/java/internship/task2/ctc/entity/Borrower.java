package internship.task2.ctc.entity;

import java.util.ArrayList;
import java.util.Collection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class Borrower {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int borrowerId;	 
	
	private String name;
	private String email;
	
	@ManyToMany(mappedBy="borrowerList")	
	private Collection<TheBook> borrowedBooks = new ArrayList<>();
	
	
	public int getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(int borrowerId) {
		this.borrowerId = borrowerId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Collection<TheBook> getBorrowedBooks() {
		return borrowedBooks;
	}
	public void setBorrowedBooks(ArrayList<TheBook> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}
	
}
