package src.main.java;

public class CrimeRecord {
    private int serialNumber;
    private String crimeName;
    private String suspectName;
    private String date;
    private String Evidence;
    private String Witness;
    private String Status;

    public CrimeRecord(int serialNumber, String crimeName, String suspectName, String date, String Evidence, String Witness, String Status) {
        this.serialNumber = serialNumber;
        this.crimeName = crimeName;
        this.suspectName = suspectName;
        this.date = date;
        this.Evidence = Evidence;
        this.Witness = Witness;
        this.Status = Status;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getCrimeName() {
        return crimeName;
    }

    public String getSuspectName() {
        return suspectName;
    }

    public String getDate() {
        return date;
    }

    public String getEvidence() {
        return Evidence;
    }

    public String getWitness() {
        return Witness;
    }

    public String getStatus() {
        return Status;
    }
}
