package com.pahanaedu.util;

import java.sql.Connection;

public class TransactionUtil {
    public interface TransactionCallback<T> {
        T doInTransaction(Connection conn) throws Exception;
    }

    public static <T> T execute(TransactionCallback<T> callback) throws Exception {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                T result = callback.doInTransaction(conn);
                conn.commit();
                return result;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}

