
Tema 1 IDP

	Proiectul se afla la adresa 
		https://github.com/JimyPavel/IDP.git
		
	date de acces Jimy Pavel:
		JimyPavel
		ImagineCup2013#
		
	date de acces Liliana Luchian:
		LilianaLuchian
		ImagineCup2013#
		
		
	Aplicatia poate fi folosita atat de cumparatori, cat si de furnizori.
	Pentru logarea unui cumparatori, se pot folosi:
		- username: jimy
		- password: test
	Pentru logarea unui furnizor, se pot folosi:
		- username: lili
		- password: test
	Simularea bazei de date pentru utilizatori se face folosind fisiere de configurare.
	
	
	Pentru testare, am decis sa folosim butoane, care simuleaza diverse actiuni:
	Pentru cumparator:
		- acesta trebuie sa faca o actiune de Launch offer request
		- pentru a simula o oferta, folosim butonul Test Offer Made
		- cumparatorul poate accepta sau refuza oferta
		- in cazul in care accepta, incepe transferul
		
	Pentru furnizor:
		- pentru a simula o cerere de serviciu, folosim butonul Simulate offer requests
		- acesta poate face o oferta sau poate face drop auction
		- daca face o oferta, pentru a simula acceptarea ofertei se foloseste butonul Simulate Offers Accepted, iar transferul incepe
		
		
	Pentru rulare, am construit un fisier build.xml, care contine atat directiva de rulare cat si de clean.	