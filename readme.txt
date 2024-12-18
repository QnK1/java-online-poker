1. Zasady
	Poker Texas Hold'Em, lekko uproszczony:
	-dwie karty w ręce, 5 na stole
	-najpierw są odkrywane 3 karty ze stołu (FLOP), potem 1 (TURN), potem jeszcze 1 (RIVER)
	-dostępne opcje: FOLD, CHECK, CALL, RAISE; przed odsłonięciem kart bez CHECK
	-CHECK tylko 1 raz na turę obstawiania na gracza
	-obstawianie w danej rundzie odbywa się aż wszystkie zakłady graczy, którzy się nie poddali i w ostatniej kolejce nie użyli CHECK są równe
	-zwycięzca bierze całą pulę


2. Dostępne opcje

Przed dołączeniem do gry:
	create <nazwa_gry> <nick>
	join <nazwa_gry> <nick>

jeśli polecenie jest niepoprawne, to możemy po prostu wpisać je ponownie

Po dołączeniu do gry:
	LEAVE - opuszczenie gry
	FOLD - złożenie kart, poddanie się
	CHECK - przekazanie ruchu kolejnemu graczowi (dostępne tylko w turach z jakimiś odsłoniętymi kartami, tylko raz na turę na gracza)
	CALL - dorównanie do najwyższego zakładu
	RAISE <wartość> - podniesienie swojego zakładu o podaną wartość, musi w sumie dawać więcej niż najwyższy zakład na stole

Wciskając Enter możemy odświeżać informacje po stronie klienta.
Na niepoprawne komendy serwer odpowiada "ENTER VALID COMMAND" (pełni również rolę promptowania użytkownika), na poprawne "COMMAND SUCCESSUFUL".

W komunikacie serwera jest widoczne, który gracz jest na ruchu, jakie kart znajdują się na stole, ile postawili gracze, jakie karty mamy w ręce.

3. Inne informacje:
- możliwe jest odbywanie się kilku gier jednocześnie na jednym serwerze