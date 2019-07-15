# Client Server system that classifies sneakers using Tensorflow &amp; Keras

This project consists of an Android Application that is connected to a LAMP server hosted on Digital Ocean. The user takes a picture of a pair of sneakers, then they upload it to the server and the result-prediction from the convolutional neural network is returned. The application is connected to the server through PHP to exchange data and then python (tensorflow and keras) is used for the machine learning algorithm in oder to make predictions. The system returns a result whether the pair of sneakers uploaded belong to Nike or Adidas.

## Getting Started

Following the instructions will help you set up the application to your android phone alongside with a LAMP server to communicate with one another.

### Prerequisites

The list above is necessary in order to run the application:

```
Android Phone to install the application
Apache server to communicate and make predictions (I hosted mine on Digital Ocean)
Python
Tensorflow
Keras
PHP
```

### How to install

```
Install the application on your mobile phone
Inside the MainActivity file, line 290 and line 374 change the IP address to the one that correlates 
to your server
There is a folder named "python", inside it there's a file named info6.php which is used to save 
the image the user uploads. Inside that folder there are 3 files necessary to build the model, 
train the convolutional neural network, make predictions and then return the results.

Depending on the computational power of your computer/server, the model can be trained on a robust machine
in order to do it faster, then save the model and copy it to the server so it can be used for predictions.
```

* dissertation.py - To create the dataset
* DissTrainKeras.py - To train the convolutional neural network
* DebianPrediction.py - To make the prediction and save the file to a text file


## Design

The communication is happening between the system and the user. The administrator isn't involved in the exchange.


![alt text](https://raw.githubusercontent.com/Jimakosg/Thesis-Project/master/Annotation%202019-07-15%20143622.png)

![alt text](https://raw.githubusercontent.com/Jimakosg/Thesis-Project/master/Annotation%202019-07-15%20143657.png)
![alt text](https://raw.githubusercontent.com/Jimakosg/Thesis-Project/master/Annotation%202019-07-15%20143710.png)
![alt text](https://raw.githubusercontent.com/Jimakosg/Thesis-Project/master/Annotation%202019-07-15%20160545.png)


## Testing

Using a tesnorflow callback called Tensorboard i measured the accuracy of the predictions the neural network attempted.

![alt text](https://raw.githubusercontent.com/Jimakosg/Thesis-Project/master/Annotation%202019-07-15%20160840.png)

![alt text](https://raw.githubusercontent.com/Jimakosg/Thesis-Project/master/Annotation%202019-07-15%20160853.png)

