
db.cegek.insertMany(raw.ceg)
db.gyakornokok.insertMany(raw.gyakornok)
db.tulajdonosok.insertMany(raw.tulajdonos)
db.megrendelok.insertMany(raw.megrendelo)
db.fejlesztok.insertMany(raw.fejleszto)
db.projektek.insertMany(raw.projekt)


show collections

db.cegek.find()

db.gyakornokok.find()

db.cegek.find({}, { nev: 1, "cim.varos": 1, _id: 0 })

db.cegek.find({ "cim.varos": "Debrecen" })

db.fejlesztok.find({ fizetes: { $gte: 700000 } })

db.cegek.updateOne(
  { nev: "AlphaSoft Kft." },
  { $set: { "cim.utca": "Kossuth utca" } }
)

db.gyakornokok.updateOne(
  { nev: "Kiss Péter" },
  { $push: { mentor: "Kelemen Gábor" } }
)

db.fejlesztok.updateOne(
  { nev: "Horváth Márton" },
  { $set: { fizetes: 750000 } }
)

db.projektek.deleteMany({ osszeg: { $lt: 6000000 } })


db.cegek.aggregate([
  {
    $group: {
      _id: "$cim.varos",
      cegek_szama: { $sum: 1 }
    }
  },
  { $sort: { cegek_szama: -1 } }
])

db.fejlesztok.aggregate([
  { $unwind: "$vegzettseg" },
  {
    $group: {
      _id: "$vegzettseg",
      fejlesztok_szama: { $sum: 1 }
    }
  },
  { $sort: { fejlesztok_szama: -1 } }
])

db.projektek.aggregate([
  {
    $lookup: {
      from: "megrendelok",
      localField: "_m_p",
      foreignField: "_mkod",
      as: "megrendelo_adatok"
    }
  },
  { $unwind: "$megrendelo_adatok" },
  {
    $project: {
      _id: 0,
      projekt: "$nev",
      osszeg: 1,
      hatarido: 1,
      megrendelo: "$megrendelo_adatok.nev",
      varos: "$megrendelo_adatok.cim.varos"
    }
  }
])

db.cegek.createIndex({ nev: 1 })

db.cegek.find({ nev: "AlphaSoft Kft." }).explain("executionStats")

