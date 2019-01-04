package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO instanca = null;
    private Connection konekcija;
    private PreparedStatement tempState;


    private GeografijaDAO() {
        try {
            String url="jdbc:sqlite:baza.db";
            konekcija = DriverManager.getConnection(url);
            tempState = konekcija.prepareStatement("DELETE FROM drzava");
            tempState.executeUpdate();
            tempState = konekcija.prepareStatement("DELETE FROM grad");
            tempState.executeUpdate();
            ArrayList<Drzava> drzavaList = new ArrayList<>();
            ArrayList<Grad> gradList = new ArrayList<>();
            defaultDataFill(drzavaList, gradList);
            tempState = konekcija.prepareStatement("INSERT INTO drzava (?, ?, NULL");
            for (var drzava : drzavaList) {
                try {
                    tempState.setInt(1, drzava.getBr());
                    tempState.setString(2, drzava.getIme());
                    tempState.executeUpdate();
                } catch (SQLException ignored) {
                    System.out.println("Pogresni podaci");
                }
            }

            tempState = konekcija.prepareStatement("INSERT INTO grad VALUES (?, ?, ?, ?)");
            for (var grad : gradList) {
                try {
                    tempState.setInt(1, grad.getBr());
                    tempState.setString(2, grad.getIme());
                    tempState.setInt(3, grad.getBrStanovnika());
                    tempState.setInt(4, grad.getDrzava().getBr());
                    tempState.executeUpdate();
                } catch (SQLException ignored) {
                    System.out.println("Pogresni podaci");
                }
            }

            tempState = konekcija.prepareStatement("UPDATE drzava SET glavni_grad = ? WHERE id = ?");
            for (var drzava : drzavaList) {
                try {
                    tempState.setInt(1, drzava.getGlavniGrad().getBr());
                    tempState.setInt(2, drzava.getBr());
                    tempState.executeUpdate();
                } catch (SQLException ignored) {
                    System.out.println("Pogresni podaci");
                }
            }


        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void defaultDataFill(ArrayList<Drzava> drzavaList, ArrayList<Grad> gradList) {
        Drzava hrvatska = new Drzava(1, "Hrvatska", null);
        Drzava srbija = new Drzava(2, "Srbija", null);
        Drzava engleska = new Drzava(3, "Velika Britanija", null);

        Grad zagreb = new Grad(1, 1120000, "Zagreb" , null);
        Grad london = new Grad(3, 8800000,  	"London" , null);
        Grad beograd = new Grad(2, 299000,  	"Beograd", null);

        hrvatska.setGlavniGrad(zagreb);
        srbija.setGlavniGrad(beograd);
        engleska.setGlavniGrad(london);

        drzavaList.add(hrvatska);
        drzavaList.add(srbija);
        drzavaList.add(engleska);

        zagreb.setDrzava(hrvatska);

        gradList.add(zagreb);

        london.setDrzava(engleska);

    }

    private static void initialize() {
        instanca = new GeografijaDAO();
    }

    public static GeografijaDAO getInstanca() {
        if (instanca == null) initialize();
        return instanca;
    }

    public static void removeInstance() { instanca = null; }


    public void obrisiDrzavu(String drzava) {
        try {
            tempState = konekcija.prepareStatement("SELECT g.id FROM grad g, drzava d WHERE g.drzava = d.id AND d.naziv = ?");
            tempState.setString(1, drzava);
            ResultSet result = tempState.executeQuery();
            int brojac = 0;
            while (result.next()) {
                int idGrad = result.getInt(1);
                PreparedStatement podUpit = konekcija.prepareStatement("DELETE FROM grad WHERE id = ?");
                podUpit.setInt(1, idGrad);
                podUpit.executeUpdate();
                brojac++;
            }
            if (brojac != 0) {
                tempState = konekcija.prepareStatement("DELETE FROM drzava WHERE naziv = ?");
                tempState.setString(1, drzava);
                tempState.executeUpdate();
            }
        } catch (SQLException ignored) {
            System.out.println("Ne postoji data drzava");
        }
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> gradovi = new ArrayList<>();
        try {
            tempState = konekcija.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
            ResultSet resultGradovi = tempState.executeQuery();
            while (resultGradovi.next()) {
                Grad g = new Grad();
                int gradID = resultGradovi.getInt(1);
                g.setBr(gradID);
                String nazivGrad = resultGradovi.getString(2);
                g.setIme(nazivGrad);
                int brojStanovnika = resultGradovi.getInt(3);
                g.setBrStanovnika(brojStanovnika);
                int drzavaId = resultGradovi.getInt(4);
                g.setDrzava(new Drzava(drzavaId, "", null));
                gradovi.add(g);
            }
            tempState = konekcija.prepareStatement("SELECT * FROM drzava");
            ResultSet resultDrzave = tempState.executeQuery();
            while (resultDrzave.next()) {
                Drzava d = new Drzava();
                int drzavaID = resultDrzave.getInt(1);
                d.setBr(drzavaID);
                String nazivDrzava = resultDrzave.getString(2);
                d.setIme(nazivDrzava);
                int glavniGradId = resultDrzave.getInt(3);
                for (var grad : gradovi) {
                    if (grad.getDrzava().getBr() == d.getBr()) grad.setDrzava(d);
                    if (glavniGradId == grad.getBr())  d.setGlavniGrad(grad);
                }
            }
        } catch (SQLException ignored) {
            return null;
        }
        return gradovi;
    }

    public Grad glavniGrad(String drzava) {
        Grad grad = new Grad();
        try {
            tempState = konekcija.prepareStatement("SELECT g.id, g.naziv, g.broj_stanovnika, d.id, d.naziv FROM grad g, drzava d WHERE d.glavni_grad = g.id AND d.naziv = ?"); //SELECT() + FROM() + WHERE() + AND();
            tempState.setString(1, drzava);
            ResultSet result = tempState.executeQuery();
            Drzava d = new Drzava();
            grad.setDrzava(d);
            d.setGlavniGrad(grad);
            int brojac = 0;
            while (result.next()) {
                int idGrad = result.getInt(1);
                grad.setBr(idGrad);
                String nazivGrad = result.getString(2);
                grad.setIme(nazivGrad);
                int brojStanovnika = result.getInt(3);
                grad.setBrStanovnika(brojStanovnika);
                int idDrzava = result.getInt(4);
                d.setBr(idDrzava);
                String nazivDrzave = result.getString(5);
                d.setIme(nazivDrzave);
                brojac++;
            }
            if (brojac == 0) {
                System.out.println("Data drzava ne postoji");
                return null;
            }
        } catch (SQLException ignored) {
            return null;
        }
        return grad;
    }

    public Drzava nadjiDrzavu(String drzava) {
        Drzava drzavaResult = new Drzava();
        try {
            tempState = konekcija.prepareStatement("SELECT d.id, d.naziv, g.id, g.naziv, g.broj_stanovnika FROM drzava d, grad g WHERE d.glavni_grad = g.id AND d.naziv = ?");
            tempState.setString(1, drzava);
            ResultSet result = tempState.executeQuery();
            Grad glavniGrad = new Grad();
            drzavaResult.setGlavniGrad(glavniGrad);
            glavniGrad.setDrzava(drzavaResult);
            while (result.next()) {
                int idDrzava = result.getInt(1);
                drzavaResult.setBr(idDrzava);
                String nazivDrzave = result.getString(2);
                drzavaResult.setIme(nazivDrzave);
                int idGrad = result.getInt(3);
                glavniGrad.setBr(idGrad);
                String nazivGrad = result.getString(4);
                glavniGrad.setIme(nazivGrad);
                int brojStanovnika = result.getInt(5);
                glavniGrad.setBrStanovnika(brojStanovnika);
            }
        } catch (SQLException ignored) {
            System.out.println("Data drzava ne postoji");
            return null;
        }
        return drzavaResult;
    }

    private int dajSljedeciID(String nazivTabele) throws SQLException {
        tempState = konekcija.prepareStatement("SELECT id FROM " + nazivTabele + " ORDER BY id DESC LIMIT 1");
        var result = tempState.executeQuery();
        int id = 0;
        while (result.next()) id = result.getInt(1);
        return id + 1;
    }

    private int dajGradIDAkoPostoji(String naziv) throws SQLException {
        tempState = konekcija.prepareStatement("SELECT id FROM grad WHERE naziv = ? AND broj_stanovnika IS NULL");
        tempState.setString(1, naziv);
        var result = tempState.executeQuery();
        int id = -1;
        while (result.next()) id = result.getInt(1);
        return id;
    }

    public void dodajGrad(Grad grad) {
        try {
            int idAkoPostoji = dajGradIDAkoPostoji( grad.getIme());
            if (idAkoPostoji != -1) {
                grad.setBr(idAkoPostoji);
                tempState = konekcija.prepareStatement("SELECT id FROM drzava WHERE glavni_grad = ?");
                tempState.setInt(1, idAkoPostoji);
                var result = tempState.executeQuery();
                int id = -1;
                while (result.next()) id = result.getInt(1);
                Drzava temp = new Drzava();
                temp.setBr(id);
                grad.setDrzava(temp);
                izmijeniGrad(grad);
                return;
            }

            tempState = konekcija.prepareStatement("SELECT id FROM drzava WHERE naziv = ?");
            tempState.setString(1, grad.getDrzava().getIme());
            ResultSet result = tempState.executeQuery();
            int brojac = 0;
            int idDrzave = 0;
            while (result.next()) {
                idDrzave = result.getInt(1);
                brojac++;
            }

            int sljedeciIDGrad = dajSljedeciID("grad");
            tempState = konekcija.prepareStatement("INSERT INTO grad VALUES (?, ?, ?, ?)");
            tempState.setInt(1, sljedeciIDGrad);
            tempState.setString(2, grad.getIme());
            tempState.setInt(3, grad.getBrStanovnika());
            if (brojac == 0) {
                tempState.setNull(4, Types.INTEGER);
            } else {
                tempState.setInt(4, idDrzave);
            }
            tempState.executeUpdate();

            if (brojac == 0) {
                int sljedeciIDDrzava = dajSljedeciID("drzava");
                tempState = konekcija.prepareStatement("INSERT INTO drzava VALUES (?, ?, ?)");
                tempState.setInt(1, sljedeciIDDrzava);
                tempState.setString(2, grad.getDrzava().getIme());
                tempState.setInt(3, sljedeciIDGrad);
                tempState.executeUpdate();
            }
        } catch (SQLException ignored) {
            System.out.println("Greska");
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            tempState = konekcija.prepareStatement("SELECT id FROM grad WHERE naziv = ?");
            tempState.setString(1, drzava.getGlavniGrad().getIme());
            ResultSet result = tempState.executeQuery();
            int brojac = 0;
            int idGrada = 0;
            while (result.next()) {
                idGrada = result.getInt(1);
                brojac++;
            }

            int sljedeciIDDrzava = dajSljedeciID("drzava");
            tempState = konekcija.prepareStatement("INSERT INTO drzava VALUES (?, ?, ?)");
            tempState.setInt(1, sljedeciIDDrzava);
            tempState.setString(2, drzava.getIme());
            if (brojac == 0) {
                tempState.setNull(3, Types.INTEGER);
            } else {
                tempState.setInt(3, idGrada);
            }
            tempState.executeUpdate();

            if (brojac == 0) {
                int sljedeciIDGrad = dajSljedeciID("grad");
                tempState = konekcija.prepareStatement("INSERT INTO grad VALUES (?, ?, NULL, ?)");
                tempState.setInt(1, sljedeciIDGrad);
                tempState.setString(2, drzava.getGlavniGrad().getIme());
                tempState.setInt(3, sljedeciIDDrzava);
                tempState.executeUpdate();
                tempState = konekcija.prepareStatement("UPDATE drzava SET glavni_grad = ? WHERE id = ?");
                tempState.setInt(1, sljedeciIDGrad);
                tempState.setInt(2, sljedeciIDDrzava);
                tempState.executeUpdate();
            }
        } catch (SQLException ignored) {
            System.out.println("Greska");
        }
    }

    public void izmijeniGrad(Grad grad) {
        try{
            tempState = konekcija.prepareStatement("UPDATE grad SET naziv = ?, broj_stanovnika = ? , drzava= ?, WHERE id = ?");
            tempState.setString(1, grad.getIme());
            tempState.setInt(2, grad.getBrStanovnika());
            tempState.setInt(3, grad.getDrzava().getBr());
            tempState.setInt(4, grad.getBr());
            System.out.println("Uspjesno izmijenjeno " + tempState.executeUpdate() + " red");
        }catch (SQLException ignored){
            System.out.println("Grad ne postoji!");
        }
    }
}


