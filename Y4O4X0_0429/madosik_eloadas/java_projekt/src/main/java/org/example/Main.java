package org.example;

public class Main {
    static void main() {
        String connectionString = "mongodb://localhost:27017"; 

         try (MongoClient mongoClient = MongoClients.create(connectionString)) { 
            MongoDatabase database = mongoClient.getDatabase("VendeglatasDB"); 
 
            MongoCollection<Document> collection = database.getCollection("etterem"); 
 
            Document e1 = new Document("_id", "e6") 
                    .append("nev", "Gundel") 
                    .append("cim", new Document("varos", "Budapest") 
                    .append("utca", "Gundel Károly") 
                    .append("hazszam", "14")) 
                    .append("tipus", "Magyaros"); 
 
            Document e2 = new Document("_id", "e7") 
                    .append("nev", "Trattoria") 
                    .append("cim", new Document("varos", "Szeged") 
                    .append("utca", "Oskola utca") 
                    .append("hazszam", "10")) 
                    .append("tipus", "Olasz"); 
 
            Document e3 = new Document("_id", "e8") 
                    .append("nev", "Sakura") 
                    .append("cim", new Document("varos", "Debrecen") 
                    .append("utca", "Piac utca") 
                    .append("hazszam", "22")) 
                    .append("tipus", "Japán"); 
 
            collection.insertMany(Arrays.asList(e1, e2, e3)); 
 
        } catch (Exception e) { 
            System.err.println("Hiba történt: " + e.getMessage()); 
            e.printStackTrace();
        }

        MongoCollection<Document> etteremColl = database.getCollection("etterem"); 
            String ettermekRaw = Files.readString(Paths.get("ettermek.json")); 
            List<Document> ettermekList = 
            Document.parse(ettermekRaw).getList("TempList", Document.class); 
            
                        MongoCollection<Document> szakacsColl = 
            database.getCollection("szakacsok"); 
                        String szakacsokRaw = Files.readString(Paths.get("szakacsok.json")); 
                        List<Document> szakacsokList = 
            Document.parse(szakacsokRaw).getList("TempList", Document.class); 

        List<Document> formattedEtteremList = ettermekList.stream().map(doc -> { 
                String ekod = doc.getString("ekod"); 
                doc.put("_id", ekod);
                doc.remove("ekod"); 
                return doc; 
            }).collect(Collectors.toList()); 
 
        etteremColl.insertMany(formattedEtteremList);

        List<Document> formattedSzakacsok = szakacsokList.stream().map(doc -> { 
                String szKod = doc.getString("sz_kod"); 
                doc.put("_id", szKod); 
                doc.remove("sz_kod"); 
 
                if (doc.containsKey("eletkor")) { 
                    String korStr = doc.getString("eletkor"); 
                    doc.put("eletkor", Integer.parseInt(korStr)); 
                } 
                return doc; 
            }).collect(Collectors.toList()); 
 
        szakacsColl.insertMany(formattedSzakacsok);

        System.out.println("--- a) Összes étterem lekérdezése ---"); 
            List<Document> ettermek = etteremColl.find().into(new ArrayList<>()); 
            for (Document doc : ettermek) { 
                System.out.println(doc.toJson()); 
        }

         System.out.println("\n--- b) Étterem lekérdezése (ekod: e3) ---"); 
            Document e3 = etteremColl.find(eq("_id", "e3")).first(); 
            if (e3 != null){ 
                System.out.println(e3.toJson()); 
        }

         System.out.println("\n--- c) Szakácsok, akik idősebbek 35 évnél ---"); 
            List<Document> szakacsok = szakacsColl.find(gt("eletkor", 35)).into(new ArrayList<>()); 
            for (Document doc : szakacsok) { 
                System.out.println(doc.toJson()); 
            }

         System.out.println("\n--- d) Szakács (40 éves) és étterme (JOIN) ---"); 
            List<Document> eredmeny = szakacsColl.aggregate(Arrays.asList( 
                    match(eq("eletkor", 40)), 
                    lookup("etterem", "e_sz", "ekod", "etterem_info") 
            )).into(new ArrayList<>()); 
 
            for (Document doc : eredmeny) { 
                System.out.println(doc.toJson()); 
            } 

          System.out.println("\n--- e) Szakácsok átlagos életkora ---"); 
             Document atlag = szakacsColl.aggregate(Arrays.asList( 
                    group(null, avg("atlagEletkor", "$eletkor")) 
            )).first(); 
            System.out.println("Átlagéletkor: " + atlag.get("atlagEletkor")); 

        etteremColl.updateOne( eq("_id", "e2"), new Document("$set", new Document("nev", "Új Étterem Név"))); 
        etteremColl.deleteOne(eq("_id", "e4")); 
        szakacsColl.deleteMany(lt("kor", 35));
    }
}
