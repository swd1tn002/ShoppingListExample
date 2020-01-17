package database;

import java.util.List;

import model.ShoppingListItem;

/**
 * TODO: Complete the implementation of this DAO-class by implementing database
 * operations for each of the CRUD methods.
 */
public class JDBCShoppingListItemDao implements ShoppingListItemDao {

    @Override
    public List<ShoppingListItem> getAllItems() {
        return null;
    }

    @Override
    public ShoppingListItem getItem(long id) {
        return null;
    }

    @Override
    public boolean addItem(ShoppingListItem newItem) {
        return false;
    }

    @Override
    public boolean removeItem(ShoppingListItem item) {
        return false;
    }

}
