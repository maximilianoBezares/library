/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Reservation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * The data access object of the {@link Reservation} entity.
 */
public final class ReservationDao {

    /**
     * The ORMLite DAO.
     */
    private final Dao<Reservation, Integer> dao;

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     * @throws SQLException if the DAO cannot be created.
     */
    public ReservationDao(ConnectionSource connectionSource) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, Reservation.class);
    }

    /**
     * Finds all the reservations.
     *
     * @return The list of all the reservations.
     * @throws SQLException if the query fails.
     */
    public List<Reservation> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a reservation by its ID.
     *
     * @param id The ID of the reservation.
     * @return The reservation, or null if it does not exist.
     * @throws SQLException if the query fails.
     */
    public Reservation findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Creates a reservation.
     *
     * @param reservation The reservation to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Reservation reservation) throws SQLException {
        dao.create(reservation);
    }

    /**
     * Updates a reservation.
     *
     * @param reservation The reservation to update.
     * @throws SQLException if the update fails.
     */
    public void update(Reservation reservation) throws SQLException {
        dao.update(reservation);
    }
}
