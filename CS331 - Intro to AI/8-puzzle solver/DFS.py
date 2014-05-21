import Common
import copy

path = []
def dfs_rec(oldCur, d, oldVis, goal):
    cur = copy.deepcopy(oldCur)
    vis = copy.deepcopy(oldVis)
    #change current state in the direction of d 
    cur = Common.mod(cur, d)  
    
    #if our current state could not be reached (ie trying to swap with outside of the matrix) don't check this branch
    if(cur == None):
        return False
    hashstate = Common.stateToHash(cur)
    #if we are the goal state return our current state and true
    if(hashstate == goal):
        path.insert(0, cur)
        return True
    #if this is a visited state stop
    if(hashstate in vis):
        return False
    #if not visited then add it to the visited list
    else:
        vis.append(hashstate)
    #check right move
    ret = dfs_rec(cur, 'l', vis, goal)
    if(ret == True):
        path.insert(0, cur)
        return True
    #check left move
    ret = dfs_rec(cur, 'r', vis, goal)
    if(ret == True):
        path.insert(0, cur)
        return True
    #check up move
    ret = dfs_rec(cur, 'u', vis, goal)
    if(ret == True):
        path.insert(0, cur)
        return True
    #check left move
    ret = dfs_rec(cur, 'd', vis, goal)
    if(ret == True):
        path.insert(0, cur)
        return True
    else:
        return False 

def DFS(start, goal):
    #convert goal state to hash string
    g = Common.stateToHash(goal)
    #make the list of visted an empty list
    vis = []
    d = 'n'
    res = dfs_rec(start, d, vis, g)
    if(res): 
        print("found goal state")
        print(path)
    else:
        print("did not find goal state")
