package internship.task2.ctc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import internship.task2.ctc.entity.TheBook;

public interface BookRepository  extends JpaRepository<TheBook, Integer> {

}
