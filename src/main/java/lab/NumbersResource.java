package lab;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Path("/numbers")
public class NumbersResource {

    @Inject
    PgPool client;


    @GET
    @Path("/integers")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Integer>> numbers() {
        Log.info("Getting numbers from the database");
        Random rand = new Random();
        int randomNum = rand.nextInt((10000 - 10) + 1) + 10;

       List<Integer> resultNumbers = new ArrayList<>();
        return client.query("SELECT count(test_column) as total FROM numbers_table limit " +randomNum)
                .execute()
                .onItem().transform(rows -> {
                    for(var row: rows) {
                        resultNumbers.add(row.getInteger("total"));
                    }
                    return resultNumbers;
                });

    }
}
