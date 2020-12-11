package database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShoppingListItem;

public class JDBCShoppingListItemDaoTest {

    private JDBCShoppingListItemDao dao = new JDBCShoppingListItemDao();

    /**
     * This method clears the test database and inserts two rows directly in the
     * database before each test with a delete statement.
     * 
     * This way every time the tests are executed they have exactly the same data to
     * work with.
     * 
     * !! Make sure to always use a different database in tests to prevent data loss
     * or corruption !!
     */
    @BeforeEach
    public void setUp() throws SQLException {
        String deleteAll = "DELETE FROM ShoppingListItem";
        String insertTwo = "INSERT INTO ShoppingListItem (id, title) VALUES (1, 'Milk'), (2, 'Juice')";

        Database db = new Database();
        Connection connection = db.connect();

        // delete all existing data
        PreparedStatement deleteStmt = connection.prepareStatement(deleteAll);
        deleteStmt.executeUpdate();

        // insert two new rows
        PreparedStatement insertStmt = connection.prepareStatement(insertTwo);
        insertStmt.executeUpdate();

        deleteStmt.close();
        insertStmt.close();
        connection.close();
    }

    @Test
    public void testFirstItemIsMilk() {
        List<ShoppingListItem> items = this.dao.getAllItems();
        ShoppingListItem first = items.get(0);

        assertEquals("Milk", first.getTitle());
    }

    @Test
    public void testLastItemIsJuice() {
        List<ShoppingListItem> items = this.dao.getAllItems();
        ShoppingListItem last = items.get(items.size() - 1);

        assertEquals("Juice", last.getTitle());
    }

    @Test
    public void testNewItemIdIsSetAfterSavingItem() {
        ShoppingListItem balloon = new ShoppingListItem(0, "Balloon");
        this.dao.addItem(balloon);

        assertTrue(balloon.getId() > 0);
    }
}