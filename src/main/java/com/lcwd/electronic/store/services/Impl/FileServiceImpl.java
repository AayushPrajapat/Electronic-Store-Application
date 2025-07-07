package com.lcwd.electronic.store.services.Impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Override/*Multipart file se uss file(Upload ki gai file) ki information nikal skte hai*/
	public String uplaodImage(MultipartFile file, String path) throws IOException {

		String originalFilename = file.getOriginalFilename();
		log.info(" Original File name {}", originalFilename);
		String fileName = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtension = fileName + extension;
		String fullPathWithFileName = path + File.separator + fileNameWithExtension;
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg")
				|| extension.equalsIgnoreCase(".jpg")) {
//			file save------create folder
			File folder = new File(path);
			if (!folder.exists()) {
//				create the folder
				folder.mkdirs();
			}
//			upload
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
			return fileNameWithExtension;

		} else {
			throw new BadApiRequest("File With this " + extension + "not allowed");
		}

	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {

		String fullPath = path + File.separator + name;

		InputStream inputStream = new FileInputStream(fullPath);

		return inputStream;
	}

}
