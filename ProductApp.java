import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


// Interface & ProductCalculations Class

interface IProductCalculations {
    double GetVat();
    double GetCommission();
    double GetTotal();
}

class ProductCalculations implements IProductCalculations {
    private final double price;
    private static final double VAT_RATE = 0.15;        // 15% VAT
    private static final double COMMISSION_RATE = 0.05; // 5% Commission

    public ProductCalculations(double price) {
        this.price = price;
    }

    @Override
    public double GetVat() {
        return price * VAT_RATE;
    }

    @Override
    public double GetCommission() {
        return price * COMMISSION_RATE;
    }

    @Override
    public double GetTotal() {
        return price + GetVat() + GetCommission(); // Total = Price + VAT + Commission
    }
}


// Unit Tests Class

class ProductCalculationsTest {

    public static void GetVat_WithValidData_ReturnsCorrectValue() {
        ProductCalculations instance = new ProductCalculations(3500.0);
        double expected = 525.0; // 15% of 3500
        double actual = instance.GetVat();

        if (Math.abs(expected - actual) < 0.001) {
            System.out.println("[PASS] GetVat_WithValidData_ReturnsCorrectValue: Expected " + expected + ", Got " + actual);
        } else {
            throw new AssertionError("[FAIL] GetVat_WithValidData_ReturnsCorrectValue: Expected " + expected + ", Got " + actual);
        }
    }

    public static void GetTotal_WithValidData_ReturnsCorrectValue() {
        ProductCalculations instance = new ProductCalculations(3500.0);
        double expected = 4200.0; // 3500 + 525 + 175
        double actual = instance.GetTotal();

        if (Math.abs(expected - actual) < 0.001) {
            System.out.println("[PASS] GetTotal_WithValidData_ReturnsCorrectValue: Expected " + expected + ", Got " + actual);
        } else {
            throw new AssertionError("[FAIL] GetTotal_WithValidData_ReturnsCorrectValue: Expected " + expected + ", Got " + actual);
        }
    }

    public static void runAllTests() {
        System.out.println("_ RUNNING UNIT TESTS _");
        GetVat_WithValidData_ReturnsCorrectValue();
        GetTotal_WithValidData_ReturnsCorrectValue();
        System.out.println("_ ALL TESTS PASSED _\n");
    }
}


// Main GUI Application & File Handling

public class ProductApp extends JFrame {
    private JTextField txtCustomerName;
    private JComboBox<String> cmbProduct;
    private JTextField txtPrice;
    private JButton btnProcess;

    private JMenuBar menuBar;
    private JMenu menuFile, menuTools;
    private JMenuItem itemExit, itemProcess, itemClear;

    public ProductApp() {
        setTitle("Product App");
        setSize(420, 270);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initMenus();
        initComponents();
    }

    private void initMenus() {
        menuBar = new JMenuBar();

        // File Menu -> Exit 
        menuFile = new JMenu("File");
        itemExit = new JMenuItem("Exit");
        itemExit.addActionListener(e -> System.exit(0));
        menuFile.add(itemExit);

        // Tools Menu -> Process, Clear 
        menuTools = new JMenu("Tools");
        itemProcess = new JMenuItem("Process");
        itemClear = new JMenuItem("Clear");

        itemProcess.addActionListener(e -> processSale());
        itemClear.addActionListener(e -> clearFields());

        menuTools.add(itemProcess);
        menuTools.add(itemClear);

        menuBar.add(menuFile);
        menuBar.add(menuTools);
        setJMenuBar(menuBar);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer Name 
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCustomerName = new JTextField(18);
        mainPanel.add(txtCustomerName, gbc);

        // Select Product ComboBox 
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Select Product:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        String[] products = {
            "Cozy Nights Monarch",
            "Restonic Barbados",
            "Sealy Phuket"
        };
        cmbProduct = new JComboBox<>(products);
        mainPanel.add(cmbProduct, gbc);

        // Enter Price Text Field 
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Enter Price:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPrice = new JTextField(18);
        mainPanel.add(txtPrice, gbc);

        // Process Button 
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 5, 8);
        btnProcess = new JButton("PROCESS");
        btnProcess.addActionListener(e -> processSale());
        mainPanel.add(btnProcess, gbc);

        add(mainPanel);
    }

    private void processSale() {
        String customerName = txtCustomerName.getText().trim();
        String productName = (String) cmbProduct.getSelectedItem();
        String priceText = txtPrice.getText().trim();

        if (customerName.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name and price.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive numeric price.", "Invalid Price", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculations
        ProductCalculations calc = new ProductCalculations(price);
        double vat = calc.GetVat();
        double commission = calc.GetCommission();
        double total = calc.GetTotal();

        // Display Message Dialog 
        String message = String.format(
            "Customer Name: %s\nProduct: %s\nVAT: R %.1f\nCOMMISSION: R %.1f\nTOTAL: R %.1f",
            customerName, productName, vat, commission, total
        );

        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);

        // Save to data.txt 
        saveToFile(customerName, productName, vat, commission, total);
    }

    private void saveToFile(String customerName, String productName, double vat, double commission, double total) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
            writer.write("DATA LOG");
            writer.newLine();
            writer.write("_____");
            writer.newLine();
            writer.write("Customer Name: " + customerName);
            writer.newLine();
            writer.write("Product: " + productName);
            writer.newLine();
            writer.write(String.format("VAT: R %.1f", vat));
            writer.newLine();
            writer.write(String.format("COMMISSION: R %.1f", commission));
            writer.newLine();
            writer.write(String.format("TOTAL: R %.1f", total));
            writer.newLine();
            writer.write("____");
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to data.txt: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtCustomerName.setText("");
        txtPrice.setText("");
        cmbProduct.setSelectedIndex(0);
        txtCustomerName.requestFocus();
    }

    public static void main(String[] args) {
        // 1. Run Unit Tests and output results to console
        ProductCalculationsTest.runAllTests();

        // 2. Launch the GUI Application
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new ProductApp().setVisible(true));
    }
}