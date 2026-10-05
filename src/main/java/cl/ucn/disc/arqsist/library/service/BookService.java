/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.model.Book;

import java.sql.SQLException;
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
     * @throws SQLException if the query fails.
     */
    public List<Book> listAll() throws SQLException {
        return dao.findAll();
    }

    /**
     * Finds a book by its ID.
     *
     * @param id The ID of the book.
     * @return The book, or null if it does not exist.
     * @throws SQLException if the query fails.
     */
    public Book findById(int id) throws SQLException {
        return dao.findById(id);
    }

    /**
     * Creates a book. The available copies are set to the total copies.
     *
     * @param book The book to create.
     * @return The created book.
     * @throws SQLException if the insert fails.
     */
    public Book create(Book book) throws SQLException {
        book.setAvailableCopies(book.getTotalCopies());
        dao.create(book);
        return book;
    }

    /**
     * Takes one copy of a book out of the inventory.
     *
     * @param bookId The ID of the book.
     * @throws SQLException if the query or the update fails.
     */
    public void borrow(int bookId) throws SQLException {
        Book book = dao.findById(bookId);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        dao.update(book);
    }

    /**
     * Puts one copy of a book back into the inventory.
     *
     * @param bookId The ID of the book.
     * @throws SQLException if the query or the update fails.
     */
    public void returnCopy(int bookId) throws SQLException {
        Book book = dao.findById(bookId);
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        dao.update(book);
    }
}
