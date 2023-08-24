CREATE TABLE `book` (
	`id` int NOT NULL,
	`title` varchar(30) NOT NULL,
	`student_id` int,
	PRIMARY KEY (`id`)
);

CREATE TABLE `student` (
	`id` int NOT NULL,
	`name` varchar(30) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `librarian` (
	`id` int NOT NULL,
	`name` varchar(30) NOT NULL,
	`password` int NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `book` ADD CONSTRAINT `book_fk0` FOREIGN KEY (`student_id`) REFERENCES `student`(`id`);




