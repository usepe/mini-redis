package com.urielsepe.miniredis.delivery;

import com.urielsepe.miniredis.delivery.command.*;
import com.urielsepe.miniredis.domain.action.*;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.SystemClock;
import com.urielsepe.miniredis.domain.model.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    private final List<Command> availableCommands = new ArrayList<>();

    public Application(Repository repository) {
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

    public String processInput(String input) {
        String[] arguments = input.split(" ");
        String command = arguments[0].toLowerCase();

        for (Command availableCommand : availableCommands) {
            if (availableCommand.shouldCall(command)) {
                return availableCommand.run(arguments);
            }
        }

        return "unknown command '" + command + "'";
    }

    public static void main(String[] args) {
        InMemoryRepository repository = new InMemoryRepository(new SystemClock());
        Application app = new Application(repository);

        Scanner in = new Scanner(System.in);

        String input;

        while (!(input = in.nextLine()).equals("exit")) {
            try {
                if (input.isEmpty()) {
                    continue;
                }

                System.out.println(app.processInput(input));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
