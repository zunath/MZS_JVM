package mzsJVM.Data;
import mzsJVM.Entities.AuthorizedDMEntity;
import mzsJVM.Entities.DMRoleEntity;
import mzsJVM.Entities.PlayerEntity;
import mzsJVM.Entities.StorageEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DataAccess {
    private static String _host;
    private static String _username;
    private static String _password;
    private static String _schema;

    private static SessionFactory _sessionFactory;
    private static Configuration _configuration;
    private static ServiceRegistry _serviceRegistry;

    public static void Initialize()
    {
        Path path = Paths.get("./jvm/mzs2-db-settings.txt");

        try
        {
            List<String> rows = Files.readAllLines(path, Charset.defaultCharset());


            for(String row : rows)
            {
                String[] parts = row.split("=");

                switch (parts[0]) {
                    case "host":
                        _host = parts[1];
                        break;
                    case "username":
                        _username = parts[1];
                        break;
                    case "password":
                        _password = parts[1];
                        break;
                    case "schema":
                        _schema = parts[1];
                        break;
                }

            }
        }
        catch (IOException ex) {
            // TODO: Log exception
        }

        CreateSessionFactory();
    }


    private static void CreateSessionFactory()
    {
        _configuration = new Configuration();

        _configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        _configuration.setProperty("hibernate.connection.url", "jdbc:mysql://" + _host + "/" + _schema);
        _configuration.setProperty("hibernate.connection.username", _username);
        _configuration.setProperty("hibernate.connection.password", _password);
        _configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        _configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        _configuration.setProperty("hibernate.cache.use_query_cache", "false");
        _configuration.setProperty("hibernate.current_session_context_class", "thread");

        // Link all DB entities to the configuration here.
        _configuration.addAnnotatedClass(AuthorizedDMEntity.class);
        _configuration.addAnnotatedClass(DMRoleEntity.class);
        _configuration.addAnnotatedClass(PlayerEntity.class);
        _configuration.addAnnotatedClass(StorageEntity.class);


        _serviceRegistry = new ServiceRegistryBuilder().applySettings(
                        _configuration.getProperties()).buildServiceRegistry();

        _sessionFactory = _configuration.buildSessionFactory(_serviceRegistry);

    }

    public static Session getSession()
    {
        return _sessionFactory.getCurrentSession();
    }
}
