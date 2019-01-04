package ba.unsa.etf.rpr;

import java.util.Scanner;

public class Main {
    private static GeografijaDAO dataBase = GeografijaDAO.getInstanca();

    public static void main(String[] args) {

        System.out.println("Gradovi:\n" + ispisiGradove());
        glavniGrad();
    }

    private static void glavniGrad() {
        Scanner ulaz = new Scanner(System.in);
        String d = ulaz.nextLine();
        var grad = dataBase.glavniGrad(d);
        System.out.println("Glavni grad države " + grad.getDrzava().getIme() + " je " + grad.getIme());
    }

    public static String ispisiGradove() {
        var gradovi = dataBase.gradovi();
        String result = "";
        for (var grad : gradovi)
            result += grad.toString() + "\n";
        return result;
    }
}