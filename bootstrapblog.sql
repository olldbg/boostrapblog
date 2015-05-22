CREATE DATABASE IF NOT EXISTS `bootstrapblog`;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`user` (
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(45) NULL DEFAULT '',
  `sex` TINYINT(1) NULL DEFAULT 0,
  `email` VARCHAR(45) NULL DEFAULT '',
  `birthday` VARCHAR(45) NULL DEFAULT '',
  `phonenumber` VARCHAR(45) NULL DEFAULT '',
  `headphoto` VARCHAR(45) NULL DEFAULT '',
  `sign` VARCHAR(45) NULL DEFAULT '',
  `session` VARCHAR(45) NULL DEFAULT '',
  PRIMARY KEY (`username`)
)charset=utf8;

insert into `bootstrapblog`.`user` (username) values('')

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`blogs` (
  `blogid` VARCHAR(65) NOT NULL,
  `author` VARCHAR(20) NULL DEFAULT '',
  `publishtime` VARCHAR(45) NULL DEFAULT '',
  `title` VARCHAR(100) NULL DEFAULT '',
  `scope` VARCHAR(20) NULL DEFAULT '',
  PRIMARY KEY (`blogid`),
  CONSTRAINT `FK_blogs_user`
    FOREIGN KEY (`author`)
    REFERENCES `bootstrapblog`.`user` (`username`)
)charset=utf8;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`tag` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `blogid` VARCHAR(65) NULL DEFAULT '',
  `tag` VARCHAR(45) NULL DEFAULT '',
  PRIMARY KEY (`id`),
  INDEX `fk_tag_1_idx` (`blogid` ASC),
  CONSTRAINT `fk_tag_1`
    FOREIGN KEY (`blogid`)
    REFERENCES `bootstrapblog`.`blogs` (`blogid`)
)charset=utf8;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`comment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `blogid` VARCHAR(65) NULL DEFAULT '',
  `commentuser` VARCHAR(20) NULL DEFAULT '',
  `commenttime` VARCHAR(45) NULL DEFAULT '',
  `comment` VARCHAR(100) NULL DEFAULT '',
  `isreply` TINYINT(1) NULL DEFAULT 0,
  `replyuser` VARCHAR(20) NULL DEFAULT '',
  PRIMARY KEY (`id`),
  INDEX `fk_comment_1_idx` (`blogid` ASC),
  INDEX `fk_comment_2_idx` (`commentuser` ASC),
  INDEX `fk_comment_3_idx` (`replyuser` ASC),
  CONSTRAINT `fk_comment_1`
    FOREIGN KEY (`blogid`)
    REFERENCES `bootstrapblog`.`blogs` (`blogid`),
  CONSTRAINT `fk_comment_2`
    FOREIGN KEY (`commentuser`)
    REFERENCES `bootstrapblog`.`user` (`username`),
  CONSTRAINT `fk_comment_3`
    FOREIGN KEY (`replyuser`)
    REFERENCES `bootstrapblog`.`user` (`username`)
)charset=utf8;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`album` (
  `albumname` VARCHAR(45) NOT NULL,
  `username` VARCHAR(20) NOT NULL,
  `facepath` VARCHAR(45) NULL DEFAULT '',
  `scope` VARCHAR(45) NULL DEFAULT '',
  INDEX `fk_album_1_idx` (`username` ASC),
  PRIMARY KEY (`albumname`, `username`),
  CONSTRAINT `fk_album_1`
    FOREIGN KEY (`username`)
    REFERENCES `bootstrapblog`.`user` (`username`)
)charset=utf8;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`picture` (
  `picturemd5` VARCHAR(45) NOT NULL,
  `picturename` VARCHAR(45) NULL DEFAULT '',
  `uploadtime` VARCHAR(45) NULL DEFAULT '',
  `uploaduser` VARCHAR(45) NULL DEFAULT '',
  `albumname` VARCHAR(45) NULL DEFAULT '',
  PRIMARY KEY (`picturemd5`),
  INDEX `fk_picture_1_idx` (`uploaduser` ASC),
  INDEX `fk_picture_2_idx` (`albumname` ASC),
  CONSTRAINT `fk_picture_1`
    FOREIGN KEY (`uploaduser`)
    REFERENCES `bootstrapblog`.`user` (`username`),
  CONSTRAINT `fk_picture_2`
    FOREIGN KEY (`albumname`)
    REFERENCES `bootstrapblog`.`album` (`albumname`)
)charset=utf8;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`collection` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `blogid` VARCHAR(65) NULL DEFAULT '',
  `collectuser` VARCHAR(20) NULL DEFAULT '',
  PRIMARY KEY (`id`),
  INDEX `fk_collection_1_idx` (`blogid` ASC),
  CONSTRAINT `fk_collection_1`
    FOREIGN KEY (`blogid`)
    REFERENCES `bootstrapblog`.`blogs` (`blogid`)
)charset=utf8;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`friend` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL DEFAULT '',
  `friendname` VARCHAR(45) NULL DEFAULT '',
  PRIMARY KEY (`id`),
  INDEX `fk_friend_1_idx` (`username` ASC),
  INDEX `fk_friend_2_idx` (`friendname` ASC),
  CONSTRAINT `FK_friend_1`
    FOREIGN KEY (`username`)
    REFERENCES `bootstrapblog`.`user` (`username`),
  CONSTRAINT `fk_friend_2`
    FOREIGN KEY (`friendname`)
    REFERENCES `bootstrapblog`.`user` (`username`)
)charset=utf8;

CREATE TABLE IF NOT EXISTS `bootstrapblog`.`configure` (
  `username` VARCHAR(45) NOT NULL,
  `dividnumber` INT(11) NULL DEFAULT 5,
  `background` VARCHAR(45) NULL DEFAULT '',
  PRIMARY KEY (`username`),
  CONSTRAINT `FK_configure_user`
    FOREIGN KEY (`username`)
    REFERENCES `bootstrapblog`.`user` (`username`)
)charset=utf8;

ALTER TABLE `bootstrapblog`.`comment`
DROP FOREIGN KEY `fk_comment_1`,
DROP FOREIGN KEY `fk_comment_2`,
DROP FOREIGN KEY `fk_comment_3`;

ALTER TABLE `bootstrapblog`.`comment`
ADD CONSTRAINT `fk_comment_1`
  FOREIGN KEY (`blogid`)
  REFERENCES `bootstrapblog`.`blogs` (`blogid`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_comment_2`
  FOREIGN KEY (`commentuser`)
  REFERENCES `bootstrapblog`.`user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_comment_3`
  FOREIGN KEY (`replyuser`)
  REFERENCES `bootstrapblog`.`user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
