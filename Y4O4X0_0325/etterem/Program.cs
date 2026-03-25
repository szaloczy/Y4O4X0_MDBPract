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

            FiveStartRestaurants(root);
        }

        static void FiveStartRestaurants(XElement root)
        {
            Console.WriteLine("(1.) Five start restaurants: \n");
            var fiveStarRestaurants = root.Descendants("etterem")
                .Where(e => e.Descendants("csillag").First().Value == "5")
                .ToList();

            fiveStarRestaurants.ForEach(e => 
                Console.WriteLine(" - " + e.Descendants("nev").First().Value)
            );
        }
    }
}