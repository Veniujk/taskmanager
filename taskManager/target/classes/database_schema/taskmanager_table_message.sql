
CREATE TABLE `message` (
  `id_message` int(11) NOT NULL,
  `id_person` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_task` int(11) NOT NULL,
  `content` text COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
