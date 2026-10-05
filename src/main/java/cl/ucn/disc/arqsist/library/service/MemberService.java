/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * The service of the members.
 */
public final class MemberService {

    /**
     * The member DAO.
     */
    private final MemberDao memberDao;

    /**
     * The book DAO.
     */
    private final BookDao bookDao;

    /**
     * The loan DAO.
     */
    private final LoanDao loanDao;

    /**
     * The Constructor.
     *
     * @param memberDao The member DAO.
     * @param bookDao   The book DAO.
     * @param loanDao   The loan DAO.
     */
    public MemberService(MemberDao memberDao, BookDao bookDao, LoanDao loanDao) {
        this.memberDao = memberDao;
        this.bookDao = bookDao;
        this.loanDao = loanDao;
    }

    /**
     * Registers a member.
     *
     * @param member The member to register.
     * @return The registered member.
     * @throws SQLException if the insert fails.
     */
    public Member register(Member member) throws SQLException {
        memberDao.create(member);
        return member;
    }

    /**
     * Finds all the members.
     *
     * @return The list of all the members.
     * @throws SQLException if the query fails.
     */
    public List<Member> findAll() throws SQLException {
        return memberDao.findAll();
    }

    /**
     * Lends a book to a member.
     *
     * @param memberId The ID of the member.
     * @param bookId   The ID of the book.
     * @return The created loan.
     * @throws SQLException if a query or a write fails.
     */
    public Loan checkout(int memberId, int bookId) throws SQLException {
        Member member = memberDao.findById(memberId);
        Book book = bookDao.findById(bookId);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDao.update(book);

        // Today date:
        LocalDate today = LocalDate.now();
        // Due date:
        LocalDate dueDate = LoanPolicy.computeDueDate(today);

        Loan loan = new Loan(member, book, today, dueDate);
        loanDao.create(loan);
        return loan;
    }
}
