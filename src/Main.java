
import database.querybuilder.QueryBuilder;
import parser.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Parser p = new Parser("");
//        p.meth();

        QueryBuilder qb = new QueryBuilder();
        qb.generateTable();
    }
}
