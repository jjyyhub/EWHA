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

from hw2_nb import readMatrix, nb_train, nb_test

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

    output = svm.predict(matrix)

    return output

def svm_evaluate(output, label):
    error = (output != label).sum() * 1. / len(output)
    print('Error: %1.4f'%error)
    return error

def main():
    # Please set a training file that you want to use for this run below
    trainMatrix, tokenlist, trainCategory = svm_readMatrix('./data/hw2_MATRIX.TRAIN')
    testMatrix, tokenlist, testCategory = svm_readMatrix('./data/hw2_MATRIX.TEST')

    # SVM Classifier model

    # Hard margin SVM
    svm_clf_hard = SVC(kernel="linear", C=float("inf"), max_iter=10_000, random_state=42)      

    # Soft margin SVM
    # Find out the best parameters of C, max_iter, and so on
    svm_clf_soft = SVC(kernel="linear", C=0.1, max_iter=10_000, random_state=42)

    # Gaussian RBF SVM
    # Find out the best parameters of gamma, C, max_iter, and so on
    svm_clf_rbf = SVC(kernel="rbf", gamma=1e-5, C=100, max_iter=10_000, random_state=42)

    scaler = StandardScaler()

    # Scaled version for each SVM and we will use these
    scaled_svm_clf_hard = make_pipeline(scaler, svm_clf_hard)
    scaled_svm_clf_soft = make_pipeline(scaler, svm_clf_soft)
    scaled_svm_clf_rbf = make_pipeline(scaler, svm_clf_rbf)

    # Train an SVM by forming suitable X and y
    scaled_svm_clf_hard.fit(trainMatrix, trainCategory)
    scaled_svm_clf_soft.fit(trainMatrix, trainCategory)
    scaled_svm_clf_rbf.fit(trainMatrix, trainCategory)
    # Refer to the above code for other SVMs

    output_hard = svm_test(scaled_svm_clf_hard, testMatrix)
    output_soft = svm_test(scaled_svm_clf_soft, testMatrix)
    output_rbf = svm_test(scaled_svm_clf_rbf, testMatrix)

    print("Hard-margin SVM ")
    svm_evaluate(output_hard, testCategory)
    print("Soft-margin SVM")
    svm_evaluate(output_soft, testCategory)
    print("Gaussian RBF kernel SVM")
    svm_evaluate(output_rbf, testCategory)

    #### (b) ####
    size = [50,100,200,400,800,1400]
    e_nb, e_hard, e_soft, e_rbf = [],[],[],[]

    for i in size:
        trainMatrix, tokenlist, trainCategory = readMatrix(f'./data/hw2_MATRIX.TRAIN.{i}')
        testMatrix, tokenlist, testCategory   = readMatrix('./data/hw2_MATRIX.TEST')

        # Naive Bayes
        state = nb_train(trainMatrix,trainCategory)
        output = nb_test(testMatrix,state)
        e = (output != testCategory).mean()
        e_nb.append(e)

        # SVM
        scaled_svm_clf_hard.fit(trainMatrix, trainCategory)
        scaled_svm_clf_soft.fit(trainMatrix, trainCategory)
        scaled_svm_clf_rbf.fit(trainMatrix, trainCategory)
        output_hard = svm_test(scaled_svm_clf_hard, testMatrix)
        output_soft = svm_test(scaled_svm_clf_soft, testMatrix)
        output_rbf = svm_test(scaled_svm_clf_rbf, testMatrix)


        e_hard.append((output_hard != testCategory).mean())
        e_soft.append((output_soft != testCategory).mean())
        e_rbf.append((output_rbf != testCategory).mean())
    
    plt.figure(figsize=(8,5))
    plt.plot(size, e_nb,   'o-', label='Naive Bayes')
    plt.plot(size, e_hard, 's-', label='SVM Hard Margin')
    plt.plot(size, e_soft, '^-', label='SVM Soft Margin')
    plt.plot(size, e_rbf,  'd-', label='SVM RBF Kernel')

    plt.xlabel('Training set size')
    plt.ylabel('Test Error')
    plt.legend()
    plt.grid(True)
    plt.show()

    return

if __name__ == '__main__':
    main()
