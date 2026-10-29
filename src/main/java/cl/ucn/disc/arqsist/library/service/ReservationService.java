/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * The service of the reservations. Fulfilling a reservation lends the reserved book through the book service.
 */
public final class ReservationService {

    /**
     * The reservation DAO.
     */
    private final ReservationDao reservationDao;

    /**
     * The member DAO.
     */
    private final MemberDao memberDao;

    /**
     * The loan DAO.
     */
    private final LoanDao loanDao;

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
     * @param reservationDao The reservation DAO.
     * @param memberDao      The member DAO.
     * @param loanDao        The loan DAO.
     * @param bookService    The book service.
     * @param clock          The clock used to read today's date.
     */
    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, LoanDao loanDao,
                              BookService bookService, Clock clock) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.loanDao = loanDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Reserves a book for a member.
     *
     * @param bookId   The ID of the book.
     * @param memberId The ID of the member.
     * @return The created reservation.
     * @throws NotFoundException if the book or the member does not exist.
     */
    public Reservation reserve(int bookId, int memberId) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }

        Reservation reservation = new Reservation(member, book, LocalDate.now(clock));
        reservationDao.create(reservation);
        return reservation;
    }

    /**
     * Finds all the reservations.
     *
     * @return The list of all the reservations.
     */
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    /**
     * Fulfills a reservation and creates the loan of the reserved book. The inventory change,
     * the reservation update and the loan creation run in one transaction.
     *
     * @param reservationId The ID of the reservation.
     * @return The created loan.
     * @throws NotFoundException     if the reservation does not exist.
     * @throws IllegalStateException if the reservation is already fulfilled or the book has no available copies.
     * @throws SQLException          if the transaction fails.
     */
    public Loan fulfill(int reservationId) throws SQLException {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation == null) {
            throw new NotFoundException("Reservation not found: " + reservationId);
        }
        if (reservation.isFulfilled()) {
            throw new IllegalStateException("Reservation already fulfilled: " + reservationId);
        }

        LocalDate today = LocalDate.now(clock);
        Loan loan = new Loan(reservation.getMember(), reservation.getBook(), today, LoanPolicy.dueDate(today));

        return reservationDao.transaction(() -> {
            // The inventory guard runs first, so a book without copies fails before any other write
            bookService.borrow(reservation.getBook().getId());
            reservation.setFulfilled(true);
            reservationDao.update(reservation);
            loanDao.create(loan);
            return loan;
        });
    }
}
