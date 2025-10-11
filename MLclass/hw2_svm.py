import sys
assert sys.version_info >= (3, 7)

from packaging import version
import sklearn

assert version.parse(sklearn.__version__) >= version.parse("1.0.1")

import matplotlib.pyplot as plt
import numpy as np
from sklearn.svm import SVC
from sklearn.preprocessing import StandardScaler
from sklearn.pipeline import make_pipeline

def svm_readMatrix(file):
    fd = open(file, 'r')
    hdr = fd.readline()
    rows, cols = [int(s) for s in fd.readline().strip().split()]
    tokens = fd.readline().strip().split()
    matrix = np.zeros((rows, cols))
    Y = []
    for i, line in enumerate(fd):
        nums = [int(x) for x in line.strip().split()]
        Y.append(nums[0])
        kv = np.array(nums[1:])
        k = np.cumsum(kv[:-1:2])
        v = kv[1::2]
        matrix[i, k] = v
    category = (np.array(Y) * 2) - 1
    return matrix, tokens, category


def svm_test(svm, matrix):
    M, N = matrix.shape
    output = np.zeros(M)

    ###################
    # Fill out your code here!
    # e.g., svm.predict()
    ###################

    return output

def svm_evaluate(output, label):
    error = (output != label).sum() * 1. / len(output)
    print('Error: %1.4f'%error)
    return error

def main():
    # Please set a training file that you want to use for this run below
    trainMatrix, tokenlist, trainCategory = svm_readMatrix('./data/hw2_MATRIX.TRAIN.400')
    testMatrix, tokenlist, testCategory = svm_readMatrix('./data/hw2_MATRIX.TEST')

    # SVM Classifier model

    # Hard margin SVM
    svm_clf_hard = SVC(kernel="linear", C=float("inf"), max_iter=10_000, random_state=42)      

    # Soft margin SVM
    # Find out the best parameters of C, max_iter, and so on
    svm_clf_soft = SVC(kernel="linear", C=1, max_iter=10_000, random_state=42)

    # Gaussian RBF SVM
    # Find out the best parameters of gamma, C, max_iter, and so on
    svm_clf_rbf = SVC(kernel="rbf", gamma=8, C=0.001, max_iter=10_000, random_state=42)

    scaler = StandardScaler()

    # Scaled version for each SVM and we will use these
    scaled_svm_clf_hard = make_pipeline(scaler, svm_clf_hard)
    scaled_svm_clf_soft = make_pipeline(scaler, svm_clf_soft)
    scaled_svm_clf_rbf = make_pipeline(scaler, svm_clf_rbf)

    # Train an SVM by forming suitable X and y
    # e.g., scaled_svm_clf_hard.fit(X, y) 
    # Refer to the above code for other SVMs

    output = svm_test(scaled_svm_clf_hard, testMatrix)

    svm_evaluate(output, testCategory)
    return

if __name__ == '__main__':
    main()
