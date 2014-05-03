Example use:
------------

```java
package joot.playground.example;

import java.sql.Connection;
import javax.sql.DataSource;
import joot.playground.Joot;
import joot.playground.JootFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 *
 * @author witoldsz
 */
public class Main {

    public static void main(String... args) throws Exception {
        JdbcDataSource rawDs = new JdbcDataSource();
        rawDs.setURL("jdbc:h2:mem:db1");

        JootFactory jootFactory = new JootFactory(rawDs);
        Joot joot = jootFactory.joot();
        DataSource ds = jootFactory.jootDataSource();

        DSLContext ctx = DSL.using(ds, SQLDialect.H2);

        joot.transaction(() -> {

            //using JooQ
            ctx.insertInto(table("EXAMPLE"), field("ID"))
                    .values(1)
                    .execute();

            //using plain JDBC
            try (Connection c = ds.getConnection()) {
                c.createStatement().executeQuery("SELECT * FROM EXAMPLE");
            }

            //using anything else? no problem: use "ds" or objects which use it internally, like JooQ
            //...
        });

        Long id = joot.transactionResult(() -> {
            return ctx.select(field("ID"))
                    .from(table("EXAMPLE"))
                    .fetchOne(field("ID", Long.class));
        });
    }
}

```