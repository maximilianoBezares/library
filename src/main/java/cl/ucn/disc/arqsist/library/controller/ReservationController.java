/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.service.ReservationService;
import io.javalin.config.JavalinConfig;

import java.util.Objects;

/**
 * The HTTP controller of the reservations.
 */
public final class ReservationController {

    /**
     * The reservation service.
     */
    private final ReservationService service;

    /**
     * The Constructor.
     *
     * @param service The reservation service.
     */
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * Registers the routes of the reservations.
     *
     * @param config The Javalin configuration.
     */
    public void register(JavalinConfig config) {
        config.routes.post("/reservations", ctx -> {
            int memberId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("memberId")));
            int bookId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("bookId")));
            ctx.json(service.reserve(bookId, memberId));
        });
        config.routes.get("/reservations", ctx -> ctx.json(service.findAll()));
        config.routes.post("/reservations/{id}/fulfill", ctx -> ctx.json(service.fulfill(Integer.parseInt(ctx.pathParam("id")))));
    }
}
