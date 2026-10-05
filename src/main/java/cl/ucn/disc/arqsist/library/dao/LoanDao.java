/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Loan;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * The data access object of the {@link Loan} entity.
 */
public final class LoanDao {

    /**
     * The ORMLite DAO.
     */
    private final Dao<Loan, Integer> dao;

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     * @throws SQLException if the DAO cannot be created.
     */
    public LoanDao(ConnectionSource connectionSource) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, Loan.class);
    }

    /**
     * Finds all the loans.
     *
     * @return The list of all the loans.
     * @throws SQLException if the query fails.
     */
    public List<Loan> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a loan by its ID.
     *
     * @param id The ID of the loan.
     * @return The loan, or null if it does not exist.
     * @throws SQLException if the query fails.
     */
    public Loan findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Creates a loan.
     *
     * @param loan The loan to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Loan loan) throws SQLException {
        dao.create(loan);
    }

    /**
     * Updates a loan.
     *
     * @param loan The loan to update.
     * @throws SQLException if the update fails.
     */
    public void update(Loan loan) throws SQLException {
        dao.update(loan);
    }
}
