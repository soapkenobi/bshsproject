import java.util.Scanner;

class Product {
    private final String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void premium(double toAdd) {
        price+=toAdd;
    }

    public void discord(double toSub) {
        price-=toSub;
    }
}

class ShoppingCart {
    private Product[] items = new Product[0];

    public void addItem(Product product) {
        Product[] temp = items;
        items = new Product[temp.length + 1];
        System.arraycopy(temp, 0, items, 0, temp.length);
        items[temp.length] = product;
    }

    public Product[] getItems() {
        return items;
    }

    public double calculateTotal() {
        double total = 0;
        for (Product item : items) {
            total += item.getPrice();
        }
        return total;
    }
}

class SportsEquipmentStore {
    private Product[] availableProducts = new Product[0];
    private ShoppingCart shoppingCart = new ShoppingCart();

    public void initializeProducts() {
        addProduct(new Product("Football", 20.0));
        addProduct(new Product("Basketball", 15.0));
        addProduct(new Product("Tennis Racket", 50.0));
    }

    public void addProduct(Product product){
        Product[] temp = availableProducts;
        availableProducts = new Product[temp.length+1];
        System.arraycopy(temp, 0, availableProducts, 0, temp.length);
        availableProducts[temp.length] = product;
    }

    public void displayProducts() {
        System.out.println("Available Products:");
        for (int i = 0; i < availableProducts.length; i++) {
            Product product = availableProducts.get(i);
            System.out.println(i + 1 + ". " + product.getName() + " - $" + product.getPrice());
        }
    }

    public void addToCart(int productIndex) {
        Product selectedProduct = availableProducts[productIndex - 1];
        shoppingCart.addItem(selectedProduct);
        System.out.println(selectedProduct.getName() + " added to your cart.");
    }

    public void displayCart() {
        Product[] cartItems = shoppingCart.getItems();
        System.out.println("Shopping Cart:");
        for (int i = 0; i < cartItems.length; i++) {
            Product item = cartItems[i];
            System.out.println(i + 1 + ". " + item.getName() + " - $" + item.getPrice());
        }
        System.out.println("Total: $" + shoppingCart.calculateTotal());
    }
}

class test3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SportsEquipmentStore store = new SportsEquipmentStore();
        store.initializeProducts();

        while (true) {
            System.out.println("\n1. Display Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    store.displayProducts();
                    break;
                case 2:
                    System.out.print("Enter the product number to add to cart: ");
                    int productIndex = scanner.nextInt();
                    store.addToCart(productIndex);
                    break;
                case 3:
                    store.displayCart();
                    break;
                case 4:
                    System.out.println("Thank you for shopping!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
