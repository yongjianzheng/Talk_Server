-- phpMyAdmin SQL Dump
-- version 2.11.9.2
-- http://www.phpmyadmin.net
--
-- 主机: 127.0.0.1:3306
-- 生成日期: 2016 年 07 月 24 日 12:02
-- 服务器版本: 5.1.28
-- PHP 版本: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `talk_db`
--

-- --------------------------------------------------------

--
-- 表的结构 `asses`
--

CREATE TABLE IF NOT EXISTS `asses` (
  `t_id` int(11) NOT NULL,
  `t_name` varchar(20) NOT NULL,
  `content` varchar(200) DEFAULT NULL,
  `assgrade` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 导出表中的数据 `asses`
--

INSERT INTO `asses` (`t_id`, `t_name`, `content`, `assgrade`) VALUES
(2, '李华', '李老师好厉害', '好评'),
(3, '王明', '王老师一般般', '中评'),
(2, '李华', '李老师超级棒', '好评'),
(3, '王明', '王老师口语真赞', '好评'),
(2, '李华', '老师不热情', '中评'),
(3, '王明', '老师好严肃', '差评'),
(2, '李华', '没啥用', '差评'),
(2, '李华', '一般般吧', '差评');

-- --------------------------------------------------------

--
-- 表的结构 `orderlist`
--

CREATE TABLE IF NOT EXISTS `orderlist` (
  `l_id` int(11) NOT NULL,
  `t_id` int(11) NOT NULL,
  `cost` varchar(20) NOT NULL,
  `date` varchar(30) NOT NULL,
  `duration` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 导出表中的数据 `orderlist`
--

INSERT INTO `orderlist` (`l_id`, `t_id`, `cost`, `date`, `duration`) VALUES
(1, 2, '30', '2016-05-22 14:20:20', '0.5'),
(1, 3, '40', '2016-05-30 09:20:32', '0.6'),
(1, 2, '20', '2016-05-11 14:20:20', '0.5'),
(1, 2, '80', '2016-05-07 14:20:20', '2');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `phonenum` varchar(20) NOT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `pwd` varchar(20) NOT NULL,
  `photo` mediumblob,
  `wallet` varchar(20) DEFAULT '0',
  `isOnline` tinyint(4) NOT NULL DEFAULT '0',
  `hourypay` varchar(10) DEFAULT NULL,
  `grade` varchar(20) DEFAULT NULL,
  `gradepho` mediumblob,
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- 导出表中的数据 `user`
--

INSERT INTO `user` (`id`, `name`, `phonenum`, `sex`, `pwd`, `photo`, `wallet`, `isOnline`, `hourypay`, `grade`, `gradepho`, `type`) VALUES
(1, '王强', '15813323840', '男', '11111', NULL, '20', 1, NULL, NULL, NULL, 'learner'),
(2, '李华', '18819423762', '女', '123456', NULL, '100', 1, '20', '教授水平', NULL, 'teacher'),
(3, '王明', '15107548812', '男', '369369', NULL, '60', 0, '20', '口语四级', NULL, 'teacher'),
(4, '陈红', '15521396280', '女', '456456', NULL, '70', 1, '50', '教师水平', NULL, 'teacher'),
(5, '彤彤', '15107548812', '男', '258963245', NULL, '0', 1, NULL, NULL, NULL, 'learner'),
(6, '关关', '15632458695', '女', '2589632548', NULL, '0', 0, NULL, '教师水平', NULL, 'teacher');
