import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
    private Shop shop = new Shop();

    public Shop getShop() {
        return shop;
    }

    Scanner sc = new Scanner(System.in);

    public void start() {

        loadProductList();
        while (true) {
            System.out.println("Добро пожаловать в магазин!\n" +
                    "1. Просмотр товаров\n" +
                    "2. Добавление товара\n" +
                    "3. Удаление товара\n" +
                    "4. Редактирование товара\n" +
                    "5. Купить\n" +
                    "6. Выход");
            int a = sc.nextInt();
            if (a == 1) {
                showShop();
            }
            if (a == 2) {
                addProduct();
            }
            if (a == 3) {
                deleteProd();
            }
            if (a == 4) {
                editProduct();
            }
            if (a == 5) {
                buyProduct();
            }
            if (a == 6) {
                System.out.println("Всего доброго!");
                break;
            }
        }
    }

    public void showShop() {

        System.out.println("Сортировать по:\n" +
                "1. цене(возростание)\n" +
                "2. цене(убывание)\n" +
                "3. по добавлению\n" +
                "4. фильтр по цене\n" +
                "5. вернуться в прошлое меню");
        int choice = sc.nextInt();
        if (choice == 1) {
            shop.sortWithLambda(shop.productsList, sorter -> shop.productsList.sort(Comparator.comparing(Product::getPrice)));
        }
        if (choice == 2) {
            shop.sortWithLambda(shop.productsList, sorter -> shop.productsList.sort(Comparator.comparing(Product::getPrice).reversed()));
        }
        if (choice == 3) {
            shop.sortWithLambda(shop.productsList, sorter -> shop.productsList.sort(Comparator.comparing(Product::getDateAdded)));
        }
        if (choice == 4) {
            filterByPrice();
        }
        if (choice == 5) {
            start();
        }
    }

    public void addProduct() {
        String nameProd;
        System.out.println("Введите id товара");
        int idProd = sc.nextInt();
        System.out.println("Введите название товара");
        while (true) {
            String name = sc.nextLine();
            if (checkName(name)) {
                nameProd = name;
                break;
            } else {
                System.out.println("Неккоректное название");
            }
        }

        System.out.println("Введите цену товара");
        int priceProd = sc.nextInt();
        shop.addProduct(idProd, nameProd, priceProd);
        DBConnector.saveProduct(new Product(idProd, nameProd, priceProd));
    }

    public void deleteProd() {
        System.out.println("Введите id товара");
        int idProd = sc.nextInt();
        shop.deleteProd(idProd);
        DBConnector.deleteProduct(idProd);
    }

    public void loadProductList() {
        shop.productsList = DBConnector.loadProducts();
    }

    public void editProduct() {
        String nameProd;
        System.out.println("Введите id товара");
        int idProd = sc.nextInt();
        System.out.println("Введите название товара");
        while (true) {
            String name = sc.nextLine();
            if (checkName(name)) {
                nameProd = name;
                break;
            } else {
                System.out.println("Неккоректное название");
            }
        }
        System.out.println("Введите цену товара");
        int priceProd = sc.nextInt();

        shop.editProduct(idProd, nameProd, priceProd);
        DBConnector.updateProduct(new Product(idProd, nameProd, priceProd));
    }

    public void buyProduct() {
        shop.sortWithLambda(shop.productsList, sorter -> shop.productsList.sort(Comparator.comparing(Product::getPrice)));
        List<Product> buyList = new ArrayList<>();
        Random rnd = new Random();
        DBConnector con = new DBConnector();
        System.out.println("Введите ID товаров ");
        int id;
        do {
            id = sc.nextInt();
            int i = id;
            if (id != 0) {
                Optional<Product> optionalProduct = shop.productsList.stream()
                        .filter(p -> p.getId() == i)
                        .findAny();
                if (optionalProduct.isPresent()) {
                    addIf(buyList, optionalProduct.get());
                    System.out.println("Товар добавлен в корзину, для подтверждения покупки введите 0 либо введите ID следующего товара");
                } else {
                    System.out.println("Товара с таким ID не существует, для подтверждения покупки введите 0 либо введите ID следующего товара");
                }
            } else {
                break;
            }
        } while (true);

        con.saveReceipt(new Purchases(buyList,rnd.nextInt()));

//        for (Product prod :
//                buyList) {
//            con.deleteProduct(prod.getId());
//        }

        shop.productsList.removeAll(buyList);
        System.out.println("Спасибо за покупку!");
    }

    public void addIf(List<Product> pl, Product p) {
        if (!pl.contains(p)) {
            pl.add(p);
        }
    }

    public void filterByPrice() {
        System.out.println("Введите минимальную стоимость товара");
        int minPrice = sc.nextInt();
        System.out.println("Введите максимальную стоимость товара");
        int maxPrice = sc.nextInt();
        shop.filterByPrice(minPrice, maxPrice);
    }

    public boolean checkName(String name) {
        boolean isTrue = false;
        Pattern pattern = Pattern.compile("[A-ZА-Я]\\s?([a-zа-я]+\\s?)*\\S?(\\s?\\d\\s?\\d*)*");
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (name.substring(start, end).equals(name)) {
                isTrue = true;
            }
        }
        return isTrue;
    }
}
