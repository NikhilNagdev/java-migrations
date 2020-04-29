package commands;

import constants.Commands;
import database.migrations.MigrationCreator;
import database.migrations.Migrator;

public class CommandProcessor {

    public CommandProcessor(){
        migrationCreator = new MigrationCreator();
        migrator = new Migrator();
    }

    public void processCommand(String[] command){

        if(command[1].equals(Commands.CREATE_COMMAND) && command[2].equals(Commands.MIGRATION_COMMAND)){
            migrationCreator.createMigration(Commands.CREATE_COMMAND, command[3]);
        }else if(command[1].equals(Commands.MIGRATE_COMMAND)){
            migrator.runMigrations();
        }else{
            System.out.println("This command is not supported");
        }

    }

    private MigrationCreator migrationCreator = null;
    private Migrator migrator = null;
    //create migration users_table : to create a migration
}
