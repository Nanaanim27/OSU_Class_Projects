'''
Created on Oct 3, 2012

@author: Nick
'''
import os

"""**************************************

User is prompted to enter a number that corresponds 
with a function.  The number is stored and switched on.
A function is executed according to what the user enters

***************************************"""
def main():
    
    selection = promptUser()
   
    while selection > 4 or selection == 0:   
        print "--INVALID ENTRY-- try again\n"
        selection = promptUser()
        
    if selection == 1:
        makeDirectory()
    if selection == 2:
        fiveDigitProd() 
    if selection == 3:
        nameValue()
    if selection == 4:
        triWords()
        
"""**************************************

 This function controls the prompt for selecting
 which function to call/check.  The user gets a description
 of each function a enters a digit.  The value is returned and
 the corresponding function is called from main.
 
 ****************************************"""   
def promptUser():


    
            
    print "Enter 1 to call the function that makes directories"
    print "Enter 2 to call the function that finds the greatest 5 digit product"
    print "Enter 3 to call the function that computes the total name scores"
    print "Enter 4 to call the function that computes the amount of triangle words"
    var = raw_input("Enter a number for which function you want to call: ")
    print ""
    intSelect = int(var)
     
    return intSelect


"""*****************************************

This function creates the specified subfolders in 
the CURRENT WORKING DIRECTORY. (after making sure 
it does not already exist) It then creates the two
specified symbolic links 

******************************************""" 

def makeDirectory():
    
    term = raw_input("Enter term of class: ")
    classX = raw_input("Enter Class Name: ")
    dirName = '.'
    linkPath1 = "/usr/local/classes/eecs/" +  term + "/" + classX + "/public_html"
    linkPath2 = "/usr/local/classes/eecs/" +  term + "/" + classX + "/handin"
    
    if not os.path.isdir(dirName + '/' + term):
        os.mkdir(dirName + '/' + term)
        os.mkdir(dirName + '/' + term + classX)
        os.mkdir(dirName + '/' + term + classX + '/assignment')
        os.mkdir(dirName + '/' + term + classX + '/examples')
        os.mkdir(dirName + '/' + term + classX + '/exams')
        os.mkdir(dirName + '/' + term + classX + '/lectrue_notes')
        os.mkdir(dirName + '/' + term + classX + '/submissions')
    
    if not os.path.islink(linkPath1):
        os.symlink(linkPath1, "website")
    if not os.path.islink(linkPath2):
        os.symlink(linkPath2, "handin")    
"""******************************************

This function creates a dictionary to assign 
number values to letters, reads in a text document
(as a string) and then using the split function converts 
it to a list. Then each name is iterated through and the 
numeric value of their name is multiplied by it's 
position in the array.  Then all of the scores are totalled.

*******************************************"""
         
def nameValue():
    
    letrDict = ({'A':1, 'B':2, 'C':3, 'D':4, 'E':5, 'F':6, 'G':7, 'H':8, 'I':9, 'J':10, 'K':11, 'L':12, 'M':13, 'N':14, 'O':15, 'P':16, 'Q':17, 'R':18, 'S':19, 'T':20, 'U':21, 'V':22, 'W':23, 'X':24, 'Y':25, 'Z':26})
    nameDoc = open('names.txt')
    nameList = nameDoc.read()
    newStr = nameList.replace("\"", "")
    nameList = newStr.split(',')
    nameList.sort()
  
    #iterative and summing variables 
    counter = 0
    wordVal = 0
    totalVal = 0
    
    for x in nameList:
        listName = list(x)
        counter += 1;
        for y in listName:
            temp  = letrDict.get(y)
            wordVal += temp
        
        wordVal = wordVal*counter 
        totalVal += wordVal   
        wordVal = 0;    
        
    print "the total of all the name scores is: %d "  % totalVal     
"""******************************************

This Function converts a large number into a string
(so it can be iterated through) then using a starting
index of zero and enters a while loop, that creates a
"sliding window" of 5 consecutive numbers, computes their 
product and stores it in the runningMax variable.  The 
loop terminates when the front index is (length - 5) 
and the tail index is the end.  Then the runningMax
is printed

*******************************************"""
def fiveDigitProd():
    #converts 1000 digit number to string
    bigNum = 7316717653133062491922511967442657474235534919493496983520312774506326239578318016984801869478851843858615607891129494954595017379583319528532088055111254069874715852386305071569329096329522744304355766896648950445244523161731856403098711121722383113622298934233803081353362766142828064444866452387493035890729629049156044077239071381051585930796086670172427121883998797908792274921901699720888093776657273330010533678812202354218097512545405947522435258490771167055601360483958644670632441572215539753697817977846174064955149290862569321978468622482839722413756570560574902614079729686524145351004748216637048440319989000889524345065854122758866688116427171479924442928230863465674813919123162824586178664583591245665294765456828489128831426076900422421902267105562632111110937054421750694165896040807198403850962455444362981230987879927244284909188845801561660979191338754992005240636899125607176060588611646710940507754100225698315520005593572972571636269561882670428252483600823257530420752963450
    bigNumStr = str(bigNum)
    
    #iterative and summing variables
    index = 0
    tempFive = 0
    tempMax = 0
    x = 0
    runningMax = 0

    while x < (len(bigNumStr) - 4):
        tempStr = str(bigNumStr[index] + bigNumStr[index+1] + bigNumStr[index+2] + bigNumStr[index+3] + bigNumStr[index+4])
        tempFive = map(int, tempStr)
        tempMax = tempFive[0]*tempFive[1]*tempFive[2]*tempFive[3]*tempFive[4]
        if tempMax > runningMax:
            runningMax = tempMax
        index += 1
        x += 1
        
    
    print "The maximum product of 5 consecutive integers in the 1000 digit number is: %d" % runningMax     
    
    
 
        
     
            
"""******************************************

This function creates a dictionary to assign 
number values to letters, reads in a text document
(as a string) and then using the split function converts 
it to a list. Then a sufficiently large list for this 
is created and populated with computed triangle numbers.
The list of words is the iterated through, computing their
numeric value.  Each value is then checked against the list of
triangle numbers; if the list of triangle numbers contains
the word value, a variable tracking the number of triangle numbers
is incremented, and at the end, printed.

*******************************************"""            
def triWords():
    
    letrDict = ({'A':1, 'B':2, 'C':3, 'D':4, 'E':5, 'F':6, 'G':7, 'H':8, 'I':9, 'J':10, 'K':11, 'L':12, 'M':13, 'N':14, 'O':15, 'P':16, 'Q':17, 'R':18, 'S':19, 'T':20, 'U':21, 'V':22, 'W':23, 'X':24, 'Y':25, 'Z':26})
    wordDoc = open('words.txt')
    wordList = wordDoc.read()
    
    newStr = wordList.replace("\"", "")
    wordList = newStr.split(',')
    
    
    num = range(100)
    index = 0 
    triNumbers =[]
    wordVals = []
    numTris = 0
    for x in num:
        index =  x * (x+1) / 2
        triNumbers.append(index) 
    
    
    wordVal = 0    
    for x in wordList:
        listWord = list(x)
        for y in listWord:
            temp  = letrDict.get(y)
            wordVal += temp
        wordVals.append(wordVal)    
        wordVal = 0
        
 
    for x in wordVals:
        if x in triNumbers:
            numTris += 1
    
    print "The number of triangle words in the list of words is: %d" % numTris  
    
        
   
if __name__ == '__main__':   
    main() 
  
  
        
