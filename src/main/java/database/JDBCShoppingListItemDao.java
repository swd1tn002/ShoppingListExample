package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ShoppingListItem;

/**
 * JDBCShoppingListItemDao toteuttaa rajapinnan, ja sisältää konkreettisen
 * SQL-logiikan
 */
public class JDBCShoppingListItemDao implements ShoppingListItemDao {

    private final Database db = new Database();

    @Override
    public List<ShoppingListItem> getAllItems() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;

        List<ShoppingListItem> items = new ArrayList<>();

        try {
            // muodostetaan yhteys tietokantaan
            connection = this.db.connect();

            // muodostetaan kysely "SELECT * FROM ShoppingListItem"
            statement = connection.prepareStatement("SELECT * FROM ShoppingListItem");

            // suoritetaan kysely
            results = statement.executeQuery();

            // käydään läpi tuloksena saadut rivit ja muodostetaan niitä vastaavat oliot
            while (results.next()) {
                long id = results.getLong("id");
                String title = results.getString("title");

                // luodaan riviä vastaava olio:
                ShoppingListItem newItem = new ShoppingListItem(id, title);

                // lisätään olio talteen listalle:
                items.add(newItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.db.close(connection, statement, results);
        }
        return items;
    }

    @Override
    public ShoppingListItem getItem(long id) {
        // unohdetaan hetkeksi suorituskyky (joudumme lataamaan kaikki vaikka haluamme
        // vain yhden):
        List<ShoppingListItem> allItems = this.getAllItems();
        for (ShoppingListItem item : allItems) {
            if (id == item.getId()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean addItem(ShoppingListItem newItem) {
        String sql = "INSERT INTO ShoppingListItem (title) VALUES (?)";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet ids = null;

        try {
            connection = this.db.connect();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newItem.getTitle());
            int rows = statement.executeUpdate();
            if (rows == 1) {
                ids = statement.getGeneratedKeys();
                ids.next();
                long generatedId = ids.getLong(1);
                newItem.setId(generatedId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.db.close(connection, statement, ids);
        }
        return false;
    }

    @Override
    public boolean removeItem(ShoppingListItem item) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.db.connect();
            statement = connection.prepareStatement("DELETE FROM ShoppingListItem WHERE id = ?");
            statement.setLong(1, item.getId());
            int rows = statement.executeUpdate();
            if (rows == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.db.close(connection, statement, null);
        }
        return false;
    }
}