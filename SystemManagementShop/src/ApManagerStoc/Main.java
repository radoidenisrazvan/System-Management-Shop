package ApManagerStoc;

import java.util.ArrayList;

/**
 * Clasa destinata testarii functionalitatii al celorlalte clase
 * de gestionare a stocurilor
 */

public class Main {
	
	/**
	 * Metoda principala pentru exemplul de utilizare
	 * 
	 * @param args Argumentele din linia de comanda (nu sunt folosite in acest caz)
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ManagerStoc managerStoc = new ManagerStoc();

        managerStoc.adaugaProdus(new Produs("Laptop", "Electronice", 10, 4500));
        managerStoc.adaugaProdus(new Produs("Telefon", "Electronice", 20, 6500));
        managerStoc.adaugaProdus(new Produs("Mouse", "Periferice", 50, 300));
        
        
        System.out.println("Afisarea tuturor produselor:");
        ArrayList<Produs> toateProdusele = managerStoc.afiseazaToateProdusele();
        for (Produs produs : toateProdusele) {
            System.out.println(produs);
        }
        System.out.println("------------------------");

        System.out.println("Afisare dupa actualizare cantitate si pret la 'Laptop'");
        managerStoc.actualizeazaProdus("Laptop", 5, 5500);
        toateProdusele = managerStoc.afiseazaToateProdusele();
        for (Produs produs : toateProdusele) {
            System.out.println(produs);
        }
        System.out.println("------------------------");

        System.out.println("Cautare dupa 'Telefon':");
        ArrayList<Produs> rezultateCautare = managerStoc.cauta("Telefon");
        for (Produs produs : rezultateCautare) {
            System.out.println(produs);
        }
        System.out.println("------------------------");

        System.out.println("Afisare articole cu cantitate scazuta (sub 15):");
        ArrayList<Produs> articoleCantScazuta = managerStoc.afiseazaProduseCuCantitateScazuta(15);
        for (Produs produs : articoleCantScazuta) {
            System.out.println(produs);
        }
        
        System.out.println("------------------------");

        System.out.println("Afisare articole ramase dupa stergerea lui 'Mouse':");
        managerStoc.stergeProdus("Mouse");
        toateProdusele = managerStoc.afiseazaToateProdusele();
        for (Produs produs : toateProdusele) {
            System.out.println(produs);
        }
        
        System.out.println("------------------------");
	}

}
