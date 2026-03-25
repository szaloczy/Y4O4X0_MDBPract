using System;
using System.Xml.Linq;

namespace OwnLinq
{
    internal class Program
    {
        static void Main(string[] args)
        {
            XDocument document = XDocument.Load("./xml/Y4O4X0XMLOwn.xml");
            XElement root = document.Descendants("cegek").First();

            //Console.WriteLine("(0. ) Full document: \n\n" + root);

            internsWith12week(root);
        }

        static void internsWith12week(Xelement root)
        {
            
        }
    }
}