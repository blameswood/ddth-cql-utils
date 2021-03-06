package com.github.ddth.cql.qnd;

import java.util.Iterator;
import java.util.UUID;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.github.ddth.cql.CqlUtils;
import com.github.ddth.cql.SessionManager;

/**
 * Statement that was prepared for a session can be used by another session from
 * the same cluster.
 * 
 * @author btnguyen
 */
public class QndPreparedStatementCrossSessions {

    public static void main(String[] args) throws Exception {
        try (SessionManager sm = new SessionManager()) {
            sm.init();

            Session session = sm.getSession("localhost", "demo", "demo", "demo", true);
            System.out.println("Session: " + session);

            final PreparedStatement pstm = CqlUtils.prepareStatement(session,
                    "SELECT * FROM tbldemo WHERE id=?");

            {
                ResultSet rs = CqlUtils.execute(session, pstm,
                        UUID.fromString("62c36092-82a1-3a00-93d1-46196ee77204"));
                Iterator<Row> it = rs.iterator();
                while (it.hasNext()) {
                    Row row = it.next();
                    System.out.println(row);
                }
            }

            session = sm.getSession("localhost", "demo", "demo", "demo", true);
            System.out.println(session);

            {
                ResultSet rs = CqlUtils.execute(session, pstm,
                        UUID.fromString("444c3a8a-25fd-431c-b73e-14ef8a9e22fc"));
                Iterator<Row> it = rs.iterator();
                while (it.hasNext()) {
                    Row row = it.next();
                    System.out.println(row);
                }
            }
        }
    }
}
