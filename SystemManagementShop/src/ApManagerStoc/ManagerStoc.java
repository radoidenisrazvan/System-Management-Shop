package ApManagerStoc;

import java.util.ArrayList;

/**
 * Clasa pentru gestionarea stocurilor magazinului
 * @author Radoi Denis-Razvan
 */

public class ManagerStoc {
	private ArrayList<Produs> produse;
	
	/**
	 * Constructor fara parametrii pentru initializarea
	 * unui sistem de management al stocului de produse
	 */
	
	public ManagerStoc() {
		this.produse = new ArrayList<Produs>();
	}
	
	/** 
	 * Functie pentru a adauga un produs in stoc
	 * @param produs
	 */
	public void adaugaProdus(Produs produs) {
		this.produse.add(produs);
	}
	/**
	 * Actualizeaza cantitatea unui produs
	 * @param denumire Denumirea produsului
	 * @param cantitateNoua Cantitatea noua a produsului
	 */
	public void actualizeazaProdus(String denumire, int cantitateNoua, double pretNou) {
        for (Produs produs : produse) {
            if (produs.getDenumire().equals(denumire)) {
                produs.setCantitate(cantitateNoua);
                produs.setPret(pretNou);
                break;
            }
        }
    }
	

	
	/**
	 * Sterge un produs din stoc
	 * @param denumire Denumirea produsului care va fi sters
	 */
	public void stergeProdus(String denumire) {
        produse.removeIf(produs -> produs.getDenumire().equals(denumire));
    }
	
	/**
	 * Cauta un produs dupa denumire / categorie
	 * @param criteriu Criteriul de cautare ( dupa denumire sau categorie )
	 * @return Lista de produse care corespund criteriului de cautare
	 */
	public ArrayList<Produs> cauta(String criteriu) {
        ArrayList<Produs> rezultate = new ArrayList<Produs>();
        for (Produs produs : produse) {
            if (produs.getDenumire().contains(criteriu) || produs.getCategorie().contains(criteriu)) {
                rezultate.add(produs);
            }
        }
        return rezultate;
    }
	
	/**
	 * Metoda pentru a afisa toate produsele din stoc
	 * @return Returneaza toate produsele aflate in stoc
	 */
	public ArrayList<Produs> afiseazaToateProdusele() {
    	ArrayList<Produs> rezultate = new ArrayList<Produs>();
    	for (Produs produs : produse) {
    		rezultate.add(produs);
        }
    	return rezultate;
    }
	
	/**
	 * Metoda care afiseaza lista produselor cu cantitate scazuta 
	 * @param limita Limita Cantitatea minima pentru a fi considerata scazuta
	 * @return Returneaza toate produsele cu cantitate scazuta
	 */
	public ArrayList<Produs> afiseazaProduseCuCantitateScazuta(int limita) {
    	ArrayList<Produs> rezultate = new ArrayList<Produs>();
    	for (Produs produs : produse) {
            if (produs.getCantitate() < limita) {
            	rezultate.add(produs);
            }
        }
    	return rezultate;
    }
	
	/**
	 * Metoda care afiseaza daca un produs exista in stoc
	 * @param numeProdus Produsul cautat in stoc
	 * @return Returneaza daca produsul exista sau nu in stoc.
	 */
	public boolean existaProdus(String numeProdus) {
	    for (Produs produs : produse) {
	        if (produs.getDenumire().equals(numeProdus)) {
	            return true;
	        }
	    }
	    return false;
	}

}
