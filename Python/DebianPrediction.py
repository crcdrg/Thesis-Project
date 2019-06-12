import cv2
import tensorflow as tf
import numpy as np
cat = ["Nike", "Adidas"]


def prepare(filepath):
    IMG_SIZE = 50  # 50 in txt-based
    img_array = cv2.imread(filepath, cv2.IMREAD_UNCHANGED)
    RGB_img = cv2.cvtColor(img_array, cv2.COLOR_BGR2RGB)
    newimage = cv2.resize(RGB_img, (IMG_SIZE, IMG_SIZE))
    #print(len(img_array))
    return newimage.reshape(-1, IMG_SIZE, IMG_SIZE, 3)


model = tf.keras.models.load_model("/home/vnc/Downloads/disserationmodel")

prediction = model.predict([prepare('/home/vnc/Public/test.png')])
print(cat[int(prediction[0][0])])

with open('/home/vnc/Downloads/results.txt', 'w') as f:
    f.write(cat[int(prediction[0][0])])
