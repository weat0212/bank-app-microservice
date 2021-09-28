package com.webcomm.creditcardservice.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webcomm.creditcardservice.domain.CreditCard;
import com.webcomm.creditcardservice.domain.CreditCardDto;
import com.webcomm.creditcardservice.domain.UserCaseDto;
import com.webcomm.creditcardservice.filetransfer.FtpClient;
import com.webcomm.creditcardservice.service.CreditCardService;


/**
 * @author andy.wang
 *
 */

@RestController
public class CreditCardController {

	final Logger logger = LoggerFactory.getLogger(CreditCardController.class);
	
	@Autowired
	private CreditCardService creditCardService;
	

	@GetMapping("/filetrans/test")
	@ResponseBody
	public String test() {
		return "{\"message\":\"This is a test\"}";
	}
	
//	@GetMapping("/error")
//	@ResponseBody
//	public String testError() {
//		throw new RuntimeException("Some error has happended...");
//	}
	
	@PostMapping(path = "/filetrans/{userId}/trans/{caseNumber}", produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String uploadCase(@PathVariable String userId, @PathVariable String caseNumber, @RequestBody CreditCardDto creditCardDto) throws IOException {

		logger.debug("Getting Credit Card Information...");

		if(!creditCardService.findByCaseNumber(caseNumber).isPresent()) {
			CreditCard toSave = new CreditCard();
			toSave.setUserId(userId);
			toSave.setCaseNumber(caseNumber);
			toSave.setMessageNumber(creditCardDto.getNo());
			toSave.setCreditNumber(creditCardDto.getCno());
			toSave.setCustomerName(creditCardDto.getName());
			toSave.setUploadTime(new Timestamp(System.currentTimeMillis()));
			
			creditCardService.save(toSave);
		} else {
			logger.error(caseNumber + "Repeat Error");
			throw new IllegalArgumentException("Case Number:" + caseNumber + " Already Exist!");
		}
		
		
		String outXml = createXmlFormat(caseNumber, creditCardDto);
		FtpClient ftpClient = connectFtpFactory();
		
		// Create file based on the caseNumber .xml
		try {
			ftpClient.putFileToPath(writeString2File(outXml, caseNumber),
					String.format("/%s.xml", caseNumber));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			logger.info("Case Number:" + caseNumber + " Upload Success!");
			ftpClient.close();
		}
		return outXml;
	}


	@GetMapping(path = "/filetrans/download/{caseNumber}")
	public void downloadCaseFile(@PathVariable String caseNumber) throws IOException {
		if(creditCardService.findByCaseNumber(caseNumber).isPresent()) {
			FtpClient ftpClient = connectFtpFactory();
			
			// TODO: A Better file location
			ftpClient.downloadFile(String.format("/%s.xml", caseNumber), 
					String.format("c:/Downloads/%s.xml", caseNumber));
			
			logger.info("Case Number:" + caseNumber + " Download Success!");
			
			ftpClient.close();
			
		} else {
			throw new IllegalArgumentException("Case Number: " + caseNumber + "Not Found!");
		}
	}
	
	
	@GetMapping(path = "/filetrans/{userId}/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	@ResponseBody
	public List<UserCaseDto> findUserCases(@PathVariable String userId) throws IOException {
		List<CreditCard> list = creditCardService.findAllUserCases(userId);
		List<UserCaseDto> returnList;
		
		returnList = list.stream().map(c -> 
			new UserCaseDto(c.getCaseNumber(),c.getMessageNumber(), c.getCreditNumber(), c.getCustomerName(), c.getUploadTime())
		).collect(Collectors.toList());
				
		return returnList;
	}
	
	
	/*******************/
	/* Utils Functions */
	/*******************/
	
	/**
	 * For creating XML String file
	 * 
	 * @param caseNumber
	 * @param creditCardDto
	 * @return String XML format
	 */
	private String createXmlFormat(String caseNumber, CreditCardDto creditCardDto) {

		Document document = DocumentHelper.createDocument();

		document.addElement("EMI");

		Element root = document.getRootElement();

		root.addElement("EMI-NO").addText(creditCardDto.getNo());

		Element creditCard = root.addElement("CREDIT-CARD");

		creditCard.addElement("NUMBER").addText(creditCardDto.getCno());
		creditCard.addElement("NAME").addText(creditCardDto.getName());

		String outXml = document.asXML();
		return outXml;
	}

	/**
	 * Write String to java.io.File
	 * 
	 * @param text
	 * @param caseNumber
	 * @return File
	 * @throws IOException
	 */
	private File writeString2File(String text, String caseNum) throws IOException {

		// TODO: BUG -> file store on the project(X)
		// Temporal file is stored inside the project
		File returnFile = new File(caseNum + ".xml");

		FileWriter fileWriter = new FileWriter(returnFile);
		fileWriter.write(text);
		fileWriter.flush();
		fileWriter.close();

		return returnFile;
	}

	
	
	/**
	 * Return a ftpClient instance
	 * 
	 * @return FtpClient
	 * @throws IOException
	 */
	private FtpClient connectFtpFactory() throws IOException {
		
		FtpClient ftpClient = new FtpClient("localhost", 21, "admin", "password");
		
		try {
			ftpClient.open();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return ftpClient;
	}
	
}
