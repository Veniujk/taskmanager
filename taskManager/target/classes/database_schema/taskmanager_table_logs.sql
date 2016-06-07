
CREATE TABLE `logs` (
  `id_log` int(11) NOT NULL,
  `modified` int(11) NOT NULL,
  `id_person` int(11) NOT NULL,
  `id_task` int(11) NOT NULL,
  `action` enum('status_change','assign_change','','') COLLATE utf8_polish_ci NOT NULL,
  `from_value` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  `to_value` varchar(30) COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
