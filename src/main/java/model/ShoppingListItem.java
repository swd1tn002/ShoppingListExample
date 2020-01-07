package model;

public class ShoppingListItem {

    private long id;
    private String title;

    /** Empty constructor only used by GSON when converting JSON to Java objects */
    @SuppressWarnings("unused")
    private ShoppingListItem() {
    }

    public ShoppingListItem(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ShoppingListItem && ((ShoppingListItem) other).id == this.id;
    }
}
