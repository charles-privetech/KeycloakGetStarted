package com.example.keycloak.service;

import java.util.List;

import com.example.keycloak.entity.DomainObject;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.Permission;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class KeyCloakService {
   
    private static final AuthzClient authzClient = AuthzClient.create();
     
    public <T extends DomainObject> void createResource(final T domainObject, final List<String> scopes) {
        
        ResourceRepresentation res = new ResourceRepresentation();
        res.setId(domainObject.getId());
        res.setName(domainObject.getId());        
        if(!CollectionUtils.isEmpty(scopes)) {
            for (String scope : scopes) {
                res.addScope(scope);
            }
        }
        res.setType(domainObject.getClass().getName());

        authzClient.protection().resource().create(res);
    }
   
    public void evaluatePermissions(final String username, final String password) {
        
        AuthorizationResponse response = authzClient.authorization(username, password).authorize(new AuthorizationRequest());
        String tokenStr = response.getToken();  //This is a JWT token

        TokenIntrospectionResponse token = authzClient.protection().introspectRequestingPartyToken(tokenStr);

        System.out.println("Granted permission of user:" + username);
        for (Permission p : token.getPermissions()) {
            System.out.println(p.toString());
        }
    }
}