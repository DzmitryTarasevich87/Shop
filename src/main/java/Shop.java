import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Shop {

    public List<Product> productsList = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public List<Product> editShopList() {
        return productsList;
    }

    public void addProduct(int idProd, String nameProd, int priceProd) {

        Product product = new Product(idProd, nameProd, priceProd);

        Optional<Product> isPresent = productsList.stream().filter(p -> p.getId() == idProd).findAny();
        isPresent.ifPresentOrElse(
                v -> System.out.println("Товар с таким id уже существует"),
                () -> productsList.add(product)
        );
    }

    public void deleteProd(int idProd) {
        productsList.removeIf(i -> i.getId() == idProd);

    }

    public void editProduct(int idProd, String nameProd, int priceProd) {

        Optional<Product> optionalProduct = productsList.stream()
                .filter(p -> p.getId() == idProd)
                .findAny();
        if (optionalProduct.isPresent()) {
            optionalProduct.get().setName(nameProd);
            optionalProduct.get().setPrice(priceProd);
        }
    }

    public void filterByPrice(int minPrice, int maxPrice) {
        System.out.println(productsList.stream()
                .filter(p -> p.getPrice() >= minPrice)
                .filter(p -> p.getPrice() <= maxPrice)
                .collect(Collectors.toList()));
    }

    public void sortWithLambda(List<Product> list, AnySort sort) {
        sort.doing(list);
        System.out.println(list);
    }

}

@FunctionalInterface
interface AnySort {
    void doing(List<Product> productList);
}



