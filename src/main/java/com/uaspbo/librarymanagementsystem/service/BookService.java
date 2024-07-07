package com.uaspbo.librarymanagementsystem.service;

import java.util.List;

import com.uaspbo.librarymanagementsystem.entity.Book;

public interface BookService {

	public List<Book> findAllBooks(Long categoryId, Long publisherId, Long authorId, String sortField, String sortDir);
	
	public List<Book> searchBooks(String keyword);

	public Book findBookById(Long id);

	public void createBook(Book book);

	public void updateBook(Book book);

	public void deleteBook(Long id);

	public long getTotalBooks();
}
