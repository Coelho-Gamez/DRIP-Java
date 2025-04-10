import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

public class DRIP {
    // Declare variables as class-level fields
    private static List<Double> years = new ArrayList<>();
    private static List<Double> portfolioValues = new ArrayList<>();
    private static List<Double> totalDividendsPerYearList = new ArrayList<>();
    private static List<Double> stockPrices = new ArrayList<>();
    private static List<Integer> stockAmounts = new ArrayList<>();
    private static List<Double> individualDividends = new ArrayList<>();
    private static List<Double> taxedIncomes = new ArrayList<>();
    private static double totalDividendsPerYear;

    public static void main(String[] args) {
        try {
            // Set FlatLaf Dark theme
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        // Apply custom UI properties
        UIManager.put("Button.arc", 10); // Rounded buttons
        UIManager.put("Component.arc", 10); // Rounded corners for all components
        UIManager.put("TextComponent.arc", 5); // Rounded text fields
        UIManager.put("Button.background", Color.DARK_GRAY); // Custom button background color
        UIManager.put("Button.foreground", Color.WHITE); // Custom button text color

        // Launch your application
        SwingUtilities.invokeLater(() -> {
            new DRIP().start();
        });
    }

    public void start() {
        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Create the main frame
        JFrame frame = new JFrame("Investment Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth / 2, (int) (screenHeight * 0.75)); // Occupy half the width and 75% of the height
        frame.setLayout(new BorderLayout());

        // Create input panel with GridLayout
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 columns, 10px horizontal and vertical gaps
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the grid

        // Create input fields and labels
        JLabel annualContributionLabel = new JLabel("Annual contribution ($):");
        JTextField annualContributionField = new JTextField();

        JLabel numStocksLabel = new JLabel("Number of stocks:");
        JTextField numStocksField = new JTextField();

        JLabel stockPriceLabel = new JLabel("Price per stock:");
        JTextField stockPriceField = new JTextField();

        JLabel annualDividendLabel = new JLabel("Annual dividend yield (%):");
        JTextField annualDividendField = new JTextField();

        JLabel dividendFrequencyLabel = new JLabel("Dividend payout frequency (times/year):");
        JTextField dividendFrequencyField = new JTextField();

        JLabel holdingTimeLabel = new JLabel("Holding time (years):");
        JTextField holdingTimeField = new JTextField();

        JLabel stockGrowthRateLabel = new JLabel("Annual stock price growth rate (%):");
        JTextField stockGrowthRateField = new JTextField();

        JLabel dividendGrowthRateLabel = new JLabel("Annual dividend growth rate (%):");
        JTextField dividendGrowthRateField = new JTextField();

        JLabel taxRateLabel = new JLabel("Tax rate on dividends (%):");
        JTextField taxRateField = new JTextField();

        JLabel capitalGainsTaxRateLabel = new JLabel("Capital gains tax rate (%):");
        JTextField capitalGainsTaxRateField = new JTextField();

        JLabel reinvestmentRateLabel = new JLabel("Reinvestment rate (%):");
        JTextField reinvestmentRateField = new JTextField();

        JLabel managementFeeLabel = new JLabel("Management fee (%):");
        JTextField managementFeeField = new JTextField();

        JLabel transactionFeeLabel = new JLabel("Transaction fee (%):");
        JTextField transactionFeeField = new JTextField();

        // Add components to the input panel
        inputPanel.add(annualContributionLabel);
        inputPanel.add(annualContributionField);
        inputPanel.add(numStocksLabel);
        inputPanel.add(numStocksField);
        inputPanel.add(stockPriceLabel);
        inputPanel.add(stockPriceField);
        inputPanel.add(annualDividendLabel);
        inputPanel.add(annualDividendField);
        inputPanel.add(dividendFrequencyLabel);
        inputPanel.add(dividendFrequencyField);
        inputPanel.add(holdingTimeLabel);
        inputPanel.add(holdingTimeField);
        inputPanel.add(stockGrowthRateLabel);
        inputPanel.add(stockGrowthRateField);
        inputPanel.add(dividendGrowthRateLabel);
        inputPanel.add(dividendGrowthRateField);
        inputPanel.add(taxRateLabel);
        inputPanel.add(taxRateField);
        inputPanel.add(capitalGainsTaxRateLabel);
        inputPanel.add(capitalGainsTaxRateField);
        inputPanel.add(reinvestmentRateLabel);
        inputPanel.add(reinvestmentRateField);
        inputPanel.add(managementFeeLabel);
        inputPanel.add(managementFeeField);
        inputPanel.add(transactionFeeLabel);
        inputPanel.add(transactionFeeField);

        // Create buttons and result area
        JButton calculateButton = new JButton("Calculate");
        JButton templateButton = new JButton("Use Template");
        JButton lightModeButton = new JButton("Light Mode");
        JButton uploadCSVButton = new JButton("Upload CSV");
        JButton exportCSVButton = new JButton("Export to CSV");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Wrap the JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create a JTable for the yearly summary
        String[] columnNames = {
            "Year", "Stock Price ($)", "Portfolio Value ($)", "Stock Amount",
            "Individual Div ($)", "Total Dividends ($)", "Taxed Income ($)"
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        resultTable.setEnabled(false); // Make the table read-only
        resultTable.setFillsViewportHeight(true);

        // Wrap the JTable in a JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create the Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            annualContributionField.setText("");
            numStocksField.setText("");
            stockPriceField.setText("");
            annualDividendField.setText("");
            dividendFrequencyField.setText("");
            holdingTimeField.setText("");
            stockGrowthRateField.setText("");
            dividendGrowthRateField.setText("");
            taxRateField.setText("");
            capitalGainsTaxRateField.setText("");
            reinvestmentRateField.setText("");
            managementFeeField.setText("");
            transactionFeeField.setText("");
            resultArea.setText("");
        });

        // Create a button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(calculateButton);
        buttonPanel.add(templateButton);
        buttonPanel.add(uploadCSVButton);
        buttonPanel.add(exportCSVButton);
        buttonPanel.add(lightModeButton);
        buttonPanel.add(resetButton); // Add the Reset button here

        // Create the portfolio graph
        ChartPanel portfolioGraphPanel = createChart(
            years, 
            portfolioValues, 
            "Portfolio Value Over Time", 
            "Portfolio Value ($)", 
            "Portfolio Value"
        );

        // Create the dividends graph
        ChartPanel dividendsGraphPanel = createChart(
            new ArrayList<>(), 
            new ArrayList<>(), 
            "Total Dividends Over Time", 
            "Total Dividends ($)", 
            "Total Dividends"
        );

        // Create the graph window
        JFrame graphFrame = createGraphWindow(portfolioGraphPanel, dividendsGraphPanel);

        // Create a panel for final totals
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns, 10px gaps

        // Add labels and placeholders for final totals
        totalsPanel.add(new JLabel("Final Portfolio Value ($):"));
        JLabel finalPortfolioValueLabel = new JLabel();
        totalsPanel.add(finalPortfolioValueLabel);

        totalsPanel.add(new JLabel("Total Dividends Earned ($):"));
        JLabel totalDividendsLabel = new JLabel();
        totalsPanel.add(totalDividendsLabel);

        totalsPanel.add(new JLabel("Capital Gains Tax Paid ($):"));
        JLabel capitalGainsTaxLabel = new JLabel();
        totalsPanel.add(capitalGainsTaxLabel);

        totalsPanel.add(new JLabel("Cost Basis ($):"));
        JLabel costBasisLabel = new JLabel();
        totalsPanel.add(costBasisLabel);

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH); // Input panel at the top
        frame.add(tableScrollPane, BorderLayout.CENTER); // Table in the center
        frame.add(buttonPanel, BorderLayout.SOUTH); // Button panel at the bottom

        // Enable Dark Mode by default
        final boolean[] isDarkMode = {true};
        applyDarkMode(frame);

        // Light/Dark Mode button action listener
        lightModeButton.addActionListener(e -> {
            if (isDarkMode[0]) {
                applyLightMode(frame);
                isDarkMode[0] = false;
                lightModeButton.setText("Dark Mode");

                // Apply Light Mode to the charts
                applyLightModeToChart(portfolioGraphPanel.getChart());
                applyLightModeToChart(dividendsGraphPanel.getChart());

                // Update button colors to gray
                for (Component component : buttonPanel.getComponents()) {
                    if (component instanceof JButton) {
                        component.setBackground(Color.LIGHT_GRAY);
                        component.setForeground(Color.BLACK);
                    }
                }
            } else {
                applyDarkMode(frame);
                isDarkMode[0] = true;
                lightModeButton.setText("Light Mode");

                // Apply Dark Mode to the charts
                applyDarkModeToChart(portfolioGraphPanel.getChart());
                applyDarkModeToChart(dividendsGraphPanel.getChart());

                // Update button colors to dark gray
                for (Component component : buttonPanel.getComponents()) {
                    if (component instanceof JButton) {
                        component.setBackground(Color.DARK_GRAY);
                        component.setForeground(Color.WHITE);
                    }
                }
            }

            // Refresh the UI
            SwingUtilities.updateComponentTreeUI(frame);
            portfolioGraphPanel.repaint();
            dividendsGraphPanel.repaint();
        });

        // Add action listener to the Template button
        templateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pre-fill fields with example values
                annualContributionField.setText("100");
                numStocksField.setText("20");
                stockPriceField.setText("45");
                annualDividendField.setText("19");
                dividendFrequencyField.setText("1");
                holdingTimeField.setText("10");
                stockGrowthRateField.setText("5");
                dividendGrowthRateField.setText("1");
                taxRateField.setText("15");
                capitalGainsTaxRateField.setText("20");
                reinvestmentRateField.setText("100");
                managementFeeField.setText("1");
                transactionFeeField.setText("5");
                resultArea.setText("Template values have been loaded. You can now calculate!");
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // List of input fields and their corresponding names
                    JTextField[] fields = {
                        annualContributionField, numStocksField, stockPriceField, annualDividendField,
                        dividendFrequencyField, holdingTimeField, stockGrowthRateField, dividendGrowthRateField,
                        taxRateField, capitalGainsTaxRateField, reinvestmentRateField,
                        managementFeeField, transactionFeeField
                    };
                    String[] fieldNames = {
                        "Annual contribution", "Number of stocks", "Price per stock", "Annual dividend yield",
                        "Dividend payout frequency", "Holding time", "Annual stock price growth rate",
                        "Annual dividend growth rate", "Tax rate on dividends", "Capital gains tax rate",
                         "Reinvestment rate", "Management fee", "Transaction fee"
                    };

                    // Validate inputs
                    if (!validateInputs(fields, fieldNames, resultArea)) {
                        return; // Stop execution if validation fails
                    }

                    // Clear the table for new data
                    tableModel.setRowCount(0);

                    // Read input values
                    double annualContribution = Double.parseDouble(annualContributionField.getText());
                    double numStocks = Double.parseDouble(numStocksField.getText());
                    double stockPrice = Double.parseDouble(stockPriceField.getText());
                    double annualDividendPercentage = Double.parseDouble(annualDividendField.getText());
                    double dividendFrequency = Double.parseDouble(dividendFrequencyField.getText());
                    double holdingTimeInYears = Double.parseDouble(holdingTimeField.getText());
                    double stockGrowthRate = Double.parseDouble(stockGrowthRateField.getText());
                    double dividendGrowthRate = Double.parseDouble(dividendGrowthRateField.getText());
                    double taxRate = Double.parseDouble(taxRateField.getText());
                    double capitalGainsTaxRate = Double.parseDouble(capitalGainsTaxRateField.getText());
                    double reinvestmentRate = Double.parseDouble(reinvestmentRateField.getText());
                    double managementFee = Double.parseDouble(managementFeeField.getText());
                    double transactionFee = Double.parseDouble(transactionFeeField.getText());

                    // Additional validation for numeric values
                    if (annualContribution < 0 || numStocks <= 0 || stockPrice <= 0 ||
                        annualDividendPercentage < 0 || dividendFrequency <= 0 || holdingTimeInYears <= 0 ||
                        stockGrowthRate < 0 || dividendGrowthRate < 0 || taxRate < 0 || capitalGainsTaxRate < 0 ||
                        reinvestmentRate < 0 || managementFee < 0 || transactionFee < 0) {
                        resultArea.setText("Error: Please enter valid positive values for all inputs.");
                        return;
                    }

                    // Initialize variables for calculations
                    double totalDividend = 0;
                    double totalStockValue = numStocks * stockPrice;
                    double annualDividend = (annualDividendPercentage / 100) * stockPrice;
                    double leftoverCash = 0;

                    // Initialize lists to track stock purchases
                    List<Double> stockPurchasePrices = new ArrayList<>();
                    List<Integer> stockPurchaseCounts = new ArrayList<>();

                    // Prepare result display
                    StringBuilder resultBuilder = new StringBuilder();
                    resultBuilder.append("=== Yearly Investment Summary ===\n");
                    resultBuilder.append(String.format(
                        "%-8s %-15s %-20s %-15s %-20s %-20s %-15s\n",
                        "Year", "Stock Price", "Portfolio Value", "Stock Amount",
                        "Individual Div", "Total Dividends", "Taxed Income"));

                    // Simulate growth over the holding period
                    for (double year = 1; year <= holdingTimeInYears; year++) {
                        // Add annual contribution to the total stock value
                        totalStockValue += annualContribution;

                        // Calculate individual dividend and total dividends
                        double individualDividend = annualDividend;
                        totalDividendsPerYear = individualDividend * numStocks;

                        // Calculate dividends after tax
                        double annualDividendAfterTax = totalDividendsPerYear * (1 - taxRate / 100);

                        // Calculate the total amount available for reinvestment
                        double totalReinvestment = annualDividendAfterTax + annualContribution + leftoverCash;

                        // Deduct transaction fees (e.g., $5 per transaction)
                        if (totalReinvestment > transactionFee) {
                            totalReinvestment -= transactionFee;
                        } else {
                            transactionFee = totalReinvestment;
                            totalReinvestment = 0;
                        }

                        // Calculate the number of new stocks that can be purchased
                        int newStocks = (int) Math.floor(totalReinvestment / stockPrice); // Only whole stocks can be purchased
                        if (newStocks > 0) {
                            stockPurchasePrices.add(stockPrice); // Record the price of the new stocks
                            stockPurchaseCounts.add(newStocks); // Record the number of new stocks
                        }

                        // Update the remaining stock value after purchasing new stocks
                        leftoverCash = totalReinvestment - (newStocks * stockPrice); // Update leftover cash
                        numStocks += newStocks; // Add the new stocks to the total stock count
                        totalStockValue = numStocks * stockPrice;

                        // Deduct management fee from the total stock value
                        totalStockValue *= (1 - managementFee / 100);

                        // Apply market volatility to stock price (e.g., ±2%)
                        double randomStockGrowth = (Math.random() * 2 - 1) * 0.02;
                        stockPrice *= (1 + stockGrowthRate / 100 + randomStockGrowth);

                        // Grow dividend using compound growth
                        annualDividend *= (1 + dividendGrowthRate / 100);

                        // Calculate portfolio value
                        double portfolioValue = numStocks * stockPrice;

                        // Calculate taxed income (dividends after tax)
                        double taxedIncome = annualDividendAfterTax;

                        // Append yearly data with adjusted column widths
                        resultBuilder.append(String.format(
                            "%-8.0f %-15s %-20s %-15d %-20s %-20s %-15s\n",
                            year, formatNumber(stockPrice), formatNumber(portfolioValue), (int) numStocks,
                            formatNumber(individualDividend), formatNumber(totalDividendsPerYear), formatNumber(taxedIncome)
                        ));

                        // Add data for graph and tracking (only once per year)
                        years.add(year);
                        portfolioValues.add(portfolioValue);

                        totalDividendsPerYearList.add(totalDividendsPerYear);
                        stockPrices.add(stockPrice);
                        stockAmounts.add((int) numStocks);
                        individualDividends.add(individualDividend);
                        taxedIncomes.add(taxedIncome);

                        // Add data to the table
                        tableModel.addRow(new Object[]{
                            year,
                            formatNumber(stockPrice),
                            formatNumber(portfolioValue),
                            (int) numStocks,
                            formatNumber(individualDividend),
                            formatNumber(totalDividendsPerYear),
                            formatNumber(taxedIncome)
                        });
                    }

                    // Calculate capital gains tax
                    double totalCostBasis = 0; // Total cost basis of all stocks
                    for (int i = 0; i < stockPurchasePrices.size(); i++) {
                        totalCostBasis += stockPurchasePrices.get(i) * stockPurchaseCounts.get(i);
                    }
                    double capitalGains = totalStockValue - totalCostBasis; // Gains from stock price growth
                    double capitalGainsTax = capitalGains > 0 ? capitalGains * (capitalGainsTaxRate / 100) : 0; // Apply tax only on positive gains

                    // Deduct capital gains tax from the final portfolio value
                    totalStockValue -= capitalGainsTax;

                    // Append final totals to the summary
                    resultBuilder.append("\n=== Final Totals ===\n");
                    resultBuilder.append(String.format("%-25s %19s\n", "Final Portfolio Value:", formatNumber(totalStockValue)));
                    resultBuilder.append(String.format("%-25s %15s\n", "Total Dividends Earned:", formatNumber(totalDividend)));
                    resultBuilder.append(String.format("%-25s %17s\n", "Capital Gains Tax Paid:", formatNumber(capitalGainsTax)));
                    resultBuilder.append(String.format("%-25s %23s\n", "Cost Basis:", formatNumber(totalCostBasis)));

                    // Update totals panel labels
                    finalPortfolioValueLabel.setText(formatNumber(totalStockValue));
                    totalDividendsLabel.setText(formatNumber(totalDividend));
                    capitalGainsTaxLabel.setText(formatNumber(capitalGainsTax));
                    costBasisLabel.setText(formatNumber(totalCostBasis));

                    // Display the results
                    resultArea.setText(resultBuilder.toString());

                    // Update the portfolio graph
                    DefaultCategoryDataset portfolioDataset = (DefaultCategoryDataset) portfolioGraphPanel.getChart().getCategoryPlot().getDataset();
                    portfolioDataset.clear();
                    for (int i = 0; i < years.size(); i++) {
                        portfolioDataset.addValue(portfolioValues.get(i), "Portfolio Value", years.get(i));
                    }

                    // Update the dividends graph
                    DefaultCategoryDataset dividendsDataset = (DefaultCategoryDataset) dividendsGraphPanel.getChart().getCategoryPlot().getDataset();
                    dividendsDataset.clear();
                    for (int i = 0; i < years.size(); i++) {
                        dividendsDataset.addValue(totalDividendsPerYearList.get(i), "Total Dividends", years.get(i));
                    }

                    // Apply dark mode to the charts if enabled
                    if (isDarkMode[0]) {
                        applyDarkModeToChart(portfolioGraphPanel.getChart());
                        applyDarkModeToChart(dividendsGraphPanel.getChart());
                    } else {
                        applyLightModeToChart(portfolioGraphPanel.getChart());
                        applyLightModeToChart(dividendsGraphPanel.getChart());
                    }

                    // Refresh the graphs
                    portfolioGraphPanel.repaint();
                    dividendsGraphPanel.repaint();

                    // Show the graph window
                    graphFrame.setVisible(true);

                } catch (NumberFormatException ex) {
                    resultArea.setText("Error: Please enter numeric values for all inputs.");
                }
            }
        });

        // Add action listener to the Upload CSV button
        uploadCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(","); // Assuming CSV is comma-separated
                            if (values.length >= 14) { // Ensure all fields are present
                                annualContributionField.setText(values[0]);
                                numStocksField.setText(values[1]);
                                stockPriceField.setText(values[2]);
                                annualDividendField.setText(values[3]);
                                dividendFrequencyField.setText(values[4]);
                                holdingTimeField.setText(values[5]);
                                stockGrowthRateField.setText(values[6]);
                                dividendGrowthRateField.setText(values[7]);
                                taxRateField.setText(values[8]);
                                capitalGainsTaxRateField.setText(values[9]);
                                reinvestmentRateField.setText(values[11]);
                                managementFeeField.setText(values[12]);
                                transactionFeeField.setText(values[13]);
                                resultArea.setText("CSV data loaded successfully. You can now calculate!");
                            } else {
                                resultArea.setText("Error: CSV file does not contain enough fields.");
                            }
                        }
                    } catch (IOException ex) {
                        resultArea.setText("Error: Unable to read the CSV file.");
                    }
                }
            }
        });

        // Add action listener to the Export CSV button
        exportCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save CSV File");
                int userSelection = fileChooser.showSaveDialog(frame);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    // Ensure the file has a .csv extension
                    if (!fileToSave.getName().endsWith(".csv")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
                    }

                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        // Write the CSV header
                        writer.append("Year,Stock Price,Portfolio Value,Stock Amount,Individual Dividend,Total Dividends,Taxed Income\n");

                        // Write the yearly data (no duplicates)
                        for (int i = 0; i < years.size(); i++) {
                            writer.append(String.format(
                                "%.0f,%.2f,%.2f,%d,%.2f,%.2f,%.2f\n",
                                years.get(i),
                                stockPrices.get(i),
                                portfolioValues.get(i),
                                stockAmounts.get(i),
                                individualDividends.get(i),
                                totalDividendsPerYearList.get(i),
                                taxedIncomes.get(i)
                            ));
                        }

                        // Notify the user that the file was saved successfully
                        JOptionPane.showMessageDialog(frame, "CSV file saved successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error saving CSV file: " + ex.getMessage(), "Export Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    // Simplified Dark Mode and Light Mode methods
    private static void applyDarkMode(JFrame frame) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.updateComponentTreeUI(frame); // Refresh the UI
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private static void applyLightMode(JFrame frame) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(frame); // Refresh the UI
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private static void applyDarkModeToChart(JFreeChart chart) {
        // Set static colors for dark mode
        chart.setBackgroundPaint(Color.DARK_GRAY);
        chart.getTitle().setPaint(Color.WHITE);

        if (chart.getPlot() instanceof org.jfree.chart.plot.CategoryPlot) {
            org.jfree.chart.plot.CategoryPlot plot = (org.jfree.chart.plot.CategoryPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.BLACK); // Static dark background
            plot.setOutlinePaint(Color.WHITE); // Static outline color
            plot.getDomainAxis().setLabelPaint(Color.WHITE); // Static axis label color
            plot.getDomainAxis().setTickLabelPaint(Color.WHITE); // Static tick label color
            plot.getRangeAxis().setLabelPaint(Color.WHITE); // Static range axis label color
            plot.getRangeAxis().setTickLabelPaint(Color.WHITE); // Static range tick label color
            plot.setDomainGridlinePaint(Color.GRAY); // Static gridline color
            plot.setRangeGridlinePaint(Color.GRAY); // Static gridline color

            // Set static renderer colors
            org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = new org.jfree.chart.renderer.category.LineAndShapeRenderer(true, true);
            renderer.setSeriesPaint(0, Color.CYAN); // Static line color for series 0
            renderer.setBaseToolTipGenerator(new org.jfree.chart.labels.StandardCategoryToolTipGenerator(
                "{0}: Year {1}, Value {2}", java.text.NumberFormat.getInstance()
            ));
            plot.setRenderer(renderer);
        }
    }

    private static void applyLightModeToChart(JFreeChart chart) {
        // Set static colors for light mode
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setPaint(Color.BLACK);

        if (chart.getPlot() instanceof org.jfree.chart.plot.CategoryPlot) {
            org.jfree.chart.plot.CategoryPlot plot = (org.jfree.chart.plot.CategoryPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.LIGHT_GRAY); // Static light background
            plot.setOutlinePaint(Color.BLACK); // Static outline color
            plot.getDomainAxis().setLabelPaint(Color.BLACK); // Static axis label color
            plot.getDomainAxis().setTickLabelPaint(Color.BLACK); // Static tick label color
            plot.getRangeAxis().setLabelPaint(Color.BLACK); // Static range axis label color
            plot.getRangeAxis().setTickLabelPaint(Color.BLACK); // Static range tick label color
            plot.setDomainGridlinePaint(Color.DARK_GRAY); // Static gridline color
            plot.setRangeGridlinePaint(Color.DARK_GRAY); // Static gridline color

            // Set static renderer colors
            org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = new org.jfree.chart.renderer.category.LineAndShapeRenderer(true, true);
            renderer.setSeriesPaint(0, Color.BLUE); // Static line color for series 0
            renderer.setBaseToolTipGenerator(new org.jfree.chart.labels.StandardCategoryToolTipGenerator(
                "{0}: Year {1}, Value {2}", java.text.NumberFormat.getInstance()
            ));
            plot.setRenderer(renderer);
        }
    }

    private static String formatNumber(double value) {
        if (value < 1000) {
            return String.format("%.4g", value); // Use 4 significant figures for small numbers
        }
        int exp = (int) (Math.log10(value) / 3); // Determine the exponent for thousands (k, m, b, etc.)
        char[] suffixes = {'k', 'm', 'b', 't'}; // Suffixes for thousands, millions, billions, trillions

        // Ensure the exponent does not exceed the bounds of the suffixes array
        if (exp >= suffixes.length) {
            exp = suffixes.length - 1; // Cap at the largest suffix
        }

        double scaledValue = value / Math.pow(1000, exp);
        return String.format("%.3g%s", scaledValue, suffixes[exp - 1]); // Use 3 significant figures for scaled values
    }

    private static ChartPanel createChart(List<Double> years, List<Double> values, String chartTitle, String yAxisLabel, String datasetLabel) {
        // Create the dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < years.size(); i++) {
            dataset.addValue(values.get(i), datasetLabel, years.get(i));
        }

        // Create the line chart
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,  // Chart title
                "Year",      // X-axis label
                yAxisLabel,  // Y-axis label
                dataset      // Data
        );

        // Customize the chart
        lineChart.setBackgroundPaint(Color.WHITE);
        lineChart.getTitle().setPaint(Color.DARK_GRAY);

        // Customize the plot
        org.jfree.chart.plot.CategoryPlot plot = (org.jfree.chart.plot.CategoryPlot) lineChart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);

        // Customize the renderer
        org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = new org.jfree.chart.renderer.category.LineAndShapeRenderer(true, true);
        renderer.setBaseToolTipGenerator(new org.jfree.chart.labels.StandardCategoryToolTipGenerator(
            "{0}: Year {1}, Value {2}", java.text.NumberFormat.getInstance()
        ));
        plot.setRenderer(renderer);

        // Customize the axes
        org.jfree.chart.axis.CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryMargin(0.1);
        xAxis.setLabelPaint(Color.DARK_GRAY);
        xAxis.setTickLabelPaint(Color.DARK_GRAY);

        org.jfree.chart.axis.NumberAxis yAxis = (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        yAxis.setLabelPaint(Color.DARK_GRAY);
        yAxis.setTickLabelPaint(Color.DARK_GRAY);

        // Create the chart panel
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(500, 350));
        chartPanel.setMouseWheelEnabled(true);

        // Explicitly enable tooltips
        chartPanel.setDisplayToolTips(true);

        return chartPanel;
    }

    private static JFrame createGraphWindow(ChartPanel portfolioGraphPanel, ChartPanel dividendsGraphPanel) {
        // Create a new frame for the graphs
        JFrame graphFrame = new JFrame("Investment Graphs");
        graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        graphFrame.setLayout(new GridLayout(1, 2, 10, 10)); // 1 row, 2 columns with spacing
        graphFrame.add(portfolioGraphPanel);
        graphFrame.add(dividendsGraphPanel);
        graphFrame.setSize(1100, 400); // Adjust size to fit both graphs
        graphFrame.setLocationRelativeTo(null); // Center the graph window
        return graphFrame;
    }

    private static boolean validateInputs(JTextField[] fields, String[] fieldNames, JTextArea resultArea) {
        for (int i = 0; i < fields.length; i++) {
            if (isFieldEmpty(fields[i])) {
                showError(resultArea, fieldNames[i] + " is required.");
                return false;
            }
        }
        return true;
    }

    private static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    private static void showError(JTextArea resultArea, String message) {
        resultArea.setText("Error: " + message);
    }
}