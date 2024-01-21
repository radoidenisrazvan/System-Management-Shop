package ApManagerStoc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;



/**
 * Clasa care contine testele unitare ale aplicatiei
 */

class ManagerStocTest {

	@Test
    void adaugaProdus() {
        ManagerStoc managerStoc = new ManagerStoc();
        Produs produs = new Produs("Tastatura", "Periferice", 15, 200);

        managerStoc.adaugaProdus(produs);

        assertTrue(managerStoc.cauta("Tastatura").contains(produs));
    }

    @Test
    void actualizeazaProdus() {
        ManagerStoc managerStoc = new ManagerStoc();
        Produs produs = new Produs("Casti", "Periferice", 25, 300);
        managerStoc.adaugaProdus(produs);

        managerStoc.actualizeazaProdus("Casti", 10, 150);

        assertEquals(10, managerStoc.cauta("Casti").get(0).getCantitate());
    }

    @Test
    void stergeProdus() {
        ManagerStoc managerStoc = new ManagerStoc();
        Produs produs = new Produs("Monitor", "Periferice", 30, 1000);
        managerStoc.adaugaProdus(produs);

        managerStoc.stergeProdus("Monitor");

        assertFalse(managerStoc.cauta("Monitor").contains(produs));
    }

    @Test
    void cauta() {
        ManagerStoc managerStoc = new ManagerStoc();
        Produs produs1 = new Produs("Tableta", "Electronice", 18, 2000);
        Produs produs2 = new Produs("Router", "Retelistica", 50, 200);
        managerStoc.adaugaProdus(produs1);
        managerStoc.adaugaProdus(produs2);

        assertEquals(2, managerStoc.cauta("").size());
        assertEquals(1, managerStoc.cauta("Electronice").size());
        assertEquals(1, managerStoc.cauta("Router").size());
    }

    @Test
    void afiseazaProduseCuCantitateScazuta() {
        ManagerStoc managerStoc = new ManagerStoc();
        Produs produs1 = new Produs("Placa Video", "Componente", 8, 3000);
        Produs produs2 = new Produs("Procesor", "Componente", 15, 2200);
        managerStoc.adaugaProdus(produs1);
        managerStoc.adaugaProdus(produs2);

        assertEquals(1, managerStoc.afiseazaProduseCuCantitateScazuta(10).size());
        assertEquals(2, managerStoc.afiseazaProduseCuCantitateScazuta(17).size());
    }

    @Test
    void afiseazaToateProdusele() {
        ManagerStoc managerStoc = new ManagerStoc();
        Produs produs1 = new Produs("Aer Conditionat", "Electrocasnice", 12, 1900);
        Produs produs2 = new Produs("Frigider", "Electrocasnice", 20, 3500);
        managerStoc.adaugaProdus(produs1);
        managerStoc.adaugaProdus(produs2);

        assertEquals(2, managerStoc.afiseazaToateProdusele().size());
    }

}
