--- module (NICHT AENDERN!)
module LascaBot where
--- imports (NICHT AENDERN!)
import Util
import Data.Char
import System.Environment
--- external signatures (NICHT AENDERN!)
getMove   :: String -> String
listMoves :: String -> String

-- *==========================================* --
-- |    HIER BEGINNT EURE IMPLEMENTIERUNG!    | --
-- *==========================================* --

--- types/structures (TODO)

data Color = White | Black

    --- ... ---

--- logic (TODO)
getMove :: String -> String
getMove   x:xs = take 5 xs
listMoves s = "[g3-f4,...]" -- Eigene Definition einfügen!

    --- ... ---

--- input (TODO)

--takes eg. "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w" and makes ["b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w","w"]
splitInput :: String -> [String]
splitInput x = [(take 46 x), [last x]]

--parseInput :: [String] -> ...
--parseInput (board:color:[])   = ... (parseColor color) ...
--parseInput (board:color:move:[]) = ... (parseColor color) ...

parseColor :: String -> Color
parseColor "w" = White
parseColor "b" = Black

toInt :: Char -> Int
toInt c = ((ord c) - (ord 'a') + 1)

    --- ... ---

--- output (TODO)

colorToString :: Color -> String
colorToString White = "w"
colorToString Black = "b"

instance Show Color where
    show = colorToString
    
instance Eq Color where
    (==) White White = True
    (==) Black Black = True
    (==) _ _ = False    

    --- ... ---
    
    
    -----------------------------
fen1 = "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w"
fen2 = "b,,b,b/b,,b/,,,b/,b,w/,bw,bw,w/,bbww,w/w,w,w,w"



-- wenn ausgeführt -> ["b","","b","b","b","","b","","","","b","","b","w","","bw","bw","w","","bbww","w","w","w","w","w"]


listToSublist x = splitOn ("/") x
listToSublist2 x =  splitOn (",") x
listToSubList3 x = let s = (listToSublist x) 
					in (listToSublist2 (s!!0)) ++ (listToSublist2 (s!!1)) ++ (listToSublist2 (s!!2)) ++ (listToSublist2 (s!!3)) ++ (listToSublist2 (s!!4)) ++ (listToSublist2 (s!!5)) ++ (listToSublist2 (s!!6)) 

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
    
--Spielregeln--

--Counter muss 4 sein
whiteMoveLeftUp :: [([Char],[Char])] -> ([Char],[Char]) -> Int -> [([Char],[Char])]
whiteMoveLeftUp currentBoard currentPosition counter 
			if ("b" `elem` (snd currentPosition - counter)) == false then []
				else if ("B" `elem` (snd currentPosition - counter)) == false then []
					else if ("w" `elem` (snd currentPosition - counter)) == false then []
						else if ("W" `elem` (snd currentPosition - counter) == false then []
							else (remove last currentPosition - counter) ++ snd currentPosition

--Counter muss 3 sein
whiteMoveRightUp :: [([Char],[Char])] -> ([Char],[Char]) -> Int -> [([Char],[Char])]
whiteMoveRightUp currentBoard currentPosition counter 
			if ("b" `elem` (snd currentPosition - counter)) == false then []
				else if ("B" `elem` (snd currentPosition - counter)) == false then []
					else if ("w" `elem` (snd currentPosition - counter)) == false then []
						else if ("W" `elem` (snd currentPosition - counter) == false then []
							else (remove last currentPosition - counter) ++ snd currentPosition

--Counter muss 4							
blackMoveLeftUp :: [([Char],[Char])] -> ([Char],[Char]) -> Int -> [([Char],[Char])]
blackMoveLeftUp currentBoard currentPosition counter 
			if ("b" `elem` (snd currentPosition - counter)) == false then []
				else if ("B" `elem` (snd currentPosition - counter)) == false then []
					else if ("w" `elem` (snd currentPosition - counter)) == false then []
						else if ("W" `elem` (snd currentPosition - counter) == false then []
							else (remove last currentPosition - counter) ++ snd currentPosition
		

--Counter muss 3 sein		
blackMoveRightUp :: [([Char],[Char])] -> ([Char],[Char]) -> Int -> [([Char],[Char])]
blackMoveRightUp currentBoard currentPosition counter 
			if ("b" `elem` (snd currentPosition - counter)) == false then []
				else if ("B" `elem` (snd currentPosition - counter)) == false then []
					else if ("w" `elem` (snd currentPosition - counter)) == false then []
						else if ("W" `elem` (snd currentPosition - counter) == false then []
							else (remove last currentPosition - counter) ++ snd currentPosition

							
-- Schlagen funktion Counter muss 8 sein							
-- whiteBeatLeftUp :: [([Char],[Char])] -> ([Char],[Char]) -> Int -> [([Char],[Char])]
-- blackMoveRightUp currentBoard currentPosition counter 
--			if ("b" `elem` (snd currentPosition - counter)) == false then []
--				else if ("B" `elem` (snd currentPosition - counter)) == false then []
--					else if ("w" `elem` (snd currentPosition - counter)) == false then []
--						else if ("W" `elem` (snd currentPosition - counter) == false then []
--							else 
--									(remove last currentPosition - counter) ++ snd currentPosition	


--Soldat zu Offizier
whiteSolToOff if "a7" `elem` currentPosition == true then ((remove last currentPosition) ++ "W")
				else if "c7" `elem` currentPosition == true then ((remove last currentPosition) ++ "W")
					else if "e7" `elem` currentPosition == true then ((remove last currentPosition) ++ "W")
						else if "g7" `elem` currentPosition == true then ((remove last currentPosition) ++ "W")
							else []
							
blackSolToOff if "a1" `elem` currentPosition == true then ((remove last currentPosition) ++ "B")
				else if "c1" `elem` currentPosition == true then ((remove last currentPosition) ++ "B")
					else if "e1" `elem` currentPosition == true then ((remove last currentPosition) ++ "B")
						else if "g1" `elem` currentPosition == true then ((remove last currentPosition) ++ "B")
							else []


checkPossibleMoves b = if "w" `elem` (last (splitInput x)) then	
							
changeStones :: [(String,String)] -> String -> [(String,String)]
changeStones x y = [makeToTuple (head (head x)) y]

makeToTuple :: String -> String -> (String,String)
makeToTuple x y = (x,y)
