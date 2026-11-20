/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Member;

import java.util.List;

/**
 * The service of the members. It only manages the member lifecycle; the lending logic belongs to the loan service.
 */
public final class MemberService {

    /**
     * The member DAO.
     */
    private final MemberDao memberDao;

    /**
     * The Constructor.
     *
     * @param memberDao The member DAO.
     */
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    /**
     * Registers a member.
     *
     * @param member The member to register.
     * @return The registered member.
     */
    public Member register(Member member) {
        memberDao.create(member);
        return member;
    }

    /**
     * Finds all the members.
     *
     * @return The list of all the members.
     */
    public List<Member> findAll() {
        return memberDao.findAll();
    }

    /**
     * Find a member by ID.
     *
     * @param id the member ID
     * @return the member
     * @throws NotFoundException if the member does not exist
     */
    public Member findById(int id) {
        // ORMLite returns null when the row does not exist.
        Member member = memberDao.findById(id);
        if (member == null) {
            throw new NotFoundException("Member not found: " + id);
        }
        return member;
    }

    /**
     * Searches for members by a nonblank name fragment.
     *
     * @param query the name fragment, which may have surrounding whitespace
     * @return matching members ordered by ID
     * @throws IllegalArgumentException if the query is blank
     */
    public List<Member> search(String query) {
        String fragment = query.strip();
        if (fragment.isEmpty()) {
            throw new IllegalArgumentException("Search query must not be blank");
        }
        return memberDao.searchByName(fragment);
    }
}
