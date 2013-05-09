package ru.rpn.publicrequestform.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.rpn.publicrequestform.errors.FileSizeException;
import ru.rpn.publicrequestform.model.Attachment;
import ru.rpn.publicrequestform.model.RequestData;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.documentlibrary.DuplicateFolderNameException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

@Service
public class LiferayService {

	public static final String PUBLIC_REQUEST_FORM_PARENT_FOLDER_NAME = "Public Request Form";

	private void initiatePermissionCheckerIfNotStarted(long companyId) throws Exception {
		if (PermissionThreadLocal.getPermissionChecker() == null) {
			User user = UserLocalServiceUtil.getDefaultUser(companyId);
			PrincipalThreadLocal.setName(user.getUserId());
			PermissionChecker permissionChecker = PermissionCheckerFactoryUtil.create(UserLocalServiceUtil.getUser(user.getUserId()), true);
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}
	}
	
	private long getFolderId(RequestData requestData, long companyId, long groupId) throws PortalException, SystemException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(requestData.getDate());

		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		long prfFolderId = createFolder(PUBLIC_REQUEST_FORM_PARENT_FOLDER_NAME, parentFolderId, companyId, groupId);
		String folderName = requestData.getId().toString();
		long folderId = createFolder(folderName, prfFolderId, companyId, groupId);
		return folderId;
	}

	private long createFolder(String name, long parentFolderId, long companyId, long groupId) throws PortalException, SystemException {
		DLFolder dlFolder;
		try {
			dlFolder = DLFolderLocalServiceUtil.addFolder(getUserId(companyId), groupId, getRepositoryId(groupId, parentFolderId), name, name, new ServiceContext());
			return dlFolder.getFolderId();
		} catch (DuplicateFolderNameException dfne) {
			dlFolder = DLFolderLocalServiceUtil.getFolder(groupId, parentFolderId, name);
			return dlFolder.getFolderId();
		}
	}

	private long getUserId(long companyId) throws PortalException, SystemException {
		return CompanyLocalServiceUtil.getCompany(companyId).getDefaultUser().getUserId();
	}

	private long getRepositoryId(long groupId, long folderId) throws PortalException, SystemException {
		if (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			return groupId;
		} else {
			DLFolder dlFolder = DLFolderLocalServiceUtil.getDLFolder(folderId);
			return dlFolder.getFolderId();
		}
	}

	public List<Attachment> addFiles(RequestData requestData, long companyId) throws Exception {
		initiatePermissionCheckerIfNotStarted(companyId);
		long groupId = CompanyLocalServiceUtil.getCompany(companyId).getGroup().getGroupId();
		long parentFolderId = getFolderId(requestData, companyId, groupId);
		List<Attachment> attachments = new ArrayList<Attachment>();
		for (MultipartFile multipartFile : requestData.getMultipartFiles()) {
			if (!multipartFile.isEmpty()) {
				String fileName = multipartFile.getOriginalFilename();
				InputStream inputStream = multipartFile.getInputStream();
				long fileId = addFile(fileName, parentFolderId, inputStream, companyId, groupId);
				Attachment attachment = new Attachment();
				attachment.setEntryFileId(fileId);
				attachment.setFileName(fileName);
				attachments.add(attachment);
			}
		}
		return attachments;
	}
	
	
	private long addFile(String fileName, long parentFolderId, InputStream inputStream, long companyId,  long groupId)
		throws Exception {
		byte[] bytes = IOUtils.toByteArray(inputStream);
		if (bytes.length > 2097152) {
			throw new FileSizeException();
		}
		DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.addFileEntry(
			getUserId(companyId), groupId, parentFolderId, fileName, fileName, fileName, StringPool.BLANK,
			StringPool.BLANK, new ByteArrayInputStream(bytes), bytes.length, new ServiceContext());
		return fileEntry.getFileEntryId();
	}

	public void deleteFiles(RequestData requestData, long companyId) throws Exception {
		initiatePermissionCheckerIfNotStarted(companyId);
		long groupId = CompanyLocalServiceUtil.getCompany(companyId).getGroup().getGroupId();
		long parentFolderId = getFolderId(requestData, companyId, groupId);
		DLFolderLocalServiceUtil.deleteFolder(parentFolderId);
	}
	
}
