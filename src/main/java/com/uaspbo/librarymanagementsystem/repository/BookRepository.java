package com.uaspbo.librarymanagementsystem.repository;

import java.util.List;

import com.uaspbo.librarymanagementsystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("SELECT b FROM Book b WHERE b.name LIKE %?1%" + " OR b.isbn LIKE %?1%" + " OR b.serialName LIKE %?1%")
	public List<Book> search(String keyword);

	@Query("SELECT b FROM Book b " +
			"LEFT JOIN b.categories c " +
			"LEFT JOIN b.publishers p " +
			"LEFT JOIN b.authors a " +
			"WHERE (:categoryId IS NULL OR c.id = :categoryId) " +
			"AND (:publisherId IS NULL OR p.id = :publisherId) " +
			"AND (:authorId IS NULL OR a.id = :authorId)")
	List<Book> findAllByFilters(@Param("categoryId") Long categoryId,
								@Param("publisherId") Long publisherId,
								@Param("authorId") Long authorId);
}
