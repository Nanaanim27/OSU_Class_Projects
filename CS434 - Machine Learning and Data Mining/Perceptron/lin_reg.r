main=function()
{

args <- commandArgs(trailingOnly = TRUE)
train=args[1]
test=args[2]
train=read.table(file=train, sep=",")
test=read.table(file=test, sep=",")


sum(c(4,3,2)*c(2,4,6))
 
print(train)
ap_train(train, 1)
}

#try to find some wx+ wx+ wx+ w0 thats maps a value between -1 and 1.


ap_train= function(data, epochs)
{
act_result=data[,1]
values=data[,2:ncol(data)]
cur_weights=vector(length = ncol(data)-1, mode="integer")
	for (j in 1:epochs) {
		for (i in 1:nrow(data)) {
			pred_result= sum(cur_weights * values[i,])
			print(pred_result)
			print(cur_weights)
			error= (pred_result - act_result[i])
			#Determines if our weights yield a incorrect result for 
			#this set of inputs. Runs correction routine.
					
			if (pred_result >= 0 & act_result[i] == -1){ 
				cur_weights= correct_weights(cur_weights, -1, 
				                             error, 1/j) 
			}
			else if (pred_result <= 0 & act_result[i] == 1) {
				cur_weights= correct_weights(cur_weights, 1, 
				                             error, 1/j) 
			}
		}
	}

}

#ap_classify (exam_x, weights)
#{




#}
correct_weights= function(weights, cor_dir,  error, cor_rate){



	return(weights)	
}


main()



