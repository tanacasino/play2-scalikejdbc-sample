CREATE TABLE User(
    `user_id` bigint(20) NOT NULL,
    `name` varchar(100) NOT NULL,
    `password` varchar(100) NOT NULL,
    `birthday` date NOT NULL,
    `registered_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE Task(
    `task_id` bigint(20) NOT NULL,
    `title` varchar(30) NOT NULL,
    `content` text,
    `user_id` bigint(20) NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`task_id`),
    CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

