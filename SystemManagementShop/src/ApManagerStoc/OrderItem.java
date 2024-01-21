package ApManagerStoc;



public class OrderItem {
	private int id;
    private Produs produs;
    private int cantitate;
    private String clientName;


    public OrderItem(Produs produs, int cantitate, String clientName) {
        this.produs = produs;
        this.cantitate = cantitate;
        this.clientName = clientName;
    }
    public OrderItem(int id, int cantitate, String clientName) {
        this.id = id;
        this.cantitate = cantitate;
        this.clientName = clientName;
    }


    public Produs getProdus() {
        return produs;
    }

    public int getProdusId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCantitate() {
        return cantitate;
    }

    public String getClientName() {
        return clientName;
    }
}
