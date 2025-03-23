import numpy as np
import matplotlib.pyplot as plt
import math

## 방법1
mu_x = 0
sigma_x = 1
mu_y = 1 
sigma_y = 1
x = np.random.normal(mu_x, sigma_x, 1000)
y = np.random.normal(mu_y, sigma_y, 1000)

z1 = x+y

plt.figure(figsize=(12,5))

plt.subplot(1,2,1)
plt.hist(z1, bins=30, edgecolor='black')
plt.title("Histogram of Z=X+Y")
plt.xlabel("Z")
plt.ylabel("Density")
plt.xlim(-6, 6) 

## 방법2
mu_z = 1
sigma_z = math.sqrt(2)
z2 = np.random.normal(mu_z, sigma_z, 1000)

plt.subplot(1,2,2)
plt.hist(z2, bins=30, color='green',edgecolor='black')
plt.title("Histogram of PDF Z")
plt.xlabel("Z")
plt.ylabel("Density")
plt.xlim(-6, 6) 
plt.show()
