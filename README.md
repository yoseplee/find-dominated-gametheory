# find-dominated-gametheory
2019.06.06

Language: Java

This program has an attribute represents game matrix with 2-demension integer array

can find dominated row or column from integer array
and also adjust a matrix into dominated game matrix

now you can utilize this program as a simple study of game theory. help you find out a matrix is can find minimax strategies easily or not.

result is below
```
the number of rows: 4
the number of columns: 4

=======GAME MATRIX=======

0th |  16   3   2  13 
1th |   5  10  11   8 
2th |   9   6   7  12 
3th |   4  15  14   1 

=======DOMINATION MATRIX=======
there's no dominated row or column
0th |  16   3   2  13 
1th |   5  10  11   8 
2th |   9   6   7  12 
3th |   4  15  14   1 

the number of rows: 6
the number of columns: 6

=======GAME MATRIX=======

0th |  13  29   8  12  16  23 
1th |  18  22  21  22  29  31 
2th |  18  22  31  31  27  37 
3th |  11  22  12  21  21  26 
4th |  18  16  19  14  19  28 
5th |  23  22  19  23  30  34 

=======DOMINATION MATRIX=======
there is dominated row or column but it's not enough to adjust to 2x2 matrix
0th |  13  29   8 
1th |  18  22  31 
2th |  23  22  19 
```
