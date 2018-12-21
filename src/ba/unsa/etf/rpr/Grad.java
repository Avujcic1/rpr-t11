package ba.unsa.etf.rpr;

public class Grad {

    private int br;
    private int brStanovnika;
    private String ime;
    private Drzava drzave;

    public Grad() {}

    public Grad(int br, int brStanovnika, String ime, Drzava drzava) {
        this.br = br;
        this.brStanovnika = brStanovnika;
        this.ime = ime;
        this.drzave = drzava;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setBrStanovnika(int brStanovnika) {
        this.brStanovnika = brStanovnika;
    }

    public void setDrzava(Drzava drzava) {
        this.drzave = drzava;
    }

    public String getIme() {
        return ime;
    }

    public int getBrStanovnika() {
        return brStanovnika;
    }

    public Drzava getDrzava() {
        return drzave;
    }

    public void setBr(int br) {
        this.br = br;
    }

    public int getBr() {
        return br;
    }

}
