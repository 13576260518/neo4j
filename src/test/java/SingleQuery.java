import org.neo4j.driver.v1.*;

import java.sql.*;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.neo4j.driver.v1.Values.parameters;

public class SingleQuery {
    public static void main(String... args) throws Exception {
        tst();
    }

    public static void tstJDBC() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:neo4j:bolt://localhost:7687/", "neo4j", "neo4jneo4j");

// Querying
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("MATCH (n:Greeting) RETURN n.message");
            while (rs.next()) {
                System.out.println(rs.getString("n.message"));
            }
        }
        conn.close();
    }

    public static void tst() {

        org.neo4j.driver.v1.Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jneo4j"));
        Session session = driver.session();

        session.run("CREATE (a:Person {name: {name}, title: {title}})",
                parameters("name", "Arthur", "title", "King"));

        StatementResult result = session.run("MATCH (a:Person) WHERE a.name = {name} " +
                        "RETURN a.name AS name, a.title AS title",
                parameters("name", "Arthur"));
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
        }

        session.close();
        driver.close();

    }
}
