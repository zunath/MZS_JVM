package mzsJVM.Data;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Connection;
import java.util.List;

public class DataAccess {
    private static String _host;
    private static String _username;
    private static String _password;
    private static String _schema;
    private static Connection _connection;


    public static void Initialize()
    {
        Path path = Paths.get("mzs2-db-settings.txt");

        try
        {
            List<String> rows = Files.readAllLines(path, Charset.defaultCharset());


            for(String row : rows)
            {
                String[] parts = row.split("=");

                if(parts[0].equals("host"))
                {
                    _host = parts[1];
                }
                else if(parts[0].equals("username"))
                {
                    _username = parts[1];
                }
                else if(parts[0].equals("password"))
                {
                    _password = parts[1];
                }
                else if(parts[0].equals("schema"))
                {
                    _schema = parts[1];
                }

            }
        }
        catch (IOException ex) {
            // TODO: Log exception
        }

        LoadDriver();
        CreateConnection();
    }

    private static void LoadDriver()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex)
        {
            // TODO: Logging
        }
    }

    private static void CreateConnection()
    {
        try
        {
            _connection = DriverManager.getConnection("jdbc:mysql://" + _host + "/" + _schema,
                    _username,
                    _password);
        }
        catch (SQLException ex)
        {
            // TODO: Logging
        }
    }

    public static void Shutdown()
    {
        try {
            _connection.close();
        }
        catch (SQLException ex)
        {
            // TODO: Log error
        }
    }
}
