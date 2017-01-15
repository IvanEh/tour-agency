CREATE TABLE IF NOT EXISTS `tour` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(75) NOT NULL,
  `description` VARCHAR(512) NULL DEFAULT NULL,
  `type` INT(11) NOT NULL,
  `hot` TINYINT(1) NOT NULL DEFAULT '0',
  `price` DECIMAL(10,4) NOT NULL,
  `enabled` INT(11) NOT NULL DEFAULT '1',
  `avg_rating` DECIMAL(3,2) NULL,
  `votes_count` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `firstName` VARCHAR(35) CHARACTER SET 'utf8' NOT NULL,
  `lastName` VARCHAR(35) CHARACTER SET 'utf8' NOT NULL,
  `password` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `discount` INT(11) NULL DEFAULT '0'
  PRIMARY KEY (`id`))
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `purchase` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `tour_id` INT(11) NOT NULL,
  `date` DATETIME NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_purchase_tour_id` (`tour_id` ASC),
  INDEX `fk_purchase_user_id` (`user_id` ASC),
  CONSTRAINT `fk_purchase_tour_id0`
    FOREIGN KEY (`tour_id`)
    REFERENCES `tour` (`id`),
  CONSTRAINT `fk_purchase_user_id0`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`))
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `role_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_id` (`user_id` ASC),
  INDEX `fk_role_id` (`role_id` ASC),
  CONSTRAINT `fk_role_id0`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`),
  CONSTRAINT `fk_user_id0`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`))
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `review` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `text` NVARCHAR(1024) NOT NULL,
  `rating` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `author_id` INT NOT NULL,
  `tour_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_review_author_id` (`author_id` ASC),
  INDEX `fk_review_tour_id` (`tour_id` ASC),
  CONSTRAINT `fk_review_author_id`
    FOREIGN KEY (`author_id`)
    REFERENCES `user` (`id`),
  CONSTRAINT `fk_review_tour_id`
    FOREIGN KEY (`tour_id`)
    REFERENCES `tour` (`id`));
