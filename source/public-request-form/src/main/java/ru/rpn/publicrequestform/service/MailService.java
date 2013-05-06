package ru.rpn.publicrequestform.service;

import java.io.File;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Service;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

@Service
public class MailService {

	public void sendMail(String to, String subject, String content, Map<String, File> files) throws AddressException {
		MailMessage message = null;
		String from = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_USER);
		
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
