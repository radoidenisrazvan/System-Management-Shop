package ApManagerStoc;

import javax.swing.*;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Statement;

public class MainGUI extends JFrame {

    private ManagerStoc managerStoc;
    private JTextArea outputArea;
    private JPanel cards; 
    private CardLayout cardLayout;
    private List<Produs> inventory;
    private Map<String, List<OrderItem>> ordersByClient;

    public MainGUI() {
        managerStoc = new ManagerStoc();
        inventory = new ArrayList<>();
        ordersByClient = new HashMap<>();

        setTitle("Sistem gestionare magazin componente PC");
        setSize(800, 400); // Am mărit latimea la 800 pentru a avea mai mult spațiu pentru output
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        add(cards);
        createMainCard();
        createDespreProduseCard();

        setVisible(true);
    }

    private void createMainCard() {
        JPanel mainCard = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(0, 1));

        JButton despreProduseButton = new JButton("Despre produse");
        JButton searchButton = new JButton("Cautare");
        JButton displayButton = new JButton("Afisare Produse");
        JButton sellButton = new JButton("Vinde produse");
        JButton orderButton = new JButton("Vizualizare comenzi");

        leftPanel.add(despreProduseButton);
        leftPanel.add(searchButton);
        leftPanel.add(displayButton);
        leftPanel.add(sellButton);
        leftPanel.add(orderButton);

        mainCard.add(leftPanel, BorderLayout.WEST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        mainCard.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JTextArea reportArea = new JTextArea();
        mainCard.add(reportArea, BorderLayout.EAST);

        despreProduseButton.addActionListener(e -> showDespreProduseCard());
        searchButton.addActionListener(e -> searchProduct());
        displayButton.addActionListener(e -> displayProducts());
        sellButton.addActionListener(e-> vindeProdus());
        orderButton.addActionListener(e->viewOrders(reportArea));

        cards.add(mainCard, "Main");
    }

    private void createDespreProduseCard() {
        JPanel despreProduseCard = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(0, 1));

        JButton backButton = new JButton("Inapoi la meniul principal");
        JButton addButton = new JButton("Adauga produs");
        JButton updateButton = new JButton("Modifica produs");
        JButton deleteButton = new JButton("Stergere produs");
        JButton lowQuantityButton = new JButton("Produse cu Cantitate Scazuta");

        leftPanel.add(backButton);
        leftPanel.add(addButton);
        leftPanel.add(updateButton);
        leftPanel.add(deleteButton);
        leftPanel.add(lowQuantityButton);

        despreProduseCard.add(leftPanel, BorderLayout.WEST);
        
        backButton.addActionListener(e->showMainCard());
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        lowQuantityButton.addActionListener(e -> showLowQuantityProducts());

        cards.add(despreProduseCard, "DespreProduse");
    }

    private void showDespreProduseCard() {
        cardLayout.show(cards, "DespreProduse");
    }
    
    private void showMainCard() {
        cardLayout.show(cards, "Main");
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog(this, "Introduceti numele produsului:");
        String category = JOptionPane.showInputDialog(this, "Introduceti categoria produsului:");
        String quantityStr = JOptionPane.showInputDialog(this, "Introduceti cantitatea produsului:");
        double pret = Double.parseDouble(JOptionPane.showInputDialog(this, "Introduceti pretul produsului:"));

        try {
            int quantity = Integer.parseInt(quantityStr);
            Produs newProduct = new Produs(name, category, quantity, pret);
            inventory.add(newProduct);

            // Adaugă subinterogările SQL pentru a adăuga produsul în baza de date
            String insertQuery = "INSERT INTO produs (denumire, categorie, cantitate, pret) " +
                    "VALUES (?, ?, ?, ?)";

            
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
            	     PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {

            	    preparedStatement.setString(1, name);
            	    preparedStatement.setString(2, category);
            	    preparedStatement.setInt(3, quantity);
            	    preparedStatement.setDouble(4, pret);

            	    preparedStatement.executeUpdate();

            	    updateOutput("Produs adaugat in baza de date: " + newProduct.getDenumire() +
            	            " Cantitate: " + newProduct.getCantitate() +
            	            " Pret: " + newProduct.getPret() + " RON");

            	} catch (SQLException ex) {
            	    ex.printStackTrace();
            	    JOptionPane.showMessageDialog(this, "Eroare la adaugarea produsului in baza de date.");
            	}

            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantitate invalida. Introduceti un numar valid.");
        }
        
        showMainCard();
    }


    private void updateProduct() {
        String name = JOptionPane.showInputDialog(this, "Introduceti numele produsului dorit modificat:");
        String quantityStr = JOptionPane.showInputDialog(this, "Introduceti noua cantitate:");
        double pret = Double.parseDouble(JOptionPane.showInputDialog(this, "Introduceti noul pret al produsului:"));

        try {
            int quantity = Integer.parseInt(quantityStr);
            managerStoc.actualizeazaProdus(name, quantity, pret);

            // Adaugă operația de actualizare în baza de date
            String updateQuery = "UPDATE produs SET cantitate = ?, pret = ? WHERE denumire = ?";
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
                 PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {

                preparedStatement.setInt(1, quantity);
                preparedStatement.setDouble(2, pret);
                preparedStatement.setString(3, name);

                preparedStatement.executeUpdate();

                updateOutput("Produsul '" + name + "' a fost actualizat in baza de date. Noua cantitate: " +
                        quantity + " bucati, noul pret: " + pret + " RON");

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la actualizarea produsului in baza de date.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantitate invalida. Introduceti un numar valid.");
        }
    }


    private void searchProduct() {
        try {
            String criteria = JOptionPane.showInputDialog(this, "Cautati dupa criteriu (denumire sau categorie):");
            String query = "SELECT * FROM produs WHERE denumire LIKE ? OR categorie LIKE ?";
            
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
                 PreparedStatement stmt = con.prepareStatement(query)) {
                
                stmt.setString(1, "%" + criteria + "%");
                stmt.setString(2, "%" + criteria + "%");
                
                try (ResultSet rs = stmt.executeQuery()) {
                    ArrayList<Produs> searchResults = new ArrayList<>();

                    while (rs.next()) {
                        String denumire = rs.getString("denumire");
                        String categorie = rs.getString("categorie");
                        int cantitate = rs.getInt("cantitate");
                        double pret = rs.getDouble("pret");

                        Produs product = new Produs(denumire, categorie, cantitate, pret);
                        searchResults.add(product);
                    }

                    if (!searchResults.isEmpty()) {
                        updateOutput("Cautarea pentru '" + criteria + "':");
                        for (Produs product : searchResults) {
                            updateOutput(product.toString());
                        }
                    } else {
                        updateOutput("Nu s-au gasit produse pentru '" + criteria + "'.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la căutarea produselor.");
        }
    }


    private void deleteProduct() {
        String nameToDelete = JOptionPane.showInputDialog(this, "Introduceti numele produsului pentru a fi sters:");

        try {
            String query = "DELETE FROM produs WHERE denumire = ?";

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
                 PreparedStatement stmt = con.prepareStatement(query)) {

                stmt.setString(1, nameToDelete);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    updateOutput("Produs '" + nameToDelete + "' sters.");
                } else {
                    updateOutput("Eroare: Produsul '" + nameToDelete + "' nu exista in stoc.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la stergerea produsului.");
        }
    }


    private void vindeProdus() {
        // Obținem numele produselor disponibile din baza de date
        List<String> productNames = new ArrayList<>();
        String selectQuery = "SELECT denumire FROM produs";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
             PreparedStatement stmt = con.prepareStatement(selectQuery);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String productName = rs.getString("denumire");
                productNames.add(productName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la obținerea listei de produse.");
            return;
        }

        // Verificați dacă există produse disponibile
        if (productNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nu există produse disponibile pentru vânzare.", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creați o listă de selecție a produsului
        JComboBox<String> produsComboBox = new JComboBox<>(productNames.toArray(new String[0]));
        JTextField cantitateField = new JTextField();
        JTextField clientNameField = new JTextField();

        // Creare de panou pentru interfața de vânzare
        JPanel vânzarePanel = new JPanel();
        vânzarePanel.setLayout(new GridLayout(0, 2));
        vânzarePanel.add(new JLabel("Selectați produsul:"));
        vânzarePanel.add(produsComboBox);
        vânzarePanel.add(new JLabel("Introduceți cantitatea:"));
        vânzarePanel.add(cantitateField);
        vânzarePanel.add(new JLabel("Introduceți numele clientului:"));
        vânzarePanel.add(clientNameField);

        // Afișare caseta de dialog pentru vânzare
        int option = JOptionPane.showConfirmDialog(this, vânzarePanel, "Vânzare Produs",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String selectedProductName = (String) produsComboBox.getSelectedItem();
                int selectedQuantity = Integer.parseInt(cantitateField.getText());
                String clientName = clientNameField.getText();

                // Obținem ID-ul produsului selectat din baza de date
                String selectProductIdQuery = "SELECT id FROM produs WHERE denumire = ?";
                int productId;

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
                     PreparedStatement selectProductStmt = con.prepareStatement(selectProductIdQuery)) {

                    selectProductStmt.setString(1, selectedProductName);

                    try (ResultSet productRs = selectProductStmt.executeQuery()) {
                        if (productRs.next()) {
                            productId = productRs.getInt("id");
                        } else {
                            JOptionPane.showMessageDialog(this, "Eroare la obținerea ID-ului produsului.", "Eroare", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Actualizam baza de date și inseram datele în tabela orderitem
                String updateProductQuery = "UPDATE produs SET cantitate = cantitate - ? WHERE denumire = ?";
                String insertOrderItemQuery = "INSERT INTO orderitem (produs_id, cantitate, client_name) VALUES (?, ?, ?)";

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
                     PreparedStatement updateProductStmt = con.prepareStatement(updateProductQuery);
                     PreparedStatement insertOrderItemStmt = con.prepareStatement(insertOrderItemQuery, Statement.RETURN_GENERATED_KEYS)) {

                    // Actualizam cantitatea produsului
                    updateProductStmt.setInt(1, selectedQuantity);
                    updateProductStmt.setString(2, selectedProductName);
                    int rowsAffected = updateProductStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Inseram datele în tabela orderitem
                        insertOrderItemStmt.setInt(1, productId);
                        insertOrderItemStmt.setInt(2, selectedQuantity);
                        insertOrderItemStmt.setString(3, clientName);

                        int rowsInserted = insertOrderItemStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            ResultSet generatedKeys = insertOrderItemStmt.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                int orderId = generatedKeys.getInt(1);
                                System.out.println("Generated Order ID: " + orderId);
                            }
                        } else {
                            System.out.println("Failed to insert order item.");
                        }

                        // Afișați mesajul de vânzare cu succes
                        updateOutput("Produsul '" + selectedProductName + "' a fost vandut in cantitate de: " +
                        		selectedQuantity + " de catre clientul cu numele " + clientName );
                        updateReport("Product sold: " + selectedProductName + " Quantity: " + selectedQuantity + " Client: " + clientName);
                        JOptionPane.showMessageDialog(this, "Vânzare cu succes.", "Vânzare", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Vânzarea nu a putut fi realizată. Verificați cantitatea disponibilă.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Introduceți o cantitate validă.", "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la vânzarea produsului.");
            }
        }
    }

    
    private void updateReport(String message) {
        System.out.println(message);
    }

    private void displayProducts() {
        try {
            String query = "SELECT * FROM produs";
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
                 PreparedStatement stmt = con.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                ArrayList<Produs> allProducts = new ArrayList<>();

                while (rs.next()) {
                    String nume = rs.getString("denumire");
                    String categorie = rs.getString("categorie");
                    int cantitate = rs.getInt("cantitate");
                    double pret = rs.getDouble("pret");

                    Produs product = new Produs(nume, categorie, cantitate, pret);
                    allProducts.add(product);
                }

                updateOutput("Toate produsele afisate:");

                for (Produs product : allProducts) {
                    updateOutput(product.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la afișarea produselor.");
        }
    }


    private void viewOrders(JTextArea reportArea) {
        StringBuilder ordersReport = new StringBuilder("Comanda de catre clienti:\n");

        String selectQuery = "SELECT oi.client_name, p.denumire, oi.cantitate FROM orderitem oi " +
                "JOIN produs p ON oi.produs_id = p.id";


        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "");
             PreparedStatement stmt = con.prepareStatement(selectQuery);
             ResultSet rs = stmt.executeQuery()) {

            Map<String, List<OrderItem>> ordersByClient = new HashMap<>();

            while (rs.next()) {
                String clientName = rs.getString("client_name");
                String productName = rs.getString("denumire");
                int quantity = rs.getInt("cantitate");

                // Construiește raportul
                ordersReport.append("Nume client: ")
                        .append(clientName)
                        .append(", Produs: ")
                        .append(productName)
                        .append(", Cantitate vanduta: ")
                        .append(quantity)
                        .append("\n");

                // Adaugă informațiile în map-ul ordersByClient
                ordersByClient
                        .computeIfAbsent(clientName, k -> new ArrayList<>())
                        .add(new OrderItem(new Produs(0, productName, "", 0, 0.0), quantity, clientName));
            }

            // Afișează raportul în JTextArea
            reportArea.setText(ordersReport.toString());

            // Actualizează map-ul ordersByClient pentru utilizarea ulterioară
            this.ordersByClient = ordersByClient;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la obținerea rapoartelor de comenzi.");
        }
    }


    private void showLowQuantityProducts() {
        JTextField limitaField = new JTextField();
        Object[] message = {
                "Introduceti limita de cantitate:", limitaField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Vizualizare Produse cu Cantitate Scazuta", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc", "root", "")) {
                String selectQuery = "SELECT * FROM produs WHERE cantitate < ?";
                int limita = Integer.parseInt(limitaField.getText());

                try (PreparedStatement stmt = con.prepareStatement(selectQuery)) {
                    stmt.setInt(1, limita);

                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Produs> lowQuantityProducts = new ArrayList<>();

                        while (rs.next()) {
                            String nume = rs.getString("denumire");
                            String categorie = rs.getString("categorie");
                            int cantitate = rs.getInt("cantitate");
                            double pret = rs.getDouble("pret");

                            Produs product = new Produs(nume, categorie, cantitate, pret);
                            lowQuantityProducts.add(product);
                        }

                        updateOutput("Produse cu cantitate scazuta (sub " + limita + "):");
                        for (Produs product : lowQuantityProducts) {
                            updateOutput(product.toString());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la afișarea produselor cu cantitate scăzută.");
            }
        }
        showMainCard();
    }



    private void updateOutput(String message) {
    	System.out.println(message);
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
            new MainGUI();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con;
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/managerstoc","root","");
            	System.out.println("Database Connected");
        
    }

    
    
}