xquery version "3.1";
(:

for $c in /cegek/ceg
return <ceg>
    <nev>{ $c/nev/text() }</nev>
    <varos>{ $c/cim/varos/text() }</varos>
</ceg>


for $g in /cegek/gyakornok
return <gyakornok>
    <nev>{ $g/nev/text() }</nev>
    <idotartam>{ $g/gyakorlat/idotartam/text() }</idotartam>
</gyakornok>


for $t in /cegek/tulajdonos
where count($t/vegzettseg) > 1
return $t/nev


for $f in /cegek/fejleszto
order by xs:integer($f/fizetes) descending
return <fejleszto>
    <nev>{ $f/nev/text() }</nev>
    <fizetes>{ $f/fizetes/text() }</fizetes>
</fejleszto>


for $p in /cegek/projekt
where xs:date($p/hatarido) lt xs:date("2026-01-01")
return $p/nev


for $c in /cegek/ceg
let $gy := /cegek/gyakornok[@c_gy = $c/@ckod]
return <ceg>
    <nev>{ $c/nev/text() }</nev>
    <gyakornokok>{
        for $g in $gy return <gyakornok>{ $g/nev/text() }</gyakornok>
    }</gyakornokok>
</ceg>


for $m in /cegek/megrendelo
return <megrendelo>
    <nev>{ $m/nev/text() }</nev>
    <cim>{ concat($m/cim/varos, ", ", $m/cim/utca, " ", $m/cim/hazszam) }</cim>
</megrendelo>


for $f in /cegek/fejleszto[reszleg = "Backend"]
return $f/nev

:)


for $p in /cegek/projekt
let $c := /cegek/ceg[@ckod = $p/@c_v_p]
let $m := /cegek/megrendelo[@mkod = $p/@m_p]
return <projektinfo>
    <projekt>{ $p/nev/text() }</projekt>
    <ceg>{ $c/nev/text() }</ceg>
    <megrendelo>{ $m/nev/text() }</megrendelo>
</projektinfo>