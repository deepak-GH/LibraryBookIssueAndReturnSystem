package internship.task2.ctc.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import internship.task2.ctc.entity.Borrower;
import internship.task2.ctc.entity.TheBook;
import internship.task2.ctc.service.LibraryServiceFeatures;

@Controller
public class LibraryController {
	
	@Autowired	
	private LibraryServiceFeatures libSerFeatures;
	
	public LibraryController(LibraryServiceFeatures libSerFeatures) {
		super();
		this.libSerFeatures = libSerFeatures;
	}
	
	@GetMapping("/")
	public String viewHomePage(Model model) {
		
		Borrower borrower = new Borrower();
		model.addAttribute("user", borrower);
		TheBook sb=new TheBook();
		model.addAttribute("searchedBookObj", sb);
		return "index";
	}
	
	@PostMapping("/bookSearch")
	public String searched(@ModelAttribute("searchedBookObj") TheBook book, Model model) {	
				
		int searchedBookId= libSerFeatures.searchBookByName(book.getTitle());
		
		if(searchedBookId<0) {
			model.addAttribute("searchFailureMsg", "Book/Genre not found!");
			return "searchFailure";
		}
		else {
			model.addAttribute("searchedBookId", searchedBookId);
			model.addAttribute("searchedBookObj", libSerFeatures.getBookById(searchedBookId));
			return "searchSuccess";
		}			
	}
	
	@GetMapping("/loginBorrower/{bookId}")
	public String loginBorrower(@PathVariable("bookId") int searchedBookId,
								Model model) {
		
		model.addAttribute("searchedBookId", searchedBookId);
		Borrower borrower = new Borrower();
		model.addAttribute("user", borrower);
		return "loginForm1";		
	}
	
	@PostMapping("/showBorrowerDetails/{bookId}")
	public String showBorrowerDetails(@ModelAttribute("user") Borrower b ,
									  @PathVariable("bookId") int searchedBookId,
									Model model) {	
		
		String action="";
		TheBook book1 = libSerFeatures.getBookById(searchedBookId);
		model.addAttribute("searchedBookObj", book1);			
		boolean isValidBorrower = libSerFeatures.isValidUser(b);
		if(isValidBorrower) {
			Borrower borrower = libSerFeatures.getBorrowerById(b.getBorrowerId());
			action = libSerFeatures.decideIssueAction(b.getBorrowerId());
			libSerFeatures.calculatePenalty(borrower);
			model.addAttribute("user", borrower);				
		}
		else 
			action = "Wrong LogIn Details .";
		
		model.addAttribute("issuePermission", action);
		return "borrowerDetails";		
	}
	
	@GetMapping("/confirm/{uid}/book/{bid}")
	public String confirmed(@PathVariable("uid") int uid,
						  @PathVariable("bid") int bid, Model model) {
		
		model.addAttribute("user", libSerFeatures.getBorrowerById(uid));
		model.addAttribute("issuedBook", libSerFeatures.getBookById(bid));
		libSerFeatures.issueConfirmed(uid, bid);		
		return "confirmation";		
	}
	
	@GetMapping("/returnBook/{uid}/{bid}")
	public String returnBook(@PathVariable("uid") int uid,
			  @PathVariable("bid") int bid, Model model) {
		
		Borrower borrower = libSerFeatures.getBorrowerById(uid);
		model.addAttribute("user", borrower);
		for(TheBook book : borrower.getBorrowedBooks()) {			
			if((book.getBookId()==bid) && (book.getFineAmt()>0.0)) {
				model.addAttribute("returnBookObj", book);
				return "paymentPage";
			}
		}
		libSerFeatures.returningBook(uid , bid);
		return "returnSuccess";
	}
	
	@GetMapping("/confirmPayment/{uid}/{bid}")
	public String confirmPay(@PathVariable("uid") int uid,
			  @PathVariable("bid") int bid, Model model) {
		
		libSerFeatures.returningBook(uid , bid);
		return "returnSuccess";
	}
	
	
	@GetMapping("/addBook")
	public String addBook(Model model) {
		model.addAttribute("addedBook", new TheBook());
		model.addAttribute("newBookMsg", "Add Book To Library Resource");
		return "addBookForm";
	}
	
	@PostMapping("/added")
	public String added(
			Model model, 
			@ModelAttribute("addedBook") TheBook book) {
		
		if(book.getTitle()!= null)	{		
			libSerFeatures.addBook(book);			
			return "redirect:/showAvailableBooks";
		}
		else {
			model.addAttribute("newBookMsg", "Wrong adminId or adminPassword entered!");
			return "addBookForm";
		}		
	}
	
	@GetMapping("/registerBorrower") 
	public String registerBorrower(Model model) {	  
	  model.addAttribute("user", new Borrower()); 
	  return "registrationForm"; 
	}
	
	@PostMapping("/saveBorrower")
	public String saveBorrower(@ModelAttribute("user") Borrower b , Model model) {
		libSerFeatures.saveBorrower(b);	
	
		model.addAttribute("searchFailureMsg", "Search Book/Genre");
		model.addAttribute("searchedBookObj", new TheBook());
		return "searchFailure";
	}
	 
	@GetMapping("/showAvailableBooks")
	public String showAvailableBooks(Model model) {		
		List<TheBook> listOfAvailBooks = libSerFeatures.allAvailableBooks();
		model.addAttribute("allAvailabaleBooks", listOfAvailBooks);
		return "showAvailBooks";		
	}
	
	
	
}
