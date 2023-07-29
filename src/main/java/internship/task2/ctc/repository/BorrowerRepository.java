package internship.task2.ctc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import internship.task2.ctc.entity.Borrower;

public interface BorrowerRepository  extends JpaRepository<Borrower, Integer>{

}
