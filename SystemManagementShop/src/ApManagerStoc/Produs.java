package ApManagerStoc;
/**
 * Clasa ce contine reprezentarea unui Produs din magazin
 */
public class Produs {
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public Produs(int id, String denumire, String categorie, int cantitate, double pret) {
		super();
        this.id = id;
        this.denumire = denumire;
        this.categorie = categorie;
        this.cantitate = cantitate;
        this.pret = pret;
    }
	
	private String denumire;
	private String categorie;
	private int cantitate;
	private double pret;
	
	/**
	 * Functie ce returneaza denumirea produsului
	 * @return denumire Denumirea articolului
	 */
	public String getDenumire() {
		return denumire;
	}
	
	public double getPret() {
		return pret;
	}

	public void setPret(double pret) {
		this.pret = pret;
	}

	/**
	 * Functie ce returneaza categoria produsului
	 * @return categorie Categoria produsului
	 */
	public String getCategorie() {
		return categorie;
	}
	
	/**
	 * Functie ce returneaza cantitatea produsului
	 * @return cantitate Cantitatea produsului
	 */
	public int getCantitate() {
		return cantitate;
	}
	
	/**
	 * Metoda de acces pentru a seta cantitatea produsului
	 * @param cantitate Cantitatea produsului
	 */
	public void setCantitate(int cantitate) {
		this.cantitate = cantitate;
	}
	
    /**
     * Constructor cu parametri pentru un articol
     * @param denumire denumirea produsului
     * @param categorie categoria produsului
     * @param cantitate cantitatea produsului
     */
	public Produs( String denumire, String categorie, int cantitate, double pret) {
		super();
		this.denumire = denumire;
		this.categorie = categorie;
		this.cantitate = cantitate;
		this.pret = pret;
	}

	public Produs(int id, String denumire) {
        this.id = id;
        this.denumire = denumire;
    }
	
	@Override
	public String toString() {
		return "Produs [denumire=" + denumire + ", categorie=" + categorie + ", cantitate=" + cantitate + ", pret="
				+ pret + " RON]";
	}
	
}
