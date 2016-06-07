
CREATE TABLE `team` (
  `id_team` int(11) NOT NULL,
  `name` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  `id_team_leader` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
