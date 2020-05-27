-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema motorhome
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema motorhome
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `motorhome` DEFAULT CHARACTER SET utf8 ;
USE `motorhome` ;

-- -----------------------------------------------------
-- Table `motorhome`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Address` (
                                                     `idAddress` INT NOT NULL,
                                                     `country` VARCHAR(45) NULL,
                                                     `roadName` VARCHAR(45) NULL,
                                                     `houseNumber` VARCHAR(45) NULL,
                                                     `postCode` VARCHAR(45) NULL,
                                                     PRIMARY KEY (`idAddress`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Person` (
                                                    `idPerson` INT NOT NULL AUTO_INCREMENT,
                                                    `password` VARCHAR(45) NULL,
                                                    `firstName` VARCHAR(45) NULL,
                                                    `lastName` VARCHAR(45) NULL,
                                                    `email` VARCHAR(45) NULL,
                                                    `siteRole` VARCHAR(45) NULL,
                                                    `birthDate` DATE NULL,
                                                    `joinDate` DATE NULL,
                                                    `Address_idAddress` INT NOT NULL,
                                                    PRIMARY KEY (`idPerson`),
                                                    INDEX `fk_Person_Address1_idx` (`Address_idAddress` ASC),
                                                    CONSTRAINT `fk_Person_Address1`
                                                        FOREIGN KEY (`Address_idAddress`)
                                                            REFERENCES `motorhome`.`Address` (`idAddress`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Paycard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Paycard` (
                                                     `idPaycard` INT NOT NULL AUTO_INCREMENT,
                                                     `cardType` VARCHAR(45) NULL,
                                                     `cardNumber` VARCHAR(45) NULL,
                                                     `expirationDate` DATE NULL,
                                                     `securityDigits` INT NULL,
                                                     PRIMARY KEY (`idPaycard`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Customer` (
                                                      `idCustomer` INT NOT NULL AUTO_INCREMENT,
                                                      `driversLicense` VARCHAR(45) NULL,
                                                      `approved` TINYINT NULL,
                                                      `Person_idPerson` INT NOT NULL,
                                                      `Paycard_idPaycard` INT NOT NULL,
                                                      PRIMARY KEY (`idCustomer`),
                                                      INDEX `fk_Customer_Person_idx` (`Person_idPerson` ASC),
                                                      INDEX `fk_Customer_Paycard1_idx` (`Paycard_idPaycard` ASC),
                                                      CONSTRAINT `fk_Customer_Person`
                                                          FOREIGN KEY (`Person_idPerson`)
                                                              REFERENCES `motorhome`.`Person` (`idPerson`)
                                                              ON DELETE NO ACTION
                                                              ON UPDATE NO ACTION,
                                                      CONSTRAINT `fk_Customer_Paycard1`
                                                          FOREIGN KEY (`Paycard_idPaycard`)
                                                              REFERENCES `motorhome`.`Paycard` (`idPaycard`)
                                                              ON DELETE NO ACTION
                                                              ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Employee` (
                                                      `idEmployee` INT NOT NULL,
                                                      `accountancyId` INT NULL,
                                                      `Person_idPerson` INT NOT NULL,
                                                      PRIMARY KEY (`idEmployee`),
                                                      INDEX `fk_Employee_Person1_idx` (`Person_idPerson` ASC),
                                                      CONSTRAINT `fk_Employee_Person1`
                                                          FOREIGN KEY (`Person_idPerson`)
                                                              REFERENCES `motorhome`.`Person` (`idPerson`)
                                                              ON DELETE NO ACTION
                                                              ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`carModel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`carModel` (
                                                      `idcarModel` INT NOT NULL AUTO_INCREMENT,
                                                      `modelName` VARCHAR(45) NULL,
                                                      `horsePower` INT NULL,
                                                      `beds` INT NULL,
                                                      `engineCapacity` FLOAT NULL,
                                                      `length` FLOAT NULL,
                                                      `height` FLOAT NULL,
                                                      `weight` FLOAT NULL,
                                                      `hotWaterCapacity` FLOAT NULL,
                                                      `coldWaterCapacity` FLOAT NULL,
                                                      `numberOfSeats` INT NULL,
                                                      `oven` TINYINT NULL,
                                                      `cruiseControl` TINYINT NULL,
                                                      `shower` TINYINT NULL,
                                                      PRIMARY KEY (`idcarModel`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Motorhome`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Motorhome` (
                                                       `idMotorhome` INT NOT NULL AUTO_INCREMENT,
                                                       `licensePlate` VARCHAR(45) NULL,
                                                       `notes` VARCHAR(200) NULL,
                                                       `imageURL` VARCHAR(45) NULL,
                                                       `productionYear` INT NULL,
                                                       `description` VARCHAR(45) NULL,
                                                       `minimumDaysOfRental` INT NULL,
                                                       `fuelType` VARCHAR(45) NULL,
                                                       `SeasonalDailyCharge` FLOAT NULL,
                                                       `carModel_idcarModel` INT NOT NULL,
                                                       PRIMARY KEY (`idMotorhome`),
                                                       INDEX `fk_Motorhome_carModel1_idx` (`carModel_idcarModel` ASC),
                                                       CONSTRAINT `fk_Motorhome_carModel1`
                                                           FOREIGN KEY (`carModel_idcarModel`)
                                                               REFERENCES `motorhome`.`carModel` (`idcarModel`)
                                                               ON DELETE NO ACTION
                                                               ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Period`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Period` (
                                                    `idPeriod` INT NOT NULL AUTO_INCREMENT,
                                                    `start` DATE NULL,
                                                    `end` DATE NULL,
                                                    PRIMARY KEY (`idPeriod`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Appointment` (
                                                         `idAppointment` INT NOT NULL,
                                                         `date` DATE NULL,
                                                         `time` TIME NULL,
                                                         `notes` VARCHAR(45) NULL,
                                                         `distance` FLOAT NULL,
                                                         `employees` VARCHAR(200) NULL,
                                                         `Address_idAddress` INT NOT NULL,
                                                         `Motorhome_idMotorhome` INT NOT NULL,
                                                         PRIMARY KEY (`idAppointment`),
                                                         INDEX `fk_Appointment_Address1_idx` (`Address_idAddress` ASC),
                                                         INDEX `fk_Appointment_Motorhome1_idx` (`Motorhome_idMotorhome` ASC),
                                                         CONSTRAINT `fk_Appointment_Address1`
                                                             FOREIGN KEY (`Address_idAddress`)
                                                                 REFERENCES `motorhome`.`Address` (`idAddress`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_Appointment_Motorhome1`
                                                             FOREIGN KEY (`Motorhome_idMotorhome`)
                                                                 REFERENCES `motorhome`.`Motorhome` (`idMotorhome`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Reservation` (
                                                         `idReservation` INT NOT NULL,
                                                         `notes` VARCHAR(45) NULL,
                                                         `internalNotes` VARCHAR(45) NULL,
                                                         `services` VARCHAR(200) NULL,
                                                         `Customer_idCustomer` INT NOT NULL,
                                                         `Employee_idEmployee` INT NOT NULL,
                                                         `Period_idPeriod` INT NOT NULL,
                                                         `Motorhome_idMotorhome` INT NOT NULL,
                                                         `Appointment_idAppointment` INT NOT NULL,
                                                         PRIMARY KEY (`idReservation`),
                                                         INDEX `fk_Reservation_Customer1_idx` (`Customer_idCustomer` ASC),
                                                         INDEX `fk_Reservation_Employee1_idx` (`Employee_idEmployee` ASC),
                                                         INDEX `fk_Reservation_Period1_idx` (`Period_idPeriod` ASC),
                                                         INDEX `fk_Reservation_Motorhome1_idx` (`Motorhome_idMotorhome` ASC),
                                                         INDEX `fk_Reservation_Appointment1_idx` (`Appointment_idAppointment` ASC),
                                                         CONSTRAINT `fk_Reservation_Customer1`
                                                             FOREIGN KEY (`Customer_idCustomer`)
                                                                 REFERENCES `motorhome`.`Customer` (`idCustomer`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_Reservation_Employee1`
                                                             FOREIGN KEY (`Employee_idEmployee`)
                                                                 REFERENCES `motorhome`.`Employee` (`idEmployee`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_Reservation_Period1`
                                                             FOREIGN KEY (`Period_idPeriod`)
                                                                 REFERENCES `motorhome`.`Period` (`idPeriod`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_Reservation_Motorhome1`
                                                             FOREIGN KEY (`Motorhome_idMotorhome`)
                                                                 REFERENCES `motorhome`.`Motorhome` (`idMotorhome`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_Reservation_Appointment1`
                                                             FOREIGN KEY (`Appointment_idAppointment`)
                                                                 REFERENCES `motorhome`.`Appointment` (`idAppointment`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Service` (
                                                     `idService` INT NOT NULL,
                                                     `name` VARCHAR(45) NULL,
                                                     `description` VARCHAR(45) NULL,
                                                     `unitPrice` FLOAT NULL,
                                                     `Reservation_idReservation` INT NOT NULL,
                                                     PRIMARY KEY (`idService`),
                                                     INDEX `fk_Service_Reservation1_idx` (`Reservation_idReservation` ASC),
                                                     CONSTRAINT `fk_Service_Reservation1`
                                                         FOREIGN KEY (`Reservation_idReservation`)
                                                             REFERENCES `motorhome`.`Reservation` (`idReservation`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Invoice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Invoice` (
                                                     `idInvoice` INT NOT NULL,
                                                     `services` VARCHAR(200) NULL,
                                                     `isCompleted` TINYINT NULL,
                                                     `Customer_idCustomer` INT NOT NULL,
                                                     `Period_idPeriod` INT NOT NULL,
                                                     `Motorhome_idMotorhome` INT NOT NULL,
                                                     PRIMARY KEY (`idInvoice`),
                                                     INDEX `fk_Invoice_Customer1_idx` (`Customer_idCustomer` ASC),
                                                     INDEX `fk_Invoice_Period1_idx` (`Period_idPeriod` ASC),
                                                     INDEX `fk_Invoice_Motorhome1_idx` (`Motorhome_idMotorhome` ASC),
                                                     CONSTRAINT `fk_Invoice_Customer1`
                                                         FOREIGN KEY (`Customer_idCustomer`)
                                                             REFERENCES `motorhome`.`Customer` (`idCustomer`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION,
                                                     CONSTRAINT `fk_Invoice_Period1`
                                                         FOREIGN KEY (`Period_idPeriod`)
                                                             REFERENCES `motorhome`.`Period` (`idPeriod`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION,
                                                     CONSTRAINT `fk_Invoice_Motorhome1`
                                                         FOREIGN KEY (`Motorhome_idMotorhome`)
                                                             REFERENCES `motorhome`.`Motorhome` (`idMotorhome`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
