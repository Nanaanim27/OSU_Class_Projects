#!/bin/csh
foreach t(1 2 4 6 8 16)
  g++ -DNUMTHREADS=$t 475_Project2.cpp -o p2 -lm -fopenmp
  ./p2
 end
end
