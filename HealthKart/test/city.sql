-- phpMyAdmin SQL Dump
-- version 3.3.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 06, 2012 at 12:25 PM
-- Server version: 5.1.63
-- PHP Version: 5.3.5-1ubuntu7.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `healthkart_stag`
--

-- --------------------------------------------------------

--
-- Table structure for table `city`
--

CREATE TABLE IF NOT EXISTS `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=660 ;

--
-- Dumping data for table `city`
--

INSERT INTO `city` (`id`, `name`) VALUES
(1, 'NICOBAR'),
(2, 'NORTH AND MIDDLE ANDAMAN'),
(3, 'SOUTH ANDAMAN'),
(4, 'ADILABAD'),
(5, 'ANANTHAPUR'),
(6, 'CHITTOOR'),
(7, 'CUDDAPAH'),
(8, 'EAST GODAVARI'),
(9, 'GUNTUR'),
(10, 'HYDERABAD'),
(11, 'Jammu'),
(12, 'K.V.RANGAREDDY'),
(13, 'KARIM NAGAR'),
(14, 'KHAMMAM'),
(15, 'KRISHNA'),
(16, 'KURNOOL'),
(17, 'MAHBUB NAGAR'),
(18, 'MEDAK'),
(19, 'NALGONDA'),
(20, 'NELLORE'),
(21, 'NIZAMABAD'),
(22, 'PRAKASAM'),
(23, 'SRIKAKULAM'),
(24, 'VIShAKHAPATNAM'),
(25, 'VIZIANAGARAM'),
(26, 'WARANGAL'),
(27, 'WEST GODAVARI'),
(28, 'CHANGLANG'),
(29, 'DIBANG VALLEY'),
(30, 'EAST KAMENG'),
(31, 'EAST SIANG'),
(32, 'KURUNG KUMEY'),
(33, 'LOHIT'),
(34, 'LOWER DIBANG VALLEY'),
(35, 'LOWER SUBANSIRI'),
(36, 'PAPUM PARE'),
(37, 'TAWANG'),
(38, 'TIRAP'),
(39, 'UPPER SIANG'),
(40, 'UPPER SUBANSIRI'),
(41, 'WEST KAMENG'),
(42, 'WEST SIANG'),
(43, 'BARPETA'),
(44, 'BONGAIGAON'),
(45, 'CACHAR'),
(46, 'DARRANG'),(47, 'DELHI'),
(48, 'DHEMAJI'),
(49, 'DHUBRI'),
(50, 'DIBRUGARH'),
(51, 'GOALPARA');
