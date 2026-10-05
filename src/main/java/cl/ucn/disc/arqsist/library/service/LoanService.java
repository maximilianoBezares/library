/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * The service of the loans.
 */
public final class LoanService {

    /**
     * The loan period in days.
     */
    public static final int DUE_DAYS = 21;

    /**
     * The loan DAO.
     */
    private final LoanDao loanDao;

    /**
     * The book DAO.
     */
    private final BookDao bookDao;

    /**
     * The Constructor.
     *
     * @param loanDao The loan DAO.
     * @param bookDao The book DAO.
     */
    public LoanService(LoanDao loanDao, BookDao bookDao) {
        this.loanDao = loanDao;
        this.bookDao = bookDao;
    }

    /**
     * Finds all the loans.
     *
     * @return The list of all the loans.
     * @throws SQLException if the query fails.
     */
    public List<Loan> findAll() throws SQLException {
        return loanDao.findAll();
    }

    /**
     * Returns a loan: marks it as returned, sets the overdue fee if it is late
     * and puts the copy back into the inventory.
     *
     * @param loanId The ID of the loan.
     * @return The loan, or null if it does not exist. An already returned loan is returned unchanged.
     * @throws SQLException if a query or a write fails.
     */
    public Loan returnLoan(int loanId) throws SQLException {
        Loan loan = loanDao.findById(loanId);
        if (loan == null || loan.isReturned()) {
            return loan;
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        LocalDate due = loan.getDueDate();
        LocalDate today = LocalDate.now();

        // Calculate overdue fee if the book is returned after the due date
        if (today.isAfter(loan.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(due, today);
            loan.setOverdueFee(daysOverdue * 1.0);
        }

        loanDao.update(loan);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookDao.update(book);

        return loan;
    }

    /**
     * Find all overdue loans.
     *
     * @return a list of all overdue loans.
     * @throws SQLException if the query fails.
     */
    public List<Loan> overdueLoans() throws SQLException {
        LocalDate today = LocalDate.now();

        // Filter the loans to find those that are overdue
        return loanDao.findAll().stream()
                .filter(loan -> !loan.isReturned()) // Only consider loans that have not been returned
                .filter(loan -> loan.getDueDate().isBefore(today)) // Only consider loans that are overdue
                .toList();
    }
}
