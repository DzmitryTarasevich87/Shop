import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Shop?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Amigo14159265";


    public static void saveProduct(Product p) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Products\n" +
                    "VALUE(" + p.getId() + ",'" + p.getName() + "'," + p.getPrice() + ");";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            String sql = "DELETE FROM Products WHERE id='" + id + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct(Product p){
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            String sql = "UPDATE Products SET title ='"+p.getName()+"', price ='"+p.getPrice()+"' WHERE id ='"+p.getId()+"';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void saveReceipt(Purchases purchases){
        for (Product prod: purchases.getReceipt()
             ) {
            try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
                String sql = "insert into purchases\n" +
                        "value\n" +
                        "(default,'"+purchases.getReceiptNumber()+"','"+prod.getId()+"');";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




    }

    public static List<Product> loadProducts() {
        List<Product> productList = new ArrayList<>();
        String load = "SELECT * FROM Products";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(load)) {
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("title"));
                product.setPrice(resultSet.getInt("price"));
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }
}
