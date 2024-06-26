package com.uaspbo.librarymanagementsystem.controller;

import com.uaspbo.librarymanagementsystem.service.BookService;
import com.uaspbo.librarymanagementsystem.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
