package commands;

import constants.Commands;
import database.migrations.MigrationCreator;
import database.migrations.Migrator;

public class CommandProcessor {

    public CommandProcessor(){
        migrationCreator = new MigrationCreator();
        migrator = new Migrator();
    }

    /**
     * This method is used to process the commands that were given.
     * @param command commands to be processed
     */
    public void processCommand(String[] command){

        //this is for create migration command if first command is create then next should be migration
        if(command[1].equals(Commands.CREATE_COMMAND) && command[2].equals(Commands.MIGRATION_COMMAND)){
            migrationCreator.createMigration(command[3]);//passing the name of the migration to be created
        }else if(command[1].equals(Commands.MIGRATE_COMMAND)){//This is used when there is migrate command
            migrator.runMigrations();
        }else{
            System.out.println("This command is not supported");
        }

    }

    private MigrationCreator migrationCreator = null;
    private Migrator migrator = null;
}
