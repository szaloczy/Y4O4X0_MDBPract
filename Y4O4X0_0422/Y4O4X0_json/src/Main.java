import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ObjectMapper m = new ObjectMapper();

        JsonNode root = m.readTree(new File("JSON.json"));
        JsonNode vendeglatas = root.get("vendeglatas");
        JsonNode foszakacsok = vendeglatas.get("foszakacs");
        JsonNode szakacsok = vendeglatas.get("szakacs");
        JsonNode ettermek = vendeglatas.get("etterem");
        JsonNode rendelesek = vendeglatas.get("rendeles");
        JsonNode vendegek = vendeglatas.get("vendeg");


        JsonNode schemaNode = m.readTree(new File("JSON_SCHEMA.json"));
        JsonSchema schema = JsonSchemaFactory
        .getInstance(SpecVersion.VersionFlag.V4)
        .getSchema(schemaNode);

        Set<ValidationMessage> errors = schema.validate(root);

        if (errors.isEmpty()) {
            System.out.println("Valid JSON");
        } else {
        System.out.println("Hibas JSON:");
            errors.forEach(e-> System.out.println(e.getMessage()));
        }

        System.out.println("=== Foszakacsok adatai ===");
            if (foszakacsok != null && foszakacsok.isArray()) {
                for (JsonNode szakacs : foszakacsok) {

                String nev = szakacs.get("nev").asText();
                String kor = szakacs.get("eletkor").asText();
                String iskola = szakacs.get("vegzettseg").asText();
                System.out.println("Nev: " + nev + " | Kor: " + kor + " | Vegzettseg: " + iskola);
            }
        }

        System.out.println("===  ETTERMEK  ES SZAKACSAIK ===");

        for (JsonNode etterem : ettermek) {
            String eKod = etterem.get("_ekod").asText();
            String eNev = etterem.get("nev").asText();
            System.out.println("\n Etterem: " + eNev + " [" + eKod + "]");

            System.out.println("---------------------------");

            for (JsonNode szakacs : szakacsok) {
                if (szakacs.get("_e_sz").asText().equals(eKod)) {
                    System.out.println("- " + szakacs.get("nev").asText() +
                " (" + szakacs.get("reszleg").asText() + ")");
                }
            }
        }


        System.out.println("=== Átlagos rendelési érték ===" );
        double osszeg = 0;
        int db = 0;

        for (JsonNode r : rendelesek) {
            osszeg += r.get("osszeg").asDouble();
            db ++;
        }
        System.out.println("AVG: " + ( osszeg / db) + "Ft");

        System.out.println("\n=== 5. Feladat: JSON adatok manipul´aci´oja ===");
        for (JsonNode e : ettermek) {
            ObjectNode obj = (ObjectNode) e;
            obj.put("ellenorzott", true);
            obj.remove("csillag");
        }

        Map<String, Double> koltesek = new HashMap<>();
        for (JsonNode r : rendelesek) {
            String vKod = r.get("_vkod").asText();
            double ar = r.get("osszeg").asDouble();
            koltesek.put(vKod, koltesek.getOrDefault(vKod, 0.0) + ar);
        }

        String maxVkod = "";
        double maxOsszeg = 0;

        for (Map.Entry<String, Double> entry : koltesek.entrySet()) {
            if (entry.getValue() > maxOsszeg) {
                maxOsszeg = entry.getValue();
                maxVkod = entry.getKey();
            }
        }

        String vipNev = "";
        for (JsonNode v : vendegek) {
            if (v.get("_vkod").asText().equals(maxVkod)) {
                vipNev = v.get("nev").asText();
                break;
            }
        }
        System.out.println("VIP guest: " + vipNev + " (Total spent: " + maxOsszeg + " HUF)");

        System.out.println("\n=== 8. Feladat: ´ Uj JSON f´ajl k´esz´ıt´ese ´es ment´ese ===");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode ujLista = mapper.createArrayNode();
            for (JsonNode e : ettermek) {
                String eKod = e.get("_ekod").asText();
                double bevetel = 0;
                
            for (JsonNode r : rendelesek) {
            if (r.get("_ekod").asText().equals(eKod)) {
            bevetel += r.get("osszeg").asDouble();
            }
        }
        ObjectNode csomopont = mapper.createObjectNode();
        csomopont.put("etterem_nev", e.get("nev").asText());
        csomopont.put("osszes_bevetel", bevetel);

        ujLista.add(csomopont);
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("uj_bevetel.json"), ujLista);

        System.out.println("F´ajl ki´ırva: uj_bevetel.json");
    }
}