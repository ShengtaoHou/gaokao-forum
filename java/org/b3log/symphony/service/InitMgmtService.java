/*
 * Symphony - A modern community (forum/SNS/blog) platform written in Java.
 * Copyright (C) 2012-2017,  b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.service;

import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.repository.jdbc.util.JdbcRepositories;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.Ids;
import org.b3log.latke.util.Locales;
import org.b3log.latke.util.MD5;
import org.b3log.symphony.model.*;
import org.b3log.symphony.repository.*;
import org.b3log.symphony.util.Languages;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import java.util.*;

/**
 * Initialization management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.1.5, Feb 11, 2017
 * @since 1.8.0
 */
@Service
public class InitMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(InitMgmtService.class);

    private static Set<String> VISITOR_PERMISSIONS = new HashSet<>();
    private static Set<String> DEFAULT_PERMISSIONS = new HashSet<>();
    private static Set<String> MEMBER_PERMISSIONS = new HashSet<>();
    private static Set<String> REGULAR_PERMISSIONS = new HashSet<>();
    private static Set<String> ADMIN_PERMISSIONS = new HashSet<>();
    private static Set<String> LEADER_PERMISSIONS = new HashSet<>();

    static { // Init built-in roles' permissions, see https://github.com/b3log/symphony/issues/358 for more details
        // Visitor
        // no permissions at present

        // Default
        DEFAULT_PERMISSIONS.addAll(VISITOR_PERMISSIONS);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_ADD_ARTICLE);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_UPDATE_ARTICLE);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_REMOVE_ARTICLE);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_ADD_COMMENT);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_UPDATE_COMMENT);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_REMOVE_COMMENT);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_THANK_ARTICLE);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_THANK_COMMENT);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_WATCH_ARTICLE);
        DEFAULT_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_FOLLOW_ARTICLE);

        // Member
        MEMBER_PERMISSIONS.addAll(DEFAULT_PERMISSIONS);
        MEMBER_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_GOOD_ARTICLE);
        MEMBER_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_BAD_ARTICLE);
        MEMBER_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_GOOD_COMMENT);
        MEMBER_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_BAD_COMMENT);
        MEMBER_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_AT_USER);
        MEMBER_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_USE_INVITATION_LINK);

        // Regular
        REGULAR_PERMISSIONS.addAll(MEMBER_PERMISSIONS);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_STICK_ARTICLE);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_ADD_ARTICLE_ANONYMOUS);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_ADD_COMMENT_ANONYMOUS);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_VIEW_ARTICLE_HISTORY);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_VIEW_COMMENT_HISTORY);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_AT_PARTICIPANTS);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMON_EXCHANGE_INVITATION_CODE);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_TAG_UPDATE_TAG_BASIC);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN);
        REGULAR_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_TAGS);

        // Leader
        LEADER_PERMISSIONS.addAll(REGULAR_PERMISSIONS);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_USER_ADD_USER);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_USER_UPDATE_USER_BASIC);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_USER_UPDATE_USER_ADVANCED);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_ARTICLE_UPDATE_ARTICLE_BASIC);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_ARTICLE_STICK_ARTICLE);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_ARTICLE_CANCEL_STICK_ARTICLE);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_ARTICLE_REINDEX_ARTICLE_INDEX);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMENT_UPDATE_COMMENT_BASIC);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_USER_ADD_POINT);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_USER_EXCHANGE_POINT);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_USER_DEDUCT_POINT);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_IC_GEN_IC);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_IC_UPDATE_IC_BASIC);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_RW_ADD_RW);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_RW_UPDATE_RW_BASIC);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_RW_REMOVE_RW);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_USERS);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_ARTICLES);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_COMMENTS);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_ICS);
        LEADER_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_RWS);

        // Admin
        ADMIN_PERMISSIONS.addAll(LEADER_PERMISSIONS);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_ARTICLE_ADD_ARTICLE);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_ARTICLE_REINDEX_ARTICLES_INDEX);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_ARTICLE_REMOVE_ARTICLE);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_COMMENT_REMOVE_COMMENT);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_DOMAIN_ADD_DOMAIN);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_DOMAIN_ADD_DOMAIN_TAG);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_DOMAIN_REMOVE_DOMAIN);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_DOMAIN_REMOVE_DOMAIN_TAG);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_DOMAIN_UPDATE_DOMAIN_BASIC);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_AD_UPDATE_SIDE);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_AD_UPDATE_BANNER);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MISC_ALLOW_ADD_ARTICLE);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MISC_ALLOW_ADD_COMMENT);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MISC_ALLOW_ANONYMOUS_VIEW);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MISC_LANGUAGE);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MISC_REGISTER_METHOD);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_DOMAINS);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_AD);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_ROLES);
        ADMIN_PERMISSIONS.add(Permission.PERMISSION_ID_C_MENU_ADMIN_MISC);
    }

    /**
     * Permission repository.
     */
    @Inject
    private PermissionRepository permissionRepository;

    /**
     * Role repository.
     */
    @Inject
    private RoleRepository roleRepository;

    /**
     * Role-permission repository.
     */
    @Inject
    private RolePermissionRepository rolePermissionRepository;

    /**
     * Option repository.
     */
    @Inject
    private OptionRepository optionRepository;

    /**
     * Tag repository.
     */
    @Inject
    private TagRepository tagRepository;

    /**
     * Tag management service.
     */
    @Inject
    private TagMgmtService tagMgmtService;

    /**
     * Article management service.
     */
    @Inject
    private ArticleMgmtService articleMgmtService;

    /**
     * User management service.
     */
    @Inject
    private UserMgmtService userMgmtService;

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;

    /**
     * Initializes Sym if first time setup.
     */
    public void initSym() {
        try {
            final List<JSONObject> admins = userQueryService.getAdmins();

            if (null != admins && !admins.isEmpty()) { // Initialized already
                return;
            }
        } catch (final ServiceException e) {
            LOGGER.log(Level.ERROR, "Check init error", e);

            System.exit(0);
        }

        LOGGER.info("It's your first time setup Sym, initializes Sym....");

        try {
            LOGGER.log(Level.INFO, "Database [{0}], creating all tables", Latkes.getRuntimeDatabase());

            final List<JdbcRepositories.CreateTableResult> createTableResults = JdbcRepositories.initAllTables();
            for (final JdbcRepositories.CreateTableResult createTableResult : createTableResults) {
                LOGGER.log(Level.INFO, "Creates table result[tableName={0}, isSuccess={1}]",
                        new Object[]{createTableResult.getName(), createTableResult.isSuccess()});
            }

            final Transaction transaction = optionRepository.beginTransaction();

            // Init statistic
            JSONObject option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_STATISTIC_MEMBER_COUNT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_STATISTIC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_STATISTIC_CMT_COUNT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_STATISTIC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_STATISTIC_ARTICLE_COUNT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_STATISTIC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_STATISTIC_DOMAIN_COUNT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_STATISTIC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_STATISTIC_TAG_COUNT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_STATISTIC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_STATISTIC_LINK_COUNT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_STATISTIC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_STATISTIC_MAX_ONLINE_VISITOR_COUNT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_STATISTIC);
            optionRepository.add(option);

            // Init misc
            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_MISC_ALLOW_REGISTER);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_MISC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_MISC_ALLOW_ANONYMOUS_VIEW);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_MISC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_MISC_ALLOW_ADD_ARTICLE);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_MISC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_MISC_ALLOW_ADD_COMMENT);
            option.put(Option.OPTION_VALUE, "0");
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_MISC);
            optionRepository.add(option);

            option = new JSONObject();
            option.put(Keys.OBJECT_ID, Option.ID_C_MISC_LANGUAGE);
            option.put(Option.OPTION_VALUE, "0"); // user browser
            option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_MISC);
            optionRepository.add(option);

            // Init permissions
            final JSONObject permission = new JSONObject();

            // ad management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_AD);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_AD_UPDATE_SIDE);
            permissionRepository.add(permission);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_AD_UPDATE_BANNER);
            permissionRepository.add(permission);

            // article management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_ARTICLE);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_ARTICLE_ADD_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_ARTICLE_CANCEL_STICK_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_ARTICLE_REINDEX_ARTICLES_INDEX);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_ARTICLE_REINDEX_ARTICLE_INDEX);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_ARTICLE_REMOVE_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_ARTICLE_STICK_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_ARTICLE_UPDATE_ARTICLE_BASIC);
            permissionRepository.add(permission);

            // comment management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_COMMENT);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMENT_REMOVE_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMENT_UPDATE_COMMENT_BASIC);
            permissionRepository.add(permission);

            // common permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_COMMON);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_ADD_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_ADD_ARTICLE_ANONYMOUS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_UPDATE_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_REMOVE_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_ADD_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_ADD_COMMENT_ANONYMOUS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_UPDATE_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_REMOVE_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_VIEW_COMMENT_HISTORY);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_AT_USER);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_AT_PARTICIPANTS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_BAD_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_BAD_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_EXCHANGE_INVITATION_CODE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_FOLLOW_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_WATCH_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_GOOD_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_GOOD_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_STICK_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_THANK_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_THANK_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_USE_INVITATION_LINK);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_COMMON_VIEW_ARTICLE_HISTORY);
            permissionRepository.add(permission);

            // domain management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_DOMAIN);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_DOMAIN_ADD_DOMAIN);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_DOMAIN_ADD_DOMAIN_TAG);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_DOMAIN_REMOVE_DOMAIN);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_DOMAIN_REMOVE_DOMAIN_TAG);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_DOMAIN_UPDATE_DOMAIN_BASIC);
            permissionRepository.add(permission);

            // invitecode management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_IC);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_IC_GEN_IC);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_IC_UPDATE_IC_BASIC);
            permissionRepository.add(permission);

            // misc management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_MISC);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MISC_ALLOW_ADD_ARTICLE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MISC_ALLOW_ADD_COMMENT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MISC_ALLOW_ANONYMOUS_VIEW);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MISC_LANGUAGE);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MISC_REGISTER_METHOD);
            permissionRepository.add(permission);

            // reserved word management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_RESERVED_WORD);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_RW_ADD_RW);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_RW_REMOVE_RW);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_RW_UPDATE_RW_BASIC);
            permissionRepository.add(permission);

            // tag management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_TAG);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_TAG_UPDATE_TAG_BASIC);
            permissionRepository.add(permission);

            // user management permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_USER);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_USER_ADD_POINT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_USER_ADD_USER);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_USER_DEDUCT_POINT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_USER_EXCHANGE_POINT);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_USER_UPDATE_USER_ADVANCED);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_USER_UPDATE_USER_BASIC);
            permissionRepository.add(permission);

            // menu permissions
            permission.put(Permission.PERMISSION_CATEGORY, Permission.PERMISSION_CATEGORY_C_MENU);

            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_AD);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_ARTICLES);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_COMMENTS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_DOMAINS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_ICS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_RWS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_TAGS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_USERS);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_MISC);
            permissionRepository.add(permission);
            permission.put(Keys.OBJECT_ID, Permission.PERMISSION_ID_C_MENU_ADMIN_ROLES);
            permissionRepository.add(permission);

            // Init roles
            final JSONObject role = new JSONObject();

            role.put(Keys.OBJECT_ID, Role.ROLE_ID_C_ADMIN);
            role.put(Role.ROLE_NAME, "Admin");
            role.put(Role.ROLE_DESCRIPTION, "");
            roleRepository.add(role);

            role.put(Keys.OBJECT_ID, Role.ROLE_ID_C_DEFAULT);
            role.put(Role.ROLE_NAME, "Default");
            role.put(Role.ROLE_DESCRIPTION, "");
            roleRepository.add(role);

            role.put(Keys.OBJECT_ID, Role.ROLE_ID_C_LEADER);
            role.put(Role.ROLE_NAME, "Leader");
            role.put(Role.ROLE_DESCRIPTION, "");
            roleRepository.add(role);

            role.put(Keys.OBJECT_ID, Role.ROLE_ID_C_MEMBER);
            role.put(Role.ROLE_NAME, "Member");
            role.put(Role.ROLE_DESCRIPTION, "");
            roleRepository.add(role);

            role.put(Keys.OBJECT_ID, Role.ROLE_ID_C_REGULAR);
            role.put(Role.ROLE_NAME, "Regular");
            role.put(Role.ROLE_DESCRIPTION, "");
            roleRepository.add(role);

            role.put(Keys.OBJECT_ID, Role.ROLE_ID_C_VISITOR);
            role.put(Role.ROLE_NAME, "Visitor");
            role.put(Role.ROLE_DESCRIPTION, "");
            roleRepository.add(role);

            // Init Role-Permission
            final JSONObject rolePermission = new JSONObject();

            // [Default] role's permissions
            rolePermission.put(Role.ROLE_ID, Role.ROLE_ID_C_DEFAULT);
            for (final String permissionId : DEFAULT_PERMISSIONS) {
                rolePermission.put(Keys.OBJECT_ID, Ids.genTimeMillisId());
                rolePermission.put(Permission.PERMISSION_ID, permissionId);

                rolePermissionRepository.add(rolePermission);
            }

            // [Member] role's permissions
            rolePermission.put(Role.ROLE_ID, Role.ROLE_ID_C_MEMBER);
            for (final String permissionId : MEMBER_PERMISSIONS) {
                rolePermission.put(Keys.OBJECT_ID, Ids.genTimeMillisId());
                rolePermission.put(Permission.PERMISSION_ID, permissionId);

                rolePermissionRepository.add(rolePermission);
            }

            // [Regular] role's permissions
            rolePermission.put(Role.ROLE_ID, Role.ROLE_ID_C_REGULAR);
            for (final String permissionId : REGULAR_PERMISSIONS) {
                rolePermission.put(Keys.OBJECT_ID, Ids.genTimeMillisId());
                rolePermission.put(Permission.PERMISSION_ID, permissionId);

                rolePermissionRepository.add(rolePermission);
            }

            // [Leader] role's permissions
            rolePermission.put(Role.ROLE_ID, Role.ROLE_ID_C_LEADER);
            for (final String permissionId : LEADER_PERMISSIONS) {
                rolePermission.put(Keys.OBJECT_ID, Ids.genTimeMillisId());
                rolePermission.put(Permission.PERMISSION_ID, permissionId);

                rolePermissionRepository.add(rolePermission);
            }

            // [Admin] role's permissions
            rolePermission.put(Role.ROLE_ID, Role.ROLE_ID_C_ADMIN);
            for (final String permissionId : ADMIN_PERMISSIONS) {
                rolePermission.put(Keys.OBJECT_ID, Ids.genTimeMillisId());
                rolePermission.put(Permission.PERMISSION_ID, permissionId);

                rolePermissionRepository.add(rolePermission);
            }

            transaction.commit();

            // Init admin
            final JSONObject admin = new JSONObject();
            admin.put(User.USER_EMAIL, "sym@b3log.org");
            admin.put(User.USER_NAME, "admin");
            admin.put(User.USER_PASSWORD, MD5.hash("admin"));

            final Locale defaultLocale = Locale.getDefault();
            final String lang = Locales.getLanguage(defaultLocale.toString());
            final String country = Locales.getCountry(defaultLocale.toString());
            String language = lang + "_" + country;
            if (!Languages.getAvailableLanguages().contains(language)) {
                language = "en_US";
            }
            admin.put(UserExt.USER_LANGUAGE, language);
            admin.put(User.USER_ROLE, Role.ROLE_ID_C_ADMIN);
            admin.put(UserExt.USER_STATUS, UserExt.USER_STATUS_C_VALID);
            admin.put(UserExt.USER_GUIDE_STEP, UserExt.USER_GUIDE_STEP_FIN);
            final String adminId = userMgmtService.addUser(admin);
            admin.put(Keys.OBJECT_ID, adminId);

            // Init default commenter (for sync comment from client)
            final JSONObject defaultCommenter = new JSONObject();
            defaultCommenter.put(User.USER_EMAIL, UserExt.DEFAULT_CMTER_EMAIL);
            defaultCommenter.put(User.USER_NAME, UserExt.DEFAULT_CMTER_NAME);
            defaultCommenter.put(User.USER_PASSWORD, MD5.hash(String.valueOf(new Random().nextInt())));
            defaultCommenter.put(UserExt.USER_LANGUAGE, "en_US");
            defaultCommenter.put(UserExt.USER_GUIDE_STEP, UserExt.USER_GUIDE_STEP_FIN);
            defaultCommenter.put(User.USER_ROLE, UserExt.DEFAULT_CMTER_ROLE);
            defaultCommenter.put(UserExt.USER_STATUS, UserExt.USER_STATUS_C_VALID);
            userMgmtService.addUser(defaultCommenter);

            // Add tags
            String tagTitle = Symphonys.get("systemAnnounce");
            String tagId = tagMgmtService.addTag(adminId, tagTitle);
            JSONObject tag = tagRepository.get(tagId);
            tag.put(Tag.TAG_URI, "announcement");
            tagMgmtService.updateTag(tagId, tag);

            tagTitle = "Sym";
            tagId = tagMgmtService.addTag(adminId, tagTitle);
            tag = tagRepository.get(tagId);
            tag.put(Tag.TAG_URI, "Sym");
            tag.put(Tag.TAG_ICON_PATH, "sym.png");
            tag.put(Tag.TAG_DESCRIPTION, "[Sym](https://github.com/b3log/symphony) 是一个用 [Java] 实现的现代化社区（论坛/社交网络/博客）平台，“下一代的社区系统，为未来而构建”。");
            tagMgmtService.updateTag(tagId, tag);

            tagTitle = "B3log";
            tagId = tagMgmtService.addTag(adminId, tagTitle);
            tag = tagRepository.get(tagId);
            tag.put(Tag.TAG_URI, "B3log");
            tag.put(Tag.TAG_ICON_PATH, "b3log.png");
            tag.put(Tag.TAG_DESCRIPTION, "[B3log](http://b3log.org) 是一个开源组织，名字来源于“Bulletin Board Blog”缩写，目标是将独立博客与论坛结合，形成一种新的网络社区体验，详细请看 [B3log 构思](https://hacpai.com/b3log)。目前 B3log 已经开源了多款产品： [Solo] 、 [Sym] 、 [Wide] 。");
            tagMgmtService.updateTag(tagId, tag);

            // Hello World!
            final JSONObject article = new JSONObject();
            article.put(Article.ARTICLE_TITLE, "Welcome to Sym community &hearts;");
            article.put(Article.ARTICLE_TAGS, "Sym,Announcement");
            article.put(Article.ARTICLE_CONTENT, "Hello, everyone!");
            article.put(Article.ARTICLE_EDITOR_TYPE, 0);
            article.put(Article.ARTICLE_AUTHOR_ID, admin.optString(Keys.OBJECT_ID));
            article.put(Article.ARTICLE_T_IS_BROADCAST, false);

            articleMgmtService.addArticle(article);

            LOGGER.info("Initialized Sym, have fun :)");
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Initializes Sym failed", e);

            System.exit(0);
        }
    }
}
