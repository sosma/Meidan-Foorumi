//lisää keskustelunavaus
//ei tyhjiä alueita tai keskusteluja
package tikape.runko;

import java.nio.charset.Charset;
import java.util.*;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.get;
import static spark.Spark.post;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KeskustelunavausDao;
import tikape.runko.database.ViestiDao;
import org.apache.commons.lang.StringEscapeUtils;



public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        Spark.staticFileLocation("/templates");
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.init();
        
        AlueDao alueDao = new AlueDao(database);
        KeskustelunavausDao keskustelunavausDao = new KeskustelunavausDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.haeAlueetViesteineen()); 
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            String alueNimi = req.queryParams("alue");
            alueNimi = alueNimi.trim();
            System.out.println("Alueen nimi: " + alueNimi);
            if (alueNimi.length() > 2 && alueNimi.length() < 25) {           //tarkistaa, että alueen nimi ei ole tyhjä tai pelkkiä välilyöntejä
                System.out.println("Alueen nimen pituus on " + alueNimi.length());
                alueDao.tallenna(req.queryParams("alue"));
            }
            res.redirect("/");
            return "ok";
        });
        
        get("/alue/:alueid", (req, res) -> {
            HashMap map = new HashMap<>();
            //palauta keskustelunavausten otsikot, viimeisten viestien lukumäärä ja viimeisen viestin aika
            map.put("keskustelunavaukset", keskustelunavausDao.haeKeskustelunavauksetViesteineen(Integer.parseInt(req.params(":alueid"))));
            map.put("alue", alueDao.etsi(Integer.parseInt(req.params(":alueid"))));
            return new ModelAndView(map, "keskustelunavaukset");
        }, new ThymeleafTemplateEngine());

        post("/alue/:alueid", (req, res) -> { //alueen id  --> näyttää keskustelut
            // vanha metodi, joka tallensi suoraan --> Integer keskustelunavausId = keskustelunavausDao.tallenna(req.queryParams("keskustelunavaus"), Integer.parseInt(req.params(":alueid")));
            //req.params palauttaa Stringin, jonka Integer.parseInt muuttaa luvuksi
            //String keskustelu = req.queryParams("keskustelunavaus"); //tallentaa syötetyn keskustelunavauksen nimen
            //keskustelu = keskustelu.trim();
            /*if (keskustelu.length() < 3 || keskustelu.length() > 35) {                  // ei tyhjää otsikkoa
                res.redirect("/alue/" + req.params(":alueid"));
                return "ok";
            }*/
            res.redirect("/alue/" + req.params(":alueid") + "/luouusikeskustelu"); //ohjataan luomaan viesti --keskustelunavauksen id
            return "ok";
        });
        
        get("/alue/:alueid/keskustelu/:keskusteluid", (req, res) -> { 
            HashMap map = new HashMap<>();
            map.put("viestit", viestiDao.etsiKeskustelunViestit(Integer.parseInt(req.params(":alueid")), Integer.parseInt(req.params(":keskusteluid")))); //ei toimi
            map.put("keskustelu", keskustelunavausDao.etsi(Integer.parseInt(req.params(":keskusteluid"))));
            map.put("alue", alueDao.etsi(Integer.parseInt(req.params(":alueid"))));
            
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
        
        post("/alue/:alueid/keskustelu/:keskusteluid", (req, res) -> {
            String lähettäjä = req.queryParams("lähettäjä");
            String viesti = req.queryParams("viesti");
            if (lähettäjä.trim().length() != 0 && viesti.trim().length() != 0) { //ei tyhjää viestiä tai lähettäjää
                viestiDao.tallenna(req.queryParams("lähettäjä"), req.queryParams("viesti"), Integer.parseInt(req.params(":keskusteluid"))); //Viesti (aika, lähettäjä, viesti, keskustelunavaus)
            }
            res.redirect("/alue/" + req.params(":alueid") + "/keskustelu/" + Integer.parseInt(req.params(":keskusteluid")));
            return "ok";
        });
        
        get("/alue/:alueid/luouusikeskustelu", (req, res) -> { //näyttää sivun, jossa pyydetään keskustelunavaukselle 
            HashMap map = new HashMap<>();                                      //viestin ennen kuin se luodaan
            //String keskusteluNimi = req.params(":keskusteluNimi");
            //map.put("keskustelu", keskustelu);
            map.put("alue", alueDao.etsi(Integer.parseInt(req.params(":alueid"))));
            
            return new ModelAndView(map, "uusiKeskustelu");
        }, new ThymeleafTemplateEngine());
        
        post("/alue/:alueid/luouusikeskustelu", (req, res) -> { //tallentaa annetun keskustelunavauksen ja viestin
            String keskusteluNimi = req.queryParams("keskustelunavaus");
            String lähettäjä = req.queryParams("lähettäjä");
            String viesti = req.queryParams("viesti");
            System.out.println("KeskusteluNimi: " + keskusteluNimi);
            System.out.println("lähettäjä: " + lähettäjä);
            System.out.println("viesti: " + viesti);
            if (lähettäjä.trim().length() == 0 || viesti.trim().length() == 0 || keskusteluNimi.trim().length() < 2 || keskusteluNimi.trim().length() > 35) {
                res.redirect("/alue/" + req.params(":alueid") + "/luouusikeskustelu");
                return "ok";
            }
            Integer keskustelunavausId = keskustelunavausDao.tallenna(keskusteluNimi,Integer.parseInt(req.params(":alueid")));
            viestiDao.tallenna(lähettäjä, viesti, keskustelunavausId); //Viesti (aika, lähettäjä, viesti, keskustelunavaus)
            res.redirect("/alue/" + req.params(":alueid") + "/keskustelu/" + keskustelunavausId);
            return "ok";
        });
    }
    
}
