package lab;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/numbers")
public class NumbersResource {

    @Inject
    PgPool client;

    @GET
    @Path("/integers")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Integer>> numbers() {
        List<Integer> resultNumbers = new ArrayList<>();

        return client.query("SELECT test_column FROM numbers_table LIMIT 100")
                .execute()
                .onItem().transform(rows -> {
                    for(var row: rows) {
                        resultNumbers.add(row.getInteger("test_column"));
                    }
                    return resultNumbers;
                });

    }
}
