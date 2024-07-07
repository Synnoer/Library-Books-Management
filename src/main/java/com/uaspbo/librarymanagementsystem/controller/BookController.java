package com.uaspbo.librarymanagementsystem.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.uaspbo.librarymanagementsystem.entity.Book;
import com.uaspbo.librarymanagementsystem.service.AuthorService;
import com.uaspbo.librarymanagementsystem.service.BookService;
import com.uaspbo.librarymanagementsystem.service.CategoryService;
import com.uaspbo.librarymanagementsystem.service.PublisherService;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BookController {

	private final BookService bookService;
	private final AuthorService authorService;
	private final CategoryService categoryService;
	private final PublisherService publisherService;

	@Autowired
	public BookController(BookService bookService, AuthorService authorService, CategoryService categoryService,
						  PublisherService publisherService) {
		this.bookService = bookService;
		this.authorService = authorService;
		this.categoryService = categoryService;
		this.publisherService = publisherService;
	}

	@GetMapping("/books")
	public String findAllBooks(@RequestParam(required = false) Long categoryId,
							   @RequestParam(required = false) Long publisherId,
							   @RequestParam(required = false) Long authorId,
							   @RequestParam(defaultValue = "name") String sortField,
							   @RequestParam(defaultValue = "asc") String sortDir,
							   Model model) {
		List<Book> books = bookService.findAllBooks(categoryId, publisherId, authorId, sortField, sortDir);
		model.addAttribute("books", books);
		model.addAttribute("categories", categoryService.findAllCategories());
		model.addAttribute("publishers", publisherService.findAllPublishers());
		model.addAttribute("authors", authorService.findAllAuthors());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		return "list-books";
	}

	@RequestMapping("/book/{id}")
	public String findBookById(@PathVariable("id") Long id, Model model) {
		final Book book = bookService.findBookById(id);

		model.addAttribute("book", book);
		return "list-book";
	}

	@GetMapping("/add")
	public String showCreateForm(Book book, Model model) {
		model.addAttribute("categories", categoryService.findAllCategories());
		model.addAttribute("authors", authorService.findAllAuthors());
		model.addAttribute("publishers", publisherService.findAllPublishers());
		return "add-book";
	}

	@PostMapping("/add-book")
	public String createBook(@RequestParam("coverImage") MultipartFile coverImage, Book book, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-book";
		}

		String fileName = StringUtils.cleanPath(coverImage.getOriginalFilename());
		book.setCoverImagePath("/uploads/" + fileName);

		try {
			Path path = Paths.get("uploads/" + fileName);
			Files.copy(coverImage.getInputStream(), path);
		} catch (IOException e) {
			e.printStackTrace();
			// Handle error
		}

		bookService.createBook(book);
		model.addAttribute("books", bookService.findAllBooks(null, null, null, null, null));
		return "redirect:/books";
	}


	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		Book book = bookService.findBookById(id);
		model.addAttribute("book", book);
		model.addAttribute("categories", categoryService.findAllCategories());
		model.addAttribute("authors", authorService.findAllAuthors());
		model.addAttribute("publishers", publisherService.findAllPublishers());
		return "update-book";
	}

	@PostMapping("/update-book/{id}")
	public String updateBook(@PathVariable("id") Long id, @RequestParam("coverImage") MultipartFile coverImage, @ModelAttribute("book") Book book, BindingResult result, Model model) {
		if (result.hasErrors()) {
			book.setId(id);
			return "update-book";
		}

		if (!coverImage.isEmpty()) {
			String fileName = StringUtils.cleanPath(coverImage.getOriginalFilename());
			book.setCoverImagePath("/uploads/" + fileName);

			try {
				Path path = Paths.get("uploads/" + fileName);
				Files.copy(coverImage.getInputStream(), path);
			} catch (IOException e) {
				e.printStackTrace();
				// Handle error
			}
		} else {
			// Handle the case where no new image is uploaded
			Book existingBook = bookService.findBookById(id);
			book.setCoverImagePath(existingBook.getCoverImagePath());
		}

		bookService.updateBook(book);
		return "redirect:/books";
	}

	@RequestMapping("/remove-book/{id}")
	public String deleteBook(@PathVariable("id") Long id, Model model) {
		bookService.deleteBook(id);

		model.addAttribute("books", bookService.findAllBooks(null, null, null, null, null));
		return "redirect:/books";
	}

}
