package com.uaspbo.librarymanagementsystem.service.impl;

import java.util.List;

import com.uaspbo.librarymanagementsystem.entity.Book;
import com.uaspbo.librarymanagementsystem.exception.NotFoundException;
import com.uaspbo.librarymanagementsystem.repository.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.uaspbo.librarymanagementsystem.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public List<Book> findAllBooks(Long categoryId, Long publisherId, Long authorId, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();

		if (categoryId != null || publisherId != null || authorId != null) {
			return bookRepository.findAllByFilters(categoryId, publisherId, authorId, sort);
		}
		return bookRepository.findAll(sort);
	}
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public List<Book> searchBooks(String keyword) {
		if (keyword != null) {
			return bookRepository.search(keyword);
		}
		return bookRepository.findAll();
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public Book findBookById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Book not found with ID %d", id)));
	}

	@Override
	public void createBook(Book book) {
		bookRepository.save(book);
	}

	@Override
	public void updateBook(Book book) {
		bookRepository.save(book);
	}

	@Override
	public void deleteBook(Long id) {
		final Book book = bookRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Book not found with ID %d", id)));

		bookRepository.deleteById(book.getId());
	}

	public long getTotalBooks() {
		return bookRepository.count();
	}

}
