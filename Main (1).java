
import java.util.Scanner;

// INTERFACE
interface IProduct {

    int GetTotalProductsSold();

    double GetAverageProductSold();

    String GetMostPopularProduct();
}


// PRODUCT CLASS
class Product implements IProduct {

    private String[] productNames;
    private int[][] sales;

    // Constructor
    public Product(String[] productNames, int[][] sales) {

        this.productNames = productNames;
        this.sales = sales;
    }

    // Calculate total products sold
    @Override
    public int GetTotalProductsSold() {

        int total = 0;

        for (int i = 0; i < sales.length; i++) {

            for (int j = 0; j < sales[i].length; j++) {

                total += sales[i][j];
            }
        }

        return total;
    }


    // Calculate average products sold
    @Override
    public double GetAverageProductSold() {

        int total = GetTotalProductsSold();

        int count = 0;

        for (int i = 0; i < sales.length; i++) {

            count += sales[i].length;
        }

        if (count == 0) {
            return 0;
        }

        return (double) total / count;
    }


    // Find the most popular product
    @Override
    public String GetMostPopularProduct() {

        int highestSales = -1;

        int popularIndex = 0;

        for (int i = 0; i < sales.length; i++) {

            int productTotal = 0;

            for (int j = 0; j < sales[i].length; j++) {

                productTotal += sales[i][j];
            }

            if (productTotal > highestSales) {

                highestSales = productTotal;

                popularIndex = i;
            }
        }

        return productNames[popularIndex];
    }


    // Display product sales
    public void DisplaySales() {

        System.out.println("\nPRODUCT SALES DETAILS");

        System.out.println("---------");

        for (int i = 0; i < productNames.length; i++) {

            System.out.println("\nProduct: " + productNames[i]);

            for (int j = 0; j < sales[i].length; j++) {

                System.out.println("Month " + (j + 1)
                        + " sales: " + sales[i][j]);
            }
        }
    }
}


// PRINT PRODUCT CLASS
class PrintProduct {

    // Print total sales
    public void PrintTotalSales(Product product) {

        System.out.println("TOTAL PRODUCT SALES: "
                + product.GetTotalProductsSold());
    }


    // Print average sales
    public void PrintAverageSales(Product product) {

        System.out.println("AVERAGE PRODUCT SALES: "
                + String.format("%.2f",
                product.GetAverageProductSold()));
    }


    // Print most popular product
    public void PrintMostPopularProduct(Product product) {

        System.out.println("MOST POPULAR PRODUCT: "
                + product.GetMostPopularProduct());
    }


    // Print complete report
    public void PrintReport(Product product) {

        System.out.println("\n_____");

        System.out.println("    PRODUCT SALES REPORT - 2026");

        System.out.println("_______");

        PrintTotalSales(product);

        PrintAverageSales(product);

        PrintMostPopularProduct(product);

        System.out.println("_______");
    }
}


// MAIN CLASS
public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Product names
        String[] productNames = {

            "Cozy Nights Mattress",

            "Electronic Barbecue",

            "Salty Pillows"
        };


        // Two-dimensional array for three months
        int[][] sales = new int[3][3];


        System.out.println("_______");

        System.out.println("     PRODUCT SALES SYSTEM");

        System.out.println("______");


        // Enter sales information
        for (int i = 0; i < productNames.length; i++) {

            System.out.println("\nPRODUCT: " + productNames[i]);

            for (int j = 0; j < 3; j++) {

                System.out.print(
                    "Enter sales for month "
                    + (j + 1) + ": "
                );

                // Validate user input
                while (!input.hasNextInt()) {

                    System.out.println(
                        "Invalid input. Please enter a number."
                    );

                    input.next();

                    System.out.print("Enter sales again: ");
                }

                sales[i][j] = input.nextInt();

                while (sales[i][j] < 0) {

                    System.out.println(
                        "Sales cannot be negative."
                    );

                    System.out.print("Enter sales again: ");

                    while (!input.hasNextInt()) {

                        System.out.println(
                            "Please enter a valid number."
                        );

                        input.next();

                        System.out.print("Enter sales again: ");
                    }

                    sales[i][j] = input.nextInt();
                }
            }
        }


        // Create Product object
        Product product = new Product(productNames, sales);


        // Display sales details
        product.DisplaySales();


        // Create PrintProduct object
        PrintProduct printer = new PrintProduct();


        // Generate final sales report
        printer.PrintReport(product);


        // Close scanner
        input.close();

    }
}
