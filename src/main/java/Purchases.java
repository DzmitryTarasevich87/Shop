import java.util.ArrayList;
import java.util.List;

public class Purchases {
    private List<Product> receipt = new ArrayList<>();
    private int receiptNumber;

    public Purchases(List<Product> receipt, int receiptNumber) {
        this.receipt = receipt;
        this.receiptNumber = receiptNumber;
    }

    public int getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public List<Product> getReceipt() {
        return receipt;
    }

    public void setReceipt(List<Product> receipt) {
        this.receipt = receipt;
    }
}
