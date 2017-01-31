package com.gmail.at.ivanehreshi.epam.touragency.persistence.transaction;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import org.apache.logging.log4j.*;

import java.sql.*;

@FunctionalInterface
public interface Transaction {

    static void tx(ConnectionManager cm, Transaction transaction, int transactionIsolationLevel) {
        Connection conn = cm.getConnection();

        boolean autoCommit;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(transactionIsolationLevel);
            transaction.span();
            conn.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogManager.getLogger().error("Cannot rollback transaction");
            }

            throw new RuntimeSqlException(e);
        } catch (Exception e) {
            LogManager.getLogger().error("Operations failed");
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                LogManager.getLogger().error("Failed to close connection");
            }
        }
    }

    static void tx(ConnectionManager cm, Transaction transaction) {
        tx(cm, transaction, Connection.TRANSACTION_READ_COMMITTED);
    }

    void span();
}
