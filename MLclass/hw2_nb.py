import matplotlib.pyplot as plt
import numpy as np


def readMatrix(file):
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
    return matrix, tokens, np.array(Y)

def nb_train(matrix, category):
    state = {}
    N = matrix.shape[1]
    cl = np.unique(category)

    prior = {} # 확률
    likelihood = {} # 조건부 확률

    for c in cl:
        idx = np.where(category==c)[0]
        X = matrix[idx,:]

        prior[c] = np.log(len(idx)/len(category))

        word = np.sum(X,axis=0)
        total = np.sum(word)

        # laplace smoothing 적용
        likelihood[c] = np.log((word+1)/(total+N))

    state['prior'] = prior
    state['likelihood'] = likelihood

    return state

def nb_test(matrix, state):
    output = np.zeros(matrix.shape[0])
    prior = state['prior']
    likelihood = state['likelihood']
    cl = list(prior.keys())

    for i in range(matrix.shape[0]):
        word = matrix[i,:]
        prob = {}

        for c in cl:
            # underflow 방지 -> log 덧셈
            prob[c] = prior[c] + np.sum(word * likelihood[c])

        output[i] = max(prob, key=prob.get)

    return output

def evaluate(output, label):
    error = (output != label).sum() * 1. / len(output)
    print('Error: %1.4f'%error)

def main():
    # Please set a training file that you want to use for this run below
    trainMatrix, tokenlist, trainCategory = readMatrix('./data/hw2_MATRIX.TRAIN')
    testMatrix, tokenlist, testCategory = readMatrix('./data/hw2_MATRIX.TEST')

    state = nb_train(trainMatrix, trainCategory)
    output = nb_test(testMatrix, state)

    print('(a) Test set error')
    evaluate(output, testCategory)

    ##### (b) #####
    print('(b) Top 5 tokens list')
    spam = state['likelihood'][1]
    not_spam = state['likelihood'][0]

    # log 뺄셈
    dif = spam - not_spam

    top = np.argsort(dif)[-5:][::-1]
    for i in top:
        print(tokenlist[i], '%1.4f'%dif[i])

    ##### (c) #####
    size = [50,100,200,400,800,1400]
    error = []

    for i in size:
        path = f'./data/hw2_MATRIX.TRAIN.{i}'
        trainMatrix, tokenlist, trainCategory = readMatrix(path)
        testMatrix, tokenlist, testCategory = readMatrix('./data/hw2_MATRIX.TEST')

        state = nb_train(trainMatrix, trainCategory)
        output = nb_test(testMatrix, state)
        e = (output != testCategory).mean()
        error.append(e)

    plt.plot(size, error, marker='o')
    plt.xlabel('hw2_MATRIX.TRAIN Size')
    plt.ylabel('Test Error')
    plt.ylim(0.01,0.05)
    plt.show()
    return

if __name__ == '__main__':
    main()

    

