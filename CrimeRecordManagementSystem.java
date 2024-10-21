package src.main.java;

import java.io.*;
import java.util.ArrayList;

public class CrimeRecordManagementSystem {
    private ArrayList<CrimeRecord> records;
    private int nextSerialNumber;
    private String filePath;

    // Constructor initializes the records list, sets the next serial number, and loads records from the file.
    public CrimeRecordManagementSystem(String filePath) {
        records = new ArrayList<>();
        nextSerialNumber = 1;
        this.filePath = filePath;
        loadRecords();
    }

    // Default constructor with a predefined file path.
    public CrimeRecordManagementSystem() {
        this("default/path/to/your/file.txt"); // Set a default file path
    }

    // Login method for user authentication.
    public boolean login(String username, String password) {
        return username.equals("admin") && password.equals("password");
    }

    // Loads records from a file and initializes the next serial number.
    private void loadRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int serialNumber = Integer.parseInt(parts[0]);
                String crimeName = parts[1];
                String suspectName = parts[2];
                String date = parts[3];
                String evidence = parts[4];
                String witness = parts[5];
                String status = parts[6];
                CrimeRecord record = new CrimeRecord(serialNumber, crimeName, suspectName, date, evidence, witness, status);
                records.add(record);
                nextSerialNumber = serialNumber + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Saves records to the specified file.
    public void saveRecords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (CrimeRecord record : records) {
                writer.write(record.getSerialNumber() + "," + record.getCrimeName() + "," +
                        record.getSuspectName() + "," + record.getDate() + "," + 
                        record.getEvidence() + "," + record.getWitness() + "," + record.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adds a new crime record and saves to file.
    public void addRecord(String crimeName, String suspectName, String date, String evidence, String witness, String status) {
        CrimeRecord record = new CrimeRecord(nextSerialNumber, crimeName, suspectName, date, evidence, witness, status);
        records.add(record);
        nextSerialNumber++;
        saveRecords();
    }

    // Deletes a crime record by serial number and saves to file.
    public boolean deleteRecord(int serialNumber) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getSerialNumber() == serialNumber) {
                records.remove(i);
                saveRecords();
                return true;
            }
        }
        return false;
    }

    // Modifies an existing crime record and saves changes to file.
    public void modifyRecord(int serialNumber, String crimeName, String suspectName, String date, String evidence, String witness, String status) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getSerialNumber() == serialNumber) {
                CrimeRecord modifiedRecord = new CrimeRecord(serialNumber, crimeName, suspectName, date, evidence, witness, status);
                records.set(i, modifiedRecord);
                saveRecords();
                break;
            }
        }
    }

    // Returns the list of crime records.
    public ArrayList<CrimeRecord> getRecords() {
        return records;
    }
}
