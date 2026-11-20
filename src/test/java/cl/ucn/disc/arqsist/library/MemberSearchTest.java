/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.grpc.MemberClient;
import cl.ucn.disc.arqsist.library.grpc.MemberGrpcService;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.MemberService;
import com.j256.ormlite.support.ConnectionSource;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests member searches through the gRPC client and server.
 */
final class MemberSearchTest {

    /**
     * Checks matching, ordering, literal characters and blank queries over gRPC.
     *
     * @throws Exception if database or server setup or cleanup fails
     */
    @Test
    void searchesMembersByNameFragment() throws Exception {
        Database database = new Database("jdbc:sqlite::memory:");
        try (ConnectionSource connectionSource = database.connectionSource()) {
            MemberService memberService = new MemberService(new MemberDao(connectionSource));
            String serverName = InProcessServerBuilder.generateName();
            Server server = InProcessServerBuilder.forName(serverName).directExecutor()
                    .addService(new MemberGrpcService(memberService))
                    .build()
                    .start();
            try {
                ManagedChannel channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
                try (MemberClient client = new MemberClient(channel)) {
                    client.register(new Member("Ada Lovelace", "ada@example.com"));
                    client.register(new Member("Grace Hopper", "grace@example.com"));
                    client.register(new Member("Alan Turing", "alan@example.com"));

                    List<String> ada = List.of("Ada Lovelace");
                    assertAll(
                            () -> assertEquals(ada, names(client.search("ada"))),
                            () -> assertEquals(ada, names(client.search("ADA"))),
                            () -> assertEquals(ada, names(client.search(" ada "))),
                            () -> assertEquals(List.of("Ada Lovelace", "Grace Hopper", "Alan Turing"),
                                    names(client.search("a"))),
                            () -> assertTrue(client.search("zzz").isEmpty()),
                            () -> assertTrue(client.search("%").isEmpty()),
                            () -> assertTrue(client.search("_").isEmpty()),
                            () -> assertThrows(IllegalArgumentException.class, () -> client.search("")),
                            () -> assertThrows(IllegalArgumentException.class, () -> client.search(" "))
                    );

                    client.register(new Member("Percent %", "percent@example.com"));
                    client.register(new Member("Under_score", "underscore@example.com"));
                    assertEquals(List.of("Percent %"), names(client.search("%")));
                    assertEquals(List.of("Under_score"), names(client.search("_")));
                }
            } finally {
                server.shutdownNow();
            }
        }
    }

    /**
     * Extracts names in the order returned by the RPC.
     *
     * @param members the matching members
     * @return the names of the matching members
     */
    private static List<String> names(List<Member> members) {
        return members.stream().map(Member::getName).toList();
    }
}
