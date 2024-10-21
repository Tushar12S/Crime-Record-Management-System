package src.main.java;

public class CrimeRecordManagement {
    public static void main(String[] args) {
        String filePath = "crime_records.txt";
        CrimeRecordManagementSystem system = new CrimeRecordManagementSystem(filePath);
        CrimeRecordManagementGUI gui = new CrimeRecordManagementGUI(system);
        gui.setVisible(true);
    }
}