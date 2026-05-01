package org.example;

import com.novell.ldap.*;

import java.util.Arrays;

public class Main {
    static void main(String[] args) {
        LDAPConnection conn = new LDAPConnection();
        try {
            conn.connect("172.20.112.1", 389);

            conn.bind(
                    LDAPConnection.LDAP_V3,
                    "cn=admin,dc=demo,dc=local",
                    "admin"
            );

            System.out.println("Sikeres bind!");
            /*
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(new LDAPAttribute("objectClass", "organizationalUnit"));
            attrs.add(new LDAPAttribute("ou", "people"));

            String dn = "ou=people,dc=demo,dc=local";

            conn.add(new LDAPEntry(dn, attrs));

            String dn = "uid=john,ou=people,dc=demo,dc=local";

            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(new LDAPAttribute("objectClass", "inetOrgPerson"));
            attrs.add(new LDAPAttribute("cn", "John Doe"));
            attrs.add(new LDAPAttribute("sn", "Doe"));
            attrs.add(new LDAPAttribute("uid", "john"));
            attrs.add(new LDAPAttribute("userPassword", "1234"));

            conn.add(new LDAPEntry(dn, attrs));
            */

            String base = "ou=people,dc=demo,dc=local";
            String filter = "(uid=john)";
            int scope = LDAPConnection.SCOPE_SUB; // teljes részfa

            LDAPSearchResults results = conn.search(base, scope, filter, null, false);

            while(results.hasMore()) {
                LDAPEntry entry = results.next();
                System.out.println("DN: " + entry.getDN());

                LDAPAttributeSet attrs = entry.getAttributeSet();
                for (Object o : attrs) {
                    LDAPAttribute attr = (LDAPAttribute) o;
                    System.out.println(attr.getName() + " = " +
                            Arrays.toString(attr.getStringValueArray()));
                }
            }

            String dn = "uid=john,ou=people,dc=demo,dc=local";

            // Email módosítása (replace művelet)
            LDAPModification mod = new LDAPModification(
                    LDAPModification.REPLACE,
                    new LDAPAttribute("mail", "newmail@demo.local")
            );

            conn.modify(dn, mod);

            System.out.println("Sikeres módosítás!");

            // DN törlése
            conn.delete(dn);

            conn.disconnect();
        } catch (LDAPException e) {
            e.printStackTrace();
        }
    }
}
