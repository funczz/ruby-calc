package com.github.funczz.ruby_calc.data.db

import org.apache.commons.dbcp.BasicDataSource
import java.io.ByteArrayInputStream
import java.sql.Connection
import java.util.Collections
import java.util.Properties
import java.util.StringTokenizer
import javax.sql.DataSource

@Suppress("Unused", "MemberVisibilityCanBePrivate")
object DataSourceFactory {

    fun newDataSource(
        propertiesPath: String,
        classLoader: ClassLoader = Thread.currentThread().contextClassLoader!!,
        function: (Properties) -> Unit = {}
    ): DataSource {
        val inputStream = classLoader.getResourceAsStream(propertiesPath)
        val properties = Properties().also { it.load(inputStream) }
        function(properties)
        return newDataSource(properties = properties)
    }

    fun newDataSource(properties: Properties): DataSource {
        return BasicDataSourceFactory.createDataSource(properties)
    }

    /**
     * org.apache.commons.dbcp.BasicDataSourceFactoryをandroid用に移植
     */
    object BasicDataSourceFactory {

        /**
         * アクセス制限を回避する為に値を移植
         * PoolableConnectionFactory.UNKNOWN_TRANSACTIONISOLATION
         */
        private const val UNKNOWN_TRANSACTIONISOLATION = -1

        /**
         * 以下はorg.apache.commons.dbcp.BasicDataSourceFactoryから移植
         */

        private const val PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit"
        private const val PROP_DEFAULTREADONLY = "defaultReadOnly"
        private const val PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation"
        private const val PROP_DEFAULTCATALOG = "defaultCatalog"
        private const val PROP_DRIVERCLASSNAME = "driverClassName"
        private const val PROP_MAXACTIVE = "maxActive"
        private const val PROP_MAXIDLE = "maxIdle"
        private const val PROP_MINIDLE = "minIdle"
        private const val PROP_INITIALSIZE = "initialSize"
        private const val PROP_MAXWAIT = "maxWait"
        private const val PROP_TESTONBORROW = "testOnBorrow"
        private const val PROP_TESTONRETURN = "testOnReturn"
        private const val PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis"
        private const val PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun"
        private const val PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis"
        private const val PROP_TESTWHILEIDLE = "testWhileIdle"
        private const val PROP_PASSWORD = "password"
        private const val PROP_URL = "url"
        private const val PROP_USERNAME = "username"
        private const val PROP_VALIDATIONQUERY = "validationQuery"
        private const val PROP_VALIDATIONQUERY_TIMEOUT = "validationQueryTimeout"

        /**
         * The property name for initConnectionSqls.
         * The associated value String must be of the form [query;]*
         * @since 1.3
         */
        private const val PROP_INITCONNECTIONSQLS = "initConnectionSqls"
        private const val PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED =
            "accessToUnderlyingConnectionAllowed"
        private const val PROP_REMOVEABANDONED = "removeAbandoned"
        private const val PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout"
        private const val PROP_LOGABANDONED = "logAbandoned"
        private const val PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements"
        private const val PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements"
        private const val PROP_CONNECTIONPROPERTIES = "connectionProperties"

        /**
         * Creates and configures a [BasicDataSource] instance based on the
         * given properties.
         *
         * @param properties the datasource configuration properties
         * @throws Exception if an error occurs creating the data source
         */
        @Throws(Exception::class)
        fun createDataSource(properties: Properties): DataSource {
            val dataSource = BasicDataSource()
            var value: String? = properties.getProperty(PROP_DEFAULTAUTOCOMMIT)
            if (value != null) {
                dataSource.setDefaultAutoCommit(value.toBoolean())
            }
            value = properties.getProperty(PROP_DEFAULTREADONLY)
            if (value != null) {
                dataSource.setDefaultReadOnly(value.toBoolean())
            }
            value = properties.getProperty(PROP_DEFAULTTRANSACTIONISOLATION)
            if (value != null) {
                val level: Int = if ("NONE".equals(value, ignoreCase = true)) {
                    Connection.TRANSACTION_NONE
                } else if ("READ_COMMITTED".equals(value, ignoreCase = true)) {
                    Connection.TRANSACTION_READ_COMMITTED
                } else if ("READ_UNCOMMITTED".equals(value, ignoreCase = true)) {
                    Connection.TRANSACTION_READ_UNCOMMITTED
                } else if ("REPEATABLE_READ".equals(value, ignoreCase = true)) {
                    Connection.TRANSACTION_REPEATABLE_READ
                } else if ("SERIALIZABLE".equals(value, ignoreCase = true)) {
                    Connection.TRANSACTION_SERIALIZABLE
                } else {
                    try {
                        value.toInt()
                    } catch (e: NumberFormatException) {
                        System.err.println("Could not parse defaultTransactionIsolation: $value")
                        System.err.println("WARNING: defaultTransactionIsolation not set")
                        System.err.println("using default value of database driver")
                        UNKNOWN_TRANSACTIONISOLATION
                    }
                }
                dataSource.setDefaultTransactionIsolation(level)
            }
            value = properties.getProperty(PROP_DEFAULTCATALOG)
            if (value != null) {
                dataSource.setDefaultCatalog(value)
            }
            value = properties.getProperty(PROP_DRIVERCLASSNAME)
            if (value != null) {
                dataSource.setDriverClassName(value)
            }
            value = properties.getProperty(PROP_MAXACTIVE)
            if (value != null) {
                dataSource.setMaxActive(value.toInt())
            }
            value = properties.getProperty(PROP_MAXIDLE)
            if (value != null) {
                dataSource.setMaxIdle(value.toInt())
            }
            value = properties.getProperty(PROP_MINIDLE)
            if (value != null) {
                dataSource.setMinIdle(value.toInt())
            }
            value = properties.getProperty(PROP_INITIALSIZE)
            if (value != null) {
                dataSource.setInitialSize(value.toInt())
            }
            value = properties.getProperty(PROP_MAXWAIT)
            if (value != null) {
                dataSource.setMaxWait(value.toLong())
            }
            value = properties.getProperty(PROP_TESTONBORROW)
            if (value != null) {
                dataSource.setTestOnBorrow(value.toBoolean())
            }
            value = properties.getProperty(PROP_TESTONRETURN)
            if (value != null) {
                dataSource.setTestOnReturn(value.toBoolean())
            }
            value = properties.getProperty(PROP_TIMEBETWEENEVICTIONRUNSMILLIS)
            if (value != null) {
                dataSource.setTimeBetweenEvictionRunsMillis(value.toLong())
            }
            value = properties.getProperty(PROP_NUMTESTSPEREVICTIONRUN)
            if (value != null) {
                dataSource.setNumTestsPerEvictionRun(value.toInt())
            }
            value = properties.getProperty(PROP_MINEVICTABLEIDLETIMEMILLIS)
            if (value != null) {
                dataSource.setMinEvictableIdleTimeMillis(value.toLong())
            }
            value = properties.getProperty(PROP_TESTWHILEIDLE)
            if (value != null) {
                dataSource.setTestWhileIdle(value.toBoolean())
            }
            value = properties.getProperty(PROP_PASSWORD)
            if (value != null) {
                dataSource.setPassword(value)
            }
            value = properties.getProperty(PROP_URL)
            if (value != null) {
                dataSource.setUrl(value)
            }
            value = properties.getProperty(PROP_USERNAME)
            if (value != null) {
                dataSource.setUsername(value)
            }
            value = properties.getProperty(PROP_VALIDATIONQUERY)
            if (value != null) {
                dataSource.setValidationQuery(value)
            }
            value = properties.getProperty(PROP_VALIDATIONQUERY_TIMEOUT)
            if (value != null) {
                dataSource.setValidationQueryTimeout(value.toInt())
            }
            value = properties.getProperty(PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED)
            if (value != null) {
                dataSource.setAccessToUnderlyingConnectionAllowed(value.toBoolean())
            }
            value = properties.getProperty(PROP_REMOVEABANDONED)
            if (value != null) {
                dataSource.setRemoveAbandoned(value.toBoolean())
            }
            value = properties.getProperty(PROP_REMOVEABANDONEDTIMEOUT)
            if (value != null) {
                dataSource.setRemoveAbandonedTimeout(value.toInt())
            }
            value = properties.getProperty(PROP_LOGABANDONED)
            if (value != null) {
                dataSource.setLogAbandoned(value.toBoolean())
            }
            value = properties.getProperty(PROP_POOLPREPAREDSTATEMENTS)
            if (value != null) {
                dataSource.setPoolPreparedStatements(value.toBoolean())
            }
            value = properties.getProperty(PROP_MAXOPENPREPAREDSTATEMENTS)
            if (value != null) {
                dataSource.setMaxOpenPreparedStatements(value.toInt())
            }
            value = properties.getProperty(PROP_INITCONNECTIONSQLS)
            if (value != null) {
                val tokenizer = StringTokenizer(value, ";")
                dataSource.setConnectionInitSqls(Collections.list(tokenizer))
            }
            value = properties.getProperty(PROP_CONNECTIONPROPERTIES)
            if (value != null) {
                val p = getProperties(value)
                val e = p.propertyNames()
                while (e.hasMoreElements()) {
                    val propertyName = e.nextElement() as String
                    dataSource.addConnectionProperty(propertyName, p.getProperty(propertyName))
                }
            }

            // DBCP-215
            // Trick to make sure that initialSize connections are created
            if (dataSource.initialSize > 0) {
                dataSource.logWriter
            }

            // Return the configured DataSource instance
            return dataSource
        }

        /**
         *
         * Parse properties from the string. Format of the string must be [propertyName=property;]*
         *
         *
         * @param propText
         * @return Properties
         * @throws Exception
         */
        @Throws(Exception::class)
        private fun getProperties(propText: String?): Properties {
            val p = Properties()
            if (propText != null) {
                p.load(ByteArrayInputStream(propText.replace(';', '\n').toByteArray()))
            }
            return p
        }

    }
}
