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
    String ALTER = "alter";
    String CREATE = "create";
}
