using System;
using System.Xml.Linq;

namespace LinqApp
{
    internal class Program
    {
        static void Main(string[] args)
        {
            XDocument document = XDocument.Load("./xml/etterem.xml");
            XElement root = document.Descendants("vendeglatas").First();

            //Console.WriteLine("(0. ) Full document: \n\n" + root);

            FiveStarRestaurants(root);
            ComplexQuery(root);
            Aggregation1(root);
            ModifyDocument(root);
            Delete(root);
        }

        static void FiveStarRestaurants(XElement root)
        {
            Console.WriteLine("(1.) Five start restaurants: \n");
            var fiveStarRestaurants = root.Descendants("etterem")
                .Where(e => e.Descendants("csillag").First().Value == "5")
                .ToList();

            fiveStarRestaurants.ForEach(e => 
                Console.WriteLine(" - " + e.Descendants("nev").First().Value)
            );
        }

        static void ComplexQuery(XElement root)
        {
            Console.WriteLine("(2. ) Guest details: \n");

            var triplexJoin = root.Descendants("rendeles")
                .Select(e =>
                {
                    var guestID = e.Attribute("e_v_v")?.Value;

                    var guestName = root.Descendants("vendeg")
                        .Where(v => v.Attribute("vkod")?.Value == guestID)
                        .FirstOrDefault()?
                        .Descendants("nev")
                        .FirstOrDefault()?
                        .Value;

                    var restaurantID = e.Attribute("e_v_e")?.Value;

                    var restaurantName = root.Descendants("etterem")
                        .Where(r => r.Attribute("ekod")?.Value == restaurantID)
                        .FirstOrDefault()?
                        .Descendants("nev")
                        .FirstOrDefault()?
                        .Value;

                    var order = e.Descendants("etel").FirstOrDefault()?.Value;
                    var sum = e.Descendants("osszeg").FirstOrDefault()?.Value;

                    return new
                    {
                        Guest = guestName,
                        Restaurant = restaurantName,
                        Food = order,
                        Sum = sum
                    };
                })
                .ToList();

            triplexJoin.ForEach(item =>
                Console.WriteLine($"{item.Guest} - {item.Restaurant} - {item.Food} - {item.Sum}")
            );
        }

        static void Aggregation1(XElement root)
        {
            var avgSpend =  root.Descendants("rendeles")
                .Select(order => order.Descendants("osszeg").First().Value)
                .Average(osszeg => double.Parse(osszeg));

            System.Console.WriteLine($"(3. ) Average spending: {avgSpend}");
        }

        static void ModifyDocument(XElement root)
        {
            System.Console.WriteLine("(4. ) Doubling all order and saving in a new file \n");

            root.Descendants("rendeles")
                .ToList()
                .ForEach(order =>
                {
                    var sumElement = order.Descendants("osszeg").First();
                    var sum = double.Parse(sumElement.Value);

                    sum *= 2;

                    sumElement.Value = sum.ToString();
                });

                XDocument modifiedDocument = new XDocument(root);
                modifiedDocument.Save("modified_etterem.xml");
        }
    
        static void Delete(XElement root)
        {
            System.Console.WriteLine("(5. ) Delete all the 3 star restaurent: \n");

            root.Descendants("etterem")
                .Where(e => e.Descendants("csillag").First().Value == "3")
                .ToList()
                .ForEach(e =>
                {
                    e.Remove();
                });

                XDocument deletedeDocuemnt = new XDocument(root);
                deletedeDocuemnt.Save("deleted_etterem.xml");
        }
    
    }
}