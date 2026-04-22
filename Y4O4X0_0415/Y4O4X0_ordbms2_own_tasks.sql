CREATE OR REPLACE TYPE szemely_t AS OBJECT (
    nev VARCHAR2(100),

    MEMBER FUNCTION bemutatkozas RETURN VARCHAR2,
    STATIC FUNCTION minimalis_eletkor RETURN INTEGER,
    ORDER MEMBER FUNCTION nev_szerint_rendezes (other szemely_t) RETURN INTEGER
) NOT FINAL;


CREATE OR REPLACE TYPE BODY szemely_t AS

    MEMBER FUNCTION bemutatkozas RETURN VARCHAR2 IS
    BEGIN
        RETURN 'A nevem: ' || SELF.nev;
    END;

    STATIC FUNCTION minimalis_eletkor RETURN INTEGER IS
    BEGIN
        RETURN 18;
    END;

    ORDER MEMBER FUNCTION nev_szerint_rendezes (other szemely_t)
        RETURN INTEGER IS
    BEGIN
        RETURN CASE
            WHEN SELF.nev < other.nev THEN -1
            WHEN SELF.nev > other.nev THEN 1
            ELSE 0
        END;
    END;

END;


CREATE OR REPLACE TYPE gyakornok_t UNDER szemely_t (
    gykod VARCHAR2(20),
    ceg_kod NUMBER,
    kezdet DATE,
    idotartam VARCHAR2(20),

    OVERRIDING MEMBER FUNCTION bemutatkozas RETURN VARCHAR2
);


CREATE OR REPLACE TYPE BODY gyakornok_t AS
    OVERRIDING MEMBER FUNCTION bemutatkozas RETURN VARCHAR2 IS
    BEGIN
        RETURN 'Gyakornok vagyok. Nev: ' || SELF.nev ||
               ', kezdés: ' || TO_CHAR(SELF.kezdet,'YYYY-MM-DD') ||
               ', időtartam: ' || SELF.idotartam;
    END;
END;

CREATE OR REPLACE TYPE tulajdonos_t UNDER szemely_t (
    tkod VARCHAR2(20),
    ceg_kod NUMBER,
    eletkor NUMBER,

    OVERRIDING MEMBER FUNCTION bemutatkozas RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tulajdonos_t AS
    OVERRIDING MEMBER FUNCTION bemutatkozas RETURN VARCHAR2 IS
    BEGIN
        RETURN 'Tulajdonos vagyok. Nev: ' || SELF.nev ||
               ', életkor: ' || SELF.eletkor;
    END;
END;


CREATE OR REPLACE TYPE fejleszto_t UNDER szemely_t (
    fkod VARCHAR2(20),
    ceg_kod NUMBER,
    reszleg VARCHAR2(50),
    fizetes NUMBER,

    OVERRIDING MEMBER FUNCTION bemutatkozas RETURN VARCHAR2
);


CREATE OR REPLACE TYPE BODY fejleszto_t AS
    OVERRIDING MEMBER FUNCTION bemutatkozas RETURN VARCHAR2 IS
    BEGIN
        RETURN 'Fejlesztő vagyok. Nev: ' || SELF.nev ||
               ', részleg: ' || SELF.reszleg ||
               ', fizetés: ' || SELF.fizetes;
    END;
END;


CREATE TABLE cegek_szemelyek_tbl OF szemely_t;

INSERT INTO cegek_szemelyek_tbl
SELECT gyakornok_t(
           x.nev,
           x.gykod,
           x.ceg,
           x.kezdet,
           x.idotartam
       )
FROM cegek_xml c,
     XMLTABLE('/cegek/gyakornok'
        PASSING c.adat
        COLUMNS
            gykod      VARCHAR2(10) PATH '@gykod',
            ceg        NUMBER       PATH '@c_gy',
            nev        VARCHAR2(100) PATH 'nev',
            kezdet     DATE          PATH 'gyakorlat/kezdet',
            idotartam  VARCHAR2(20)  PATH 'gyakorlat/idotartam'
     ) x;

INSERT INTO cegek_szemelyek_tbl
SELECT tulajdonos_t(
           x.nev,
           x.tkod,
           x.ceg,
           x.eletkor
       )
FROM cegek_xml c,
     XMLTABLE('/cegek/tulajdonos'
        PASSING c.adat
        COLUMNS
            tkod     VARCHAR2(10) PATH '@tkod',
            ceg      NUMBER       PATH '@c_t',
            nev      VARCHAR2(100) PATH 'nev',
            eletkor  NUMBER        PATH 'eletkor'
     ) x;


INSERT INTO cegek_szemelyek_tbl
SELECT fejleszto_t(
           x.nev,
           x.fkod,
           x.ceg,
           x.reszleg,
           x.fizetes
       )
FROM cegek_xml c,
     XMLTABLE('/cegek/fejleszto'
        PASSING c.adat
        COLUMNS
            fkod     VARCHAR2(10) PATH '@fkod',
            ceg      NUMBER       PATH '@c_f',
            nev      VARCHAR2(100) PATH 'nev',
            reszleg  VARCHAR2(50)  PATH 'reszleg',
            fizetes  NUMBER        PATH 'fizetes'
     ) x;


SELECT s.nev, s.bemutatkozas()
FROM cegek_szemelyek_tbl s
ORDER BY VALUE(s);


SELECT s.nev
FROM cegek_szemelyek_tbl s
WHERE VALUE(s) IS OF (fejleszto_t);


SELECT s.nev,
       TREAT(VALUE(s) AS fejleszto_t).fizetes AS fizetes
FROM cegek_szemelyek_tbl s
WHERE VALUE(s) IS OF (fejleszto_t);
