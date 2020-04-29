package commands;

import constants.Commands;
import database.migrations.MigrationCreator;

public class CommandProcessor {

    public CommandProcessor(){
        migrationCreator = new MigrationCreator();
    }
    public void processCommand(String[] command){

        if(command[1].equals(Commands.CREATE_COMMAND) && command[2].equals(Commands.MIGRATION_COMMAND)){
            migrationCreator.createMigration(Commands.CREATE_COMMAND, command[2]);
        }else{
            System.out.println("This command is not supported");
        }

    }

    private MigrationCreator migrationCreator = null;
    //create migration users_table : to create a migration
}
