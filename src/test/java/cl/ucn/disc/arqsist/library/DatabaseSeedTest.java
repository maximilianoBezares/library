/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks the seed data and that seeding twice does not duplicate it (Errors #7 and #8).
 */
class DatabaseSeedTest {

    /**
     * The seed creates consistent data and a second seed changes nothing.
     *
     * @throws Exception in case of any error.
     */
    @Test
    void seedIsConsistentAndIdempotent() throws Exception {
        Database db = new Database("jdbc:sqlite::memory:");
        BookDao bookDao = new BookDao(db.connectionSource());
        LoanDao loanDao = new LoanDao(db.connectionSource());
        ReservationDao reservationDao = new ReservationDao(db.connectionSource());

        db.seedIfEmpty();

        List<Loan> loans = loanDao.findAll();
        assertFalse(loans.isEmpty(), "the seed should create loans");
        assertFalse(reservationDao.findAll().isEmpty(), "the seed should create reservations");

        LocalDate today = LocalDate.now();
        assertTrue(loans.stream().anyMatch(loan -> !loan.isReturned() && loan.getDueDate().isBefore(today)),
                "the seed should create an overdue loan");

        for (Loan loan : loans) {
            if (!loan.isReturned()) {
                Book book = bookDao.findById(loan.getBook().getId());
                assertTrue(book.getAvailableCopies() < book.getTotalCopies(),
                        "a book with an open loan should have fewer available copies: " + book.getTitle());
            }
        }

        int loanCount = loans.size();
        int reservationCount = reservationDao.findAll().size();

        db.seedIfEmpty();

        assertEquals(loanCount, loanDao.findAll().size(), "a second seed should not create loans");
        assertEquals(reservationCount, reservationDao.findAll().size(), "a second seed should not create reservations");
    }
}
