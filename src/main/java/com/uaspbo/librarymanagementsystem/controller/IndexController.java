package com.uaspbo.librarymanagementsystem.controller;

import com.uaspbo.librarymanagementsystem.entity.Book;
import com.uaspbo.librarymanagementsystem.service.BookService;
import com.uaspbo.librarymanagementsystem.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

	@Autowired
	private BookService bookService;

	@Autowired
	private AuthorService authorService;

	@GetMapping("/")
	public String list(Model model) {
		long totalBooks = bookService.getTotalBooks();
		long totalAuthors = authorService.getTotalAuthors();

		model.addAttribute("totalBooks", totalBooks);
		model.addAttribute("totalAuthors", totalAuthors);

		return "index";
	}

	@RequestMapping("/searchBook")
	public String searchBook(@Param("keyword") String keyword, Model model) {
		final List<Book> books = bookService.searchBooks(keyword);

		model.addAttribute("books", books);
		model.addAttribute("keyword", keyword);
		return "list-books";
	}
}
