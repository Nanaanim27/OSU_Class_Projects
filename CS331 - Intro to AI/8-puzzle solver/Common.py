import csv
import copy
import math
#Reads in all files as CSV
def read_data_set(fileName):

    data = []
    data_reader = csv.reader(open(fileName, 'rb')) 
    
    for row in data_reader:
        data.append(map(int, row))
    
    return data
#traverses grid and finds the 0 (open space).
def findOpen(state):
    for i in [0, 1, 2]:
        for j in [0, 1, 2]:
            if(state[i][j] == 0):
                return [i, j]
    return None
  
#Move tile FROM left TO right one square
def TakeRight(state):
    op = findOpen(state)
    if(op[1] == 2):
        return [None, False]
    else: 
        state[op[0]][op[1]] = state[op[0]][op[1]+1]
        state[op[0]][op[1]+1] = 0
        return [state, True]

#Move tile FROM right TO left one square
def TakeLeft(state):
    op = findOpen(state)
    if(op[1] == 0):
        return [None, False]
    else: 
        state[op[0]][op[1]] = state[op[0]][op[1]-1]
        state[op[0]][op[1]-1] = 0
        return [state, True]

#Move tile FROM down TO up one square
def TakeTop(state):
    op = findOpen(state)
    if(op[0] == 0):
        return [None, False]
    else: 
        state[op[0]][op[1]] = state[op[0]-1][op[1]]
        state[op[0]-1][op[1]] = 0
        return [state, True]

#Move tile FROM up TO down one square
def TakeBottom(state):
    op = findOpen(state)
    if(op[0] == 2):
        return [None, False]
    else: 
        state[op[0]][op[1]] = state[op[0]+1][op[1]]
        state[op[0]+1][op[1]] = 0
        return [state, True]


#Selects which move to make based on direction input, makes move, returnsNone if invalid or current state if success. 
def mod(s, direction):
    state = copy.deepcopy(s)
    res = None;

    if(direction == 'l'):
        res = TakeLeft(state);
    elif(direction == 'r'):
        res = TakeRight(state);
    elif(direction == 'u'):
        res = TakeTop(state);
    elif(direction == 'd'):
        res = TakeBottom(state);
    elif(direction == 'n'):
        return state;
    else:
        print "Invalid argument for direction choice"
  
    if(res[1] == False):
        return None;
    else: 
        return res[0];  
  
#takes a board state and returns a hash value  
def stateToHash(state):
    retString = ''
    
    for i in state:
        for j in i:
            
            retString = retString + str(j)
            
    return hash(retString)    

def getNewNodes(cur, vis):
    nodes = []
    nodes.append(Node(mod(cur.curState, 'u'), cur, cur.depth +1, cur.cost +1))
    nodes.append(Node(mod(cur.curState, 'r'), cur, cur.depth +1, cur.cost +1))
    nodes.append(Node(mod(cur.curState, 'd'), cur, cur.depth +1, cur.cost +1))
    nodes.append(Node(mod(cur.curState, 'l'), cur, cur.depth +1, cur.cost +1))
    
    nodes = [node for node in nodes if node.curState != None and stateToHash(node.curState) not in vis]
    
    return nodes

def display_board( state ):
    print "-------------"
    print "| %i | %i | %i |" % (state[0][0], state[0][1], state[0][2])
    print "-------------"
    print "| %i | %i | %i |" % (state[1][0], state[1][1], state[1][2])
    print "-------------"
    print "| %i | %i | %i |" % (state[2][0], state[2][1], state[2][2])
    print "------------"
def stateToString(state):
    retString = ''
    for i in state:
        for j in i:
            retString = retString + str(j)
          
    return retString
def pathString(path):
    s = ""
    for n in path:
        s = s + format(n) + "\n"
    return s

def format(state):
    s = ""
    for i in range(0, 3):
        s = s + str(state[i][0]) + ","
        s = s + str(state[i][1]) + "," 
        s = s + str(state[i][2]) + "\n"
    return s
                       
def getPath(node):
    
    temp = node
    moves = []
    while temp != None:
        moves.insert(0, temp.curState)
        temp = temp.parent
        
    return moves
        

def dfs(start, goal, depth): 
    nodes = []
    nodes.insert(0, Node(start, None, 0, 0))
    visited = []
    count = 0
    
    while True:
        if len(nodes) == 0:
            return {'node':None, 'count':count }
        
        node = nodes.pop(0)
        
        if node.curState == goal:
            return {'node':node, 'count':count }
        
        visited.append(stateToHash(node.curState))
        if node.depth < depth :
            newNodes = getNewNodes(node, visited)
            count = count + 1
            newNodes.extend(nodes)
            nodes = newNodes
            
        
def idfs(start, goal):
    count = 0
    for i in range(0, 32):
        solution = dfs(start, goal, i)
        count = count + solution['count']
        if solution['node'] != None: 
            sols = getPath(solution['node'])
            for step in sols:
                display_board(step)
            print "solution found at depth %i" % (i) 
            print "Expanded %i nodes " % (count)
            return pathString(sols)
    print "No solution found for depth range [0,32]"
    print "Expanded %i nodes " % (count)
    return "No Solution"
    
def dfs_main(start, goal):
    solution = dfs(start, goal, 32)
    sols = getPath(solution['node'])
    for step in sols:
        display_board(step)
    print "Expanded %i nodes " % (solution['count'])
    print "path length %i" % (len(sols))
    return pathString(sols)
      
def manDist(entry, row, col):
    if entry == 0:
        return row + col
    elif entry == 1:
        return row + abs(col - 1)
    elif entry == 2:
        return row + abs(col - 2)
    elif entry == 3:
        return abs(row - 1) + col
    elif entry == 4:
        return abs(row - 1) + abs(col - 1)
    elif entry == 5:
        return abs(row - 1) + abs(col - 2)
    elif entry == 6:
        return abs(row - 2) + col
    elif entry == 7:
        return abs(row - 2) + abs(col - 1)
    else:
        return abs(row - 2) + abs(col - 2)
    
      
def guess(state):
    cost = 0
    for i in range(0,3):
        for j in range(0,3):
            cost = cost + manDist(state[i][j], i, j)
            
    return cost
    
    
def astar(start, goal):
    nodes = []
    nodes.insert(0, Node(start, None, 0, 0))
    count = 0
    
    while True:
        if len(nodes) == 0:
            return {'node':None, 'count':count }
        
        node = nodes.pop(0)
        
        if node.curState == goal:
            return {'node':node, 'count':count }
        
    
        newNodes = getNewNodes(node, [])
        count = count + 1
        newNodes.extend(nodes)
        nodes = newNodes
        nodes.sort()

def astar_main(start, goal):
    solution = astar(start, goal)
    sols = getPath(solution['node'])
    for step in sols:
        display_board(step)
    print "Expanded %i nodes " % (solution['count'])
    return pathString(sols)

class Node:
    def __init__( self, state, parent, depth, cost):
        self.curState = state
        self.parent = parent
        self.depth = depth
        self.cost = cost
        if state != None:
            self.guess = guess(state)
        else: 
            self.guess = 0
    
    def __lt__(self, other):
        return (self.cost + self.guess) < (other.cost + other.guess) 

