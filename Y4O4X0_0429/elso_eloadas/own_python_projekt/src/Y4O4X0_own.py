import pymongo as mongo

# Kapcsolódás a MongoDB szerverhez
client = mongo.MongoClient("mongodb://localhost:27017/")
db = client.local

# Collectionök létrehozása
ceg_coll = db["ceg"]
gyakornok_coll = db["gyakornok"]
tulajdonos_coll = db["tulajdonos"]
megrendelo_coll = db["megrendelo"]
fejleszto_coll = db["fejleszto"]
projekt_coll = db["projekt"]

# Törlés, hogy tiszta legyen a feltöltés
ceg_coll.delete_many({})
gyakornok_coll.delete_many({})
tulajdonos_coll.delete_many({})
megrendelo_coll.delete_many({})
fejleszto_coll.delete_many({})
projekt_coll.delete_many({})

# Adatok a JSON alapján
cegek = [
    {
        "_id": "1",
        "nev": "AlphaSoft Kft.",
        "cim": {"varos": "Miskolc", "utca": "Fő utca", "hazszam": "12"}
    },
    {
        "_id": "2",
        "nev": "BetaTech Zrt.",
        "cim": {"varos": "Debrecen", "utca": "Bem tér", "hazszam": "8"}
    }
]

gyakornokok = [
    {
        "_id": "GY1",
        "nev": "Kiss Péter",
        "gyakorlat": {"kezdet": "2024-06-01", "idotartam": "8 hét"},
        "mentor": ["Nagy Anna", "Szabó Béla"],
        "ceg_id": "1"
    },
    {
        "_id": "GY2",
        "nev": "Tóth Eszter",
        "gyakorlat": {"kezdet": "2024-07-10", "idotartam": "12 hét"},
        "mentor": ["Kovács Imre"],
        "ceg_id": "2"
    }
]

tulajdonosok = [
    {
        "_id": "T1",
        "nev": "Nagy László",
        "eletkor": 45,
        "vegzettseg": ["Mérnök", "Közgazdász"],
        "ceg_id": "1"
    },
    {
        "_id": "T2",
        "nev": "Kovács Ádám",
        "eletkor": 52,
        "vegzettseg": ["Informatikus"],
        "ceg_id": "2"
    }
]

megrendelok = [
    {
        "_id": "M1",
        "nev": "Delta Corp",
        "hatarido": "2025-12-31",
        "cim": {"varos": "Budapest", "utca": "Kossuth Lajos utca", "hazszam": "5"}
    },
    {
        "_id": "M2",
        "nev": "Gamma Solutions",
        "hatarido": "2026-03-01",
        "cim": {"varos": "Szeged", "utca": "Petőfi Sándor utca", "hazszam": "22"}
    }
]

fejlesztok = [
    {
        "_id": "F1",
        "nev": "Szabó Dóra",
        "reszleg": "Backend",
        "fizetes": 650000,
        "vegzettseg": ["Programtervező informatikus", "Mérnökinformatikus"],
        "ceg_id": "1"
    },
    {
        "_id": "F2",
        "nev": "Horváth Márton",
        "reszleg": "Frontend",
        "fizetes": 720000,
        "vegzettseg": ["Programozó"],
        "ceg_id": "2"
    }
]

projektek = [
    {
        "_id": "P1",
        "nev": "Számlázó alkalmazás fejlesztés",
        "osszeg": 5000000,
        "hatarido": "2025-10-15",
        "prioritas": "Közepes",
        "ceg_id": "1",
        "megrendelo_id": "M1"
    },
    {
        "_id": "P2",
        "nev": "Webshop fejlesztés",
        "osszeg": 12000000,
        "hatarido": "2026-01-20",
        "prioritas": "Magas",
        "ceg_id": "2",
        "megrendelo_id": "M2"
    }
]

# Adatok feltöltése
ceg_coll.insert_many(cegek)
gyakornok_coll.insert_many(gyakornokok)
tulajdonos_coll.insert_many(tulajdonosok)
megrendelo_coll.insert_many(megrendelok)
fejleszto_coll.insert_many(fejlesztok)
projekt_coll.insert_many(projektek)

print("Adatok sikeresen feltöltve.\n")

# --- LEKÉRDEZÉSEK ---

print("--- Összes cég ---")
for c in ceg_coll.find():
    print(c)

print("\n--- Összes gyakornok ---")
for g in gyakornok_coll.find():
    print(g)

print("\n--- BetaTech Zrt. (ID: 2) ---")
print(ceg_coll.find_one({"_id": "2"}))

print("\n--- 700000 Ft alatti fizetésű fejlesztők ---")
for f in fejleszto_coll.find({"fizetes": {"$lt": 700000}}):
    print(f)

print("\n--- Tulajdonosok átlagéletkora ---")
pipeline_avg = [
    {"$group": {"_id": None, "atlag": {"$avg": "$eletkor"}}}
]
atlag = list(tulajdonos_coll.aggregate(pipeline_avg))[0]["atlag"]
print(f"Átlagéletkor: {atlag:.2f} év")

print("\n--- Backend fejlesztők és cégeik ---")
pipeline = [
    {"$match": {"reszleg": "Backend"}},
    {
        "$lookup": {
            "from": "ceg",
            "localField": "ceg_id",
            "foreignField": "_id",
            "as": "ceg_adatok"
        }
    }
]

for d in fejleszto_coll.aggregate(pipeline):
    ceg_nev = d["ceg_adatok"][0]["nev"] if d["ceg_adatok"] else "Nincs adat"
    print(f"{d['nev']} -> {ceg_nev}")

# --- MÓDOSÍTÁS ---
print("\n--- AlphaSoft Kft. címének módosítása ---")
ceg_coll.update_one(
    {"_id": "1"},
    {"$set": {"cim.utca": "Megyeház tér"}}
)

print("\n--- Cégek módosítás után ---")
for c in ceg_coll.find():
    print(c)

# --- TÖRLÉS ---
print("\n--- 50 év feletti tulajdonosok törlése ---")
torles = tulajdonos_coll.delete_many({"eletkor": {"$gt": 50}})
print(f"Törölt dokumentumok: {torles.deleted_count}")

print("\n--- Megmaradt tulajdonosok ---")
for t in tulajdonos_coll.find():
    print(t)
