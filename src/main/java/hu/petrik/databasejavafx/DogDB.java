package hu.petrik.databasejavafx;



import hu.petrik.data.Dog;

import java.io.BufferedReader;
import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DogDB {
    private Connection conn;

    public static String DB_DRIVER = "mysql";
    public static String DB_HOST = "localhost";
    public static String DB_PORT = "3306";
    public static String DB_USERNAME = "root";
    public static String DB_PASSWORD = "";
    public static String DB_NAME = "java";

    public static String TABLE_NAME = "dogs";

    public static String COL_ID = "id";
    public static String COL_NAME = "name";
    public static String COL_AGE = "age";
    public static String COL_BREED = "breed";


    public DogDB() throws SQLException {
        // jdbc:mysql://localhost:3306/java
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_NAME);
        conn = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);
    }

    public List<Dog>  getDogs() throws SQLException {
        List<Dog> dogs = new ArrayList<>();

        String sql = "SELECT * FROM dogs";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);

        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            int age = result.getInt("age");
            String breed = result.getString("breed");

            Dog dog = new Dog(id, name, age, breed);
            dogs.add(dog);
        }
        return dogs;
    }

    public boolean insertDog(Dog dog) throws SQLException {
        String sql = String.format("INSERT INTO %s(%s, %s, %s) VALUES (?, ?, ?)",
                TABLE_NAME, COL_NAME, COL_AGE, COL_BREED);

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, dog.getName());
        statement.setInt(2, dog.getAge());
        statement.setString(3, dog.getBreed());
        return statement.executeUpdate() > 0;
    }

    public boolean deleteDog(int id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        return statement.executeUpdate() > 0;
    }

    public boolean updateDog(Dog dog) throws SQLException {
        String sql = String.format("UPDATE %s SET name = ?, age = ?, breed = ? WHERE id = ?", TABLE_NAME);

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, dog.getName());
        statement.setInt(2, dog.getAge());
        statement.setString(3, dog.getBreed());
        statement.setInt(4, dog.getId());
        return statement.executeUpdate() > 0;
    }




}
