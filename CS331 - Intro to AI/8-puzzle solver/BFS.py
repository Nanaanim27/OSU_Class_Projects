import collections
import copy
import Common



class stateNode:
    def __init__(self, curState, curParent, direction):
        self.curState = curState
        self.curParent = curParent
        self.dir = direction

def BFS(start, goal):
    
    notVis = []
    pathLen = 0
    nodeCount = 0
    goalHash = Common.stateToHash(goal)
    #children = []
    n = stateToNode(start, None)
    
    if(Common.stateToHash(n.curState) == goalHash):
        return [True, 0]
    
    vis = []
    vis.append(Common.stateToHash(n.curState))
    
    while True:   
        children = []
        if(notVis.__len__() != 0):
            n = notVis.pop(0)
        
        succ = generateSuccesorStates(n.curState)
        
        if succ.__len__() == 0:
            break
        for x in succ:
            child = stateToNode(x, n)
            children.append(child)
            
        for i in children:
            curHash = Common.stateToHash(i.curState)
            nodeCount = nodeCount+1
            
            if(curHash not in vis):
                if(curHash == goalHash):
                    #path = getPath(i, (pathLen+1))
                    path = getPath(i)
                    for j in reversed(path):
                        displayBoard(j)
                        print""
                    print "Number of Nodes Expanded: ", nodeCount
                    stateStrings = Common.pathString(path)
                    return stateStrings
               
                notVis.append(i)
                vis.append(curHash)
        pathLen = pathLen + 1
        
    print "No path Found"
    return []
        
        
        
def generateSuccesorStates(state):
    ret = []
    temp = copy.deepcopy(state)
    
    up = Common.mod(temp, 'u')
    if up != None:
        temp = copy.deepcopy(state)
        ret.append(up)
    right = Common.mod(temp, 'r')
    if right != None:
        temp = copy.deepcopy(state)
        ret.append(right)
    down = Common.mod(temp, 'd')
    if down != None:
        temp = copy.deepcopy(state)
        ret.append(down)
    left = Common.mod(temp, 'l')
    if left != None:
        temp = copy.deepcopy(state)
        ret.append(left)
    
    return ret
    
def getPath(curNode):
    path = []
    path.append(curNode.curState)
    while curNode.curParent != None:
        path.append(curNode.curParent.curState)
        temp = curNode
        curNode = temp.curParent
    return path

def stateToNode(cur, parent):
    
    node = stateNode(cur, parent, None)
    #node.curState = cur
    #node.curParent = parent
    return node    
    
def displayBoard( state ):
    print "-------------"
    print "| %i | %i | %i |" % (state[0][0], state[0][1], state[0][2])
    print "-------------"
    print "| %i | %i | %i |" % (state[1][0], state[1][1], state[1][2])
    print "-------------"
    print "| %i | %i | %i |" % (state[2][0], state[2][1], state[2][2])
    print "------------"
   
    
    
    