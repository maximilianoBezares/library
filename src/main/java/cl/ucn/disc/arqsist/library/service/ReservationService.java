/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * The service of the reservations.
 */
public final class ReservationService {

    /**
     * The reservation DAO.
     */
    private final ReservationDao reservationDao;

    /**
     * The book DAO.
     */
    private final BookDao bookDao;

    /**
     * The member DAO.
     */
    private final MemberDao memberDao;

    /**
     * The loan DAO.
     */
    private final LoanDao loanDao;

    /**
     * The Constructor.
     *
     * @param reservationDao The reservation DAO.
     * @param bookDao        The book DAO.
     * @param memberDao      The member DAO.
     * @param loanDao        The loan DAO.
     */
    public ReservationService(ReservationDao reservationDao, BookDao bookDao, MemberDao memberDao, LoanDao loanDao) {
        this.reservationDao = reservationDao;
        this.bookDao = bookDao;
        this.memberDao = memberDao;
        this.loanDao = loanDao;
    }

    /**
     * Reserves a book for a member.
     *
     * @param bookId   The ID of the book.
     * @param memberId The ID of the member.
     * @return The created reservation.
     * @throws SQLException if a query or the insert fails.
     */
    public Reservation reserve(int bookId, int memberId) throws SQLException {
        Book book = bookDao.findById(bookId);
        Member member = memberDao.findById(memberId);
        Reservation reservation = new Reservation(member, book, LocalDate.now());
        reservationDao.create(reservation);
        return reservation;
    }

    /**
     * Finds all the reservations.
     *
     * @return The list of all the reservations.
     * @throws SQLException if the query fails.
     */
    public List<Reservation> findAll() throws SQLException {
        return reservationDao.findAll();
    }

    /**
     * Fulfills a reservation and creates the loan of the reserved book.
     *
     * @param reservationId The ID of the reservation.
     * @return The created loan.
     * @throws IllegalStateException if the reservation does not exist or is already fulfilled.
     * @throws SQLException          if a query or a write fails.
     */
    public Loan fulfill(int reservationId) throws SQLException {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation == null || reservation.isFulfilled()) {
            throw new IllegalStateException("Reservation not available");
        }

        reservation.setFulfilled(true);
        reservationDao.update(reservation);

        LocalDate dueDate = LoanPolicy.dueDate(LocalDate.now());
        Loan loan = new Loan(reservation.getMember(), reservation.getBook(), LocalDate.now(), dueDate);
        loanDao.create(loan);
        return loan;
    }
}
