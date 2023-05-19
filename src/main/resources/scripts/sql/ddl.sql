CREATE TABLE `member` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `created_at` datetime NOT NULL,
                          `last_modified_at` datetime NOT NULL,
                          `email` varchar(255) NOT NULL,
                          `name` varchar(255) NOT NULL,
                          `social_id` varchar(255) NOT NULL,
                          `biography` varchar(255) NOT NULL DEFAULT '',
                          `profile_url` varchar(255) NOT NULL DEFAULT 'https://kr.object.ncloudstorage.com/sns-image-s3/profile/default.jpeg',
                          `username` varchar(255) NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `device` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `created_at` datetime NOT NULL,
                          `last_modified_at` datetime NOT NULL,
                          `token` varchar(255) NOT NULL,
                          `member_id` bigint NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKs2ah6o1y9r1ox99fh8vj5y0ol` (`member_id`),
                          CONSTRAINT `FKs2ah6o1y9r1ox99fh8vj5y0ol` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `post` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `created_at` datetime NOT NULL,
                        `last_modified_at` datetime NOT NULL,
                        `content` text NOT NULL DEFAULT (''),
                        `member_id` bigint NOT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FK83s99f4kx8oiqm3ro0sasmpww` (`member_id`),
                        CONSTRAINT `FK83s99f4kx8oiqm3ro0sasmpww` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `post_image` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `image_path` varchar(255) NOT NULL,
                              `post_id` bigint NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `FKsip7qv57jw2fw50g97t16nrjr` (`post_id`),
                              CONSTRAINT `FKsip7qv57jw2fw50g97t16nrjr` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `follow` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `created_at` datetime NOT NULL,
                          `last_modified_at` datetime NOT NULL,
                          `follower_id` bigint NOT NULL,
                          `following_id` bigint NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKkcoemc64xrm83cdmhyaphcuiu` (`following_id`),
                          KEY `FKtps7gpodlrhxlji90u6r3mlng` (`follower_id`),
                          CONSTRAINT `FKkcoemc64xrm83cdmhyaphcuiu` FOREIGN KEY (`following_id`) REFERENCES `member` (`id`),
                          CONSTRAINT `FKtps7gpodlrhxlji90u6r3mlng` FOREIGN KEY (`follower_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `likes` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `created_at` datetime NOT NULL,
                         `last_modified_at` datetime NOT NULL,
                         `member_id` bigint NOT NULL,
                         `post_id` bigint NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FKa4vkf1skcfu5r6o5gfb5jf295` (`member_id`),
                         KEY `FKowd6f4s7x9f3w50pvlo6x3b41` (`post_id`),
                         CONSTRAINT `FKa4vkf1skcfu5r6o5gfb5jf295` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
                         CONSTRAINT `FKowd6f4s7x9f3w50pvlo6x3b41` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `comment` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `created_at` datetime NOT NULL,
                           `last_modified_at` datetime NOT NULL,
                           `content` varchar(255) NOT NULL,
                           `member_id` bigint NOT NULL,
                           `post_id` bigint NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKmrrrpi513ssu63i2783jyiv9m` (`member_id`),
                           KEY `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id`),
                           CONSTRAINT `FKmrrrpi513ssu63i2783jyiv9m` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
                           CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `alarm` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `created_at` datetime NOT NULL,
                         `last_modified_at` datetime NOT NULL,
                         `member_id` bigint NOT NULL,
                         `is_read` tinyint(1) NOT NULL,
                         `text` varchar(255) NOT NULL,
                         `type` varchar(10) NOT NULL,
                         PRIMARY KEY (`id`),
                         CONSTRAINT `memberAlarm` FOREIGN KEY (`id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
