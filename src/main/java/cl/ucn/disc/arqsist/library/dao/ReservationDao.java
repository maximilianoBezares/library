/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Reservation;
import com.j256.ormlite.support.ConnectionSource;

/**
 * The data access object of the {@link Reservation} entity.
 */
public final class ReservationDao extends BaseDao<Reservation> {

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     */
    public ReservationDao(ConnectionSource connectionSource) {
        super(connectionSource, Reservation.class);
    }
}
