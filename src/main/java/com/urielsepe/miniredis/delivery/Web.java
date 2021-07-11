package com.urielsepe.miniredis.delivery;

import com.urielsepe.miniredis.delivery.command.*;
import com.urielsepe.miniredis.domain.action.*;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.SystemClock;
import com.urielsepe.miniredis.domain.model.Repository;
import io.vertx.core.*;
import io.vertx.core.http.HttpServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Web extends AbstractVerticle {
    private HttpServer server;
    private List<Command> availableCommands = new ArrayList<>();

    public Web(Repository repository) {
        DbSize dbSize = new DbSize(repository);
        DbSizeCommand dbSizeCmd = new DbSizeCommand(dbSize);
        availableCommands.add(dbSizeCmd);

        Del del = new Del(repository);
        DelCommand delCmd = new DelCommand(del);
        availableCommands.add(delCmd);

        Get get = new Get(repository);
        GetCommand getCmd = new GetCommand(get);
        availableCommands.add(getCmd);

        Incr incr = new Incr(repository);
        IncrCommand incrCmd = new IncrCommand(incr);
        availableCommands.add(incrCmd);

        Set set = new Set(repository);
        SetCommand setCmd = new SetCommand(set);
        availableCommands.add(setCmd);

        ZAdd zAdd = new ZAdd(repository);
        ZAddCommand zAddCmd = new ZAddCommand(zAdd);
        availableCommands.add(zAddCmd);

        ZCard zCard = new ZCard(repository);
        ZCardCommand zCardCmd = new ZCardCommand(zCard);
        availableCommands.add(zCardCmd);

        ZRange zRange = new ZRange(repository);
        ZRangeCommand zRangeCmd = new ZRangeCommand(zRange);
        availableCommands.add(zRangeCmd);

        ZRank zRank = new ZRank(repository);
        ZRankCommand zRankCmd = new ZRankCommand(zRank);
        availableCommands.add(zRankCmd);
    }

    @Override
    public void start() {
        server = vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader("content-type", "text/plain");

            String cmd = req.getParam("cmd");

            if (Objects.isNull(cmd) || cmd.isEmpty()) {
                req.response().end("cmd must be provided");
            } else {
                String[] arguments = cmd.split(" ");
                String command = arguments[0];

                for (Command availableCommand : availableCommands) {
                    if (availableCommand.shouldCall(command)) {
                        req.response().end(availableCommand.run(arguments));
                    }
                }
                req.response().end("unknown command '" + command + "'");
            }
        });

        server.listen(8080, res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
            }
        });
    }

    public static void main(String[] args) {
        InMemoryRepository repository = new InMemoryRepository(new SystemClock());
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new Web(repository));
    }
}
