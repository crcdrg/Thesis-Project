# -*- coding: utf-8 -*-
import numpy as np
import matplotlib.pyplot as plt
import os
import cv2
import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, Activation, Flatten
from tensorflow.keras.layers import Conv2D, MaxPooling2D
from tensorflow.keras.callbacks import TensorBoard
#import time

#tens_name = "Nike-vs-Adidas-cnn"
dir = "D:/Datasets/Sneakers"
cat = ["Nike", "Adidas"]

for category in cat: #for loop to go through the categories in the dataset
    path = os.path.join(dir, category) #create the path for the sneakers
    for img in os.listdir(path): #iterate all of the images
        img_array = cv2.imread(os.path.join(path, img), cv2.IMREAD_UNCHANGED) # convert the images into an array.

   # break 
   # break 

image_size = 50

#newimages = cv2.resize(img_array, (image_size, image_size))
# plt.imshow(newimages)
# plt.show()

training_data = []


def createdata():
    for category in cat:
        path = os.path.join(dir, category)
        class_no = cat.index(category)

        for img in os.listdir(path):

            try:
                #RGB_img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
                img2 = cv2.imread(os.path.join(path, img))
                RGB_img = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)
                newimage = cv2.resize(RGB_img, (image_size, image_size))
                training_data.append([newimage, class_no])
                #plt.imshow(newimage) # graph it
                #plt.show() # display!
            except Exception as e:
                pass


createdata()
print(len(training_data))

import random

random.shuffle(training_data)

#for sample in training_data[:2]:
 #print(sample[1])


X = []
y = []

for features, label in training_data:
         X.append(features)
         y.append(label)

#print(X[0].reshape(-1, image_size, image_size, 1))

X = np.array(X).reshape(-1, image_size, image_size, 3)
print(len(X))

import pickle

pickle_out = open("X.pickle","wb")
pickle.dump(X, pickle_out)
pickle_out.close()

pickle_out = open("y.pickle","wb")
pickle.dump(y, pickle_out)
pickle_out.close()



pickle_in = open("X.pickle","rb")
X = pickle.load(pickle_in)

pickle_in = open("y.pickle","rb")
y = pickle.load(pickle_in)

X = X / 255.0

# model = Sequential()
# 
# model.add(Conv2D(256, (3, 3), input_shape=X.shape[1:]))
# model.add(Activation('relu'))
# model.add(MaxPooling2D(pool_size=(2, 2)))
# 
# model.add(Conv2D(256, (3, 3)))
# model.add(Activation('relu'))
# model.add(MaxPooling2D(pool_size=(2, 2)))
# 
# model.add(Flatten()) # this converts our 3D feature maps to 1D feature vectors
# 
# model.add(Dense(64))
# model.add(Activation('relu'))
# 
# model.add(Dense(1))
# model.add(Activation('sigmoid'))
# 
# tensorboard = TensorBoard(log_dir="D:/Datasets/{}".format(tens_name))
# 
# model.compile(loss='binary_crossentropy',
# optimizer='adam',
# metrics=['accuracy'])
# 
# model.fit(X, y, batch_size=32, epochs=3, validation_split=0.3, callbacks=[tensorboard])

