#!/bin/bash

#Maybe make a line which makes the direcotry if it doesn't exist yet?
rm ./J2VParser/*.class
cp ./J2V.java ./hw3/J2V.java
cp ./J2VParser/* ./hw3/J2VParser/
tar -czvf hw3.tgz hw3
