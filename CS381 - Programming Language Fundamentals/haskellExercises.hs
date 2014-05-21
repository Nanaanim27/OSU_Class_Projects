module HW1 where

type Name = String			
type ANum = Int
type Pars = [Name]
type Vals = [ANum]
		
data Mode =  Up | Down     deriving Show
data Pos = I ANum | S Name
data Cmd = Pen Mode
		| Moveto (Int, Int)
		| Def Name (Pars) Cmd
		| Call Name (Vals) Cmd
		| Commands [Cmd]
		deriving Show
	

--Vector :: Commands
--Vector = Def "vector" ["x1", "y1", "x2", "y2"]	Commands [Pen Up, --Moveto("x1", "y1"), Pen Down, Moveto("x2", "y2"), Pen Up]
	
steps :: Int -> Cmd
steps 0 = Pen Up
steps n = Commands ([Pen Up, Moveto(n,n), Pen Down, Moveto(n-1, n), Moveto(n-1, n-1)] ++ [steps(n-1)])

--Part 2

type BNum = Int
type Circuit = (Gates, Links)
type Gates = [(BNum, GateFn)]
type Links = [Link]

data GateFn = And
			|Or
			|Xor
			|Not
			deriving Show
data Link = Lnk (BNum, BNum) (BNum, BNum)
			deriving Show

halfAdder :: Circuit
halfAdder = ([(1,Xor), (2,And)],  [Lnk (1,1) (2,1) , Lnk (1,2) (2,2)])  
			
--([(1, Xor), (2, And)], [Lnk (1,1) (2,1) , Lnk( 1,2) (2,2)])

--Pretty Printer

ppCircuit :: Circuit -> String 
ppCircuit (g, l) = ppGates g ++ "\n" ++ ppLink l ++ "\n" 

ppGates :: Gates -> String
ppGates [] = ""
ppGates ((i, fn) :gs) = show i ++ ":" ++ show fn ++ ";\n" ++ ppGates gs



ppLink :: Links -> String
ppLink [] = ""
ppLink (Lnk (w,x) (y,z) : ls) = "from " ++ show w ++ "." ++ show x ++ " to " ++ show y ++ "." ++ show z ++ ";\n" ++ ppLink ls


--Part 3

data Expr = N Int
			|Plus Expr Expr
			|Times Expr Expr
			|Neg Expr
			
data Op = Add | Multiply | Negate

data Exp = CNum Int
			|Apply Op [Exp]
			
			
--Expression = Multiply(Negate((Add 3 4)) 7)