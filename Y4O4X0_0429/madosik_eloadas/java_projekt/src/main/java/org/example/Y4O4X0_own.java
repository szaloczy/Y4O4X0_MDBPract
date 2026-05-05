package org.example;


public class Y4O4X0_own {
    public static void main(String[] args) {
        String conn = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(conn)) {

            MongoDatabase db = mongoClient.getDatabase("CegAdatbazis");

            MongoCollection<Document> cegColl = db.getCollection("ceg");
            MongoCollection<Document> gyakColl = db.getCollection("gyakornok");
            MongoCollection<Document> tulajColl = db.getCollection("tulajdonos");
            MongoCollection<Document> megrColl = db.getCollection("megrendelo");
            MongoCollection<Document> fejlesztoColl = db.getCollection("fejleszto");
            MongoCollection<Document> projektColl = db.getCollection("projekt");

            // JSON beolvasása
            String raw = Files.readString(Paths.get("cegek.json"));
            Document root = Document.parse(raw).get("cegek", Document.class);

            // --- 1) CÉGEK ---
            List<Document> cegek = root.getList("ceg", Document.class);
            List<Document> formattedCeg = cegek.stream().map(doc -> {
                doc.put("_id", doc.getString("_ckod"));
                doc.remove("_ckod");
                return doc;
            }).collect(Collectors.toList());
            cegColl.insertMany(formattedCeg);

            // --- 2) GYAKORNOKOK ---
            List<Document> gyakornokok = root.getList("gyakornok", Document.class);
            List<Document> formattedGyak = gyakornokok.stream().map(doc -> {
                doc.put("_id", doc.getString("_gykod"));
                doc.remove("_gykod");
                return doc;
            }).collect(Collectors.toList());
            gyakColl.insertMany(formattedGyak);

            // --- 3) TULAJDONOSOK ---
            List<Document> tulajok = root.getList("tulajdonos", Document.class);
            List<Document> formattedTulaj = tulajok.stream().map(doc -> {
                doc.put("_id", doc.getString("_tkod"));
                doc.remove("_tkod");

                if (doc.containsKey("eletkor")) {
                    doc.put("eletkor", Integer.parseInt(doc.getString("eletkor")));
                }
                return doc;
            }).collect(Collectors.toList());
            tulajColl.insertMany(formattedTulaj);

            // --- 4) MEGRENDELŐK ---
            List<Document> megrendelok = root.getList("megrendelo", Document.class);
            List<Document> formattedMegr = megrendelok.stream().map(doc -> {
                doc.put("_id", doc.getString("_mkod"));
                doc.remove("_mkod");
                return doc;
            }).collect(Collectors.toList());
            megrColl.insertMany(formattedMegr);

            // --- 5) FEJLESZTŐK ---
            List<Document> fejlesztok = root.getList("fejleszto", Document.class);
            List<Document> formattedFejl = fejlesztok.stream().map(doc -> {
                doc.put("_id", doc.getString("_fkod"));
                doc.remove("_fkod");

                if (doc.containsKey("fizetes")) {
                    doc.put("fizetes", Integer.parseInt(doc.getString("fizetes")));
                }
                return doc;
            }).collect(Collectors.toList());
            fejlesztoColl.insertMany(formattedFejl);

            // --- 6) PROJEKTEK ---
            List<Document> projektek = root.getList("projekt", Document.class);
            List<Document> formattedProj = projektek.stream().map(doc -> {

                if (doc.containsKey("osszeg")) {
                    doc.put("osszeg", Integer.parseInt(doc.getString("osszeg")));
                }
                return doc;
            }).collect(Collectors.toList());
            projektColl.insertMany(formattedProj);

            // --- LEKÉRDEZÉSEK ---

            System.out.println("\n--- a) Összes cég ---");
            cegColl.find().forEach(d -> System.out.println(d.toJson()));

            System.out.println("\n--- b) Gyakornokok, akiknek több mentora van ---");
            gyakColl.find(size("mentor", 2)).forEach(d -> System.out.println(d.toJson()));

            System.out.println("\n--- c) Tulajdonosok 50 év felett ---");
            tulajColl.find(gt("eletkor", 50)).forEach(d -> System.out.println(d.toJson()));

            System.out.println("\n--- d) Fejlesztők átlagfizetése ---");
            Document atlag = fejlesztoColl.aggregate(
                    Arrays.asList(group(null, avg("atlagFizetes", "$fizetes")))
            ).first();
            System.out.println(atlag.toJson());

            System.out.println("\n--- e) Projektek és megrendelőik (JOIN) ---");
            List<Document> join = projektColl.aggregate(Arrays.asList(
                    lookup("megrendelo", "_m_p", "_id", "megrendelo_info")
            )).into(new ArrayList<>());
            join.forEach(d -> System.out.println(d.toJson()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
