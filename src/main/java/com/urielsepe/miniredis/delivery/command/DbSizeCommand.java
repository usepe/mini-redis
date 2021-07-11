package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.DbSize;

public class DbSizeCommand extends Command {
    private final DbSize dbSize;

    public DbSizeCommand(DbSize dbSize) {
        this.dbSize = dbSize;
        this.name = "dbsize";
    }

    @Override
    public String run(String[] arguments) {
        if (arguments.length != 1) {
            return "wrong number of arguments for 'dbsize' command";
        }

        return "(integer) " + dbSize.call();
    }
}
