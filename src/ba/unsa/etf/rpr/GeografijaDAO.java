package ba.unsa.etf.rpr;

import java.sql.Connection;
import java.util.ArrayList;

public class GeografijaDAO {

    private ArrayList<Grad> gradovi;
    private ArrayList<Drzava> drzave;
    private static GeografijaDAO instanca = null;
    private Connection veza;

    private static void initialize() {
        instanca = new GeografijaDAO();
    }

    public static void removeInstance() {
        instanca = null;
    }

    public static GeografijaDAO getInstance() {
        if(instanca != null) return instanca;
        else initialize();
        return instanca;
    }

    public Grad glavnigrad(String drzava) {
        for(int i=0; i<drzave.size(); i++) {
            if(drzave.get(i).getIme().equals(drzava)) return drzave.get(i).getGlGrad();
        }
        return null;
    }

    public ArrayList<Grad> gradovi() {
        return null;
    }

    public Drzava nadjiDrzavu(String drzava) {
        for (int i = 0; i < drzave.size(); i++) {
            if (drzave.get(i).getIme().equals(drzava)) return drzave.get(i);
        }
        return null;
    }

    public void obrisiDrzavu(String drzava) throws IllegalArgumentException {
        for (int i = 0; i < drzave.size(); i++) {
            if (drzave.get(i).getIme().equals(drzava)) {
                drzave.remove(i);
                break;
            }
        }
        throw new IllegalArgumentException("Drzava nije pronadjena");
    }

    public void dodajGrad(Grad grad) throws IllegalArgumentException {
        if(gradovi.contains(grad)) throw new IllegalArgumentException("Grad je postojeci");
        gradovi.add(grad);
    }

    public void izmijeniGrad(Grad grad) throws IllegalArgumentException {
        for (int i = 0; i < gradovi.size(); i++) {
            if (gradovi.get(i).equals(grad)) {
                gradovi.get(i).setIme(grad.getIme());
                gradovi.get(i).setBrStanovnika(grad.getBrStanovnika());
                gradovi.get(i).setDrzava(grad.getDrzava());
                gradovi.get(i).setBr(grad.getBr());
                break;
            }
        }
        throw new IllegalArgumentException("Grad je postojeci");
    }

    public void dodajDrzavu(Drzava drzava) throws IllegalArgumentException{
        if(drzave.contains(drzava)) throw new IllegalArgumentException("Drzava je postojeca");
        drzave.add(drzava);
    }

    public Grad glGrad(String drzava) {
        for (int i = 0; i < drzave.size(); i++) {
            if (drzave.get(i).getIme().equals(drzava)) return drzave.get(i).getGlGrad();
        }
        return null;
    }
}

