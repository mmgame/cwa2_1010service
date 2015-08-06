CREATE TABLE `testuser` (
	`userId` bigint(20) NOT NULL,
        `level` int(11) DEFAULT '0',
	`name` varchar(32) DEFAULT '',
	`exp` int(11) DEFAULT '0',
	PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
