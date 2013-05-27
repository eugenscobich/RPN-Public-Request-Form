package ru.rpn.publicrequestform.service;

import java.io.File;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Service;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;

@Service
public class MailService {

	public void sendMail(String from, String to, String subject, String content, Map<String, File> files) throws AddressException {
		MailMessage message = null;
		message = new MailMessage(new InternetAddress(from), new InternetAddress(to),
				subject, content, true);
		if (files != null && !files.isEmpty()) {
			for (String fileName : files.keySet()) {
				message.addAttachment(files.get(fileName));	
			}
		}
		MailServiceUtil.sendEmail(message);
	}
}
