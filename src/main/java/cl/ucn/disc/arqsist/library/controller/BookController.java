/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.service.BookService;
import io.javalin.config.JavalinConfig;

/**
 * The HTTP controller of the books.
 */
public final class BookController {

    /**
     * The book service.
     */
    private final BookService service;

    /**
     * The book DAO.
     */
    private final BookDao dao;

    /**
     * The Constructor.
     *
     * @param service The book service.
     * @param dao     The book DAO.
     */
    public BookController(BookService service, BookDao dao) {
        this.service = service;
        this.dao = dao;
    }

    /**
     * Registers the routes of the books.
     *
     * @param config The Javalin configuration.
     */
    public void register(JavalinConfig config) {
        config.routes.get("/books", ctx -> ctx.json(dao.findAll()));
        config.routes.get("/books/{id}", ctx -> ctx.json(service.findById(Integer.parseInt(ctx.pathParam("id")))));
        config.routes.post("/books", ctx -> ctx.json(service.create(ctx.bodyAsClass(Book.class))));
    }
}
