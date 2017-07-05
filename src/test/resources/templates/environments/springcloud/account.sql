
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `account_d_account`;
CREATE TABLE `account_d_account` (
  `id` varchar(32) NOT NULL,
  `account` varchar(30) DEFAULT NULL,
  `phone` varchar(18) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `passwd` varchar(32) DEFAULT NULL,
  `dic_default_role_type` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `dic_sex_type` int(11) DEFAULT NULL,
  `dic_id_type` int(11) DEFAULT NULL,
  `id_number` int(11) DEFAULT NULL,
  `global_state_type` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_login_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_unique` (`account`),
  UNIQUE KEY `phone_unique` (`phone`) USING BTREE,
  UNIQUE KEY `email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
