SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `account_d_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `account_d_dictionary`;
CREATE TABLE `account_d_dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `global_data_type` int(11) DEFAULT NULL,
  `global_state_type` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `code_class` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `account_d_dictionary`
-- ----------------------------
BEGIN;
INSERT INTO `account_d_dictionary` VALUES ('1', '0', 'sex', '性别', '1', '1', '2017-07-05 10:44:05', '2017-07-05 10:44:05', '0'), ('2', '1', 'male', '男', '1', '1', '2017-07-05 10:44:29', '2017-07-05 10:44:33', 'sex'), ('3', '1', 'female', '女', '1', '1', '2017-07-05 10:44:57', '2017-07-05 10:44:59', 'sex'), ('1000', '0', 'role', '角色', '1', '1', '2017-07-05 10:42:44', '2017-07-05 10:42:44', '0'), ('1001', '1000', 'admin', '管理员', '1', '1', '2017-07-05 10:43:42', '2017-07-05 10:43:42', 'role');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
