package database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.ShoppingListItem;

/**
 * This is a FAKE implementation of a DAO class for demonstration purposes only.
 * The shopping list items returned are not actually persisted in a database of
 * any kind. Instead, the class internally uses a collection to hold the items,
 * which will be lost during each restart of the process.
 * 
 * Feel free to modify the implementation of this class to support an actual
 * database. You should not need to modify any of the public interfaces.
 */
public class FakeShoppingListItemDao implements ShoppingListItemDao {

    private List<ShoppingListItem> items = new ArrayList<>();

    private int nextId = 1; // to provide each item an unique incrementing id

    public FakeShoppingListItemDao() {
        // Add a few initial values to start with:
        this.addItem(new ShoppingListItem(0, "Milk"));
        this.addItem(new ShoppingListItem(0, "Eggs"));
        this.addItem(new ShoppingListItem(0, "Bread"));
    }

    public List<ShoppingListItem> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    public ShoppingListItem getItem(long id) {
        synchronized (this) {
            return items.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
        }
    }

    public boolean addItem(ShoppingListItem newItem) {
        synchronized (this) {
            newItem.setId(nextId++);
            return items.add(newItem);
        }
    }

    public boolean removeItem(ShoppingListItem item) {
        synchronized (this) {
            return items.remove(item); // returns true if item was removed
        }
    }

}
