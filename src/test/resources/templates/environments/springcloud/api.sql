SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `resource_d_api`;
CREATE TABLE `resource_d_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api` varchar(100) NOT NULL,
  `description` varchar(150) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_public` bit(1) NOT NULL DEFAULT b'0',
  `global_state_type` int(11) DEFAULT NULL,
  `service_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `apiunique` (`api`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;