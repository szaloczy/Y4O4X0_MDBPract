using System;
using System.Linq;
using System.Xml.Linq;

namespace LinqApp
{
    internal class Program
    {
        static void Main(string[] args)
        {
            XDocument document = XDocument.Load("./xml/cegek.xml");
            XElement root = document.Descendants("cegek").First();

            ListCompanies(root);
            InternsWithMentors(root);
            ComplexProjectQuery(root);
            AverageDeveloperSalary(root);
            ModifyProjectAmounts(root);
            DeleteLowPriorityProjects(root);
        }

        // (1) List all companies
        static void ListCompanies(XElement root)
        {
            Console.WriteLine("(1.) Companies:\n");

            var companies = root.Descendants("ceg")
                .Select(c => new
                {
                    Name = c.Element("nev")?.Value,
                    City = c.Element("cim")?.Element("varos")?.Value
                })
                .ToList();

            companies.ForEach(c =>
                Console.WriteLine($" - {c.Name} ({c.City})")
            );
        }

        // (2) Interns + mentors
        static void InternsWithMentors(XElement root)
        {
            Console.WriteLine("\n(2.) Interns and mentors:\n");

            var interns = root.Descendants("gyakornok")
                .Select(g => new
                {
                    Name = g.Element("nev")?.Value,
                    CompanyID = g.Attribute("c_gy")?.Value,
                    Mentors = g.Elements("mentor").Select(m => m.Value).ToList()
                })
                .ToList();

            interns.ForEach(i =>
            {
                Console.WriteLine($"{i.Name} (Company {i.CompanyID})");
                i.Mentors.ForEach(m => Console.WriteLine($"   - Mentor: {m}"));
            });
        }

        // (3) Complex join: project → company → client
        static void ComplexProjectQuery(XElement root)
        {
            Console.WriteLine("\n(3.) Project overview:\n");

            var query = root.Descendants("projekt")
                .Select(p =>
                {
                    var companyID = p.Attribute("c_v_p")?.Value;
                    var clientID = p.Attribute("m_p")?.Value;

                    var companyName = root.Descendants("ceg")
                        .Where(c => c.Attribute("ckod")?.Value == companyID)
                        .FirstOrDefault()?
                        .Element("nev")?.Value;

                    var clientName = root.Descendants("megrendelo")
                        .Where(m => m.Attribute("mkod")?.Value == clientID)
                        .FirstOrDefault()?
                        .Element("nev")?.Value;

                    return new
                    {
                        Project = p.Element("nev")?.Value,
                        Company = companyName,
                        Client = clientName,
                        Amount = p.Element("osszeg")?.Value,
                        Deadline = p.Element("hatarido")?.Value
                    };
                })
                .ToList();

            query.ForEach(item =>
                Console.WriteLine($"{item.Project} | {item.Company} → {item.Client} | {item.Amount} Ft | {item.Deadline}")
            );
        }

        // (4) Average developer salary
        static void AverageDeveloperSalary(XElement root)
        {
            Console.WriteLine("\n(4.) Average developer salary:\n");

            var avg = root.Descendants("fejleszto")
                .Select(f => double.Parse(f.Element("fizetes")?.Value ?? "0"))
                .Average();

            Console.WriteLine($"Average salary: {avg} Ft");
        }

        // (5) Modify: double project amounts
        static void ModifyProjectAmounts(XElement root)
        {
            Console.WriteLine("\n(5.) Doubling project amounts and saving...\n");

            root.Descendants("projekt")
                .ToList()
                .ForEach(p =>
                {
                    var amountElement = p.Element("osszeg");
                    double amount = double.Parse(amountElement.Value);
                    amount *= 2;
                    amountElement.Value = amount.ToString();
                });

            XDocument modified = new XDocument(root);
            modified.Save("modified_cegek.xml");
        }

        // (6) Delete low-priority projects
        static void DeleteLowPriorityProjects(XElement root)
        {
            Console.WriteLine("\n(6.) Deleting all 'Közepes' priority projects...\n");

            root.Descendants("projekt")
                .Where(p => p.Element("prioritas")?.Value == "Közepes")
                .ToList()
                .ForEach(p => p.Remove());

            XDocument deleted = new XDocument(root);
            deleted.Save("deleted_cegek.xml");
        }
    }
}
