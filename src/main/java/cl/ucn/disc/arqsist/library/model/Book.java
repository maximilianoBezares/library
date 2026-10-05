/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Book entity.
 */
@DatabaseTable(tableName = "books")
public final class Book {

    /**
     * The ID.
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * The title.
     */
    @DatabaseField(canBeNull = false)
    private String title;

    /**
     * The author.
     */
    @DatabaseField(canBeNull = false)
    private String author;

    /**
     * The ISBN.
     */
    @DatabaseField(canBeNull = false)
    private String isbn;

    /**
     * The total number of copies owned by the library.
     */
    @DatabaseField(canBeNull = false)
    private int totalCopies;

    /**
     * The number of copies available for loan.
     */
    @DatabaseField(canBeNull = false)
    private int availableCopies;

    /**
     * The empty constructor for ORMLite.
     */
    public Book() {
    }

    /**
     * The Constructor. All the copies start as available.
     *
     * @param title       The title.
     * @param author      The author.
     * @param isbn        The ISBN.
     * @param totalCopies The total number of copies.
     */
    public Book(String title, String author, String isbn, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    /**
     * Gets the ID.
     *
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID.
     *
     * @param id The new ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title.
     *
     * @param title The new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author.
     *
     * @return The author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author.
     *
     * @param author The new author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the ISBN.
     *
     * @return The ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Set the ISBN.
     *
     * @param isbn The new ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the total number of copies.
     *
     * @return The total number of copies.
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Set the total number of copies.
     *
     * @param totalCopies The new total number of copies.
     */
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    /**
     * Gets the number of available copies.
     *
     * @return The number of available copies.
     */
    public int getAvailableCopies() {
        return availableCopies;
    }

    /**
     * Set the number of available copies.
     *
     * @param availableCopies The new number of available copies.
     */
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}
