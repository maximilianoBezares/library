/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * The base data access object: holds the CRUD code shared by all the DAOs.
 *
 * @param <T> The type of the entity.
 */
public abstract class BaseDao<T> {

    /**
     * The ORMLite DAO.
     */
    protected final Dao<T, Integer> dao;

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     * @param clazz            The class of the entity.
     * @throws RuntimeException if the DAO cannot be created.
     */
    protected BaseDao(ConnectionSource connectionSource, Class<T> clazz) {
        try {
            this.dao = DaoManager.createDao(connectionSource, clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds all the entities.
     *
     * @return The list of all the entities.
     * @throws RuntimeException if the query fails.
     */
    public List<T> findAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return The entity, or null if it does not exist.
     * @throws RuntimeException if the query fails.
     */
    public T findById(int id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an entity.
     *
     * @param entity The entity to create.
     * @throws RuntimeException if the insert fails.
     */
    public void create(T entity) {
        try {
            dao.create(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an entity.
     *
     * @param entity The entity to update.
     * @throws RuntimeException if the update fails.
     */
    public void update(T entity) {
        try {
            dao.update(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes an entity.
     *
     * @param entity The entity to delete.
     * @throws RuntimeException if the delete fails.
     */
    public void delete(T entity) {
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs a callable in one transaction. If the callable fails, the transaction rolls back.
     * A {@link RuntimeException} thrown by the callable is rethrown as it is, so the domain
     * error (for example a not found error) is not hidden by the transaction.
     *
     * @param callable The code to run in the transaction.
     * @param <R>      The type of the result.
     * @return The result of the callable.
     * @throws SQLException     if the transaction fails.
     * @throws RuntimeException the cause of the failure, if it is a runtime exception.
     */
    public <R> R transaction(Callable<R> callable) throws SQLException {
        try {
            return TransactionManager.callInTransaction(dao.getConnectionSource(), callable);
        } catch (SQLException e) {
            if (e.getCause() instanceof RuntimeException cause) {
                throw cause;
            }
            throw e;
        }
    }
}
