package constants;

public interface Files {

    public static final String CREATE_TABLE_MIGRATION_STRUCTURE =
            "{" +
                    "\n\t\"table_name\": \"\"," +
                    "\n\t\"columns\":[]" + "\n" +
            "}";
    public static final String ALTER_TABLE_MIGRATION_STRUCTURE =
            "{" +
                    "\n\t\"table_name\": \"\"," +
                    "\n\t\"alter_columns\":[]" + "\n" +
                    "}";
    public static final String ALTER_DROP_TABLE_MIGRATION_STRUCTURE =
            "{" +
                    "\n\t\"table_name\": \"\"," +
                    "\n\t\"drop_columns\":[]" + "\n" +
                    "}";
    String ALTER_ADD = "alter_add";
    String ALTER_CHANGE = "alter_change";
    String ALTER_DROP = "alter_drop";
    String CREATE = "create";
}
