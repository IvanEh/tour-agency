CREATE TABLE IF NOT EXISTS `tour` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(75) NOT NULL,
  `description` VARCHAR(512) NULL DEFAULT NULL,
  `type` INT(11) NOT NULL,
  `hot` TINYINT(1) NOT NULL DEFAULT '0',
  `price` DECIMAL(10,4) NOT NULL,
  `enabled` INT(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(35) NOT NULL,
  `lastName` VARCHAR(35) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `discount` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`id`));

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
    REFERENCES `user` (`id`));

DROP TABLE IF EXISTS `role`;

CREATE TABLE IF NOT EXISTS `role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));

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
    REFERENCES `user` (`id`));

INSERT INTO `role` VALUES(1, 'CUSTOMER');
INSERT INTO `role` VALUES(2, 'AGENT');