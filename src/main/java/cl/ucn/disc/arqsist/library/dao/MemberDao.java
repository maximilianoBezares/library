/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Member;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * The data access object of the {@link Member} entity.
 */
public final class MemberDao {

    /**
     * The ORMLite DAO.
     */
    private final Dao<Member, Integer> dao;

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     * @throws SQLException if the DAO cannot be created.
     */
    public MemberDao(ConnectionSource connectionSource) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, Member.class);
    }

    /**
     * Finds all the members.
     *
     * @return The list of all the members.
     * @throws SQLException if the query fails.
     */
    public List<Member> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a member by its ID.
     *
     * @param id The ID of the member.
     * @return The member, or null if it does not exist.
     * @throws SQLException if the query fails.
     */
    public Member findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Creates a member.
     *
     * @param member The member to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Member member) throws SQLException {
        dao.create(member);
    }

    /**
     * Updates a member.
     *
     * @param member The member to update.
     * @throws SQLException if the update fails.
     */
    public void update(Member member) throws SQLException {
        dao.update(member);
    }
}
