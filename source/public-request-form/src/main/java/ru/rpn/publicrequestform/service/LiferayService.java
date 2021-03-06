package ru.rpn.publicrequestform.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.rpn.publicrequestform.errors.FileSizeException;
import ru.rpn.publicrequestform.model.Attachment;
import ru.rpn.publicrequestform.model.RequestData;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
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

	private static final String[] FOLDER_ACTIONS = { "VIEW", "ACCESS" };
	private static final String FOLDER_RESOURCE_NAME = "com.liferay.portlet.documentlibrary.model.DLFolder";
	private static final String ROLE_NAME = "Power User";
	private static final String[] ENTRY_ACTIONS = { "VIEW" };
	private static final String ENTRY_RESOURCE_NAME = "com.liferay.portlet.documentlibrary.model.DLFileEntry";

	
	
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
			final long roleId = RoleLocalServiceUtil.getRole(companyId, ROLE_NAME).getRoleId();
			dlFolder = DLFolderLocalServiceUtil.addFolder(getUserId(companyId), groupId, getRepositoryId(groupId, parentFolderId), name, name, new ServiceContext());
			ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId,
					FOLDER_RESOURCE_NAME, ResourceConstants.SCOPE_INDIVIDUAL, Long.toString(dlFolder.getPrimaryKey()), roleId, FOLDER_ACTIONS);

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
		final long roleId = RoleLocalServiceUtil.getRole(companyId, ROLE_NAME).getRoleId();
		List<ResourcePermission> rps = ResourcePermissionLocalServiceUtil.getResourcePermissions(companyId, ENTRY_RESOURCE_NAME, ResourceConstants.SCOPE_INDIVIDUAL, Long.toString(fileEntry.getPrimaryKey()));
		if (rps != null && !rps.isEmpty()) {	
			ResourcePermissionLocalServiceUtil.deleteResourcePermission(rps.get(0).getResourcePermissionId());
		} 
		ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, ENTRY_RESOURCE_NAME, ResourceConstants.SCOPE_INDIVIDUAL, Long.toString(fileEntry.getPrimaryKey()), roleId, ENTRY_ACTIONS);
		/*
		SearchEngineUtil.deleteDocument(companyId, fileEntry.getUuid());
		Indexer indexer = IndexerRegistryUtil.getIndexer(DLFileEntry.class);
		indexer.delete(fileEntry);
		*/
		DLFileEntryLocalServiceUtil.updateStatus(getUserId(companyId), fileEntry.getFileEntryId(), WorkflowConstants.STATUS_DRAFT, new ServiceContext());
		return fileEntry.getFileEntryId();
	}

	public void deleteFiles(RequestData requestData, long companyId) throws Exception {
		initiatePermissionCheckerIfNotStarted(companyId);
		long groupId = CompanyLocalServiceUtil.getCompany(companyId).getGroup().getGroupId();
		long parentFolderId = getFolderId(requestData, companyId, groupId);
		DLFolderLocalServiceUtil.deleteFolder(parentFolderId);
	}

	public void fix(long companyId) throws Exception {
		initiatePermissionCheckerIfNotStarted(companyId);
		long groupId = CompanyLocalServiceUtil.getCompany(companyId).getGroup().getGroupId();
		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		DLFolder dlFolder = DLFolderLocalServiceUtil.getFolder(groupId, parentFolderId, PUBLIC_REQUEST_FORM_PARENT_FOLDER_NAME);
		List<DLFolder> dlFolders = DLFolderLocalServiceUtil.getFolders(groupId, getRepositoryId(groupId, dlFolder.getFolderId()));
		
		for (DLFolder dlFolder2 : dlFolders) {
			List<DLFileEntry> dlFileEntries = DLFileEntryLocalServiceUtil.getFileEntries(groupId, getRepositoryId(groupId, dlFolder2.getFolderId()));
			if (CollectionUtils.isNotEmpty(dlFileEntries)) {
				for (DLFileEntry dlFileEntry : dlFileEntries) {
					System.out.println(dlFileEntry.getTitle());
					DLFileEntryLocalServiceUtil.updateStatus(getUserId(companyId), dlFileEntry.getFileEntryId(), WorkflowConstants.STATUS_DRAFT, new ServiceContext());	
				}
			}
		}
	}
	
}
