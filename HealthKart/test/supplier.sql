-- phpMyAdmin SQL Dump
-- version 3.3.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 03, 2012 at 12:03 PM
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
-- Table structure for table `supplier`
--

CREATE TABLE IF NOT EXISTS `supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `state` varchar(100) NOT NULL,
  `city` varchar(100) DEFAULT NULL,
  `contact_number` varchar(45) DEFAULT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `line1` varchar(200) DEFAULT NULL,
  `line2` varchar(200) DEFAULT NULL,
  `tin_number` varchar(45) DEFAULT NULL,
  `pincode` varchar(45) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `brands` varchar(45) DEFAULT NULL,
  `dam_exp_cond` varchar(500) DEFAULT NULL,
  `email_id` varchar(50) DEFAULT NULL,
  `terms_of_trade` varchar(100) DEFAULT NULL,
  `credit_period` varchar(10) DEFAULT NULL,
  `margins` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tin_number_UNIQUE` (`tin_number`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=375 ;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`id`, `name`, `state`, `city`, `contact_number`, `contact_person`, `line1`, `line2`, `tin_number`, `pincode`, `create_date`, `update_date`, `brands`, `dam_exp_cond`, `email_id`, `terms_of_trade`, `credit_period`, `margins`) VALUES
(-1, 'MIGRATE', 'state', 'city', '12345', 'contact_person', 'line1', 'line2', '12345', 'pincode', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(1, 'Healthpoint', 'DELHI', 'New Delhi', NULL, NULL, 'A-21 Basement Amar Colony, Lajpat', 'Nagar-4', '07510305402', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(2, 'Decent Healthcare', 'DELHI', 'New Delhi', '9810611270', NULL, '36/05 Ashok Nagar Near tilak Nagar Metro Station', NULL, '07840284026', '110018', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 'Equinox Overseas Pvt Ltd', 'Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(5, 'Niscomed India Pvt Ltd', 'Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(6, 'Microgene Healthcare', 'Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(9, 'Vaishnavi Surgicals', 'PUNJAB', 'Punjab', '9815160116', NULL, 'Sagar Market, Pindi Street, Ludhiana', NULL, '03572073260', '141008', '2012-06-15 14:31:12', '2012-06-15 14:31:12', NULL, NULL, NULL, NULL, NULL, NULL),
(11, 'Nidek Medical India Pvt. Ltd.', 'DELHI', 'New Delhi', '26165742', NULL, '238 2nd Floor, Ansal Chamber-II, 6', 'Bhakaji Cama Place', '07600254379', '110066', '2012-07-09 12:32:11', '2012-07-09 12:32:11', NULL, NULL, NULL, NULL, NULL, NULL),
(12, 'Vinayak Pharma', 'DELHI', 'New Delhi', '09310461036', 'Mr. Mayank', 'C-123, Lajpat Nagar-1', 'New Delhi', '07690264951', '110024', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(15, 'Rattan Enterprices', 'Haryana', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(16, 'Unique Trader', 'DELHI', 'New Delhi', NULL, NULL, 'C/184, Lajpat Nagar-I', NULL, '07340077303', '110024', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(17, 'Gulati Traders', 'HARYANA', 'Gurgaon', '9818048012', NULL, '87/8 Marla, Model Town', NULL, '06881922302', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(18, 'Shree Shyam Traders', 'HARYANA', 'Gurgaon', '9350507915', NULL, 'A-52, Ashok Vihar, Near Hanuman Mandir', NULL, '06541925690', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(20, 'GNC', 'Gurgaon', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(21, 'Unlimited Nutrition', 'MAHARASHTRA', 'Mumbai', '23424079', NULL, '58/B, Resham Wala Building, 2nd Floor,', 'Room No. 6, Mohamedali Road', '27340037785', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(25, 'Optimum Nutrition', 'Mumbai', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(28, 'Navrang Distributors', 'HARYANA', 'Gurgaon', '9999604446/ 7503931562', 'satish', 'Sec-37 Plot No-460 Pace City-II', NULL, '06511936328', NULL, '2012-06-20 12:01:12', '2012-06-20 12:01:12', NULL, NULL, NULL, NULL, NULL, NULL),
(30, 'Sachdeva Distributors', 'HARYANA', 'Gurgaon', '9810735345', NULL, '37, Main Road, Luxmi Garden', NULL, '06271922007', '122001', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(33, 'Sr Enterprise', 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(36, 'Universal Corporation Ltd.', 'HARYANA', NULL, NULL, NULL, NULL, NULL, '06201831496', NULL, '2012-06-25 15:15:32', '2012-06-25 15:15:32', NULL, NULL, NULL, NULL, NULL, NULL),
(40, 'Aditi Marketing', 'HARYANA', 'Gurgaon', '9811603604', NULL, 'H.No-5/8 Model Town', NULL, '06981927388', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(41, 'Smart Nutrition', 'New Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(42, 'Beauty Impex Private Limited', 'HARYANA', 'Faridabad', NULL, NULL, 'H.No:1473-7E Faridabad', NULL, '06751216941', '121006', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(43, 'Lotus Herbals Ltd.', 'DELHI', 'New Delhi', NULL, NULL, 'WZ-572 D, Naraina Village', NULL, '07860262966', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(44, 'S.R. Enterprises', 'DELHI', 'New Delhi', '41682941', NULL, 'C-5/39, Safdarjung Development Area', NULL, '07230353086', NULL, '2012-07-09 13:21:20', '2012-07-09 13:21:20', NULL, NULL, NULL, NULL, NULL, NULL),
(45, 'Neulife Store', 'DELHI', 'New Delhi', '41072497', NULL, 'Ground Floor Malviya Nagar', NULL, '07350372517', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(46, 'Arora Medical Store', 'HARYANA', 'Gurgaon', '9911115702', NULL, '17/8, Model Town, Pharma Lane, Khandsa', 'Road', '06711915751', '122001', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(47, 'Aar Dee Distribution House', 'HARYANA', 'Gurgaon', '9312202265', NULL, 'K-50 South City-I', NULL, '06341825890', NULL, '2012-06-07 16:56:15', '2012-06-07 16:56:15', NULL, NULL, NULL, NULL, NULL, NULL),
(49, 'Advanced Agencies', 'HARYANA', 'Gurgaon', '3296688', NULL, '37/22, Ashok Marg, Moti Vihar, South', 'City', '06381824025', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(50, 'Girme''s', 'Mahashtra', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(51, 'OM Opticians', 'HARYANA', 'Gurgaon', '4202422', NULL, 'Near Ahuja Hospital, New Rly. Road, Daya', 'Nand Colony', '06841931151', NULL, '2012-06-20 12:18:42', '2012-06-20 12:18:42', NULL, NULL, NULL, NULL, NULL, NULL),
(52, 'Cheers Food & Beverages', 'PUNJAB', 'Punjab', '0175-2232273', NULL, 'C-73, Focal Point, Patiala(Punjab)', NULL, '03421127835', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(54, 'ARC Distributors', 'MAHARASHTRA', 'Mumbai', '022-26368899', NULL, '5, Parag Bldg, J p Road, Versova,', 'Andheri(West)', '27750633867', '400061', '2012-06-15 13:19:22', '2012-06-15 13:19:22', NULL, NULL, NULL, NULL, NULL, NULL),
(56, 'Guardian lifecare Pvt ltd', 'HARYANA', 'Gurgaon', '408788', NULL, 'Shop No-110, Sec-14, Main Market', NULL, '06461823981', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(59, 'Leader Healthcare', 'Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(60, 'Sanrai', 'DELHI', NULL, NULL, NULL, 'Tin Number Applied', NULL, '07000000001', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(63, 'Stevi0cal', 'Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(65, 'KTM Healthcare Pvt. Ltd.', 'UTTAR PRADESH', 'Noida', '4561881', NULL, 'D-210, Sec-10, Gautam budh Nagar, Noida', NULL, '09565712450', '201301', '2012-06-16 10:18:16', '2012-06-16 10:18:16', NULL, NULL, NULL, NULL, NULL, NULL),
(67, 'Chahat Distributors', 'DELHI', NULL, NULL, NULL, NULL, NULL, '07880388607', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(69, 'Aggarwal Medical & General Store', 'HARYANA', 'Gurgaon', '9810686502', NULL, '541/1 Opp- Bhim Nagar, New Railway Road', NULL, '06391925433', '122001', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(70, 'WW Group', 'TAMIL NADU', NULL, NULL, NULL, NULL, NULL, '33732392583', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(71, 'Rama Impex', 'DELHI', 'New Delhi', '9810010027', NULL, '5180, 2nd Floor, Main Sadar Bazar,', NULL, '07430341239', '110006', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(74, 'Biotech Trading Pvt. Ltd', 'DELHI', NULL, NULL, 'Siddharth', NULL, NULL, '07780343423', NULL, '2012-07-09 15:56:42', '2012-07-09 15:56:42', NULL, NULL, NULL, NULL, NULL, NULL),
(75, 'Erina Eco Craft Pvt. Ltd.', 'Maharashtra', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(76, 'Aax Global', 'KARNATAKA', 'Banglore', '9342537713', NULL, '26/3 Rain Tree Arc HMT Road, Mathikere', NULL, '29210220523', '560054', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(77, 'Unisafe Technologies', 'DELHI', 'New Delhi', '9999934697', NULL, 'C-126, Naraina Indsl. Area, Phase-I,', NULL, '07160279356', '110028', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(78, 'Saint Pure', 'DELHI', NULL, '9711097110', 'Maaria', 'M-175, Greater Kailash', NULL, '07040410081', '110048', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(79, 'SSP Modern Marketing', 'HARYANA', NULL, NULL, NULL, NULL, NULL, '06721931896', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(81, 'Rohto Pharma (India) Pvt. Ltd.', 'Haryana', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(83, 'Svaasa Nutraceuticals & Organics Pvt. Ltd.', 'DELHI', 'New Delhi', NULL, NULL, 'A-15/17 Vasant Vihaar', NULL, '07370370275', NULL, '2012-07-09 14:33:24', '2012-07-09 14:33:24', NULL, NULL, NULL, NULL, NULL, NULL),
(84, 'Baba Enterprises', 'HARYANA', 'Gurgaon', '9899283851', NULL, '42, Sheetla Mata Road, Behind Akashi', 'Petrol Pump', '06371934756', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(85, 'Kanvar Group', 'New Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(87, 'Vertech Health Solutions', 'DELHI', 'New Delhi', '22619103', NULL, 'Shop no-304,IIIrd Floor, V4, Mayur', 'Plaza-V, Plot No-5, Block-G', '07860320293', '110096', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(88, 'PID Imports India Pvt Ltd.', 'MAHARASHTRA', 'Mumbai', '09820885697', 'Sajid', 'Jeena & Co Warehouse', 'Paras Complex, Nr. Patil Poultry Farm,Falaspha fataa, Panvel', '27210834857', '400054', '2012-07-31 16:24:49', '2012-07-31 16:24:49', NULL, NULL, NULL, NULL, NULL, NULL),
(90, 'Jain Associates', 'HARYANA', 'Hisar', '01663257214', NULL, 'Kila Bazar', 'Hansi', '06101832036', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(91, 'Raj Arrey', 'Gurgaon', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(92, 'Tablets (India) Limited', 'DELHI', 'New Delhi', NULL, NULL, 'A-10/4, Mayapuri Indl Area, Phase-I', NULL, '07580020426', NULL, '2012-07-09 14:36:11', '2012-07-09 14:36:11', NULL, NULL, NULL, NULL, NULL, NULL),
(93, 'Kannu Impex', 'DELHI', 'New Delhi', '9818213243', NULL, '514, Majestic Tower G-17, Community', 'Center, Vikas Puri', '07590291099', '1100158', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(95, 'Jain Trading Co.', 'HARYANA', 'Gurgaon', '9811659042', NULL, '169/13, Ahata Pop Singh, Roshanpura', NULL, '06241921199', NULL, '2012-06-16 14:32:55', '2012-06-16 14:32:55', NULL, NULL, NULL, NULL, NULL, NULL),
(97, 'Unicity', 'Bangalore', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(98, 'Move-On', 'DELHI', 'New Delhi', '26384384', NULL, 'HO: B-36, Lajpat Nagar-II', NULL, '07830022374', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(99, 'Pro-Active Foods Pvt Ltd', 'DELHI', 'New Delhi', NULL, NULL, '16, Karkardooma Community Centre, Vikas', 'Marg Extn', '07350393954', '110092', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(100, 'Aarkios Health Pvt.Ltd.', 'MAHARASHTRA', 'Mumbai', '22004061', NULL, '3-A Bhagwan Mension 7, Cinema Road,', 'Behind Metro Cinema', '27800674725', '400020', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(102, 'Euro Cosmetics', 'DELHI', 'New Delhi', NULL, NULL, '122 A Mohammadpur', NULL, '07880319700', '110066', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(103, 'Old Inventory', 'Haryana', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(104, 'Healthy Smiles India', 'DELHI', NULL, NULL, NULL, NULL, NULL, '07170361849', NULL, '2012-06-15 12:54:26', '2012-06-15 12:54:26', NULL, NULL, NULL, NULL, NULL, NULL),
(105, 'Priya Cellulars', 'MAHARASHTRA', NULL, NULL, NULL, NULL, NULL, '27650311009', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(106, 'Wellness QED', 'DELHI', 'New Delhi', '29531861', NULL, '322, Neb Sarai', NULL, '07050308177', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(108, 'Life Line Surgicals', 'DELHI', 'New Delhi', '9899056911', NULL, 'E-106, Lajpat Nagar-1', NULL, '07040256530', '110024', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(109, 'Navin Enterprises', 'HARYANA', 'Gurgaon', '0124 - 4268911', NULL, '269/31 B, Rajiv Nagar, Opposite Shiv', 'Mandir', '06861909606', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(110, 'M D impex', 'MAHARASHTRA', 'Mumbai', '022-22914507', NULL, 'Office:3, Morarjee Velji Bldg. No.7/A,', 'Kolbhat Lane, Kalbadevi Road', '27970705720', '400002', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(111, 'Lord & Berry India', 'DELHI', 'New Delhi', NULL, NULL, '42 North West Avenue Punjabi Bagh', NULL, '07150364188', '110026', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(112, 'Gardenia Cosmocare Pvt Ltd', 'DELHI', 'New Delhi', NULL, NULL, 'B-723, DLF Tower, 7th Floor, Jasola', NULL, '07370347189', '110025', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(113, 'KNS Trading Pvt Ltd', 'DELHI', NULL, NULL, NULL, NULL, NULL, '07170370579', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(114, 'Merquiry Medica', 'Delhi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(115, 'Nourish Organic FOODS PVT. LTD.', 'HARYANA', 'Gurgaon', '0124 2291950', NULL, 'Plot No 56, Sector-6', 'Manesar', '06481931834', NULL, '2012-07-09 12:39:03', '2012-07-09 12:39:03', NULL, NULL, NULL, NULL, NULL, NULL),
(116, 'Radiohms Agenices Ltd', 'DELHI', 'New Delhi', '40500800', NULL, 'B-7/2, Okhla Industrial Area, Phase-II', NULL, '07780327612', '110020', '2012-07-09 13:06:35', '2012-07-09 13:06:35', NULL, NULL, NULL, NULL, NULL, NULL),
(117, 'My Nutrition Supplements Private Limited', 'DELHI', 'New Delhi', NULL, NULL, '53-A/7, Basement, Rama Road Industrial', 'Area', '07700380424', '110015', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(119, 'Bindal Distributors', 'HARYANA', 'Gurgoan', '9868305045', NULL, 'Shop no-13 ( Basement) New Subji Mandi', 'Ballabgarh', '06041224170', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(121, 'Nirman Associates', 'HARYANA', 'Gurgaon', '9873755185', NULL, 'Ardee City, Gate No.4, Bohara Mkt.', 'Waziradabad', '06701215950', NULL, '2012-07-09 12:33:52', '2012-07-09 12:33:52', NULL, NULL, NULL, NULL, NULL, NULL),
(122, 'Ashok Enterprises', 'DELHI', 'New Delhi', '9350812038', NULL, '400, Bagh Kade khan, Opp Community', 'Centre, Padam Nagar', '07090033548', '110007', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(125, 'Bhagwati Sons Vyapaar Pvt Ltd', 'Gurgaon', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(126, 'Tri-Star Products Pvt', 'UTTAR PRADESH', NULL, NULL, NULL, NULL, NULL, '09465800626', NULL, '2012-07-09 14:48:09', '2012-07-09 14:48:09', NULL, NULL, NULL, NULL, NULL, NULL),
(129, 'Chiranjee International', 'HARYANA', 'Gurgaon', NULL, NULL, 'Sec-4, Basai Road, Behind Ansal kataria', 'Service Station, Village- Basai', '06711931756', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(130, 'Mahendera Medical & General Store', 'HARYANA', 'Gurgaon', '9818616621', NULL, '297-B Mianwali Colony, Gurgaon', NULL, '06791913379', '122001', '2012-07-10 11:37:34', '2012-07-10 11:37:34', NULL, NULL, NULL, NULL, NULL, NULL),
(132, 'Abbott Healthcare Pvt. Ltd.', 'CHANDIGARH', 'Chandigarh', '0172 2792967', NULL, 'Sector 26', NULL, '04450017443', '160019', '2012-07-09 11:41:19', '2012-07-09 11:41:19', NULL, NULL, NULL, NULL, NULL, NULL),
(134, 'VNS Marketing Pvt Ltd', 'DELHI', NULL, NULL, NULL, NULL, NULL, '07380241308', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(137, 'Medihelp Healthcare Pvt. Ltd.', 'DELHI', 'NEW DELHI', NULL, 'MANISH', '6052/1,D-6,VASANT KUNJ,NEW DELHI', NULL, '07630380802', '110070', '2012-06-16 10:37:47', '2012-06-16 10:37:47', NULL, NULL, NULL, NULL, NULL, NULL),
(138, 'Morepen Laboratories Ltd', 'DELHI', 'New Delhi', '09810088719', 'Mr. C.M. Kaul', 'AG-82, Sanjay Gandhi Transport Nagar', 'Delhi', '07770133278', '110042', '2012-06-15 13:11:18', '2012-06-15 13:11:18', NULL, NULL, NULL, NULL, NULL, NULL),
(139, 'Mediquip Systems', 'HARYANA', 'Gurgaon', '4078202', NULL, '3-4 MC Market Near Police Post', NULL, '06121919713', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(141, 'Asro Enterprises', 'DELHI', 'New Delhi', '9717289337', NULL, 'H.No-152 ( Basement), Sant Nagar(Near', 'Gurdwara)', '07090403409', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(146, 'Seward Systems Medical Pvt Ltd', 'DELHI', 'delhi', '09820426011', 'Param', '2nd floor WZ-106/138', 'Rajauri Garden', '07070410113', '110027', '2012-07-09 14:12:05', '2012-07-09 14:12:05', NULL, NULL, NULL, NULL, NULL, NULL),
(147, 'Aryan Medicare Pvt Ltd', 'HARYANA', 'Gurgaon', '9990041109', NULL, '4/45, Shivaji Gurgaon', NULL, '06731917195', NULL, '2012-07-09 15:42:22', '2012-07-09 15:42:22', NULL, NULL, NULL, NULL, NULL, NULL),
(148, 'Gupta Traders', 'HARYANA', NULL, NULL, NULL, NULL, NULL, '06261920315', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(149, 'Om Sai Ram Sales', 'HARYANA', 'Gurgaon', NULL, NULL, '71/02, New Dayanand Colony,', 'New Railway Road', '06231933087', '122001', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(150, 'Vedic Natural Care Pvt Ltd', 'DELHI', NULL, '9650398429', 'Mr.Nagendra', NULL, NULL, '07880394002', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(151, 'Headrush Marketing Pvt. Ltd.', 'MAHARASHTRA', NULL, NULL, NULL, NULL, NULL, '27300024906', NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(153, 'Gaur Sons Enterprises', 'Delhi', 'New Delhi', '989775575', NULL, '573, Chirag Delhi', 'New Delhi', '07120350576', '110017', '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(155, 'JSB', 'Gurgaon', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(156, 'GL Parnami Marketing Company', 'Haryana', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2012-05-02 10:00:00', '2012-05-02 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL);
