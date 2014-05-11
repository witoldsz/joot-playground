What is this?
-------------

jOOQ is the superb database library. However, as of now it lacks transaction management. It is not so bad, because there are many ways to handle transactions in Java world. One example is the EJB container. It has transactions built-in, so everything you need is to get the DataSource resource and use it with jOOQ, plain JDBC, QueryDSL or what-else.

Another option is to use Spring transactions.

Not everyone likes to use EJB or Spring container, though. Some developers want or need something ultra lightweight.

I don't know why, but instead of creating something ultra lightweight for transaction management, all the Java database libraries chose to implement transactions by adding extra methods here and there to the libraries itself. IMHO this is bad. Not only we have to dig through all the API in different libraries, but also we cannot use those libraries together across single transactions. Also, when we have separated transaction management, we have bunch of methods we are no allowed to use.

As I said previously, jOOQ chose to go that way as well, see: https://github.com/jOOQ/jOOQ/issues/3229
This project is supposed to support me fighting the stereotypes that creating a transaction manager is an option to be seriously considered. With this, I can show that the simple transaction manager does not mean:
- using aspects and annotations,
- introducing complicated libraries,
- what else...

**I say otherwise.** Yes, the transaction manager as a standalone library can be:
- simple,
- lightweight,
- does not mean using complicated things, aspects, annotations (but can be used to build such a framework).

jOOT does not exist
-------------------

Well, that's true. It does not exist... yet. I Hope this stub and example will make it happen.

jOOT concept does not reference jOOQ in any way. You can use jOOQ without jOOT and you should be able to use jOOT with no jOOQ.

Example use:
------------

See how simple the API can be. Notice: it is standalone library, not tied to jOOQ or any of the alternatives.

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