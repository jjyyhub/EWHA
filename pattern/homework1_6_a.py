import numpy as np
import pandas as pd
from sklearn.svm import SVC
import matplotlib.pyplot as plt

data = pd.read_csv('pattern/data_svm_lin.csv')
x = data.iloc[:,:2].values
cls = data.iloc[:,2].values

def lin_decision_boundary(x, cls, C_value):

    model = SVC(kernel='linear', C=C_value)
    model.fit(x, cls)

    plt.figure(figsize=(5,5))
    x_min, x_max = x[:,0].min()-1, x[:,0].max()+1
    y_min, y_max = x[:,1].min()-1, x[:,1].max()+1

    xx, yy = np.meshgrid(np.linspace(x_min,x_max, 100),np.linspace(y_min, y_max, 100))
    z = model.predict(np.c_[xx.ravel(), yy.ravel()])
    z = z.reshape(xx.shape)

    plt.contourf(xx,yy,z,alpha=0.3,cmap=plt.cm.coolwarm)
    plt.scatter(x[:,0],x[:,1],c=cls, cmap=plt.cm.coolwarm,s=30,edgecolors='k')
    plt.scatter(model.support_vectors_[:,0], model.support_vectors_[:,1], s=100, facecolor='green',edgecolors='k',label='Support vectors')
    
    plt.title(f'Linear SVM (C={C_value})')
    plt.xlabel('feature 1')
    plt.ylabel('feature 2')
    plt.show()


## C=1000
lin_decision_boundary(x,cls,1000)

## C=10
lin_decision_boundary(x,cls,10)

## C=1
lin_decision_boundary(x,cls,1)

## C=0.1
lin_decision_boundary(x,cls,0.1)