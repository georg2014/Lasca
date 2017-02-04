{-# LANGUAGE OverloadedStrings #-}
module ChessBot where

import Data.Char
import Data.List.Split

-------------------------------------------
--    Example Boards and Board validation
-------------------------------------------

-- this is the start board layout
startFEN :: String
startFEN = "rnbkqbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBKQBNR"

-- an example of a board where one player is checkmate
checkMateFEN :: String
checkMateFEN = "3K3q/7r/8/8/8/8/7k/8"

-- checks if a FEN string really contains a valid chess board (8x8 fields)
validateFENString :: String -> Bool
validateFENString s = let rows = splitFENString s in
                    length rows == 8 && -- need 8 rows
                    foldr (((&&) . (== 8)) . countFigures) True rows -- 8 columns per row
                        
-- counts the fields of a string (a row)
countFigures :: String -> Int
countFigures [] = 0
countFigures s = let c = head s in  
                 if isDigit c then digitToInt c + countFigures (tail s)
                 else 
                    if c=='r' || c=='R' || c=='b' || c=='B' || c=='n' || c=='N' || c=='q' || c=='Q' || c=='k' || c=='K' || c=='p' || c=='P' then 
                        1 + countFigures (tail s)
                    else 
                        9 --more than eight means bad input
   
-- splits a string at the "/" -> converts a FEN string field to a list of FEN string rows
splitFENString :: String -> [String]
splitFENString = splitOn "/" 







-----------------------------------------
--    Bot logic
-----------------------------------------

-- returns a list of all valid moves for the given board and color
-- parameter 1: first part of FEN String
-- parameter 2: "w"/"b" 
botListAllMoves :: String -> String -> [String]
botListAllMoves board color = do 
    let edit = prepareString board in doYourWorkbefore edit color 0


-- returns one valid move in the format [source target]
-- parameter 1: first part of FEN String
-- parameter 2: "w"/"b" 
botFindMove :: String -> String -> String
botFindMove board color 
    | color == "w" =  head (botListAllMoves board color)
    | color == "b" =  head (botListAllMoves board color)


-- Hier wird pro Feld eine Stringliste erstellt. Pro r,n,b,k,q,p 
doYourWorkbefore :: String -> String -> Int -> [String]
doYourWorkbefore editBoard color number = 
    if length editBoard == number then []
    else if color == "w" then ((checkWhite editBoard (editBoard !! number) number)++(doYourWorkbefore editBoard color (succ number)))
    else ((checkBlack editBoard (editBoard !! number) number)++(doYourWorkbefore editBoard color (succ number)))


checkWhite :: String -> Char -> Int -> [String]
checkWhite editBoard currentPiece currentPosition = 
    if currentPiece == 'P' then pawnWhite editBoard currentPosition 0
    else if currentPiece == 'K' then kingWhite editBoard currentPosition 0
        else if currentPiece == 'R' then rookWhite editBoard currentPosition 0 
            else if currentPiece == 'B' then bishopWhite editBoard currentPosition 0 
                 else if currentPiece == 'Q' then queenWhite editBoard currentPosition 0 
                     else if currentPiece == 'N' then knightWhite editBoard currentPosition 0 
                         else [] 


checkBlack :: String -> Char -> Int -> [String]
checkBlack editBoard currentPiece currentPosition  = 
    if currentPiece == 'p' then pawnBlack editBoard currentPosition 0
    else if currentPiece == 'k' then kingBlack editBoard currentPosition 0 
        else if currentPiece == 'r' then rookBlack editBoard currentPosition 0 
            else if currentPiece == 'b' then bishopBlack editBoard currentPosition 0 
                 else if currentPiece == 'q' then queenBlack editBoard currentPosition 0 
                     else if currentPiece == 'n' then knightBlack editBoard currentPosition 0 
                         else [] 
-- King, Queen, Rook, Bishop, Knight, Pawn
-- Spalten: Buchstaben; Zeilen: Zahlen

-- Spalten:
--          x mod 8: 0 - 1 - 2 - 3 - 4 -5 - 6 - 7

--0 - 1 - 2 - 3 - 4 - 5 - 6 - 7
--8 - 9 - 10 - 11 - 12 - 13 - 14 - 15
--16 - 17 - 18 - 19 - 20 - 21 - 22 - 23
--24 - 25 - 26 - 27 - 28 - 29 - 30 - 31
--32 - 33 - 34 - 35 - 36 - 37 - 38 - 39
--40 - 41 - 42 - 43 - 44 - 45 - 46 - 47
--48 - 49 - 50 - 51 - 52 - 53 - 54 - 55
--56 - 57 - 58 - 59 - 60 - 61 - 62 - 63

-- Dient zur Übersichtlichkit nur
-- Nutzen: Da wir mit Zahlen als Position arbeiten, müssen jene noch zum Wunschformat konvertiert werden
replacePosition :: Int -> Int -> String
replacePosition currentPosition counter = replaceWithRealPosition currentPosition ++ " " ++ replaceWithRealPosition counter

-- Nutzen: Prüft, ob zwei Figuren in der gleichen Zeile liegen. Das ist wichtig für die Bauern beim Schrägschlagen.
sameLine :: Int -> Bool
sameLine position 
    | mod position 8 == 7 = True
    | otherwise = False

sameColumn :: Int -> Int -> Bool
sameColumn position1 position2
    | mod position1 8 == 0 && mod position2 8 == 7 = True
    | mod position1 8 == 7 && mod position2 8 == 0 = True
    | otherwise = False
----------------------------------------------------------------------------------------

--____________________________config. for diagonal moves______________________--

----------------------------------------------------------------------------------------
-- counter must be 9
straightLeftUpWhite :: String -> Int -> Int -> [String]
straightLeftUpWhite editBoard currentPosition counter
    | mod (currentPosition - counter) 8 == 7 || (currentPosition - counter) < 0 = []
    | (editBoard !! (currentPosition-counter) == 'x' && mod (currentPosition - counter) 8 /= 7) = (replacePosition currentPosition (currentPosition-counter)) : (straightLeftUpWhite editBoard currentPosition (counter + 9))
    | ((editBoard !! (currentPosition-counter)) == 'p' || (editBoard !! (currentPosition-counter)) == 'q' || (editBoard !! (currentPosition-counter)) == 'r' || (editBoard !! (currentPosition-counter)) == 'b' || (editBoard !! (currentPosition-counter)) == 'k' || (editBoard !! (currentPosition-counter)) == 'n') && (mod (currentPosition - counter) 8 /= 7) && (currentPosition - counter) >= 0 = replacePosition currentPosition (currentPosition-counter) : []
    | otherwise = []

straightRightDownWhite :: String -> Int -> Int -> [String]
straightRightDownWhite editBoard currentPosition counter
    | mod (currentPosition + counter) 8 == 7 || (currentPosition + counter) >= 64 = []
    | (editBoard !! (currentPosition+counter) == 'x' && mod (currentPosition + counter) 8 /= 7) = (replacePosition currentPosition (currentPosition+counter)) : (straightRightDownWhite editBoard currentPosition (counter + 9))
    | ((editBoard !! (currentPosition+counter)) == 'p' || (editBoard !! (currentPosition+counter)) == 'q' || (editBoard !! (currentPosition+counter)) == 'r' || (editBoard !! (currentPosition+counter)) == 'b' || (editBoard !! (currentPosition+counter)) == 'k' || (editBoard !! (currentPosition+counter)) == 'n') && (mod (currentPosition + counter) 8 /= 7) && (currentPosition + counter) < 64 = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []

-- counter must be 7
straightLeftDownWhite :: String -> Int -> Int -> [String]
straightLeftDownWhite editBoard currentPosition counter
    | mod (currentPosition + counter) 8 == 7 || (currentPosition + counter) >= 64 = []
    | (editBoard !! (currentPosition+counter) == 'x' && mod (currentPosition + counter) 8 /= 7) = (replacePosition currentPosition (currentPosition+counter)) : (straightLeftDownWhite editBoard currentPosition (counter + 7))
    | ((editBoard !! (currentPosition+counter)) == 'p' || (editBoard !! (currentPosition+counter)) == 'q' || (editBoard !! (currentPosition+counter)) == 'r' || (editBoard !! (currentPosition+counter)) == 'b' || (editBoard !! (currentPosition+counter)) == 'k' || (editBoard !! (currentPosition+counter)) == 'n') && (mod (currentPosition + counter) 8 /= 7) && (currentPosition + counter) < 64 = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []

straightRightUpWhite :: String -> Int -> Int -> [String]
straightRightUpWhite editBoard currentPosition counter
    | mod (currentPosition - counter) 8 == 0 || (currentPosition - counter) < 0 = []
    | (editBoard !! (currentPosition-counter) == 'x' && mod (currentPosition - counter) 8 /= 0) = (replacePosition currentPosition (currentPosition-counter)) : (straightRightUpWhite editBoard currentPosition (counter + 7))
    | ((editBoard !! (currentPosition-counter)) == 'p' || (editBoard !! (currentPosition-counter)) == 'q' || (editBoard !! (currentPosition-counter)) == 'r' || (editBoard !! (currentPosition-counter)) == 'b' || (editBoard !! (currentPosition-counter)) == 'k' || (editBoard !! (currentPosition-counter)) == 'n') && (mod (currentPosition - counter) 8 /= 0) && (currentPosition - counter) >= 0 = replacePosition currentPosition (currentPosition-counter) : []
    | otherwise = []


--_______________________cut that shit in two pieces, this...______________________________________________--


-- counter must be 9
straightLeftUpBlack :: String -> Int -> Int -> [String]
straightLeftUpBlack editBoard currentPosition counter
    | mod (currentPosition - counter) 8 == 7 || (currentPosition - counter) < 0 = []
    | (editBoard !! (currentPosition-counter) == 'x' && mod (currentPosition - counter) 8 /= 7) = (replacePosition currentPosition (currentPosition-counter)) : (straightLeftUpBlack editBoard currentPosition (counter + 9))
    | ((editBoard !! (currentPosition-counter)) == 'P' || (editBoard !! (currentPosition-counter)) == 'Q' || (editBoard !! (currentPosition-counter)) == 'R' || (editBoard !! (currentPosition-counter)) == 'B' || (editBoard !! (currentPosition-counter)) == 'K' || (editBoard !! (currentPosition-counter)) == 'N') && (mod (currentPosition - counter) 8 /= 7) && (currentPosition - counter) >= 0 = replacePosition currentPosition (currentPosition-counter) : []
    | otherwise = []

straightRightDownBlack :: String -> Int -> Int -> [String]
straightRightDownBlack editBoard currentPosition counter
    | mod (currentPosition + counter) 8 == 7 || (currentPosition + counter) >= 64 = []
    | (editBoard !! (currentPosition+counter) == 'x' && mod (currentPosition + counter) 8 /= 7) = (replacePosition currentPosition (currentPosition+counter)) : (straightRightDownBlack editBoard currentPosition (counter + 9))
    | ((editBoard !! (currentPosition+counter)) == 'P' || (editBoard !! (currentPosition+counter)) == 'Q' || (editBoard !! (currentPosition+counter)) == 'R' || (editBoard !! (currentPosition+counter)) == 'B' || (editBoard !! (currentPosition+counter)) == 'K' || (editBoard !! (currentPosition+counter)) == 'N') && (mod (currentPosition + counter) 8 /= 7) && (currentPosition + counter) < 64 = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []

-- counter must be 7
straightLeftDownBlack :: String -> Int -> Int -> [String]
straightLeftDownBlack editBoard currentPosition counter
    | mod (currentPosition + counter) 8 == 7 || (currentPosition + counter) >= 64 = []
    | (editBoard !! (currentPosition+counter) == 'x' && mod (currentPosition + counter) 8 /= 7) = (replacePosition currentPosition (currentPosition+counter)) : (straightLeftDownBlack editBoard currentPosition (counter + 7))
    | ((editBoard !! (currentPosition+counter)) == 'P' || (editBoard !! (currentPosition+counter)) == 'Q' || (editBoard !! (currentPosition+counter)) == 'R' || (editBoard !! (currentPosition+counter)) == 'B' || (editBoard !! (currentPosition+counter)) == 'K' || (editBoard !! (currentPosition+counter)) == 'N') && (mod (currentPosition + counter) 8 /= 7) && (currentPosition + counter) < 64 = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []

straightRightUpBlack :: String -> Int -> Int -> [String]
straightRightUpBlack editBoard currentPosition counter
    | mod (currentPosition - counter) 8 == 0 || (currentPosition - counter) < 0 = []
    | (editBoard !! (currentPosition-counter) == 'x' && mod (currentPosition - counter) 8 /= 0) = (replacePosition currentPosition (currentPosition-counter)) : (straightRightUpBlack editBoard currentPosition (counter + 7))
    | ((editBoard !! (currentPosition-counter)) == 'P' || (editBoard !! (currentPosition-counter)) == 'Q' || (editBoard !! (currentPosition-counter)) == 'R' || (editBoard !! (currentPosition-counter)) == 'B' || (editBoard !! (currentPosition-counter)) == 'K' || (editBoard !! (currentPosition-counter)) == 'N') && (mod (currentPosition - counter) 8 /= 0) && (currentPosition - counter) >= 0 = replacePosition currentPosition (currentPosition-counter) : []
    | otherwise = []



----------------------------------------------------------------------------------------

--____________________________config. for diagonal moves______________________--

----------------------------------------------------------------------------------------



----------------------------------------------------------------------------------------

--____________________________config. for straight forward moves______________________--

----------------------------------------------------------------------------------------


straightLeftWhite :: String -> Int -> Int -> [String]
straightLeftWhite editBoard currentPosition counter
    | (editBoard !! (currentPosition-counter) == 'x' && mod (currentPosition - counter) 8 /= 7) = (replacePosition currentPosition (currentPosition-counter)) : (straightLeftWhite editBoard currentPosition (succ counter))
    | ((editBoard !! (currentPosition-counter)) == 'p' || (editBoard !! (currentPosition-counter)) == 'q' || (editBoard !! (currentPosition-counter)) == 'r' || (editBoard !! (currentPosition-counter)) == 'b' || (editBoard !! (currentPosition-counter)) == 'k' || (editBoard !! (currentPosition-counter)) == 'n') && (mod (currentPosition - counter) 8 /= 7) = replacePosition currentPosition (currentPosition-counter) : []
    | mod (currentPosition - counter) 8 == 7 = []
    | otherwise = []


straightRightWhite :: String -> Int -> Int -> [String]
straightRightWhite editBoard currentPosition counter
    | mod (currentPosition + counter) 8 == 0 = []
    | (editBoard !! (currentPosition+counter) == 'x' && mod (currentPosition + counter) 8 /= 0) = (replacePosition currentPosition (currentPosition+counter)) : (straightRightWhite editBoard currentPosition (succ counter))
    | ((editBoard !! (currentPosition+counter)) == 'p' || (editBoard !! (currentPosition+counter)) == 'q' || (editBoard !! (currentPosition+counter)) == 'r' || (editBoard !! (currentPosition+counter)) == 'b' || (editBoard !! (currentPosition+counter)) == 'k' || (editBoard !! (currentPosition+counter)) == 'n') && (mod (currentPosition + counter) 8 /= 0) = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []


-- counter must be 8
straightDownWhite :: String -> Int -> Int -> [String]
straightDownWhite editBoard currentPosition counter
    | ((currentPosition + counter) >= 64) = []
    | (editBoard !! (currentPosition+counter) == 'x') && ((currentPosition+counter) < 64) = replacePosition currentPosition (currentPosition+counter) : (straightDownWhite editBoard currentPosition (counter+8))
    | ((editBoard !! (currentPosition+counter)) == 'p' || (editBoard !! (currentPosition+counter)) == 'q' || (editBoard !! (currentPosition+counter)) == 'r' || (editBoard !! (currentPosition+counter)) == 'b' || (editBoard !! (currentPosition+counter)) == 'k' || (editBoard !! (currentPosition+counter)) == 'n') && (currentPosition+counter) < 64 = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []

straightUpWhite :: String -> Int -> Int -> [String]
straightUpWhite editBoard currentPosition counter
    | ((currentPosition - counter) < 0) = []
    | (editBoard !! (currentPosition-counter) == 'x') && ((currentPosition-counter) >= 0) = replacePosition currentPosition (currentPosition-counter) : (straightUpWhite editBoard currentPosition (counter+8))
    | ((editBoard !! (currentPosition-counter)) == 'p' || (editBoard !! (currentPosition-counter)) == 'q' || (editBoard !! (currentPosition-counter)) == 'r' || (editBoard !! (currentPosition-counter)) == 'b' || (editBoard !! (currentPosition-counter)) == 'k' || (editBoard !! (currentPosition-counter)) == 'n') && (currentPosition-counter) >= 0 = replacePosition currentPosition (currentPosition-counter) : []
    | otherwise = []

--_______________________cut that shit in two pieces, this...______________________________________________--


straightLeftBlack :: String -> Int -> Int -> [String]
straightLeftBlack editBoard currentPosition counter
    | mod (currentPosition - counter) 8 == 7 = []
    | (editBoard !! (currentPosition-counter) == 'x' && mod (currentPosition - counter) 8 /= 7) = (replacePosition currentPosition (currentPosition-counter)) : (straightLeftBlack editBoard currentPosition (succ counter))
    | ((editBoard !! (currentPosition-counter)) == 'P' || (editBoard !! (currentPosition-counter)) == 'Q' || (editBoard !! (currentPosition-counter)) == 'R' || (editBoard !! (currentPosition-counter)) == 'B' || (editBoard !! (currentPosition-counter)) == 'K' || (editBoard !! (currentPosition-counter)) == 'N') && (mod (currentPosition - counter) 8 /= 7) = replacePosition currentPosition (currentPosition-counter) : []
    | otherwise = []


straightRightBlack :: String -> Int -> Int -> [String]
straightRightBlack editBoard currentPosition counter
    | mod (currentPosition + counter) 8 == 0 = []
    | (editBoard !! (currentPosition+counter) == 'x' && mod (currentPosition + counter) 8 /= 0) = (replacePosition currentPosition (currentPosition+counter)) : (straightRightBlack editBoard currentPosition (succ counter))
    | ((editBoard !! (currentPosition+counter)) == 'P' || (editBoard !! (currentPosition+counter)) == 'Q' || (editBoard !! (currentPosition+counter)) == 'R' || (editBoard !! (currentPosition+counter)) == 'B' || (editBoard !! (currentPosition+counter)) == 'K' || (editBoard !! (currentPosition+counter)) == 'N') && (mod (currentPosition + counter) 8 /= 0) = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []


-- counter must be 8
straightDownBlack :: String -> Int -> Int -> [String]
straightDownBlack editBoard currentPosition counter
    | ((currentPosition + counter) >= 64) = []
    | (editBoard !! (currentPosition+counter) == 'x') && ((currentPosition+counter) < 64) = replacePosition currentPosition (currentPosition+counter) : (straightDownBlack editBoard currentPosition (counter+8))
    | ((editBoard !! (currentPosition+counter)) == 'P' || (editBoard !! (currentPosition+counter)) == 'Q' || (editBoard !! (currentPosition+counter)) == 'R' || (editBoard !! (currentPosition+counter)) == 'B' || (editBoard !! (currentPosition+counter)) == 'K' || (editBoard !! (currentPosition+counter)) == 'N') && (currentPosition+counter) < 64 = replacePosition currentPosition (currentPosition+counter) : []
    | otherwise = []

straightUpBlack :: String -> Int -> Int -> [String]
straightUpBlack editBoard currentPosition counter
    | ((currentPosition - counter) < 0) = []
    | (editBoard !! (currentPosition-counter) == 'x') && ((currentPosition-counter) >= 0) = replacePosition currentPosition (currentPosition-counter) : (straightUpBlack editBoard currentPosition (counter+8))
    | ((editBoard !! (currentPosition-counter)) == 'P' || (editBoard !! (currentPosition-counter)) == 'Q' || (editBoard !! (currentPosition-counter)) == 'R' || (editBoard !! (currentPosition-counter)) == 'B' || (editBoard !! (currentPosition-counter)) == 'K' || (editBoard !! (currentPosition-counter)) == 'N') && (currentPosition-counter) >= 0 = replacePosition currentPosition (currentPosition-counter) : []
    | otherwise = []

----------------------------------------------------------------------------------------

--____________________________config. for straight forward moves______________________--

----------------------------------------------------------------------------------------



-----------------------------------------------------------------------------
--                Pawn - King - Rook - Bishop - Queen -          --     DONE
-----------------------------------------------------------------------------

knightWhite :: String -> Int -> Int -> [String]
knightWhite editBoard currentPosition counter
    | counter == 64 = []
    | counter == (currentPosition - 6) && (mod counter 8) /= 1 && (mod counter 8) /= 0 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))
    | counter == (currentPosition + 6) && (mod counter 8) /= 6 && (mod counter 8) /= 7 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))
    | counter == (currentPosition - 15) && (mod counter 8) /= 0 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))
    | counter == (currentPosition + 15) && (mod counter 8) /= 7 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))   
    | counter == (currentPosition - 17) && (mod counter 8) /= 7 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))   
    | counter == (currentPosition + 17) && (mod counter 8) /= 0 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))   
    | counter == (currentPosition - 10) && (mod counter 8) /= 6 && (mod counter 8) /= 7 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))   
    | counter == (currentPosition + 10) && (mod counter 8) /= 1 && (mod counter 8) /= 0 && ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightWhite editBoard currentPosition (succ counter))  
    | otherwise = knightWhite editBoard currentPosition (succ counter)
-- recursively !!!
knightBlack :: String -> Int -> Int -> [String]
knightBlack editBoard currentPosition counter
    | counter == 64 = []
    | counter == (currentPosition - 6) && (mod counter 8) /= 1 && (mod counter 8) /= 0 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))
    | counter == (currentPosition + 6) && (mod counter 8) /= 6 && (mod counter 8) /= 7 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))
    | counter == (currentPosition - 15) && (mod counter 8) /= 0 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))
    | counter == (currentPosition + 15) && (mod counter 8) /= 7 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))   
    | counter == (currentPosition - 17) && (mod counter 8) /= 7 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))   
    | counter == (currentPosition + 17) && (mod counter 8) /= 0 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))   
    | counter == (currentPosition - 10) && (mod counter 8) /= 6 && (mod counter 8) /= 7 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))   
    | counter == (currentPosition + 10) && (mod counter 8) /= 1 && (mod counter 8) /= 0 && ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') = (replacePosition currentPosition counter) : (knightBlack editBoard currentPosition (succ counter))  
    | otherwise = knightBlack editBoard currentPosition (succ counter)



queenWhite :: String -> Int -> Int -> [String]
queenWhite editBoard currentPosition counter = straightLeftWhite editBoard currentPosition 1 ++ straightRightWhite editBoard currentPosition 1 ++ straightDownWhite editBoard currentPosition 8 ++ straightUpWhite editBoard currentPosition 8 ++ straightLeftUpWhite editBoard currentPosition 9 ++ straightRightDownWhite editBoard currentPosition 9 ++ straightLeftDownWhite editBoard currentPosition 7 ++ straightRightUpWhite editBoard currentPosition 7

queenBlack :: String -> Int -> Int -> [String]
queenBlack editBoard currentPosition counter = straightLeftBlack editBoard currentPosition 1 ++ straightRightBlack editBoard currentPosition 1 ++ straightDownBlack editBoard currentPosition 8 ++ straightUpBlack editBoard currentPosition 8 ++ straightLeftUpBlack editBoard currentPosition 9 ++ straightRightDownBlack editBoard currentPosition 9 ++ straightLeftDownBlack editBoard currentPosition 7 ++ straightRightUpBlack editBoard currentPosition 7


bishopWhite :: String -> Int -> Int -> [String]
bishopWhite editBoard currentPosition counter = straightLeftUpWhite editBoard currentPosition 9 ++ straightRightDownWhite editBoard currentPosition 9 ++ straightLeftDownWhite editBoard currentPosition 7 ++ straightRightUpWhite editBoard currentPosition 7

bishopBlack :: String -> Int -> Int -> [String]
bishopBlack editBoard currentPosition counter = straightLeftUpBlack editBoard currentPosition 9 ++ straightRightDownBlack editBoard currentPosition 9 ++ straightLeftDownBlack editBoard currentPosition 7 ++ straightRightUpBlack editBoard currentPosition 7


rookWhite :: String -> Int -> Int -> [String]
rookWhite editBoard currentPosition counter = straightLeftWhite editBoard currentPosition 1 ++ straightRightWhite editBoard currentPosition 1 ++ straightDownWhite editBoard currentPosition 8 ++ straightUpWhite editBoard currentPosition 8

rookBlack :: String -> Int -> Int -> [String]
rookBlack editBoard currentPosition counter = straightLeftBlack editBoard currentPosition 1 ++ straightRightBlack editBoard currentPosition 1 ++ straightDownBlack editBoard currentPosition 8 ++ straightUpBlack editBoard currentPosition 8




kingWhite :: String -> Int -> Int -> [String]
kingWhite editBoard currentPosition counter 
    | counter == 64 = [] 
    | ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n' || (editBoard !! counter) == 'x') && (counter == (currentPosition-8) || counter == (currentPosition+8) || ((counter == (currentPosition-7) || counter == (currentPosition+7)) && sameLine currentPosition == False) || ((counter == (currentPosition-9) || counter == (currentPosition+9)) && sameColumn currentPosition counter == False) || ((counter == (currentPosition-1) || counter == (currentPosition+1)) && sameColumn currentPosition counter == False)) = (replacePosition currentPosition counter) : (kingWhite editBoard currentPosition (succ counter)) 
    | otherwise = kingWhite editBoard currentPosition (succ counter)


kingBlack :: String -> Int -> Int -> [String]
kingBlack editBoard currentPosition counter 
    | counter == 64 = [] 
    | ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N' || (editBoard !! counter) == 'x') && (counter == (currentPosition-8) || counter == (currentPosition+8) || ((counter == (currentPosition-7) || counter == (currentPosition+7)) && sameLine currentPosition == False) || ((counter == (currentPosition-9) || counter == (currentPosition+9)) && sameColumn currentPosition counter == False) || ((counter == (currentPosition-1) || counter == (currentPosition+1)) && sameColumn currentPosition counter == False)) = (replacePosition currentPosition counter) : (kingBlack editBoard currentPosition (succ counter)) 
    | otherwise = kingBlack editBoard currentPosition (succ counter)



pawnWhite :: String -> Int -> Int -> [String]
pawnWhite editBoard currentPosition counter 
    | counter == 64 = [] 
    -- Ein Feld vor dem Bauern. // DONE
    | (editBoard !! counter) == 'x' && (counter == (currentPosition-8)) = (replacePosition currentPosition counter) : (pawnWhite editBoard currentPosition (succ counter))
    -- Zwei Felder nach vorne. Bedingung: Beide Felder Frei und Bauer muss am Anfang stehen. // DONE
    | (editBoard !! counter) == 'x' && (counter == (currentPosition-16)) && (currentPosition == 55 || currentPosition == 54 || currentPosition == 53 || currentPosition == 52 ||currentPosition == 51 || currentPosition == 50 || currentPosition == 49 || currentPosition == 48) && (editBoard !! (counter+8)) == 'x' = (replacePosition currentPosition counter) : (pawnWhite editBoard currentPosition (succ counter)) 
    -- Laufe Diagonal, wenn du Schlagen kannst: //DONE
    | ((editBoard !! counter) == 'p' || (editBoard !! counter) == 'q' || (editBoard !! counter) == 'r' || (editBoard !! counter) == 'b' || (editBoard !! counter) == 'k' || (editBoard !! counter) == 'n') && ((counter == (currentPosition-7) && sameLine currentPosition == False) || (counter == (currentPosition-9) && sameColumn counter currentPosition == False)) = (replacePosition currentPosition counter) : (pawnWhite editBoard currentPosition (succ counter)) 
    -- Falls keine Bedingung erfüllt wurde, soll er einfach mit dem nächsten Feld fortfahren und nichts machen.
    | otherwise = pawnWhite editBoard currentPosition (succ counter)


pawnBlack :: String -> Int -> Int -> [String]
pawnBlack editBoard currentPosition counter
    | counter == 64 = [] 
    -- Ein Feld vor dem Bauern. // DONE
    | (editBoard !! counter) == 'x' && (counter == (currentPosition+8)) = (replacePosition currentPosition counter) : (pawnBlack editBoard currentPosition (succ counter))
    -- Zwei Felder nach vorne. Bedingung: Beide Felder Frei und Bauer muss am Anfang stehen. // DONE
    | (editBoard !! counter) == 'x' && (counter == (currentPosition+16)) && (currentPosition == 15 || currentPosition == 14 || currentPosition == 13 || currentPosition == 12 ||currentPosition == 11 || currentPosition == 10 || currentPosition == 9 || currentPosition == 8) && (editBoard !! (counter-8)) == 'x' = (replacePosition currentPosition counter) : (pawnBlack editBoard currentPosition (succ counter)) 
    -- Laufe Diagonal, wenn du Schlagen kannst: //DONE
    | ((editBoard !! counter) == 'P' || (editBoard !! counter) == 'Q' || (editBoard !! counter) == 'R' || (editBoard !! counter) == 'B' || (editBoard !! counter) == 'K' || (editBoard !! counter) == 'N') && ((counter == (currentPosition+7) && sameLine currentPosition == False) || (counter == (currentPosition+9) && sameColumn counter currentPosition == False)) = (replacePosition currentPosition counter) : (pawnBlack editBoard currentPosition (succ counter)) 
    -- Falls keine Bedingung erfüllt wurde, soll er einfach mit dem nächsten Feld fortfahren und nichts machen.
    | otherwise = pawnBlack editBoard currentPosition (succ counter)

----------------------------------------------------------------------------------------

















-----------------------------------------
--            number -> x                --
-------------------------------------------

-- replaces numbers in a string to 'x'
-- example: replaceNumberFEN pp4pp -> ppxxxxpp
replaceNumberFEN :: String -> String
replaceNumberFEN fen =  concat(map replaceNumber fen)

-- changeses number to string with accurate number of 'x'
-- example: replaceNumber 4 -> xxxx
replaceNumber:: Char -> String
replaceNumber '8' = "xxxxxxxx"
replaceNumber s    
    | s == '8' = "xxxxxxxx"
    | s == '7' = "xxxxxxx" 
    | s == '6' = "xxxxxx"
    | s == '5' = "xxxxx"
    | s == '4' = "xxxx"   
    | s == '3' = "xxx"
    | s == '2' = "xx"
    | s == '1' = "x"  
    | otherwise =  [s]


removeBackslash :: String -> String
removeBackslash fen = filter (/='/') fen 

prepareString :: String -> String
prepareString fen = replaceNumberFEN (removeBackslash fen)

replaceWithRealPosition:: Int -> String
replaceWithRealPosition s    
    | s == 0  = "a1"
    | s == 1  = "b1"
    | s == 2  = "c1"
    | s == 3  = "d1"
    | s == 4  = "e1"
    | s == 5  = "f1"
    | s == 6  = "g1"
    | s == 7  = "h1"
    | s == 8  = "a2"
    | s == 9  = "b2"
    | s == 10 = "c2"
    | s == 11 = "d2"
    | s == 12 = "e2"
    | s == 13 = "f2"
    | s == 14 = "g2"
    | s == 15 = "h2"
    | s == 16 = "a3"
    | s == 17 = "b3"
    | s == 18 = "c3"
    | s == 19 = "d3"
    | s == 20 = "e3"
    | s == 21 = "f3"
    | s == 22 = "g3"
    | s == 23 = "h3"
    | s == 24 = "a4"
    | s == 25 = "b4"
    | s == 26 = "c4"
    | s == 27 = "d4"
    | s == 28 = "e4"
    | s == 29 = "f4"
    | s == 30 = "g4"
    | s == 31 = "h4"
    | s == 32 = "a5"
    | s == 33 = "b5"
    | s == 34 = "c5"
    | s == 35 = "d5"
    | s == 36 = "e5"
    | s == 37 = "f5"
    | s == 38 = "g5"
    | s == 39 = "h5"
    | s == 40 = "a6"
    | s == 41 = "b6"
    | s == 42 = "c6"
    | s == 43 = "d6"
    | s == 44 = "e6"
    | s == 45 = "f6"
    | s == 46 = "g6"
    | s == 47 = "h6"
    | s == 48 = "a7"
    | s == 49 = "b7"
    | s == 50 = "c7"
    | s == 51 = "d7"
    | s == 52 = "e7"
    | s == 53 = "f7"
    | s == 54 = "g7"
    | s == 55 = "h7"
    | s == 56 = "a8"
    | s == 57 = "b8"
    | s == 58 = "c8"
    | s == 59 = "d8"
    | s == 60 = "e8"
    | s == 61 = "f8"
    | s == 62 = "g8"
    | s == 63 = "h8"
