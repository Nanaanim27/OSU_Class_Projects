'''
Created on Oct 15, 2013

@author: Nick
'''
from __future__ import division
import csv
from math import log 
import itertools
import sys

# from collections import Counter


'''
reads in csv files and returns list of data
'''

def read_data_set(file):
    
    data = []
    temp = []
    data_reader = csv.reader(open(file, 'rb')) 
    
    for row in data_reader:
        data.append(map(int, row))
    
    return data

choice = read_data_set('ttt_train.csv')


class Node: 

    def __init__(self, dataSet, fs, fv): 
        self.data = dataSet
        self.feat_set = fs
        self.feat_val = fv
        self.pf_choice = None
        
       
    
    '''
    Function to calculate the percentage of a feature set that passes
    and the percentage that fails.  
    '''
    def calc_pass_fail_percentage(self, data):
        
        passNum = 0
        failNum = 0
        
        for x in data:
            if x[0] == 1:
                passNum += 1
            else:
                failNum += 1
                    
        if(len(data) == 0):
            return[0,0,passNum,failNum]
        else:
            return [passNum/len(data), failNum/len(data), passNum, failNum]
    
    '''
    takes a percentage val and calculates the entropy each iteration
    '''
               
    def calc_entropy (self, x):
        
        if(x == 0 or x == 1):
            return 0
        
        temp = -1*(x)*log(x,2)
        temp2 = temp - (1-x)*log((1-x),2)
        
        return temp2
           
    '''
    each time a data set is subsetted, it is passed to this function.
    Here it checks if a feature set contains all the same values. If
    it does it removes the column from the data set as it is no longer
    needed
    
    
    def try_delete_col(self,dataSet):
        
        length = len(dataSet[0])
        temp = []
        same_col = 0
        
        for cols in xrange(1, length):
            
            for x in dataSet:
                
                temp.append(x[cols])
            #If all values in a feature set are the same, store the index
            if(len(set(temp)) == 1):
                same_col = cols
                  
            temp = []
        #If identical column remove values from column
        if(same_col == 0):
            return dataSet
        else:
            for i in dataSet:
                i.pop(same_col)
          
        return dataSet
    
    
    Based on a chosen feature, a subset of the data is created
    to be returned and recursively called
    
    def subset_data(self, dataSet, featureSet, featureVal):
        
        tempData = []
        l_data = []
        r_data = []
        
        #if the values in the target feature occurs create a subset with all rows in which it occurs
        for i in dataSet:
            if(i[featureSet] == featureVal):
                tempData.append(i)
            
        l_data = tempData
        tempData = [] 
        
        #if the values not in the target feature occurs create a subset with all rows in which it does not occur
        for i in dataSet:
            if(i[featureSet] != featureVal):
                tempData.append(i)
        
        r_data = tempData

        return[r_data, l_data]
    
    
    function to be called that will take an integer indicating a feature
    set (column).  It will return a list of lists of each possible feature 
    value in respective feature sets
    '''
    
    def seperate_features(self, dataSet, feature_set):

        
        #iterate through feature set
        #print "fs", feature_set
        temp = []
        result = []
        ind_list = []
        for i in dataSet:
            temp.append(i[feature_set])
        
        #find number of unique elements in feature set

        uniq = set()
        for i, x in enumerate(temp,1):
            uniq.add(x)
            
        set_size = len(uniq)
        
        #add each list of features to a list
        for i in xrange(0,(set_size)):
            
            for features in dataSet:
                
                if features[feature_set] == i:
                    ind_list.append(features)
            result.append(ind_list)
            
            ind_list = []
      
                
        return result      
        
    '''
    Function that iterates through each feature set.  A function is called
    to iterate through each feature value and retrieve lists of possible 
    outcome.  As the lists are returned, they will be iterated through, 
    calculating the percentage and entropy of each possible feature branch.
    a running max accompanied by feature column will be stored.  
    '''   
   

    def choose_root_node(self, dataSet, par_ent):
        
      
       
        max_info_gain = 0
        entropies = []
        done_flag = 0
        count = 0
      
        for i in xrange(1, len(dataSet[0])):
            
            vals = []
            classes = []
            classes = self.seperate_features(dataSet, i)
            #check each classes features info_gain
            for j in classes:
                #print j
                count  += 1
                
                pf = self.calc_pass_fail_percentage(j)
                #in info gain is 1 we know it is automatically the right choice
                if(pf[0] == 1.0):
                    max_info_gain = 1.0
                    max_info_set = i
                    done_flag = 1
                    least_entro_feat_val = count-1
                    passNum = pf[0]
                    failNum = pf[1]
                    break
                entropy = self.calc_entropy(pf[0])

                entropies.append(entropy)
                vals.append((pf[2]+pf[3])/len(dataSet)*entropy)
            
                  
            if(done_flag == 1):
                break
            info_gain = par_ent - (sum(vals))
            
            if(info_gain > max_info_gain):
                passNum = pf[0]
                failNum = pf[1]
                max_info_gain = info_gain
                max_info_set = i
                least_entro_feat_val = count-1
                
            count = 0 
        print '-------------------------------------------------'
        print 'Learning Rate: ', max_info_gain
        print 'pass: ', passNum
        print 'fail: ', failNum
        print 'max_info_set (chosen feature set): ', max_info_set
        print 'chosen feature value: ', least_entro_feat_val
        print '-------------------------------------------------'
        stump = Node(dataSet, max_info_set, least_entro_feat_val)
        if(failNum > passNum):
            stump.pf_choice = 0
        else:
            stump.pf_choice = 1
        
        return stump
        '''
        subsets = self.subset_data(dataSet, max_info_set, least_entro_feat_val)
        
        l_data = subsets[0]
        r_data = subsets[1]
        
        '''
       
    def weighted_error(self, dataSet, stump): 
        set = stump.feat_set
        val = stump.feat_val
        choice  = stump.pf_choice
        count = 0
        
        for entry in dataSet:
            if(entry[set] == val and entry[0] == choice):
                count += 1
        
        print count               
        error = (count/len(dataSet))*100
        print error
        return 
    
    
    
test = Node(choice, 0, 0)

perc = test.calc_pass_fail_percentage(choice)
par_ent = test.calc_entropy(perc[0])
stump = test.choose_root_node(choice, par_ent)
print 'Stump chosen'
print "set", stump.feat_set
print "val", stump.feat_val
print "choice", stump.pf_choice


dummy = test.weighted_error(choice, stump)



        


















