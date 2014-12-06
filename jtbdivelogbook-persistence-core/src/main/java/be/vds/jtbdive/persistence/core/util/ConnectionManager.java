/*
* Jt'B Dive Logbook - Electronic dive logbook.
* 
* Copyright (C) 2010  Gautier Vanderslyen
* 
* Jt'B Dive Logbook is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package be.vds.jtbdive.persistence.core.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import be.vds.jtbdive.core.exceptions.DataStoreException;

/**
 * ConnectionManager - encapsulation de la gestion des connexions JDBC, en
 * particulier pour l'initialisation d'une connexion par Driver ou DataSource.
 * <p>
 * Usage: - cr�er et initialiser un ConnectionManager:<br>
 * ConnectionManager cm = new ConnectionManager(dataSourceName);<br>
 * cm.setTransactionMode(TRAN_JDBC);<br>
 * <p>
 * - obtenir une connexion: Connection con = cm.getConnection();
 * <p>
 * - commit / rollback: cm.commit(con); cm.rollback(con, "why it went wrong");
 * <p>
 * - fermer la connexion: cm.close(con);
 * 
 * @author clm / Gautier Vanderslyen
 * 
 */
public class ConnectionManager implements Serializable {
	private static final long serialVersionUID = 127371782934350410L;

	private static final Logger logger = Logger
			.getLogger(ConnectionManager.class);

	public static final String PROPERTY_CLASS_NAME = "connection.driver_class";
	public static final String PROPERTY_URL = "connection.url";
	public static final String PROPERTY_HOST = "connection.host";
	public static final String PROPERTY_PORT = "connection.port";
	public static final String PROPERTY_PARAMETERS = "connection.parameters";
	public static final String PROPERTY_DATABASE = "connection.database";
	public static final String PROPERTY_USERNAME = "connection.username";
	public static final String PROPERTY_PASSWORD = "connection.password";

	/**
	 * Nom de la DataSource (en mode DATASOURCE)
	 */
	private String _dataSourceName;
	/**
	 * Param�tres de cr�ation de la connexion (en mode DRIVER)
	 */
	private String _jdbcDriver;
	private String _jdbcUrl;
	private String _jdbcUser;
	private char[] _jdbcPwd;
	/**
	 * Mode de gestion des transactions (JDBC ou JTA)
	 */
	public final static int TRAN_UNDEFINED = 0;
	private final static int TRAN_JDBC_DATASOURCE = 1;
	private final static int TRAN_JDBC_DRIVER = 2;
	private int transactionMode = TRAN_UNDEFINED;
	/**
	 * DataSource (en mode DATASOURCE)
	 */
	private transient DataSource _ds;

	private Properties connectionProperties;
//	/**
//	 * Propri�t�s de connexion JDBC par rapport � un serveur d'applications
//	 */
//	private static Properties _props;

	public ConnectionManager() {
	}

	/**
	 * Using this constructor means you want to use a {@link DataSource} with
	 * the given name and that you'll retrieve in a JNDI on localhost.
	 * <p>
	 * example of data source name:<br>
	 * DATASOURCE_NAME = "java:comp/env/jdbc/admin";
	 * 
	 * @param dsName
	 *            the name of the DataSource in the jndi
	 * @author Gautier Vanderslyen
	 */
	public ConnectionManager(String dsName) {
		this.initialize(dsName);
	}

	/**
	 * Using this constructor means you want to use the given {@link DataSource}
	 * 
	 * @param dataSource
	 *            the DataSource to use
	 * @author Gautier Vanderslyen
	 */
	public ConnectionManager(DataSource dataSource) {
		this.initialize(dataSource);
	}

	/**
	 * Using this constructor means you want to use the given {@link DataSource}
	 * to get connection with the given username and password
	 * 
	 * @param dataSource
	 *            the DataSource to use
	 * @param user
	 *            the user name to get the connection
	 * @param password
	 *            the password to get the connection
	 * @author Gautier Vanderslyen
	 */
	public ConnectionManager(DataSource ds, String userName, char[] password) {
		this.initialize(ds, userName, password);
	}

	/**
	 * Using this constructor means you want to use a {@link DataSource} with
	 * the given name and that you'll retrieve in a JNDI on localhost, but using
	 * a username and a password to get the connections
	 * <p>
	 * example of data source name:<br>
	 * DATASOURCE_NAME = "java:comp/env/jdbc/admin";
	 * 
	 * @param dsName
	 *            the name of the DataSource in the jndi
	 * @param user
	 *            the user name to get the connection
	 * @param password
	 *            the password to get the connection
	 * @author Gautier Vanderslyen
	 */
	public ConnectionManager(String dsName, String user, char[] pwd) {
		this.initialize(dsName, user, pwd);
	}

	/**
	 * Using this constructor means you want to get connection using the
	 * basicest way in JDBC to get connection using the driver, the url, the
	 * username and the password;
	 * 
	 * @param driver
	 * @param url
	 * @param user
	 * @param password
	 * @author Gautier Vanderslyen
	 */
	public ConnectionManager(String driver, String url, String user,
			char[] password) {
		this.initialize(driver, url, user, password);
	}

	public ConnectionManager(Properties properties) {
		this.initialize(properties);
	}

	private void initialize(String dsName) {
		logger
				.info("Connection Manager initialized for DataSource : "
						+ dsName);
		this._dataSourceName = dsName;
		this.transactionMode = TRAN_JDBC_DATASOURCE;
	}

	private void initialize(String dsName, String user, char[] pwd) {
		this._dataSourceName = dsName;
		this.transactionMode = TRAN_JDBC_DATASOURCE;
		if (user != null) {
			this._jdbcUser = user;
			this._jdbcPwd = pwd;
		}
	}

	private void initialize(DataSource ds) {
		this._ds = ds;
		this.transactionMode = TRAN_JDBC_DATASOURCE;
	}

	private void initialize(DataSource ds, String user, char[] password) {
		this._ds = ds;
		this._jdbcUser = user;
		this._jdbcPwd = password;
		this.transactionMode = TRAN_JDBC_DATASOURCE;
	}

	private void initialize(String driver, String url, String user, char[] pwd) {
		this._jdbcDriver = driver;
		this._jdbcUrl = url;
		this._jdbcUser = user;
		this._jdbcPwd = pwd;
		this.transactionMode = TRAN_JDBC_DRIVER;
	}

	private void initialize(Properties properties) {
		this.connectionProperties = properties;
		this._jdbcDriver = properties.getProperty(PROPERTY_CLASS_NAME);
		this._jdbcUrl = properties.getProperty(PROPERTY_URL);
		if (null != properties.getProperty(PROPERTY_USERNAME)) {
			this._jdbcUser = properties.getProperty(PROPERTY_USERNAME);
		}

		if (null != properties.getProperty(PROPERTY_PASSWORD)) {
			this._jdbcPwd = properties.getProperty(PROPERTY_PASSWORD)
					.toCharArray();
		}
		this.transactionMode = TRAN_JDBC_DRIVER;
	}

	/**
	 * Gets a Connection using the appropriate way given in the constructor.
	 * 
	 * @return
	 * @throws DataStoreException
	 * @author Gautier Vanderslyen
	 */
	public Connection getConnection() throws DataStoreException {
		Connection connection = null;
		try {
			if (transactionMode == TRAN_JDBC_DATASOURCE) {
				InitialContext ic = new InitialContext();
				_ds = (DataSource) ic.lookup(_dataSourceName);
				if (_jdbcUser != null)
					connection = _ds.getConnection(_jdbcUser, new String(
							_jdbcPwd));
				else
					connection = _ds.getConnection();
			} else if (transactionMode == TRAN_JDBC_DRIVER) {
				Class.forName(_jdbcDriver).newInstance();
				if (_jdbcUser != null) {
					connection = DriverManager.getConnection(_jdbcUrl,
							_jdbcUser, new String(_jdbcPwd));
				} else {
					connection = DriverManager.getConnection(_jdbcUrl);
				}
			}
		} catch (NamingException e) {
			throw new DataStoreException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new DataStoreException(e.getMessage());
		} catch (InstantiationException e) {
			throw new DataStoreException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new DataStoreException(e.getMessage());
		} catch (SQLException e) {
			throw new DataStoreException(e.getMessage());
		}
		return connection;
	}

	/**
	 * Closes a Connection, checking first if it's not null. The SQL Exception
	 * is thrown and a message is logged.
	 * 
	 * @param connection
	 *            the connection to close
	 * @author Gautier Vanderslyen
	 */
	public void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
				logger.debug("Connection closed");
			} catch (SQLException ex) {
				logger.error(ex.getMessage());
			}
		}
	}

	/**
	 * Rolls Back a Connection after Checking if this is null.
	 * 
	 * @param connection
	 *            to use for roll back
	 * @param msg
	 *            the message to send in after rolling back
	 * @throws DataStoreException
	 *             the exception that's always thrown after the roll back
	 * @author Gautier Vanderslyen
	 */
	public void rollback(Connection connection, String msg)
			throws DataStoreException {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("error rolling back : ", ex);
			}
		}
		throw new DataStoreException(msg);
	}

	/**
	 * Calls commit on the connection.
	 * 
	 * @param connection
	 *            the connection to commit
	 * @throws DataStoreException
	 *             thrown when something happens during the commit
	 * @author Gautier Vanderslyen
	 */
	public void commit(Connection connection) throws DataStoreException {
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				throw new DataStoreException(e.getMessage());
			}
		}
	}

	public Properties getProperties() {
		if (null != connectionProperties) {
			return connectionProperties;
		}

		Properties props = new Properties();
		if (transactionMode == TRAN_JDBC_DRIVER) {
			props.put(PROPERTY_CLASS_NAME, _jdbcDriver);
			props.put(PROPERTY_URL, _jdbcUrl);
			props.put(PROPERTY_USERNAME, _jdbcUser);
			props.put(PROPERTY_PASSWORD, new String(_jdbcPwd));
		}
		return props;
	}
	
	public void setUrl(String url){
		connectionProperties.put(
				ConnectionManager.PROPERTY_URL, url);	
		_jdbcUrl = url;
	}
}
