import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DRIP {
    public static void main(String[] args) {
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

        JLabel inflationRateLabel = new JLabel("Inflation rate (%):");
        JTextField inflationRateField = new JTextField();

        JLabel reinvestmentRateLabel = new JLabel("Reinvestment rate (%):");
        JTextField reinvestmentRateField = new JTextField();

        JLabel managementFeeLabel = new JLabel("Management fee (%):");
        JTextField managementFeeField = new JTextField();

        JLabel exchangeRateLabel = new JLabel("Currency exchange rate:");
        JTextField exchangeRateField = new JTextField();

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
        inputPanel.add(inflationRateLabel);
        inputPanel.add(inflationRateField);
        inputPanel.add(reinvestmentRateLabel);
        inputPanel.add(reinvestmentRateField);
        inputPanel.add(managementFeeLabel);
        inputPanel.add(managementFeeField);
        inputPanel.add(exchangeRateLabel);
        inputPanel.add(exchangeRateField);

        // Create buttons and result area
        JButton calculateButton = new JButton("Calculate");
        JButton templateButton = new JButton("Use Template");
        JButton lightModeButton = new JButton("Light Mode");
        JButton uploadCSVButton = new JButton("Upload CSV");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Wrap the JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create a button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(calculateButton);
        buttonPanel.add(templateButton);
        buttonPanel.add(uploadCSVButton);
        buttonPanel.add(lightModeButton);

        // Add panels to the frame
        frame.add(inputPanel, BorderLayout.NORTH); // Input panel at the top
        frame.add(scrollPane, BorderLayout.CENTER); // Result area in the center
        frame.add(buttonPanel, BorderLayout.SOUTH); // Buttons at the bottom

        // Enable Dark Mode by default
        final boolean[] isDarkMode = {true};
        applyDarkMode(frame, inputPanel, resultArea, templateButton, calculateButton, lightModeButton, uploadCSVButton);

        // Add action listener to the Light Mode button
        lightModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDarkMode[0] = !isDarkMode[0];
                if (isDarkMode[0]) {
                    applyDarkMode(frame, inputPanel, resultArea, templateButton, calculateButton, lightModeButton, uploadCSVButton);
                    lightModeButton.setText("Light Mode");
                } else {
                    applyLightMode(frame, inputPanel, resultArea, templateButton, calculateButton, lightModeButton, uploadCSVButton);
                    lightModeButton.setText("Dark Mode");
                }
            }
        });

        // Add action listener to the Template button
        templateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pre-fill fields with example values
                annualContributionField.setText("5000");
                numStocksField.setText("100");
                stockPriceField.setText("50");
                annualDividendField.setText("3");
                dividendFrequencyField.setText("4");
                holdingTimeField.setText("10");
                stockGrowthRateField.setText("5");
                dividendGrowthRateField.setText("2");
                taxRateField.setText("15");
                capitalGainsTaxRateField.setText("20");
                inflationRateField.setText("2");
                reinvestmentRateField.setText("100");
                managementFeeField.setText("1");
                exchangeRateField.setText("1");
                resultArea.setText("Template values have been loaded. You can now calculate!");
            }
        });

        // Add action listener to the Calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
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
                    double inflationRate = Double.parseDouble(inflationRateField.getText());
                    double reinvestmentRate = Double.parseDouble(reinvestmentRateField.getText());
                    double managementFee = Double.parseDouble(managementFeeField.getText());
                    double exchangeRate = Double.parseDouble(exchangeRateField.getText());

                    // Validate inputs
                    if (annualContribution < 0 || numStocks <= 0 || stockPrice <= 0 ||
                        annualDividendPercentage < 0 || dividendFrequency <= 0 || holdingTimeInYears <= 0 ||
                        stockGrowthRate < 0 || dividendGrowthRate < 0 || taxRate < 0 || capitalGainsTaxRate < 0 ||
                        inflationRate < 0 || reinvestmentRate < 0 || managementFee < 0 || exchangeRate <= 0) {
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

                    // Column widths for headers
                    final int HEADER_YEAR_WIDTH = 8;
                    final int HEADER_STOCK_PRICE_WIDTH = 15;
                    final int HEADER_PORTFOLIO_VALUE_WIDTH = 20;
                    final int HEADER_STOCK_AMOUNT_WIDTH = 15;
                    final int HEADER_INDIVIDUAL_DIV_WIDTH = 20;
                    final int HEADER_TOTAL_DIVIDENDS_WIDTH = 20;
                    final int HEADER_TAXED_INCOME_WIDTH = 15;

                    // Column widths for data rows
                    final int DATA_YEAR_WIDTH = 12;
                    final int DATA_STOCK_PRICE_WIDTH = 22;
                    final int DATA_PORTFOLIO_VALUE_WIDTH = 25;
                    final int DATA_STOCK_AMOUNT_WIDTH = 20;
                    final int DATA_INDIVIDUAL_DIV_WIDTH = 23;
                    final int DATA_TOTAL_DIVIDENDS_WIDTH = 24;
                    final int DATA_TAXED_INCOME_WIDTH = 12;

                    // Column widths for the summary data
                    final int SUMMARY_PORTFOLIO_WIDTH = 19; // Width for "Final Portfolio Value"
                    final int SUMMARY_DIVIDENDS_WIDTH = 15; // Width for "Total Dividends Earned"
                    final int SUMMARY_TAX_WIDTH = 17; // Width for "Capital Gains Tax Paid"
                    final int SUMMARY_COST_BASIS_WIDTH = 23; // Width for "Cost Basis"

                    // Prepare result display
                    StringBuilder resultBuilder = new StringBuilder();
                    resultBuilder.append("=== Yearly Investment Summary ===\n");
                    resultBuilder.append(String.format(
                        "%-" + HEADER_YEAR_WIDTH + "s %-" + HEADER_STOCK_PRICE_WIDTH + "s %-" + HEADER_PORTFOLIO_VALUE_WIDTH + "s %-" + HEADER_STOCK_AMOUNT_WIDTH + "s %-" +
                        HEADER_INDIVIDUAL_DIV_WIDTH + "s %-" + HEADER_TOTAL_DIVIDENDS_WIDTH + "s %-" + HEADER_TAXED_INCOME_WIDTH + "s\n",
                        "Year", "Stock Price", "Portfolio Value", "Stock Amount",
                        "Individual Div", "Total Dividends", "Taxed Income"));

                    // Simulate growth over the holding period
                    for (double year = 1; year <= holdingTimeInYears; year++) {
                        // Add annual contribution to the total stock value
                        totalStockValue += annualContribution;

                        // Calculate individual dividend and total dividends
                        double individualDividend = annualDividend;
                        double totalDividendsPerYear = individualDividend * numStocks;

                        // Apply a random dividend cut or increase (e.g., ±10%)
                        double randomDividendAdjustment = (Math.random() * 2 - 1) * 0.1;
                        annualDividend *= (1 + randomDividendAdjustment);

                        // Calculate dividends after tax
                        double annualDividendAfterTax = totalDividendsPerYear * (1 - taxRate / 100);

                        // Calculate the total amount available for reinvestment
                        double totalReinvestment = annualDividendAfterTax + annualContribution + leftoverCash;

                        // Deduct transaction fees (e.g., $5 per transaction)
                        double transactionFee = 5.0;
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
                        leftoverCash = totalReinvestment - (newStocks * stockPrice); // Update leftover cash
                        numStocks += newStocks; // Add the new stocks to the total stock count

                        // Update the remaining stock value after purchasing new stocks
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

                        // Adjust stock amount column width dynamically if stock amount >= 1000
                        int adjustedStockAmountWidth = (numStocks >= 1000) ? DATA_STOCK_AMOUNT_WIDTH - 1 : DATA_STOCK_AMOUNT_WIDTH;

                        // Adjust year column width dynamically if year >= 10 or year >= 100
                        int adjustedYearWidth;
                        if (year >= 100) {
                            adjustedYearWidth = DATA_YEAR_WIDTH - 2; // Reduce width by 2 for years >= 100
                        } else if (year >= 10) {
                            adjustedYearWidth = DATA_YEAR_WIDTH - 1; // Reduce width by 1 for years >= 10
                        } else {
                            adjustedYearWidth = DATA_YEAR_WIDTH; // Default width for years < 10
                        }

                        // Append yearly data with adjusted column widths
                        resultBuilder.append(String.format(
                            "%-" + adjustedYearWidth + ".0f %-" + DATA_STOCK_PRICE_WIDTH + "s %-" + DATA_PORTFOLIO_VALUE_WIDTH + "s %-" + adjustedStockAmountWidth + "d %-" +
                            DATA_INDIVIDUAL_DIV_WIDTH + "s %-" + DATA_TOTAL_DIVIDENDS_WIDTH + "s %-" + DATA_TAXED_INCOME_WIDTH + "s\n",
                            year, formatNumber(stockPrice), formatNumber(portfolioValue), (int) numStocks,
                            formatNumber(individualDividend), formatNumber(totalDividendsPerYear), formatNumber(taxedIncome)));

                        // Update total dividends
                        totalDividend += annualDividendAfterTax;
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
                    resultBuilder.append(String.format("%-25s %" + SUMMARY_PORTFOLIO_WIDTH + "s\n", "Final Portfolio Value:", formatNumber(totalStockValue)));
                    resultBuilder.append(String.format("%-25s %" + SUMMARY_DIVIDENDS_WIDTH + "s\n", "Total Dividends Earned:", formatNumber(totalDividend)));
                    resultBuilder.append(String.format("%-25s %" + SUMMARY_TAX_WIDTH + "s\n", "Capital Gains Tax Paid:", formatNumber(capitalGainsTax)));
                    resultBuilder.append(String.format("%-25s %" + SUMMARY_COST_BASIS_WIDTH + "s\n", "Cost Basis:", formatNumber(totalCostBasis)));

                    // Display the results
                    resultArea.setText(resultBuilder.toString());
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
                                inflationRateField.setText(values[10]);
                                reinvestmentRateField.setText(values[11]);
                                managementFeeField.setText(values[12]);
                                exchangeRateField.setText(values[13]);
                            } else {
                                resultArea.setText("Error: CSV file does not contain enough fields.");
                            }
                        }
                        resultArea.setText("CSV data loaded successfully. You can now calculate!");
                    } catch (IOException ex) {
                        resultArea.setText("Error: Unable to read the CSV file.");
                    }
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private static void applyDarkMode(JFrame frame, JPanel inputPanel, JTextArea resultArea, JButton templateButton, JButton calculateButton, JButton lightModeButton, JButton uploadCSVButton) {
        Color backgroundColor = Color.DARK_GRAY;
        Color textColor = Color.WHITE;
        Color buttonBackgroundColor = Color.GRAY;
        Color buttonTextColor = Color.WHITE;

        frame.getContentPane().setBackground(backgroundColor);
        inputPanel.setBackground(backgroundColor);
        resultArea.setBackground(backgroundColor);
        resultArea.setForeground(textColor);

        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setForeground(textColor);
            } else if (component instanceof JTextField) {
                component.setBackground(backgroundColor);
                component.setForeground(textColor);
            }
        }

        // Style buttons
        templateButton.setBackground(buttonBackgroundColor);
        templateButton.setForeground(buttonTextColor);
        calculateButton.setBackground(buttonBackgroundColor);
        calculateButton.setForeground(buttonTextColor);
        lightModeButton.setBackground(buttonBackgroundColor);
        lightModeButton.setForeground(buttonTextColor);
        uploadCSVButton.setBackground(buttonBackgroundColor);
        uploadCSVButton.setForeground(buttonTextColor);

        // Style button panel
        JPanel buttonPanel = (JPanel) lightModeButton.getParent();
        if (buttonPanel != null) {
            buttonPanel.setBackground(backgroundColor);
        }
    }

    private static void applyLightMode(JFrame frame, JPanel inputPanel, JTextArea resultArea, JButton templateButton, JButton calculateButton, JButton lightModeButton, JButton uploadCSVButton) {
        Color backgroundColor = Color.LIGHT_GRAY;
        Color textColor = Color.BLACK;
        Color buttonBackgroundColor = Color.WHITE;
        Color buttonTextColor = Color.BLACK;

        frame.getContentPane().setBackground(backgroundColor);
        inputPanel.setBackground(backgroundColor);
        resultArea.setBackground(Color.WHITE);
        resultArea.setForeground(textColor);

        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setForeground(textColor);
            } else if (component instanceof JTextField) {
                component.setBackground(Color.WHITE);
                component.setForeground(textColor);
            }
        }

        // Style buttons
        templateButton.setBackground(buttonBackgroundColor);
        templateButton.setForeground(buttonTextColor);
        calculateButton.setBackground(buttonBackgroundColor);
        calculateButton.setForeground(buttonTextColor);
        lightModeButton.setBackground(buttonBackgroundColor);
        lightModeButton.setForeground(buttonTextColor);
        uploadCSVButton.setBackground(buttonBackgroundColor);
        uploadCSVButton.setForeground(buttonTextColor);

        // Style button panel
        JPanel buttonPanel = (JPanel) lightModeButton.getParent();
        if (buttonPanel != null) {
            buttonPanel.setBackground(backgroundColor);
        }
    }

    // Helper method to format numbers with suffixes (e.g., k, m, b)
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
}