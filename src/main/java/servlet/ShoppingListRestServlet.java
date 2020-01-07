package servlet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.ShoppingListItemDao;
import model.ShoppingListItem;

@SuppressWarnings("serial")
@WebServlet("/api/shoppingList/items")
public class ShoppingListRestServlet extends HttpServlet {

    private ShoppingListItemDao dao = new ShoppingListItemDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ShoppingListItem> allItems = dao.getAllItems();

        // convert the Java objects into a JSON formatted String:
        String json = new Gson().toJson(allItems);

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().println(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // read all lines from the POST request body and join them into one String:
        String jsonString = req.getReader().lines().collect(Collectors.joining());

        // convert the read JSON input from a String into a ShoppingListItem object:
        ShoppingListItem newItem = new Gson().fromJson(jsonString, ShoppingListItem.class);

        dao.addItem(newItem);

        String json = new Gson().toJson(newItem);

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().println(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        long id = Long.parseLong(req.getParameter("id"));
        ShoppingListItem item = dao.getItem(id);

        if (item != null) {
            dao.removeItem(item);
        }

        String json = new Gson().toJson(item);

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().println(json);
    }
}
