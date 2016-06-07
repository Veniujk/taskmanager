
CREATE TABLE `person` (
  `id_person` int(11) NOT NULL,
  `username` varchar(20) COLLATE utf8_polish_ci NOT NULL,
  `password` varchar(40) COLLATE utf8_polish_ci NOT NULL,
  `name` varchar(40) COLLATE utf8_polish_ci DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
