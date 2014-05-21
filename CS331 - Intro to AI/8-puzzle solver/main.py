import sys
import BFS
import Common

startState = Common.read_data_set(sys.argv[1]);
goalState = Common.read_data_set(sys.argv[2]);
outfile = sys.argv[4];
path = ""

if sys.argv[3] == 'dfs':
    path = Common.dfs_main(startState,goalState);
elif sys.argv[3] == 'bfs':
    path = BFS.BFS(startState, goalState);
elif sys.argv[3] == 'iddfs':
    path = Common.idfs(startState, goalState);
elif sys.argv[3] == 'astar':
    path = Common.astar_main(startState, goalState);
else:
    print("Invalid argument for algorithm MODE")
    

f = open(outfile,'w')
f.write(path)
f.close()
