/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Loan;
import com.j256.ormlite.support.ConnectionSource;

/**
 * The data access object of the {@link Loan} entity.
 */
public final class LoanDao extends BaseDao<Loan> {

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     */
    public LoanDao(ConnectionSource connectionSource) {
        super(connectionSource, Loan.class);
    }
}
