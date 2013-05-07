Tema 3 IDP

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
				
				- username: ana
				- password: test
				- tip: seller
				
				- username: lili
				- password: test
				- tip: seller
				
				- username: ion
				- password: test
				- tip: buyer
			
	Din motive tehnice, am implementat doar partea de baza de date, pentru partea de servicii web nu am reusit
sa o facem functionala. De aceea, aplicatia va comunica, prin modulul de WebClient, direct cu baza de date. 
	Baza de date contine o tabela cu informatii despre utilizatori (tabela user), o tabela cu servicii( service)
si o tabela care contine informatii despre serviciile dorite/cerute de fiecare utilizator. La proiect este
atasat fisierul utilizat pentru import.
	
	