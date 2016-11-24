/*
 * LICENSE_PLACEHOLDER
 */
package fr.cnes.regards.framework.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import fr.cnes.regards.framework.hateoas.HateoasUtils;
import fr.cnes.regards.modules.accessrights.client.IProjectUsersClient;
import fr.cnes.regards.modules.accessrights.domain.projects.MetaData;
import fr.cnes.regards.modules.accessrights.domain.projects.ProjectUser;
import fr.cnes.regards.modules.accessrights.domain.projects.ResourcesAccess;

/**
 *
 * Class ProjectUsersClientStub
 *
 * Stub to simulate the responses from the administration services for the ProjectUsers entities.
 *
 * @author Sébastien Binda
 * @since 1.0-SNAPSHOT
 */
@Component
@Primary
public class ProjectUsersClientStub implements IProjectUsersClient {

    /**
     * Locale list of users
     */
    private static List<ProjectUser> users = new ArrayList<>();

    /**
     * Id generator count
     */
    private static long idCount = 0;

    @Override
    public ResponseEntity<List<Resource<ProjectUser>>> retrieveProjectUserList() {
        return new ResponseEntity<List<Resource<ProjectUser>>>(HateoasUtils.wrapList(users), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource<ProjectUser>> retrieveProjectUser(final String pUserEmail) {
        ProjectUser result = null;
        for (final ProjectUser user : users) {
            if (user.getEmail().equals(pUserEmail)) {
                result = user;
                break;
            }
        }
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Resource<ProjectUser>>(HateoasUtils.wrap(result), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateProjectUser(final Long pUserId, final ProjectUser pUpdatedProjectUser) {
        return null;
    }

    @Override
    public ResponseEntity<Void> removeProjectUser(final Long pUserId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Resource<MetaData>>> retrieveProjectUserMetaData(final Long pUserId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateProjectUserMetaData(final Long pUserId,
            final List<MetaData> pUpdatedUserMetaData) {
        return null;
    }

    @Override
    public ResponseEntity<Void> removeProjectUserMetaData(final Long pUserId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Resource<ResourcesAccess>>> retrieveProjectUserAccessRights(final String pUserLogin,
            final String pBorrowedRoleName) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateProjectUserAccessRights(final String pLogin,
            final List<ResourcesAccess> pUpdatedUserAccessRights) {
        return null;
    }

    @Override
    public ResponseEntity<Void> removeProjectUserAccessRights(final String pUserLogin) {
        return null;
    }

}