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

        // rawDs it's just an example, it can be anything like DB_URL+username+password,
        // or datasource from some connection pool library
        JootFactory jootFactory = new JootFactory(rawDs);
        Joot joot = jootFactory.joot();

        //now this is the managed datasource to be used with jOOT
        DataSource ds = jootFactory.jootDataSource();

        DSLContext jooq = DSL.using(ds, SQLDialect.H2);

        joot.transaction(() -> {

            // using JooQ
            jooq.insertInto(table("EXAMPLE"), field("ID"))
                    .values(1)
                    .execute();

            // using plain JDBC
            try (Connection c = ds.getConnection()) {
                c.createStatement().executeQuery("SELECT * FROM EXAMPLE");
            }

            // Using anything else? Hibernate? JPA?
            // No problem: use "ds" as a data source and jOOT will handle transactions
            // ...
        });

        // Convenient method to pull some result out of transaction scope;
        // just return anything, whatever object you want.
        Long id = joot.transactionResult(() -> {
            return jooq.select(field("ID"))
                    .from(table("EXAMPLE"))
                    .fetchOne(field("ID", Long.class));
        });
    }
}

```