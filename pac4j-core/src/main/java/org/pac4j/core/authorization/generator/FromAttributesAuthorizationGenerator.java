package org.pac4j.core.authorization.generator;

import java.util.StringTokenizer;

import org.pac4j.core.profile.CommonProfile;

/**
 * <p>Generate the authorization information by inspecting attributes.</p>
 * <p>The attributes containing the roles separated by the {@link #splitChar} property (can be set through {@link #setSplitChar(String)}) are
 * defined in the constructor. It's the same for the attributes containing the permissions.</p>
 * 
 * @author Jerome Leleu
 * @since 1.5.0
 */
public class FromAttributesAuthorizationGenerator<U extends CommonProfile> implements AuthorizationGenerator<U> {
    
    private final String[] roleAttributes;
    
    private final String[] permissionAttributes;
    
    private String splitChar = ",";
    
    public FromAttributesAuthorizationGenerator(final String[] roleAttributes, final String[] permissionAttributes) {
        this.roleAttributes = roleAttributes;
        this.permissionAttributes = permissionAttributes;
    }
    
    public void generate(final U profile) {
        generateAuth(profile, this.roleAttributes, true);
        generateAuth(profile, this.permissionAttributes, false);
    }
    
    private void generateAuth(final U profile, final String[] attributes, final boolean isRole) {
        if (attributes != null) {
            for (final String attribute : attributes) {
                final Object value = profile.getAttribute(attribute);
                if (value != null && value instanceof String) {
                    final StringTokenizer st = new StringTokenizer((String) value, this.splitChar);
                    while (st.hasMoreTokens()) {
                        if (isRole) {
                            profile.addRole(st.nextToken());
                        } else {
                            profile.addPermission(st.nextToken());
                        }
                    }
                }
            }
        }
    }
    
    public String getSplitChar() {
        return this.splitChar;
    }
    
    public void setSplitChar(final String splitChar) {
        this.splitChar = splitChar;
    }
}