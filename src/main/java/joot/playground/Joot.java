package joot.playground;

import javax.sql.DataSource;

/**
 *
 * @author witoldsz
 */
public interface Joot {

    void transaction(UnitOfWork u) throws Exception;
    <T> T transactionResult(UnitOfCall<T> u) throws Exception;

}
