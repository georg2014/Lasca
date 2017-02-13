--import Data.List
--import Data.Char
import Debug.Trace
import Data.List.Split

fen1 = "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w"
fen2 = "b,,b,b/b,,b/,,,b/,b,w/,bw,bw,w/,bbww,w/w,w,w,w"



-- wenn ausgeführt -> ["b","","b","b","b","","b","","","","b","","b","w","","bw","bw","w","","bbww","w","w","w","w","w"]
listToSublist x = splitOneOf (",/ ") x
	
--Spielfeld

-- 0 - 1 - 2 - 3
--   4 - 5 - 6
-- 7 - 8 - 9 - 10
--   11 - 12 - 13
-- 14 - 15 - 16 - 17
--   18 - 19 - 20
-- 21 - 22 - 23 - 24

--0		==	a7
--1		==	c7
--2		==	e7
--3 	==	g7
--4		==	b6
--5		==	d6
--6		==	f6
--7		==	a5
--8		==	c5
--9 	==	e5
--10	==	g5
--11	==	b4
--12	==	d4
--13	==	f4
--14	==	a3
--15	==	c3
--16	==	e3
--17	==	g3
--18	==	b2
--19    ==	d2
--20	==	f2
--21	==	a1
--22	==	c1
--23	==	e1
--24	==	g1


board = ["a7","c7","e7","g7","b6","d6","f6","a5","c5","e5","g5","b4","d4","f4","a3","c3","e3","g3","b2","d2","f2","a1","c1","e1","g1"]

-- merged das board und den aktuellen spielstand, man weiß jetzt genau welcher stein auf welchem punkt ist.
pieceToBoard x y = zip x y
