package com.github.funczz.ruby_calc.data.db

import org.apache.commons.dbcp.BasicDataSource
import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.Connection
import java.util.Properties

@Suppress("NonAsciiCharacters")
class DataSourceFactoryTest {

    @Test
    fun `DBCP の初期値`() {
        val properties = Properties()
        properties.setProperty("url", "jdbc:hsqldb:mem:test_db;shutdown=true")
        properties.setProperty("driverClassName", "org.hsqldb.jdbc.JDBCDriver")
        properties.setProperty("username", "sa")
        properties.setProperty("password", "sa")
        properties.setProperty("validationQuery", "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS")
        val ds = DataSourceFactory.newDataSource(properties = properties) as BasicDataSource
        assertEquals(true, ds.defaultAutoCommit)
        assertEquals(-1, ds.defaultTransactionIsolation)
        assertEquals(null, ds.defaultCatalog)
        assertEquals(8, ds.maxActive)
        assertEquals(8, ds.maxIdle)
        assertEquals(0, ds.minIdle)
        assertEquals(0, ds.initialSize)
        assertEquals(-1, ds.maxWait)
        assertEquals(-1, ds.validationQueryTimeout)
        //
        assertEquals(true, ds.connection.autoCommit)
        assertEquals(Connection.TRANSACTION_READ_COMMITTED, ds.connection.transactionIsolation)
    }

    @Test
    fun `properties ファイルを読み込む`() {
        val ds = DataSourceFactory.newDataSource(propertiesPath = "db/dbcp.properties") {
            it.setProperty("url", "jdbc:hsqldb:mem:test_db;shutdown=true")
        } as BasicDataSource
        assertEquals(false, ds.defaultAutoCommit)
        assertEquals(8, ds.defaultTransactionIsolation)
        assertEquals(null, ds.defaultCatalog)
        assertEquals(5, ds.maxActive)
        assertEquals(3, ds.maxIdle)
        assertEquals(1, ds.minIdle)
        assertEquals(1, ds.initialSize)
        assertEquals(5000, ds.maxWait)
        assertEquals(5000, ds.validationQueryTimeout)
        //
        assertEquals(false, ds.connection.autoCommit)
        assertEquals(Connection.TRANSACTION_SERIALIZABLE, ds.connection.transactionIsolation)
    }
}
