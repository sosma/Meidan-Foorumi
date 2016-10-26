package tikape.runko;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import tikape.runko.database.AlueDao;
import tikape.runko.database.KeskustelunavausDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.database.Database;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Keskustelunavaus;
import tikape.runko.domain.Viesti;


//Moi tiimi! Jotta tämä toimii, tulisi Database.javaan liittyä joku tietokantataulu
//Muuten "toimii", mutta ei tietenkään löydä mitään. Voin vielä säätää tätä myöh.
//Olisi pitänyt käyttää enempi metodeja tässä, mutta copypastella meni.
//Myös täytyy mainiin lisätä:
//
//    public static void main(String[] args) throws ClassNotFoundException {
//
//        Tekstikayttoliittyma testi = new Tekstikayttoliittyma();
//    }
//


public class Tekstikayttoliittyma {

    private Scanner lukija;

    private AlueDao aluedao;
    private ViestiDao viestidao;
    private KeskustelunavausDao keskustelunavausdao;

    public Tekstikayttoliittyma() throws ClassNotFoundException {
        Database database = new Database("jdbc:sqlite:opiskelijat.db");

        database.init();
        //Insert constructor here
        //Luo Dao-oliot
        aluedao = new AlueDao(database);
        keskustelunavausdao = new KeskustelunavausDao(database);
        viestidao = new ViestiDao(database);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Terve! Mitä haluat testata? Alue (1), Keskustelunavaus (2), Viesti (3)");
        int testaus = 0;
        String kysymys = scanner.nextLine();
        switch (kysymys) {
            case "1":

                try {
                    List<Alue> Kokeilu = aluedao.etsiKaikki();
                    System.out.println("Etsitään kaikki alueet: ");
                    for (Alue a : Kokeilu) {
                        System.out.println(a.getNimi());
                    }
                } catch (SQLException ex) {
                    System.out.println("Komento ei toiminut. Kaikkeutta ei löytynyt");
                }

//        //Toistaiseksi tallenus ei toimi, niin tätä ei voi testata.
//        System.out.println("Mikä alue tallennetaan? Anna nimi: ");
//        String aluetallennus = scanner.nextLine();
//        int aluehakuu = Integer.parseInt(aluetallennus);
//
//        try {
//            List<Alue> Kokeilutallennus = aluedao.tallenna(Alue aluehakuu);
//            System.out.println("Tallennettu");
//        } catch (SQLException ex) {
//            System.out.println("Ei tallentunut");
//        }
                System.out.println("Mikä alue haetaan? Anna ID: ");
                String aluehaku = scanner.nextLine();
                int aluehakuq = Integer.parseInt(aluehaku);

                try {
                    List<Alue> Kokeilu2 = aluedao.etsiTietyt(aluehakuq);
                    System.out.println("Yritetään etsiä yksi");
                    for (Alue b : Kokeilu2) {
                        System.out.println("a.getNimi");
                    }

                } catch (SQLException ex) {
                    System.out.println("Komento ei toiminut tai aluetta ei löytynyt");
                }

                break;
            case "2":

                try {
                    List<Keskustelunavaus> Kokeilu2 = keskustelunavausdao.etsiKaikki();
                    System.out.println("Etsitään kaikki keskustelunavaukset: ");
                    for (Keskustelunavaus a : Kokeilu2) {
                        System.out.println(a.getOtsikko());
                    }
                } catch (SQLException ex) {
                    System.out.println("Komento ei toiminut. Kaikkeutta ei löytynyt");
                }

//        //Toistaiseksi tallenus ei toimi, niin tätä ei voi testata.
//        System.out.println("Mikä keskustelunavaus tallennetaan? Anna nimi: ");
//        String keskustelutallennus = scanner.nextLine();
//        int aluehakug = Integer.parseInt(keskustelutallennus);
//
//        try {
//            List<Keskustelunavaus> Kokeilutallennus = keskustelunavausdao.tallenna(Alue aluehakug);
//            System.out.println("Tallennettu");
//        } catch (SQLException ex) {
//            System.out.println("Ei tallentunut");
//        }
                System.out.println("Mikä keskustelu haetaan? Anna ID: ");
                String keskustelunavaushaku = scanner.nextLine();
                int keskustelunvausz = Integer.parseInt(keskustelunavaushaku);

                try {
                    List<Keskustelunavaus> Kokeilu2 = keskustelunavausdao.etsiTietyt(keskustelunvausz);
                    System.out.println("Yritetään etsiä yksi");
                    for (Keskustelunavaus b : Kokeilu2) {
                        System.out.println("a.getNimi");
                    }

                } catch (SQLException ex) {
                    System.out.println("Komento ei toiminut tai keskustelunavausta ei löytynyt");
                }

                break;
            case "3":

                try {
                    List<Viesti> Kokeilu3 = viestidao.etsiKaikki();
                    System.out.println("Etsitään kaikki viestit: ");
                    for (Viesti a : Kokeilu3) {
                        System.out.println(a.getViesti());
                    }
                } catch (SQLException ex) {
                    System.out.println("Komento ei toiminut. Kaikkeutta ei löytynyt");
                }

//        //Toistaiseksi tallenus ei toimi, niin tätä ei voi testata.
//        System.out.println("Mikä viesti tallennetaan? Anna nimi: ");
//        String viestitallennus = scanner.nextLine();
//        int viestig = Integer.parseInt(viestitallennus);
//
//        try {
//            List<Viesti> Kokeilutallennus = viestidao.tallenna(Viesti viestig);
//            System.out.println("Tallennettu");
//        } catch (SQLException ex) {
//            System.out.println("Ei tallentunut");
//        }
                System.out.println("Mikä keskustelu haetaan? Anna ID: ");
                String viestihaku = scanner.nextLine();
                int viestiz = Integer.parseInt(viestihaku);

                try {
                    List<Viesti> Kokeilu2 = viestidao.etsiTietyt(viestiz);
                    System.out.println("Yritetään etsiä yksi");
                    for (Viesti b : Kokeilu2) {
                        System.out.println("a.getViesti");
                    }

                } catch (SQLException ex) {
                    System.out.println("Komento ei toiminut tai viestiä ei löytynyt");
                }

                break;
            default:
                System.out.println("Anna 1,2 tai 3, muuten en toimi...");
        }

    }

    //Tarjoa käyttäjälle eri toiminnallisuuksia
    //esim. näytä kaikki Alueet
    // Toteuta toiminta Daojen avulla
    //esim. alueDao.etsiKaikki();
}