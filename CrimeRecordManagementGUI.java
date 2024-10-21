package src.main.java;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CrimeRecordManagementGUI extends JFrame {
    private CrimeRecordManagementSystem system;
    private JPanel mainPanel;
    private JTextArea displayArea;

    public CrimeRecordManagementGUI(CrimeRecordManagementSystem system) {
        this.system = system;

        if (!showLoginDialog()) {
            JOptionPane.showMessageDialog(this, "Wrong Login Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        setTitle("Crime Record Management System");
        setSize(750, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Record");
        addButton.addActionListener(e -> showAddRecordDialog());
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Record");
        deleteButton.addActionListener(e -> showDeleteRecordDialog());
        buttonPanel.add(deleteButton);

        JButton modifyButton = new JButton("Modify Record");
        modifyButton.addActionListener(e -> showModifyRecordDialog());
        buttonPanel.add(modifyButton);

        JButton searchByMonthButton = new JButton("Search by Month");
        searchByMonthButton.addActionListener(e -> showSearchByMonthDialog());
        buttonPanel.add(searchByMonthButton);

        JButton searchByYearButton = new JButton("Search by Year");
        searchByYearButton.addActionListener(e -> showSearchByYearDialog());
        buttonPanel.add(searchByYearButton);

        JButton searchByDateButton = new JButton("Search by Date");
        searchByDateButton.addActionListener(e -> showSearchByDateDialog());
        buttonPanel.add(searchByDateButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("Enter Serial Number to Search:");
        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> {
            String searchInput = searchField.getText();
            if (!searchInput.isEmpty()) {
                try {
                    int serialNumber = Integer.parseInt(searchInput);
                    CrimeRecord record = findRecordBySerialNumber(serialNumber);
                    displaySearchResult(record);
                } catch (NumberFormatException ex) {
                    displaySearchResult(null);
                }
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        setContentPane(mainPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveAndExit();
            }
        });
    }

    private boolean showLoginDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };
        int option = JOptionPane.showOptionDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            return system.login(username, password);
        }
        return false;
    }

    private void showAddRecordDialog() {
        JDialog dialog = new JDialog(this, "Add Record", true);
        dialog.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        JTextField crimeNameField = new JTextField();
        JTextField suspectNameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField evidenceField = new JTextField();
        JTextField witnessField = new JTextField();
        JTextField statusField = new JTextField();

        panel.add(new JLabel("Crime Name:"));
        panel.add(crimeNameField);
        panel.add(new JLabel("Suspect Name:"));
        panel.add(suspectNameField);
        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Evidence:"));
        panel.add(evidenceField);
        panel.add(new JLabel("Witness:"));
        panel.add(witnessField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String crimeName = crimeNameField.getText();
            String suspectName = suspectNameField.getText();
            String date = dateField.getText();
            String evidence = evidenceField.getText();
            String witness = witnessField.getText();
            String status = statusField.getText();
            
            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate inputDate = LocalDate.parse(date, dateFormatter);
                LocalDate today = LocalDate.now();
                if (inputDate.isBefore(today) || inputDate.isEqual(today)) {
                    system.addRecord(crimeName, suspectName, date, evidence, witness, status);
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Record added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Date. Date cannot be in the future.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Date Format. Please use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addButton);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void showDeleteRecordDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter the serial number of the record to delete:");
        if (input != null && !input.isEmpty()) {
            try {
                int serialNumber = Integer.parseInt(input);
                if (system.deleteRecord(serialNumber)) {
                    JOptionPane.showMessageDialog(this, "Record with serial number " + serialNumber + " deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Record not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid serial number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showModifyRecordDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter the serial number of the record to modify:");
        if (input != null && !input.isEmpty()) {
            try {
                int serialNumber = Integer.parseInt(input);
                CrimeRecord record = findRecordBySerialNumber(serialNumber);
                if (record != null) {
                    JDialog dialog = new JDialog(this, "Modify Record", true);
                    dialog.setSize(400, 400);

                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(7, 2));

                    JTextField crimeNameField = new JTextField(record.getCrimeName());
                    JTextField suspectNameField = new JTextField(record.getSuspectName());
                    JTextField dateField = new JTextField(record.getDate());
                    JTextField evidenceField = new JTextField(record.getEvidence());
                    JTextField witnessField = new JTextField(record.getWitness());
                    JTextField statusField = new JTextField(record.getStatus());

                    panel.add(new JLabel("Crime Name:"));
                    panel.add(crimeNameField);
                    panel.add(new JLabel("Suspect Name:"));
                    panel.add(suspectNameField);
                    panel.add(new JLabel("Date (yyyy-MM-dd):"));
                    panel.add(dateField);
                    panel.add(new JLabel("Evidence:"));
                    panel.add(evidenceField);
                    panel.add(new JLabel("Witness:"));
                    panel.add(witnessField);
                    panel.add(new JLabel("Status:"));
                    panel.add(statusField);

                    JButton modifyButton = new JButton("Modify");
                    modifyButton.addActionListener(e -> {
                        String crimeName = crimeNameField.getText();
                        String suspectName = suspectNameField.getText();
                        String date = dateField.getText();
                        String evidence = evidenceField.getText();
                        String witness = witnessField.getText();
                        String status = statusField.getText();

                        try {
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate inputDate = LocalDate.parse(date, dateFormatter);
                            LocalDate today = LocalDate.now();
                            if (inputDate.isBefore(today) || inputDate.isEqual(today)) {
                                system.modifyRecord(serialNumber, crimeName, suspectName, date, evidence, witness, status);
                                dialog.dispose();
                                JOptionPane.showMessageDialog(this, "Record modified successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "Invalid Date. Date cannot be in the future.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Invalid Date Format. Please use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    panel.add(modifyButton);

                    dialog.setContentPane(panel);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Record not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid serial number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showSearchByMonthDialog() {
        String monthInput = JOptionPane.showInputDialog(this, "Enter the month (1-12):");
        if (monthInput != null && !monthInput.isEmpty()) {
            try {
                int month = Integer.parseInt(monthInput);
                ArrayList<CrimeRecord> results = searchRecordsByMonth(month);
                displaySearchResults(results);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid month.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showSearchByYearDialog() {
        String yearInput = JOptionPane.showInputDialog(this, "Enter the year (yyyy):");
        if (yearInput != null && !yearInput.isEmpty()) {
            try {
                int year = Integer.parseInt(yearInput);
                ArrayList<CrimeRecord> results = searchRecordsByYear(year);
                displaySearchResults(results);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid year.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showSearchByDateDialog() {
        String dateInput = JOptionPane.showInputDialog(this, "Enter the date (yyyy-MM-dd):");
        if (dateInput != null && !dateInput.isEmpty()) {
            try {
                LocalDate.parse(dateInput);
                ArrayList<CrimeRecord> results = searchRecordsByDate(dateInput);
                displaySearchResults(results);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private ArrayList<CrimeRecord> searchRecordsByMonth(int month) {
        ArrayList<CrimeRecord> results = new ArrayList<>();
        for (CrimeRecord record : system.getRecords()) {
            if (Integer.parseInt(record.getDate().split("-")[1]) == month) {
                results.add(record);
            }
        }
        return results;
    }

    private ArrayList<CrimeRecord> searchRecordsByYear(int year) {
        ArrayList<CrimeRecord> results = new ArrayList<>();
        for (CrimeRecord record : system.getRecords()) {
            if (Integer.parseInt(record.getDate().split("-")[0]) == year) {
                results.add(record);
            }
        }
        return results;
    }

    private ArrayList<CrimeRecord> searchRecordsByDate(String date) {
        ArrayList<CrimeRecord> results = new ArrayList<>();
        for (CrimeRecord record : system.getRecords()) {
            if (record.getDate().equals(date)) {
                results.add(record);
            }
        }
        return results;
    }

    private void displaySearchResults(ArrayList<CrimeRecord> results) {
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder message = new StringBuilder("Found Records:\n");
        for (CrimeRecord record : results) {
            message.append("Serial Number: ").append(record.getSerialNumber()).append(", Crime: ").append(record.getCrimeName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private void displaySearchResult(CrimeRecord record) {
        if (displayArea != null) {
            mainPanel.remove(displayArea);
        }

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        Font font = new Font("Arial", Font.PLAIN, 16);
        displayArea.setFont(font);

        if (record != null) {
            String result = "Serial Number: " + record.getSerialNumber() +
                    "\nCrime: " + record.getCrimeName() +
                    "\nSuspect: " + record.getSuspectName() +
                    "\nDate: " + record.getDate() +
                    "\nEvidence: " + record.getEvidence() +
                    "\nWitness: " + record.getWitness() +
                    "\nStatus: " + record.getStatus();
            displayArea.setText(result);
        } else {
            displayArea.setText("Record not found.");
        }

        mainPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private CrimeRecord findRecordBySerialNumber(int serialNumber) {
        for (CrimeRecord record : system.getRecords()) {
            if (record.getSerialNumber() == serialNumber) {
                return record;
            }
        }
        return null; // Return null if not found
    }

    private void saveAndExit() {
        try {
            system.saveRecords(); // Implement this method in your CrimeRecordManagementSystem class
            System.exit(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving records. Exiting without saving.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        CrimeRecordManagementSystem system = new CrimeRecordManagementSystem();
        CrimeRecordManagementGUI gui = new CrimeRecordManagementGUI(system);
        gui.setVisible(true);
    }
}
