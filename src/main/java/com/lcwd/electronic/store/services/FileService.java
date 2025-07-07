package com.lcwd.electronic.store.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
//	kis path pe user ki impage upload krni hai
	 String uplaodImage(MultipartFile multipartFile,String path) throws IOException;// kis path pe user ki image write krni hai

	InputStream getResource(String path,String name) throws FileNotFoundException;//folder tak ka path or name dena hoga
	

}
