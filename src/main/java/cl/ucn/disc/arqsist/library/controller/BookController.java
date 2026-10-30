/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.service.BookService;
import io.javalin.config.JavalinConfig;

/**
 * The HTTP controller of the books. It only talks to the book service.
 */
public final class BookController {

    /**
     * The book service.
     */
    private final BookService service;

    /**
     * The Constructor.
     *
     * @param service The book service.
     */
    public BookController(BookService service) {
        this.service = service;
    }

    /**
     * Registers the routes of the books.
     *
     * @param config The Javalin configuration.
     */
    public void register(JavalinConfig config) {
        config.routes.get("/books", ctx -> ctx.json(service.listAll()));
        config.routes.get("/books/{id}", ctx -> ctx.json(service.findById(Integer.parseInt(ctx.pathParam("id")))));
        config.routes.post("/books", ctx -> ctx.json(service.create(ctx.bodyAsClass(Book.class))));
    }
}
