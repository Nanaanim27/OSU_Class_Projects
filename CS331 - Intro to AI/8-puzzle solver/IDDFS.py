import Common

def getNewNodes(cur, vis):
    nodes = []
    nodes.append(Node(mod(cur.curState, 'u'), cur, cur.depth +1))
    nodes.append(Node(mod(cur.curState, 'r'), cur, cur.depth +1))
    nodes.append(Node(mod(cur.curState, 'd'), cur, cur.depth +1))
    nodes.append(Node(mod(cur.curState, 'l'), cur, cur.depth +1))
    
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
    
def getPath(node):
    
    temp = node
    moves = []
    while temp != None:
        moves.insert(0, temp.curState)
        temp = temp.parent
        
    return moves
        

def dfs(start, goal, depth): 
    nodes = []
    nodes.insert(0, Node(start, None, 0))
    visited = []
    count = 0
    
    while True:
        if len(nodes) == 0:
            return {'node':None, 'count':count }
        
        node = nodes.pop()
        
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
            for step in getPath(solution['node']):
                display_board(step)
            print "solution found at depth %i" % (i) 
            print "Expanded %i nodes " % (count)
            return
    print "No solution found for depth range [0,32]"
    print "Expanded %i nodes " % (count)
    
def dfs_main(start, goal):
    solution = dfs(start, goal, 32)
    for step in getPath(solution['node']):
        display_board(step)
    print "Expanded %i nodes " % (solution['count'])
      

class Node:
    def __init__( self, state, parent, depth):
        self.curState = state
        self.parent = parent
        self.depth = depth
