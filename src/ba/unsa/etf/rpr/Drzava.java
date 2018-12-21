package ba.unsa.etf.rpr;

public class Drzava {

    private int br;
    private String ime;
    private Grad glGrad;

    public Drzava() {}

    public Drzava(int br, String ime, Grad glGrad) {
        this.br = br;
        this.ime = ime;
        this.glGrad = glGrad;
    }

    public int getBr() {
        return br;
    }

    public void setBr(int br) {
        this.br = br;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Grad getGlGrad() {
        return glGrad;
    }


    public void setGlGrad(Grad glGrad) {
        this.glGrad = glGrad;
    }
}
