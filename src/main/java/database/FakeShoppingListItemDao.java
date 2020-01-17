package database;

import java.util.ArrayList;
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
        items.add(new ShoppingListItem(nextId++, "Milk"));
        items.add(new ShoppingListItem(nextId++, "Eggs"));
        items.add(new ShoppingListItem(nextId++, "Bread"));
    }

    public List<ShoppingListItem> getAllItems() {
        return items;
    }

    public ShoppingListItem getItem(long id) {
        /*
         * This method uses programming constructs that may not be familiar to you at
         * this point.
         * 
         * For using streams and lambda expressions for finding a value see:
         * https://www.baeldung.com/java-stream-filter-lambda
         * 
         * For using optional, potentially null values see:
         * https://www.baeldung.com/java-optional-or-else-vs-or-else-get
         */
        return items.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    public boolean addItem(ShoppingListItem newItem) {
        newItem.setId(nextId++);
        return items.add(newItem);
    }

    public boolean removeItem(ShoppingListItem item) {
        return items.remove(item); // returns true if item was removed
    }

}
