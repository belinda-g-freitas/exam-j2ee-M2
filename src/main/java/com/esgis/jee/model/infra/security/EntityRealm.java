/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.security;

/**
 *
 * @author EL Capitain
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import com.esgis.jee.model.admin.entity.Utilisateur;
import static com.esgis.jee.model.admin.service.ParametreService.LONGUEUR_MAX_MDP;
import com.esgis.jee.model.admin.service.PermissionService;
import com.esgis.jee.model.admin.service.UtilisateurService;
import com.esgis.jee.model.admin.service.impl.PermissionServiceImpl;
import com.esgis.jee.model.admin.service.impl.UtilisateurServiceImpl;
import javax.naming.NamingException;

public class EntityRealm extends AuthorizingRealm {

    protected static UtilisateurService utilisateurService;
    protected static PermissionService permissionService;
    protected static Utilisateur utilisateur;
    //protected static List<Permission> permissions;

    public EntityRealm() throws NamingException {
        this.setName("entityRealm");
        this.setCredentialsMatcher(new HashedCredentialsMatcher("SHA-256"));
        utilisateurService = UtilisateurServiceImpl.getInstance();
        permissionService = PermissionServiceImpl.getInstance();
        System.out.println("utilisateurService = "+utilisateurService+", permissionService = "+permissionService);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        //AppLicencesManager.validerLicenceSystemeAdminApplicative();
        System.out.println("utilisateurService = "+utilisateurService+", permissionService = "+permissionService);
        // Authentification partielle
        utilisateur = utilisateurService.partialAuthById2(((UsernamePasswordToken) authcToken).getUsername());
        if(utilisateur == null) {
            throw new UnknownAccountException("Compte inconnu");
        } else if(!utilisateur.isActif()) {
            throw new LockedAccountException("Votre compte est inactif, veuillez contacter l'administrateur");
        }
        String mdp = utilisateur.getMotDePasse();
        if(mdp.length() <= LONGUEUR_MAX_MDP) { // Non crypté
            mdp = new Sha256Hash(mdp).toHex();
        }
        // Authentification complète
        return new SimpleAuthenticationInfo(
                utilisateur.getIdentifiant(), 
                mdp, 
                getName()
        );
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo result = null;
        try {
            utilisateur = utilisateurService.findOneBy("identifiant", (String) principals.fromRealm(this.getName()).iterator().next());
            /*
             * On récupère les rôles de l'utilisateur
             */
            final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(); // Utilisé à cause de forEach
            utilisateurService
                    .trouverNomRolesActifs(utilisateur)
                    .stream()
                    //.map(Role::getNom)
                    .forEach((t) -> {
                        System.out.println("Rôle du user ( "+utilisateur.getIdentifiant()+" ) : "+t);
                        info.addRole(t);
                    });
            /*
             * On récupère la liste des permissions de l'utilisateur
             */
            //info.addStringPermissions(perm);
            info.addStringPermissions(utilisateurService.trouverNomPermissionsActives(utilisateur));
            result = info;
        } catch (Exception ex) {
            Logger.getLogger(EntityRealm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static Utilisateur getUtilisateur() {
        if (getSubject().isAuthenticated()) {
            return utilisateur;
        }
        return null;
    }
    
    public static Subject getSubject() {
        return SecurityUtils.getSubject();

    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

}
