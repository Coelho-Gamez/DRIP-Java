import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DRIP {
    public static void main(String[] args) {
        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Create the main frame
        JFrame frame = new JFrame("Investment Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth / 2, (int) (screenHeight * 0.85)); // Occupy half the width and 85% of the height
        frame.setLayout(new BorderLayout());

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(15, 2, 10, 10)); // Adjusted for fewer parameters

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

        JButton calculateButton = new JButton("Calculate");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Wrap the JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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
        inputPanel.add(calculateButton);

        // Add panels to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a Dark Mode button
        JButton darkModeButton = new JButton("Dark Mode");
        frame.add(darkModeButton, BorderLayout.SOUTH);

        // Add a "Use Template" button
        JButton templateButton = new JButton("Use Template");

        // Add action listener for the "Use Template" button
        templateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set predefined values for testing
                annualContributionField.setText("300"); // Annual contribution in dollars
                numStocksField.setText("20");           // Number of stocks
                stockPriceField.setText("45");          // Price per stock
                annualDividendField.setText("15");      // Annual dividend yield in percentage
                dividendFrequencyField.setText("1");    // Dividend payout frequency (times/year)
                holdingTimeField.setText("12");         // Holding time in years
                stockGrowthRateField.setText("3");      // Annual stock price growth rate in percentage
                dividendGrowthRateField.setText("2");   // Annual dividend growth rate in percentage
                taxRateField.setText("15");             // Tax rate on dividends in percentage
                capitalGainsTaxRateField.setText("20"); // Capital gains tax rate in percentage
                inflationRateField.setText("8");        // Inflation rate in percentage
                reinvestmentRateField.setText("100");   // Reinvestment rate in percentage
                managementFeeField.setText("1");        // Management fee in percentage
                exchangeRateField.setText("1");         // Currency exchange rate
            }
        });

        // Add the "Use Template" button to the frame
        frame.add(templateButton, BorderLayout.EAST);

        // Set Dark Mode as the default
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        inputPanel.setBackground(Color.DARK_GRAY);
        resultArea.setBackground(Color.BLACK);
        resultArea.setForeground(Color.WHITE);

        // Set initial colors for labels, text fields, and buttons
        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setForeground(Color.WHITE); // Set label text to white
            } else if (component instanceof JTextField) {
                component.setBackground(Color.BLACK); // Set text field background to black
                component.setForeground(Color.WHITE); // Set text field text to white
            } else if (component instanceof JButton) {
                component.setBackground(Color.DARK_GRAY); // Set button background to dark gray
                component.setForeground(Color.WHITE); // Set button text to white
            }
        }

        // Set "Use Template" button colors for Dark Mode
        templateButton.setBackground(Color.DARK_GRAY); // Dark gray background
        templateButton.setForeground(Color.WHITE);     // White text

        // Remove the Dark Mode button logic
        darkModeButton.setVisible(true); // Hide the Dark Mode button

        // Create a Light Mode button
        JButton lightModeButton = new JButton("Light Mode");

        // Set the Light Mode button to be black on startup
        lightModeButton.setBackground(Color.BLACK); // Black background
        lightModeButton.setForeground(Color.WHITE); // White text

        // Add action listener for the Light Mode button
        lightModeButton.addActionListener(new ActionListener() {
            private boolean isDarkMode = true; // Track the current mode

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDarkMode) {
                    // Switch to Light Mode
                    frame.getContentPane().setBackground(Color.LIGHT_GRAY);
                    inputPanel.setBackground(Color.LIGHT_GRAY);
                    resultArea.setBackground(Color.WHITE);
                    resultArea.setForeground(Color.BLACK);

                    // Update colors for labels, text fields, and buttons
                    for (Component component : inputPanel.getComponents()) {
                        if (component instanceof JLabel) {
                            component.setForeground(Color.BLACK); // Set label text to black
                        } else if (component instanceof JTextField) {
                            component.setBackground(Color.WHITE); // Set text field background to white
                            component.setForeground(Color.BLACK); // Set text field text to black
                        } else if (component instanceof JButton) {
                            component.setBackground(Color.LIGHT_GRAY); // Set button background to light gray
                            component.setForeground(Color.BLACK); // Set button text to black
                        }
                    }

                    // Update the "Use Template" button colors
                    templateButton.setBackground(Color.LIGHT_GRAY); // Light gray background
                    templateButton.setForeground(Color.BLACK);      // Black text

                    // Update the Light Mode button to appear as a "Dark Mode" button
                    lightModeButton.setBackground(Color.WHITE); // White background
                    lightModeButton.setForeground(Color.BLACK); // Black text

                    // Update the button text
                    lightModeButton.setText("Dark Mode");
                } else {
                    // Switch to Dark Mode
                    frame.getContentPane().setBackground(Color.DARK_GRAY);
                    inputPanel.setBackground(Color.DARK_GRAY);
                    resultArea.setBackground(Color.BLACK);
                    resultArea.setForeground(Color.WHITE);

                    // Update colors for labels, text fields, and buttons
                    for (Component component : inputPanel.getComponents()) {
                        if (component instanceof JLabel) {
                            component.setForeground(Color.WHITE); // Set label text to white
                        } else if (component instanceof JTextField) {
                            component.setBackground(Color.BLACK); // Set text field background to black
                            component.setForeground(Color.WHITE); // Set text field text to white
                        } else if (component instanceof JButton) {
                            component.setBackground(Color.DARK_GRAY); // Set button background to dark gray
                            component.setForeground(Color.WHITE); // Set button text to white
                        }
                    }

                    // Update the "Use Template" button colors
                    templateButton.setBackground(Color.DARK_GRAY); // Dark gray background
                    templateButton.setForeground(Color.WHITE);     // White text

                    // Update the Light Mode button to appear as a "Light Mode" button
                    lightModeButton.setBackground(Color.BLACK); // Black background
                    lightModeButton.setForeground(Color.WHITE); // White text

                    // Update the button text
                    lightModeButton.setText("Light Mode");
                }

                // Toggle the mode
                isDarkMode = !isDarkMode;
            }
        });

        // Add the Light Mode button to the frame
        frame.add(lightModeButton, BorderLayout.SOUTH);

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
                    int dividendFrequency = Integer.parseInt(dividendFrequencyField.getText());
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

                    // Calculate total investment in stocks
                    double totalInvestment = numStocks * stockPrice;

                    // Initialize variables for calculations
                    double totalDividend = 0;
                    double totalStockValue = totalInvestment;
                    double annualDividend = (annualDividendPercentage / 100) * stockPrice;

                    // Define column widths for headers
                    final int HEADER_YEAR_WIDTH = 8;
                    final int HEADER_STOCK_PRICE_WIDTH = 15;
                    final int HEADER_PORTFOLIO_VALUE_WIDTH = 21;
                    final int HEADER_STOCK_AMOUNT_WIDTH = 17;
                    final int HEADER_INDIVIDUAL_DIV_WIDTH = 20;
                    final int HEADER_TOTAL_DIVIDENDS_WIDTH = 22;
                    final int HEADER_TAXED_INCOME_WIDTH = 20;

                    // Define column widths for numerical values
                    final int DATA_YEAR_WIDTH = 6;
                    final int DATA_STOCK_PRICE_WIDTH = 17;
                    final int DATA_PORTFOLIO_VALUE_WIDTH = 21;
                    final int DATA_STOCK_AMOUNT_WIDTH = 23;
                    final int DATA_INDIVIDUAL_DIV_WIDTH = 22;
                    final int DATA_TOTAL_DIVIDENDS_WIDTH = 24;
                    final int DATA_TAXED_INCOME_WIDTH = 26;

                    // Define column headers as constants
                    final String HEADER_YEAR = "Year";
                    final String HEADER_STOCK_PRICE = "Stock Price";
                    final String HEADER_PORTFOLIO_VALUE = "Portfolio Value";
                    final String HEADER_STOCK_AMOUNT = "Stock Amount";
                    final String HEADER_INDIVIDUAL_DIV = "Individual Div";
                    final String HEADER_TOTAL_DIVIDENDS = "Total Dividends";
                    final String HEADER_TAXED_INCOME = "Taxed Income";

                    // Prepare result display
                    StringBuilder resultBuilder = new StringBuilder();
                    resultBuilder.append("=== Yearly Investment Summary ===\n");

                    // Append headers using header widths
                    resultBuilder.append(String.format(
                        "%-" + HEADER_YEAR_WIDTH + "s %" + HEADER_STOCK_PRICE_WIDTH + "s %" + HEADER_PORTFOLIO_VALUE_WIDTH + "s %" + HEADER_STOCK_AMOUNT_WIDTH + "s %" +
                        HEADER_INDIVIDUAL_DIV_WIDTH + "s %" + HEADER_TOTAL_DIVIDENDS_WIDTH + "s %" + HEADER_TAXED_INCOME_WIDTH + "s\n",
                        HEADER_YEAR, HEADER_STOCK_PRICE, HEADER_PORTFOLIO_VALUE, HEADER_STOCK_AMOUNT,
                        HEADER_INDIVIDUAL_DIV, HEADER_TOTAL_DIVIDENDS, HEADER_TAXED_INCOME));

                    // Initialize leftover cash
                    double leftoverCash = 0;

                    // Simulate growth over the holding period
                    for (int year = 1; year <= holdingTimeInYears; year++) {
                        // Add annual contribution to the total stock value
                        totalStockValue += annualContribution;

                        // Calculate individual dividend and total dividends
                        double individualDividend = annualDividend;
                        double totalDividendsPerYear = individualDividend * numStocks;

                        // Apply a random dividend cut or increase (e.g., ±10%)
                        double randomDividendAdjustment = (Math.random() * 2 - 1) * 0.1; // Random adjustment between -10% and +10%
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
                            transactionFee = totalReinvestment; // If not enough for fees, use all available cash
                            totalReinvestment = 0;
                        }

                        // Calculate the number of new stocks that can be purchased
                        int newStocks = (int) (totalReinvestment / stockPrice); // Only whole stocks can be purchased
                        leftoverCash = totalReinvestment - (newStocks * stockPrice); // Update leftover cash
                        numStocks += newStocks; // Add the new stocks to the total stock count

                        // Update the remaining stock value after purchasing new stocks
                        totalStockValue = numStocks * stockPrice;

                        // Deduct management fee from the total stock value
                        totalStockValue *= (1 - managementFee / 100);

                        // Apply market volatility to stock price (e.g., ±2%)
                        double randomStockGrowth = (Math.random() * 2 - 1) * 0.02; // Random adjustment between -2% and +2%
                        stockPrice *= (1 + stockGrowthRate / 100 + randomStockGrowth);

                        // Grow dividend using compound growth
                        annualDividend *= (1 + dividendGrowthRate / 100);

                        // Calculate portfolio value
                        double portfolioValue = numStocks * stockPrice;

                        // Calculate taxed income (dividends after tax)
                        double taxedIncome = annualDividendAfterTax;

                        // Adjust data widths dynamically based on the number of digits in the year
                        int adjustedYearWidth = DATA_YEAR_WIDTH - (int) Math.log10(year);
                        int adjustedIndividualDivWidth = year >= 10 ? DATA_INDIVIDUAL_DIV_WIDTH - 1 : DATA_INDIVIDUAL_DIV_WIDTH;

                        // Append yearly data using adjusted widths
                        resultBuilder.append(String.format(
                            "%-" + adjustedYearWidth + "d %" + DATA_STOCK_PRICE_WIDTH + "s %" + DATA_PORTFOLIO_VALUE_WIDTH + "s %" + DATA_STOCK_AMOUNT_WIDTH + "d %" +
                            adjustedIndividualDivWidth + "s %" + DATA_TOTAL_DIVIDENDS_WIDTH + "s %" + DATA_TAXED_INCOME_WIDTH + "s\n",
                            year, formatNumber(stockPrice), formatNumber(portfolioValue), numStocks,
                            formatNumber(individualDividend), formatNumber(totalDividendsPerYear), formatNumber(taxedIncome)));

                        // Update total dividends
                        totalDividend += annualDividendAfterTax;
                    }

                    // Append final totals to the summary
                    resultBuilder.append("\n=== Final Totals ===\n");
                    resultBuilder.append(String.format("%-25s %s\n", "Final Portfolio Value:", formatNumber(totalStockValue)));
                    resultBuilder.append(String.format("%-25s %s\n", "Total Dividends Earned:", formatNumber(totalDividend)));

                    // Display the results
                    resultArea.setText(resultBuilder.toString());
                } catch (NumberFormatException ex) {
                    resultArea.setText("Error: Please enter numeric values for all inputs.");
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    // Helper method to format numbers with 4 significant figures and abbreviations
    private static String formatNumber(double value) {
        if (value < 1000) {
            return String.format("%.4g", value); // No abbreviation for small numbers
        }
        int exp = (int) (Math.log10(value) / 3); // Determine the exponent for thousands (k, m, b, t, etc.)
        char[] suffixes = {'k', 'm', 'b', 't', 'q', 'Q', 's', 'S', 'o', 'n'}; // Extended suffixes for higher magnitudes

        // Ensure exp does not exceed the bounds of the suffixes array
        if (exp > suffixes.length) {
            exp = suffixes.length; // Cap at the largest suffix
        }

        double scaledValue = value / Math.pow(1000, exp);
        return String.format("%.4g%s", scaledValue, exp > 0 ? suffixes[exp - 1] : "");
    }
}