package joot.playground;

import javax.sql.DataSource;

/**
 *
 * @author witoldsz
 */
public class JootFactory {

    private final DataSource rawDataSource;

    public JootFactory(DataSource rawDataSource) {
        this.rawDataSource = rawDataSource;
    }

    public JootFactory(String url, String username, String password) {
        //create faked datasource which uses DriverManager.createConnection instead of ds.getConnection();
        throw new UnsupportedOperationException("TODO: Not implemented yet.");
    }

    public DataSource jootDataSource() {
        //wrapped datasource, so you can controll connections
        throw new UnsupportedOperationException("TODO: Not implemented yet.");
    }

    public Joot joot() {
        //this will be the manager of transactions
        throw new UnsupportedOperationException("TODO: Not implemented yet.");
    }

}
