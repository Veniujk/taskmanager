
CREATE TABLE `task` (
  `id_task` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci NOT NULL,
  `status` enum('active','done','waiting','suspended') COLLATE utf8_polish_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deadline` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `id_person` int(11) DEFAULT NULL,
  `id_project` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
