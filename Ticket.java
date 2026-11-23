public class Ticket {
    private final int ticketNumber;
    private String purchaserName;
    private int verificationCode;

    // Blank ticket
    public Ticket(int ticketNumber) {
        this(ticketNumber, "", 0);
    }

    // Creates a ticket
    public Ticket(int ticketNumber, String purchaserName, int verificationCode) {
        this.ticketNumber = ticketNumber;
        this.purchaserName = purchaserName;
        this.verificationCode = verificationCode;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public boolean isSold() {
        return !purchaserName.isEmpty() && verificationCode != 0;
    }

    public void sellTicket(String name) {
        if (!isSold() && !name.trim().isEmpty()) {
            purchaserName = name;
            verificationCode = 100000 + (int) (Math.random() * 900000);
        }
    }
}
