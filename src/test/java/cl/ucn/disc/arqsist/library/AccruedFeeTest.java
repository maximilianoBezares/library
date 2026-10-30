/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.BookService;
import cl.ucn.disc.arqsist.library.service.LoanService;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Checks that the list of loans shows the accrued fee of the open overdue loans (Error #11).
 */
class AccruedFeeTest {

    /**
     * The open overdue loan has the accrued fee and the active loan has none.
     *
     * @throws Exception in case of any error.
     */
    @Test
    void findAllShowsTheAccruedFeeOfTheOverdueLoan() throws Exception {
        Database db = new Database("jdbc:sqlite::memory:");
        BookDao bookDao = new BookDao(db.connectionSource());
        MemberDao memberDao = new MemberDao(db.connectionSource());
        LoanDao loanDao = new LoanDao(db.connectionSource());

        // Today is 2026-01-15: the overdue loan is 24 days past its due date
        Clock clock = Clock.fixed(Instant.parse("2026-01-15T10:00:00Z"), ZoneOffset.UTC);
        LoanService loanService = new LoanService(loanDao, memberDao, new BookService(bookDao), clock);

        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 3);
        bookDao.create(book);
        Member member = new Member("Ada Lovelace", "ada@example.com");
        memberDao.create(member);

        Loan overdue = new Loan(member, book, LocalDate.parse("2025-12-01"), LocalDate.parse("2025-12-22"));
        loanDao.create(overdue);
        Loan active = new Loan(member, book, LocalDate.parse("2026-01-10"), LocalDate.parse("2026-01-31"));
        loanDao.create(active);

        List<Loan> loans = loanService.findAll();

        assertEquals(24.0, feeOf(loans, overdue.getId()), 0.0001);
        assertEquals(0.0, feeOf(loans, active.getId()), 0.0001);
    }

    /**
     * Gets the fee of a loan from a list.
     *
     * @param loans  The loans.
     * @param loanId The ID of the loan.
     * @return The overdue fee of the loan.
     */
    private static double feeOf(List<Loan> loans, int loanId) {
        return loans.stream()
                .filter(loan -> loan.getId() == loanId)
                .findFirst()
                .orElseThrow()
                .getOverdueFee();
    }
}
