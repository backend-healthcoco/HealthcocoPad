package com.healthcoco.healthcocopad.custom;


import androidx.core.content.FileProvider;

public class MyFileProvider extends FileProvider {
    //we extend fileprovider to stop collision with chatbot library file provider
    //this class is empty and used in manifest
}