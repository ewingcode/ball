/*
SQLyog v10.2 
MySQL - 5.1.30-community : Database - ball
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ball` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ball`;

/*Table structure for table `bet_bill` */

DROP TABLE IF EXISTS `bet_bill`;

CREATE TABLE `bet_bill` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `date` varchar(20) NOT NULL,
  `account` varchar(50) NOT NULL,
  `w_id` varchar(100) NOT NULL,
  `addtime` varchar(50) DEFAULT NULL,
  `oddf_type` varchar(10) DEFAULT NULL,
  `gtype` varchar(10) DEFAULT NULL,
  `w_ms` varchar(100) NOT NULL,
  `wtype` varchar(100) DEFAULT NULL,
  `bet_wtype` varchar(10) DEFAULT NULL,
  `league` varchar(100) DEFAULT NULL,
  `team_h_show` varchar(100) DEFAULT NULL,
  `team_c_show` varchar(100) DEFAULT NULL,
  `team_h_ratio` varchar(20) DEFAULT NULL,
  `team_c_ratio` varchar(20) DEFAULT NULL,
  `ratio` varchar(100) DEFAULT NULL,
  `org_score` varchar(100) DEFAULT NULL,
  `score` varchar(100) DEFAULT NULL,
  `result` varchar(100) DEFAULT NULL,
  `pname` varchar(100) DEFAULT NULL,
  `ioratio` varchar(100) DEFAULT NULL,
  `result_data` varchar(100) DEFAULT NULL,
  `ball_act_class` varchar(50) DEFAULT NULL,
  `ball_act_ret` varchar(100) DEFAULT NULL,
  `gold` varchar(20) DEFAULT NULL,
  `win_gold` varchar(20) DEFAULT NULL,
  `push` varchar(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=238 DEFAULT CHARSET=utf8 COMMENT='投注记录';

/*Table structure for table `bet_info` */

DROP TABLE IF EXISTS `bet_info`;

CREATE TABLE `bet_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `gtype` varchar(50) DEFAULT NULL,
  `gid` varchar(50) DEFAULT NULL,
  `gidm` varchar(50) DEFAULT NULL,
  `systime` varchar(30) DEFAULT NULL,
  `re_time` varchar(30) DEFAULT NULL,
  `datetime` varchar(50) DEFAULT NULL,
  `league` varchar(100) DEFAULT NULL,
  `team_h` varchar(100) DEFAULT NULL,
  `team_c` varchar(100) DEFAULT NULL,
  `strong` varchar(10) DEFAULT NULL,
  `sw_R` varchar(10) DEFAULT NULL,
  `ratio` varchar(10) DEFAULT NULL,
  `ior_RH` varchar(10) DEFAULT NULL,
  `ior_RC` varchar(10) DEFAULT NULL,
  `sw_OU` varchar(10) DEFAULT NULL,
  `ratio_o` varchar(20) DEFAULT NULL,
  `ratio_u` varchar(20) DEFAULT NULL,
  `ior_OUH` varchar(10) DEFAULT NULL,
  `ior_OUC` varchar(10) DEFAULT NULL,
  `n_strong` varchar(10) DEFAULT NULL,
  `n_sw_R` varchar(10) DEFAULT NULL,
  `n_ratio` varchar(10) DEFAULT NULL,
  `n_ior_RH` varchar(10) DEFAULT NULL,
  `n_ior_RC` varchar(10) DEFAULT NULL,
  `n_sw_OU` varchar(10) DEFAULT NULL,
  `n_ratio_o` varchar(10) DEFAULT NULL,
  `n_ratio_u` varchar(10) DEFAULT NULL,
  `n_ior_OUH` varchar(10) DEFAULT NULL,
  `n_ior_OUC` varchar(10) DEFAULT NULL,
  `sc_total` varchar(10) DEFAULT NULL,
  `rou_dis` varchar(10) DEFAULT NULL,
  `re_dis` varchar(10) DEFAULT NULL,
  `sc_FT_A` varchar(10) DEFAULT NULL,
  `sc_FT_H` varchar(10) DEFAULT NULL,
  `sc_OT_A` varchar(10) DEFAULT NULL,
  `sc_OT_H` varchar(10) DEFAULT NULL,
  `sc_Q4_total` varchar(10) DEFAULT NULL,
  `sc_Q3_total` varchar(10) DEFAULT NULL,
  `sc_Q2_total` varchar(10) DEFAULT NULL,
  `sc_Q1_total` varchar(10) DEFAULT NULL,
  `sc_Q4_A` varchar(10) DEFAULT NULL,
  `sc_Q4_H` varchar(10) DEFAULT NULL,
  `sc_Q3_A` varchar(10) DEFAULT NULL,
  `sc_Q3_H` varchar(10) DEFAULT NULL,
  `sc_Q2_A` varchar(10) DEFAULT NULL,
  `sc_Q2_H` varchar(10) DEFAULT NULL,
  `sc_Q1_A` varchar(10) DEFAULT NULL,
  `sc_Q1_H` varchar(10) DEFAULT NULL,
  `se_now` varchar(10) DEFAULT NULL,
  `se_type` varchar(10) DEFAULT NULL,
  `t_status` varchar(10) DEFAULT NULL,
  `t_count` varchar(10) DEFAULT NULL,
  `sc_new` varchar(10) DEFAULT NULL,
  `score_h` varchar(10) DEFAULT NULL,
  `score_c` varchar(10) DEFAULT NULL,
  `ptype` varchar(20) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL COMMENT '状态 0:未开始 1：进行中 2：结束',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1923 DEFAULT CHARSET=utf8 COMMENT='投注信息';

/*Table structure for table `bet_log` */

DROP TABLE IF EXISTS `bet_log`;

CREATE TABLE `bet_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account` varchar(100) NOT NULL,
  `code` varchar(300) DEFAULT NULL,
  `ticket_id` varchar(100) DEFAULT NULL,
  `gid` varchar(50) DEFAULT NULL,
  `gtype` varchar(10) DEFAULT NULL,
  `wtype` varchar(10) DEFAULT NULL,
  `rtype` varchar(10) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `strong` varchar(10) DEFAULT NULL,
  `ioratio` varchar(10) DEFAULT NULL,
  `gold` varchar(10) DEFAULT NULL,
  `concede` varchar(10) DEFAULT NULL,
  `ratio` varchar(10) DEFAULT NULL,
  `spread` varchar(10) DEFAULT NULL,
  `date` varchar(50) DEFAULT NULL,
  `time` varchar(50) DEFAULT NULL,
  `team_id_h` varchar(100) DEFAULT NULL,
  `team_id_c` varchar(100) DEFAULT NULL,
  `league_id` varchar(100) DEFAULT NULL,
  `maxcredit` varchar(100) DEFAULT NULL,
  `mid` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `mtype` varchar(100) DEFAULT NULL,
  `league` varchar(100) DEFAULT NULL,
  `team_c` varchar(100) DEFAULT NULL,
  `team_h` varchar(100) DEFAULT NULL,
  `imp` varchar(100) DEFAULT NULL,
  `ptype` varchar(100) DEFAULT NULL,
  `ball_act` varchar(100) DEFAULT NULL,
  `score_c` varchar(10) DEFAULT NULL,
  `score_h` varchar(10) DEFAULT NULL,
  `ms` varchar(100) DEFAULT NULL,
  `timestamp` varchar(100) DEFAULT NULL,
  `errormsg` varchar(100) DEFAULT NULL,
  `errorvalue` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8 COMMENT='投注记录';

/*Table structure for table `bet_roll_info` */

DROP TABLE IF EXISTS `bet_roll_info`;

CREATE TABLE `bet_roll_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `gtype` varchar(10) DEFAULT NULL,
  `strong` varchar(10) DEFAULT NULL,
  `league` varchar(100) DEFAULT NULL,
  `team_h` varchar(100) DEFAULT NULL,
  `team_c` varchar(100) DEFAULT NULL,
  `ratio_re_c` float DEFAULT NULL,
  `ratio_re` varchar(10) DEFAULT NULL,
  `ior_REH` varchar(10) DEFAULT NULL,
  `ior_REC` varchar(10) DEFAULT NULL,
  `ratio_rou_c` float DEFAULT NULL,
  `ratio_rouo` varchar(10) DEFAULT NULL,
  `ior_ROUC` varchar(10) DEFAULT NULL,
  `ratio_rouu` varchar(10) DEFAULT NULL,
  `ior_ROUH` varchar(10) DEFAULT NULL,
  `sc_total` varchar(10) DEFAULT NULL,
  `rou_dis` varchar(10) DEFAULT NULL,
  `re_dis` varchar(10) DEFAULT NULL,
  `sc_FT_A` varchar(10) DEFAULT NULL,
  `sc_FT_H` varchar(10) DEFAULT NULL,
  `sc_OT_A` varchar(10) DEFAULT NULL,
  `sc_OT_H` varchar(10) DEFAULT NULL,
  `sc_Q1_total` varchar(10) DEFAULT NULL,
  `sc_Q2_total` varchar(10) DEFAULT NULL,
  `sc_Q3_total` varchar(10) DEFAULT NULL,
  `sc_Q4_total` varchar(10) DEFAULT NULL,
  `score_h` varchar(10) DEFAULT NULL,
  `score_c` varchar(10) DEFAULT NULL,
  `gidm` varchar(10) DEFAULT NULL,
  `systime` varchar(20) DEFAULT NULL,
  `datetime` varchar(20) DEFAULT NULL,
  `re_time` varchar(50) DEFAULT NULL,
  `gnum_c` varchar(100) DEFAULT NULL,
  `gid` varchar(10) DEFAULT NULL,
  `gnum_h` varchar(100) DEFAULT NULL,
  `gopen` varchar(10) DEFAULT NULL,
  `recv` varchar(10) DEFAULT NULL,
  `sw_RE` varchar(10) DEFAULT NULL,
  `sw_ROU` varchar(10) DEFAULT NULL,
  `hgid` varchar(20) DEFAULT NULL,
  `hgopen` varchar(10) DEFAULT NULL,
  `hrecv` varchar(10) DEFAULT NULL,
  `sw_HRE` varchar(10) DEFAULT NULL,
  `ratio_hre` varchar(10) DEFAULT NULL,
  `ior_HREH` varchar(10) DEFAULT NULL,
  `ior_HREC` varchar(10) DEFAULT NULL,
  `sw_HROU` varchar(10) DEFAULT NULL,
  `ratio_hrouo` varchar(20) DEFAULT NULL,
  `ratio_hrouu` varchar(20) DEFAULT NULL,
  `ior_HROUH` varchar(10) DEFAULT NULL,
  `ior_HROUC` varchar(10) DEFAULT NULL,
  `sc_H2_A` varchar(10) DEFAULT NULL,
  `sc_H2_H` varchar(10) DEFAULT NULL,
  `sc_H1_A` varchar(10) DEFAULT NULL,
  `sc_H1_H` varchar(10) DEFAULT NULL,
  `sc_Q4_A` varchar(10) DEFAULT NULL,
  `sc_Q4_H` varchar(10) DEFAULT NULL,
  `sc_Q3_A` varchar(10) DEFAULT NULL,
  `sc_Q3_H` varchar(10) DEFAULT NULL,
  `sc_Q2_A` varchar(10) DEFAULT NULL,
  `sc_Q2_H` varchar(10) DEFAULT NULL,
  `sc_Q1_A` varchar(10) DEFAULT NULL,
  `sc_Q1_H` varchar(10) DEFAULT NULL,
  `se_now` varchar(10) DEFAULT NULL,
  `se_type` varchar(10) DEFAULT NULL,
  `t_status` varchar(10) DEFAULT NULL,
  `t_count` varchar(10) DEFAULT NULL,
  `sc_new` varchar(10) DEFAULT NULL,
  `half_time` varchar(10) DEFAULT NULL,
  `ptype` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18586 DEFAULT CHARSET=utf8 COMMENT='滚球投注信息';

/*Table structure for table `bet_rule` */

DROP TABLE IF EXISTS `bet_rule`;

CREATE TABLE `bet_rule` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account` varchar(100) NOT NULL,
  `name` varchar(300) DEFAULT NULL,
  `param` varchar(200) DEFAULT NULL,
  `gid` varchar(50) DEFAULT NULL,
  `gtype` varchar(10) DEFAULT NULL,
  `gidm` varchar(50) DEFAULT NULL,
  `league` varchar(100) DEFAULT NULL,
  `team_h` varchar(100) DEFAULT NULL,
  `team_c` varchar(100) DEFAULT NULL,
  `level` char(2) DEFAULT NULL,
  `impl_code` varchar(200) DEFAULT NULL,
  `desc` varchar(300) DEFAULT NULL,
  `iseff` char(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='投注规则';

/*Table structure for table `stat_daily_access` */

DROP TABLE IF EXISTS `stat_daily_access`;

CREATE TABLE `stat_daily_access` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `template_package_id` int(10) NOT NULL,
  `day` varchar(300) DEFAULT NULL,
  `pv` bigint(20) DEFAULT NULL,
  `uv` bigint(20) DEFAULT NULL,
  `vv` bigint(20) DEFAULT NULL,
  `ip` bigint(20) DEFAULT NULL,
  `isleaf` char(1) NOT NULL COMMENT '是否也是 0:不是, 1:是',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `url` varchar(100) DEFAULT NULL,
  `level` char(1) NOT NULL,
  `parentid` int(10) NOT NULL,
  `iseff` char(1) NOT NULL DEFAULT '0' COMMENT '0:无效,1:有效',
  `isleaf` char(1) NOT NULL COMMENT '是否也是 0:不是, 1:是',
  `des` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `icon` varchar(100) DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_param` */

DROP TABLE IF EXISTS `sys_param`;

CREATE TABLE `sys_param` (
  `param_code` varchar(50) NOT NULL,
  `param_name` varchar(50) NOT NULL,
  `param_value` varchar(50) DEFAULT NULL,
  `root_code` varchar(50) DEFAULT NULL,
  `des` varchar(250) DEFAULT NULL,
  `seq` varchar(10) DEFAULT NULL,
  `iseff` char(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`param_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `sex` varchar(1) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `addr` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `iseff` char(1) NOT NULL COMMENT '是否也是 0:不是, 1:是',
  `des` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `web_image` */

DROP TABLE IF EXISTS `web_image`;

CREATE TABLE `web_image` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `image_url` varchar(300) DEFAULT NULL,
  `name` varchar(300) DEFAULT NULL,
  `iseff` char(1) NOT NULL COMMENT '是否也是 0:不是, 1:是',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `web_page` */

DROP TABLE IF EXISTS `web_page`;

CREATE TABLE `web_page` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `name` varchar(300) DEFAULT NULL,
  `page_url` varchar(200) DEFAULT NULL COMMENT '页面URL',
  `template_id` int(10) DEFAULT NULL COMMENT '模板ID,外键web_template.id',
  `template_package_id` int(10) DEFAULT NULL,
  `iseff` char(1) NOT NULL COMMENT '是否也是 0:不是, 1:是',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='页面';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
