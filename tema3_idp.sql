-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 07, 2013 at 06:41 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `tema3_idp`
--

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE IF NOT EXISTS `service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`id`, `name`) VALUES
(1, 'reparatii calculatoare'),
(2, 'instalare windows'),
(3, 'plimbare caine'),
(4, 'hranire batran'),
(5, 'spalat vase');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `status` varchar(100) NOT NULL DEFAULT 'INACTIVE',
  `address` varchar(100) NOT NULL,
  `port` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `type`, `status`, `address`, `port`) VALUES
(1, 'jimy', 'test', 'buyer', 'INACTIVE', '127.0.0.1', 30001),
(2, 'lili', 'test', 'seller', 'INACTIVE', '127.0.0.1', 30002),
(3, 'ion', 'test', 'buyer', 'INACTIVE', '127.0.0.1', 30003),
(4, 'ana', 'test', 'seller', 'INACTIVE', '127.0.0.1', 30004);

-- --------------------------------------------------------

--
-- Table structure for table `user_service`
--

CREATE TABLE IF NOT EXISTS `user_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `servicename` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `user_service`
--

INSERT INTO `user_service` (`id`, `username`, `servicename`) VALUES
(1, 'jimy', 'reparatii calculatoare'),
(2, 'lili', 'reparatii calculatoare'),
(3, 'ion', 'instalare windows'),
(4, 'ana', 'instalare windows'),
(5, 'jimy', 'hranire batran'),
(6, 'ana', 'hranire batran'),
(7, 'ion', 'spalat vase'),
(8, 'lili', 'spalat vase'),
(9, 'jimy', 'plimbare caine'),
(10, 'lili', 'plimbare caine'),
(11, 'ana', 'reparatii calculatoare'),
(12, 'ion', 'reparatii calculatoare');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
