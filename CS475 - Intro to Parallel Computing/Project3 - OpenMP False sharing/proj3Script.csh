#!/bin/csh
foreach t (1 2 4 )
 foreach s ( 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20)
  g++ -DNUM=$s -DNUMT=$t CS475_Project3.cpp -o p3 -lm -fopenmp
  ./p3
 end
end
