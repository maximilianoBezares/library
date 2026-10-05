/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Book;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * The data access object of the {@link Book} entity.
 */
public final class BookDao {

    /**
     * The ORMLite DAO.
     */
    private final Dao<Book, Integer> dao;

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     * @throws SQLException if the DAO cannot be created.
     */
    public BookDao(ConnectionSource connectionSource) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, Book.class);
    }

    /**
     * Finds all the books.
     *
     * @return The list of all the books.
     * @throws SQLException if the query fails.
     */
    public List<Book> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a book by its ID.
     *
     * @param id The ID of the book.
     * @return The book, or null if it does not exist.
     * @throws SQLException if the query fails.
     */
    public Book findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Creates a book.
     *
     * @param book The book to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Book book) throws SQLException {
        dao.create(book);
    }

    /**
     * Updates a book.
     *
     * @param book The book to update.
     * @throws SQLException if the update fails.
     */
    public void update(Book book) throws SQLException {
        dao.update(book);
    }

    /**
     * Deletes a book.
     *
     * @param book The book to delete.
     * @throws SQLException if the delete fails.
     */
    public void delete(Book book) throws SQLException {
        dao.delete(book);
    }
}
