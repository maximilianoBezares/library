/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Member;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * The data access object of the {@link Member} entity.
 */
public final class MemberDao extends BaseDao<Member> {

    /**
     * The Constructor.
     *
     * @param connectionSource The connection source.
     */
    public MemberDao(ConnectionSource connectionSource) {
        super(connectionSource, Member.class);
    }

    /**
     * Finds members whose names contain a literal fragment, ignoring ASCII case.
     *
     * @param query the name fragment
     * @return matching members ordered by ID
     * @throws RuntimeException if the query fails
     */
    public List<Member> searchByName(String query) {
        try {
            QueryBuilder<Member, Integer> builder = dao.queryBuilder();
            builder.where().raw("instr(lower(name), lower(?)) > 0", new SelectArg(SqlType.STRING, query));
            return builder.orderBy("id", true).query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
