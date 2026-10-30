/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * The service of the loans. It owns the lending logic: checkout, return and the overdue fees.
 */
public final class LoanService {

    /**
     * The loan DAO.
     */
    private final LoanDao loanDao;

    /**
     * The member DAO.
     */
    private final MemberDao memberDao;

    /**
     * The book service, the only owner of the inventory.
     */
    private final BookService bookService;

    /**
     * The clock used to read today's date.
     */
    private final Clock clock;

    /**
     * The Constructor.
     *
     * @param loanDao The loan DAO.
     * @param memberDao The member DAO.
     * @param bookService The book service.
     * @param clock The clock used to read today's date.
     */
    public LoanService(LoanDao loanDao, MemberDao memberDao, BookService bookService, Clock clock) {
        this.loanDao = loanDao;
        this.memberDao = memberDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Finds all the loans. The open overdue loans carry their accrued fee, which is not persisted.
     *
     * @return The list of all the loans.
     */
    public List<Loan> findAll() {
        return withAccruedFees(loanDao.findAll());
    }

    /**
     * Lends a book to a member. The inventory change and the loan creation run in one transaction.
     *
     * @param memberId The ID of the member.
     * @param bookId   The ID of the book.
     * @return The created loan.
     * @throws NotFoundException if the member or the book does not exist.
     * @throws IllegalStateException if the book has no available copies.
     * @throws SQLException if the transaction fails.
     */
    public Loan checkout(int memberId, int bookId) throws SQLException {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }

        LocalDate today = LocalDate.now(clock);
        Loan loan = new Loan(member, book, today, LoanPolicy.dueDate(today));

        return loanDao.transaction(() -> {
            bookService.borrow(bookId);
            loanDao.create(loan);
            return loan;
        });
    }

    /**
     * Returns a loan: marks it as returned, sets the overdue fee if it is late
     * and puts the copy back into the inventory. The loan update and the inventory change
     * run in one transaction.
     *
     * @param loanId The ID of the loan.
     * @return The returned loan.
     * @throws NotFoundException if the loan does not exist.
     * @throws IllegalStateException if the loan is already returned.
     * @throws SQLException if the transaction fails.
     */
    public Loan returnLoan(int loanId) throws SQLException {
        Loan loan = loanDao.findById(loanId);
        if (loan == null) {
            throw new NotFoundException("Loan not found: " + loanId);
        }
        if (loan.isReturned()) {
            throw new IllegalStateException("Loan already returned: " + loanId);
        }

        LocalDate today = LocalDate.now(clock);
        loan.setReturned(true);
        loan.setReturnDate(today);

        // Calculate overdue fee if the book is returned after the due date
        if (today.isAfter(loan.getDueDate())) {
            loan.setOverdueFee(accruedFee(loan, today));
        }

        return loanDao.transaction(() -> {
            loanDao.update(loan);
            bookService.returnCopy(loan.getBook().getId());
            return loan;
        });
    }

    /**
     * Find all overdue loans. Each loan carries its accrued fee, which is not persisted.
     *
     * @return a list of all overdue loans.
     */
    public List<Loan> overdueLoans() {
        LocalDate today = LocalDate.now(clock);

        // Filter the loans to find those that are overdue
        return withAccruedFees(loanDao.findAll().stream()
                .filter(loan -> !loan.isReturned()) // Only consider loans that have not been returned
                .filter(loan -> loan.getDueDate().isBefore(today)) // Only consider loans that are overdue
                .toList());
    }

    /**
     * Sets the accrued fee on every open overdue loan. The fee is not persisted.
     *
     * @param loans The loans.
     * @return The same loans, with the accrued fee set on the open overdue ones.
     */
    private List<Loan> withAccruedFees(List<Loan> loans) {
        LocalDate today = LocalDate.now(clock);
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getDueDate().isBefore(today)) {
                loan.setOverdueFee(accruedFee(loan, today));
            }
        }
        return loans;
    }

    /**
     * Computes the fee of a loan: the days past the due date times the daily fee.
     *
     * @param loan  The loan.
     * @param today Today's date.
     * @return The accrued fee.
     */
    private static double accruedFee(Loan loan, LocalDate today) {
        return ChronoUnit.DAYS.between(loan.getDueDate(), today) * LoanPolicy.FEE_PER_DAY;
    }
}
