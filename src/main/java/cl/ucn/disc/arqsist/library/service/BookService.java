/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.model.Book;

import java.util.List;

/**
 * The service of the book catalog.
 */
public final class BookService {

    /**
     * The book DAO.
     */
    private final BookDao dao;

    /**
     * The Constructor.
     *
     * @param dao The book DAO.
     */
    public BookService(BookDao dao) {
        this.dao = dao;
    }

    /**
     * Lists all the books.
     *
     * @return The list of all the books.
     */
    public List<Book> listAll() {
        return dao.findAll();
    }

    /**
     * Finds a book by its ID.
     *
     * @param id The ID of the book.
     * @return The book, or null if it does not exist.
     */
    public Book findById(int id) {
        return dao.findById(id);
    }

    /**
     * Creates a book. The available copies are set to the total copies.
     *
     * @param book The book to create.
     * @return The created book.
     */
    public Book create(Book book) {
        book.setAvailableCopies(book.getTotalCopies());
        dao.create(book);
        return book;
    }

    /**
     * Takes one copy of a book out of the inventory.
     *
     * @param bookId The ID of the book.
     * @throws NotFoundException     if the book does not exist.
     * @throws IllegalStateException if the book has no available copies.
     */
    public void borrow(int bookId) {
        Book book = dao.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of book " + bookId);
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        dao.update(book);
    }

    /**
     * Puts one copy of a book back into the inventory.
     *
     * @param bookId The ID of the book.
     * @throws NotFoundException if the book does not exist.
     */
    public void returnCopy(int bookId) {
        Book book = dao.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        dao.update(book);
    }
}
