package com.uaspbo.librarymanagementsystem;

import com.uaspbo.librarymanagementsystem.entity.Author;
import com.uaspbo.librarymanagementsystem.entity.Book;
import com.uaspbo.librarymanagementsystem.entity.Category;
import com.uaspbo.librarymanagementsystem.entity.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.uaspbo.librarymanagementsystem.service.BookService;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class LibrarymanagementsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarymanagementsystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialCreate(BookService bookService) {
		return (args) -> {

			Book book = new Book("123", "The Two Towers", "Lord of The Rings", "For Frodo!", "\t/uploads/TwoTowers.jpg");
			Author author = new Author("Tolkien", "Test description");
			Category category = new Category("Fantasy");
			Publisher publisher = new Publisher("Mase");

			book.addAuthors(author);
			book.addCategories(category);
			book.addPublishers(publisher);

			bookService.createBook(book);

		};
	}
}
