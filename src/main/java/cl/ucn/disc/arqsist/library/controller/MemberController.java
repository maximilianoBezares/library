/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.MemberService;
import io.javalin.config.JavalinConfig;

/**
 * The HTTP controller of the members.
 */
public final class MemberController {

    /**
     * The member service.
     */
    private final MemberService service;

    /**
     * The Constructor.
     *
     * @param service The member service.
     */
    public MemberController(MemberService service) {
        this.service = service;
    }

    /**
     * Registers the routes of the members.
     *
     * @param config The Javalin configuration.
     */
    public void register(JavalinConfig config) {
        config.routes.get("/members", ctx -> ctx.json(service.findAll()));
        config.routes.post("/members", ctx -> ctx.json(service.register(ctx.bodyAsClass(Member.class))));
    }
}
