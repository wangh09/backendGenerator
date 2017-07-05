
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `account_r_role`
-- ----------------------------
DROP TABLE IF EXISTS `account_r_role`;
CREATE TABLE `account_r_role` (
  `id` varchar(32) NOT NULL,
  `account_id` varchar(32) NOT NULL,
  `dic_role_type` int(11) NOT NULL,
  `global_state_type` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
