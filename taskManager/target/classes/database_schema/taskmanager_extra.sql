
-- Indexes for table `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id_log`),
  ADD KEY `id_user` (`id_person`,`id_task`),
  ADD KEY `id_task` (`id_task`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id_message`),
  ADD KEY `id_user` (`id_person`,`id_task`),
  ADD KEY `id_task` (`id_task`);

--
-- Indexes for table `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id_person`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `person_team`
--
ALTER TABLE `person_team`
  ADD KEY `id_person` (`id_person`,`id_team`),
  ADD KEY `id_team` (`id_team`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id_project`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `id_team` (`id_team`);

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id_task`),
  ADD KEY `id_person` (`id_person`,`id_project`),
  ADD KEY `id_project` (`id_project`);

--
-- Indexes for table `task_parent`
--
ALTER TABLE `task_parent`
  ADD KEY `id_task` (`id_task`),
  ADD KEY `id_parent` (`id_parent`);

--
-- Indexes for table `team`
--
ALTER TABLE `team`
  ADD PRIMARY KEY (`id_team`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `id_team_leader` (`id_team_leader`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `logs`
--
ALTER TABLE `logs`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `message`
--
ALTER TABLE `message`
  MODIFY `id_message` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `person`
--
ALTER TABLE `person`
  MODIFY `id_person` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `project`
--
ALTER TABLE `project`
  MODIFY `id_project` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `task`
--
ALTER TABLE `task`
  MODIFY `id_task` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `team`
--
ALTER TABLE `team`
  MODIFY `id_team` int(11) NOT NULL AUTO_INCREMENT;
--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`id_person`) REFERENCES `person` (`id_person`),
  ADD CONSTRAINT `logs_ibfk_2` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`);

--
-- Ograniczenia dla tabeli `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`id_person`) REFERENCES `person` (`id_person`),
  ADD CONSTRAINT `message_ibfk_2` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`);

--
-- Ograniczenia dla tabeli `person_team`
--
ALTER TABLE `person_team`
  ADD CONSTRAINT `person_team_ibfk_1` FOREIGN KEY (`id_person`) REFERENCES `person` (`id_person`),
  ADD CONSTRAINT `person_team_ibfk_2` FOREIGN KEY (`id_team`) REFERENCES `team` (`id_team`);

--
-- Ograniczenia dla tabeli `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `project_ibfk_1` FOREIGN KEY (`id_team`) REFERENCES `team` (`id_team`);

--
-- Ograniczenia dla tabeli `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_1` FOREIGN KEY (`id_person`) REFERENCES `person` (`id_person`),
  ADD CONSTRAINT `task_ibfk_2` FOREIGN KEY (`id_project`) REFERENCES `project` (`id_project`);

--
-- Ograniczenia dla tabeli `task_parent`
--
ALTER TABLE `task_parent`
  ADD CONSTRAINT `task_parent_ibfk_1` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`),
  ADD CONSTRAINT `task_parent_ibfk_2` FOREIGN KEY (`id_parent`) REFERENCES `task` (`id_task`);

--
-- Ograniczenia dla tabeli `team`
--
ALTER TABLE `team`
  ADD CONSTRAINT `team_ibfk_1` FOREIGN KEY (`id_team_leader`) REFERENCES `person` (`id_person`);
