package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GeografijaDAOTest {

    @Test
    void regenerateFile() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstanca();
        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("London", gradovi.get(0).getIme());
        assertEquals("Francuska", gradovi.get(1).getDrzava().getIme());
    }

    @Test
    void glavniGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstanca();
        Grad nepoznat = dao.glavniGrad("Bosna i Hercegovina");
        assertNull(nepoznat);
        Grad bech = dao.glavniGrad("Austrija");
        assertEquals("Beč", bech.getIme());
    }

    @Test
    void obrisiDrzavu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstanca();
        // Nepostojeća država, neće se desiti ništa
        dao.obrisiDrzavu("Kina");
        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("Pariz", gradovi.get(1).getIme());
        assertEquals("Austrija", gradovi.get(2).getDrzava().getIme());
    }

    @Test
    void obrisiDrzavu2() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstanca();

        // Nema gradova Beč i Graz koji su iz austrije
        dao.obrisiDrzavu("Austrija");

        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals(3, gradovi.size());
        assertEquals("London", gradovi.get(0).getIme());
        assertEquals("Pariz", gradovi.get(1).getIme());
        assertEquals("Manchester", gradovi.get(2).getIme());
    }

    @Test
    void dodajGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstanca();
        Drzava francuska = dao.nadjiDrzavu("Francuska");
        Grad grad = new Grad();
        grad.setIme("Marseille");
        grad.setBrStanovnika(869815);
        grad.setDrzava(francuska);
        dao.dodajGrad(grad);

        // Marsej je veći od Manchestera i Graza, ali manji od Pariza, Londona i Beča
        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("Marseille", gradovi.get(3).getIme());
    }

    @Test
    void dodajDrzavu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        Grad sarajevo = new Grad();
        sarajevo.setIme("Sarajevo");
        sarajevo.setBrStanovnika(500000);
        Drzava bih = new Drzava();
        bih.setIme("Bosna i Hercegovina");
        bih.setGlavniGrad(sarajevo);
        sarajevo.setDrzava(bih);

        GeografijaDAO dao = GeografijaDAO.getInstanca();
        dao.dodajDrzavu(bih);
        dao.dodajGrad(sarajevo);

        // Provjera
        Grad proba = dao.glavniGrad("Bosna i Hercegovina");
        assertEquals("Sarajevo", proba.getIme());
        assertEquals(500000, proba.getBrStanovnika());
        assertEquals("Bosna i Hercegovina", proba.getDrzava().getIme());
    }

    @Test
    void izmijeniGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstanca();
        Grad bech = dao.glavniGrad("Austrija");
        bech.setIme("Vienna");
        dao.izmijeniGrad(bech);

        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("Vienna", gradovi.get(2).getIme());
    }
}
