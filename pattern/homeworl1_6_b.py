import numpy as np
import pandas as pd
from sklearn.svm import SVC
import matplotlib.pyplot as plt

data = pd.read_csv('pattern/data_svm_rbf.csv')
x = data.iloc[:,:2].values
cls = data.iloc[:,2].values

def rbf_decision_boundary(x, cls, C_value, gamma_value):

    model = SVC(kernel='rbf', C=C_value, gamma=gamma_value)
    model.fit(x, cls)

    plt.figure(figsize=(5,5))
    x_min, x_max = x[:,0].min()-1, x[:,0].max()+1
    y_min, y_max = x[:,1].min()-1, x[:,1].max()+1

    xx, yy = np.meshgrid(np.linspace(x_min,x_max, 100),np.linspace(y_min, y_max, 100))
    z = model.predict(np.c_[xx.ravel(), yy.ravel()])
    z = z.reshape(xx.shape)

    plt.contour(xx, yy, z, levels=[0.5], colors='black')
    plt.scatter(x[:,0],x[:,1],c=cls, cmap=plt.cm.coolwarm,s=30,edgecolors='k')
    
    plt.title(f'Kernel SVM (C={C_value}, gamma={gamma_value})')
    plt.xlabel('feature 1')
    plt.ylabel('feature 2')
    plt.show()

## C=1000
rbf_decision_boundary(x,cls,1000,0.1)
rbf_decision_boundary(x,cls,1000,1)
rbf_decision_boundary(x,cls,1000,10)

## C=1
rbf_decision_boundary(x,cls,1,0.1)
rbf_decision_boundary(x,cls,1,1)
rbf_decision_boundary(x,cls,1,10)