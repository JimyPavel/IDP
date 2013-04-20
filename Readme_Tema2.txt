Tema 2 IDP

	Proiectul se afla la adresa 
		https://github.com/JimyPavel/IDP.git
		
	date de acces Jimy Pavel:
		JimyPavel
		ImagineCup2013#
		
	date de acces Liliana Luchian:
		LilianaLuchian
		ImagineCup2013#

		Pentru a rula tema, se foloseste "build.xml" care va rula atat serverul, cat si 3 instante. Din pacate,
inainte de a doua rulare a ant-ului, trebuie oprit procesul serverul din task manager, deoarece se va incerca
sa se deschida un nou server, pe acelasi port, ceea ce va genera o exceptie. Nu am gasit o solutie pentru aceasta
problema.

		Pentru logare se pot folosi urmatorii useri:
				- username: jimy
				- password: test
				- tip: buyer
				
				- username: maria
				- password: test
				- tip: buyer
				
				- username: lili
				- password: test
				- tip: seller
				
				- username: ion
				- password: test
				- tip: seller
				
		Fiecare user si serverul vor loga informatii despre actiunile lor, intr-un fisier usernameLog.txt,
unde username este numele utilizatorului.

		Fiecare instanta rulata va fi atat client, cat si server. Fiecare va asculta pe un port, pentru a putea
primi informatii de la serverul principal. Comunicarea se face doar intre utilizator si server, fara ca utilizatorii
sa poata comunica direct.

		Instanta utilizator anunta serverul ca este online si ii comunica adresa pe care asculta, astfel incat
serverul sa poata sti de existenta fiecaruia.
		
		In functie de actiunea care se face pe gui, se trimite un mesaj de un anumit tip, prin Network, acesta
ajunge la server, care, in functie de tipul mesajului, il trimite destinatarului. Destinatarul va prelucra
acest mesaj. Acest lucru este facut prin patternul State. 
		De exemplu, in momentul in care un buyer trimite un mesaj de tip "Launch offer request", modulul de 
network il trimite la server, iar serverul il trimite tuturor furnizorilor. Acestia verifica daca detin acest 
produs/serviciu si actualizeaza Gui-ul sau ignora mesajul. Daca furnizorul face o oferta, trimite un anunt serverului care va trimite mesajul doar clientului caruia
i s-a facut oferta.
		
	
		
		