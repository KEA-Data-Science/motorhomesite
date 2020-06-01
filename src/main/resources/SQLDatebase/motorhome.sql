-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema motorhome
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema motorhome
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `motorhome` DEFAULT CHARACTER SET utf8;
USE `motorhome`;

-- -----------------------------------------------------
-- Table `motorhome`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Address`
(
    `idAddress`   INT         NOT NULL AUTO_INCREMENT,
    `country`     VARCHAR(45) NULL,
    `roadName`    VARCHAR(45) NULL,
    `houseNumber` VARCHAR(45) NULL,
    `postCode`    VARCHAR(45) NULL,
    PRIMARY KEY (`idAddress`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Person`
(
    `idPerson`          INT         NOT NULL AUTO_INCREMENT,
    `password`          VARCHAR(45) NULL,
    `firstName`         VARCHAR(45) NULL,
    `lastName`          VARCHAR(45) NULL,
    `email`             VARCHAR(45) NULL,
    `siteRole`          VARCHAR(45) NULL,
    `birthDate`         DATE        NULL,
    `joinDate`          DATE        NULL,
    `Address_idAddress` INT         NOT NULL,
    PRIMARY KEY (`idPerson`),
    INDEX `fk_Person_Address1_idx` (`Address_idAddress` ASC),
    CONSTRAINT `fk_Person_Address1`
        FOREIGN KEY (`Address_idAddress`)
            REFERENCES `motorhome`.`Address` (`idAddress`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Paycard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Paycard`
(
    `idPaycard`      INT         NOT NULL AUTO_INCREMENT,
    `cardType`       VARCHAR(45) NULL,
    `cardNumber`     VARCHAR(45) NULL,
    `expirationDate` DATE        NULL,
    `securityDigits` INT         NULL,
    PRIMARY KEY (`idPaycard`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Customer`
(
    `idCustomer`        INT         NOT NULL AUTO_INCREMENT,
    `driversLicense`    VARCHAR(45) NOT NULL,
    `approved`          TINYINT     NULL,
    `Person_idPerson`   INT         NOT NULL,
    `Paycard_idPaycard` INT         NOT NULL,
    PRIMARY KEY (`driversLicense`),
    KEY `idCustomer` (`idCustomer`), /* allows ai on non-primary key*/
    INDEX `fk_Customer_Person_idx` (`Person_idPerson` ASC),
    INDEX `fk_Customer_Paycard1_idx` (`Paycard_idPaycard` ASC),
    UNIQUE INDEX `driversLicense_UNIQUE` (`driversLicense` ASC),
    CONSTRAINT `fk_Customer_Person`
        FOREIGN KEY (`Person_idPerson`)
            REFERENCES `motorhome`.`Person` (`idPerson`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Customer_Paycard1`
        FOREIGN KEY (`Paycard_idPaycard`)
            REFERENCES `motorhome`.`Paycard` (`idPaycard`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Employee`
(
    `idEmployee`      INT NOT NULL AUTO_INCREMENT,
    `accountancyId`   INT NULL,
    `Person_idPerson` INT NOT NULL,
    PRIMARY KEY (`idEmployee`),
    INDEX `fk_Employee_Person1_idx` (`Person_idPerson` ASC),
    CONSTRAINT `fk_Employee_Person1`
        FOREIGN KEY (`Person_idPerson`)
            REFERENCES `motorhome`.`Person` (`idPerson`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`carModel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`carModel`
(
    `idcarModel`        INT         NOT NULL AUTO_INCREMENT,
    `modelName`         VARCHAR(45) NULL,
    `modelNumber`       VARCHAR(45) NULL,
    `horsePower`        INT         NULL,
    `beds`              INT         NULL,
    `engineCapacity`    FLOAT       NULL,
    `length`            FLOAT       NULL,
    `height`            FLOAT       NULL,
    `width`             FLOAT       NULL,
    `weight`            FLOAT       NULL,
    `hotWaterCapacity`  FLOAT       NULL,
    `coldWaterCapacity` FLOAT       NULL,
    `numberOfSeats`     INT         NULL,
    `oven`              TINYINT(1)  NULL,
    `cruiseControl`     TINYINT(1)  NULL,
    `shower`            TINYINT(1)  NULL,
    PRIMARY KEY (`idcarModel`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Motorhome`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Motorhome`
(
    `idMotorhome`          INT          NOT NULL AUTO_INCREMENT,
    `licensePlate`         VARCHAR(45)  NULL,
    `notes`                VARCHAR(200) NULL,
    `imageURL`             VARCHAR(45)  NULL,
    `productionYear`       INT          NULL,
    `description`          VARCHAR(250)  NULL,
    `minimumDaysOfRental`  INT          NULL,
    `fuelType`             VARCHAR(45)  NULL,
    `seasonalDailyCharges` FLOAT        NULL,
    `carModel_idcarModel`  INT          NOT NULL,
    PRIMARY KEY (`idMotorhome`),
    INDEX `fk_Motorhome_carModel1_idx` (`carModel_idcarModel` ASC),
    CONSTRAINT `fk_Motorhome_carModel1`
        FOREIGN KEY (`carModel_idcarModel`)
            REFERENCES `motorhome`.`carModel` (`idcarModel`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Period`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Period`
(
    `idPeriod` INT  NOT NULL AUTO_INCREMENT,
    `start`    DATE NULL,
    `end`      DATE NULL,
    PRIMARY KEY (`idPeriod`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Appointment`
(
    `idAppointment`         INT         NOT NULL AUTO_INCREMENT,
    `date`                  DATE        NULL,
    `time`                  TIME        NULL,
    `notes`                 VARCHAR(45) NULL,
    `distance`              FLOAT       NULL,
    `Address_idAddress`     INT         NOT NULL,
    `Motorhome_idMotorhome` INT         NOT NULL,
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
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Reservation`
(
    `idReservation`             INT         NOT NULL AUTO_INCREMENT,
    `notes`                     VARCHAR(45) NULL,
    `internalNotes`             VARCHAR(45) NULL,
    `reservationStatus`         VARCHAR(45) NULL,
    `Employee_idEmployee`       INT         NOT NULL,
    `Period_idPeriod`           INT         NOT NULL,
    `Motorhome_idMotorhome`     INT         NOT NULL,
#     `Appointment_idAppointment` INT         NOT NULL,
    `Customer_driversLicense`   VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idReservation`),
    INDEX `fk_Reservation_Employee1_idx` (`Employee_idEmployee` ASC),
    INDEX `fk_Reservation_Period1_idx` (`Period_idPeriod` ASC),
    INDEX `fk_Reservation_Motorhome1_idx` (`Motorhome_idMotorhome` ASC),
#     INDEX `fk_Reservation_Appointment1_idx` (`Appointment_idAppointment` ASC),
    INDEX `fk_Reservation_Customer1_idx` (`Customer_driversLicense` ASC),
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
#     CONSTRAINT `fk_Reservation_Appointment1`
#         FOREIGN KEY (`Appointment_idAppointment`)
#             REFERENCES `motorhome`.`Appointment` (`idAppointment`)
#             ON DELETE NO ACTION
#             ON UPDATE NO ACTION,
    CONSTRAINT `fk_Reservation_Customer1`
        FOREIGN KEY (`Customer_driversLicense`)
            REFERENCES `motorhome`.`Customer` (`driversLicense`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Service`
(
    `idService`   INT         NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(45) NULL,
    `description` VARCHAR(200) NULL,
    `unitPrice`   FLOAT       NULL,
    PRIMARY KEY (`idService`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Invoice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Invoice`
(
    `idInvoice`               INT         NOT NULL AUTO_INCREMENT,
    `isCompleted`             TINYINT     NULL,
    `billPeriod`              INT         NOT NULL,
    `Motorhome_idMotorhome`   INT         NOT NULL,
    `reservationPeriod`       INT         NOT NULL,
    `Customer_driversLicense` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idInvoice`),
    INDEX `fk_Invoice_Period1_idx` (`billPeriod` ASC),
    INDEX `fk_Invoice_Motorhome1_idx` (`Motorhome_idMotorhome` ASC),
    INDEX `fk_Invoice_Period2_idx` (`reservationPeriod` ASC),
    INDEX `fk_Invoice_Customer1_idx` (`Customer_driversLicense` ASC),
    CONSTRAINT `fk_Invoice_Period1`
        FOREIGN KEY (`billPeriod`)
            REFERENCES `motorhome`.`Period` (`idPeriod`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Invoice_Motorhome1`
        FOREIGN KEY (`Motorhome_idMotorhome`)
            REFERENCES `motorhome`.`Motorhome` (`idMotorhome`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Invoice_Period2`
        FOREIGN KEY (`reservationPeriod`)
            REFERENCES `motorhome`.`Period` (`idPeriod`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Invoice_Customer1`
        FOREIGN KEY (`Customer_driversLicense`)
            REFERENCES `motorhome`.`Customer` (`driversLicense`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Reservation_has_Service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Reservation_has_Service`
(
    `idService_Reservation`     INT NOT NULL AUTO_INCREMENT,
    `Service_idService`         INT NOT NULL,
    `Reservation_idReservation` INT NOT NULL,
    PRIMARY KEY (`idService_Reservation`, `Service_idService`, `Reservation_idReservation`),
    INDEX `fk_Service_has_Reservation_Reservation1_idx` (`Reservation_idReservation` ASC),
    INDEX `fk_Service_has_Reservation_Service1_idx` (`Service_idService` ASC),
    CONSTRAINT `fk_Service_has_Reservation_Service1`
        FOREIGN KEY (`Service_idService`)
            REFERENCES `motorhome`.`Service` (`idService`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Service_has_Reservation_Reservation1`
        FOREIGN KEY (`Reservation_idReservation`)
            REFERENCES `motorhome`.`Reservation` (`idReservation`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

-- -----------hand-crafted by kcn-----------------------
-- Table 'motorhome'.'Reservation_has_Appointment'
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Reservation_has_Appointment`
(
    `idReservation_Appointment` INT NOT NULL AUTO_INCREMENT,
    `Appointment_idAppointment` INT NOT NULL,
    `Reservation_idReservation` INT NOT NULL,
    PRIMARY KEY (`idReservation_Appointment`, `Appointment_idAppointment`, `Reservation_idReservation`),
    INDEX `fk_Reservation_has_Appointment_Reservation1_idx` (`Reservation_idReservation` ASC),
    INDEX `fk_Reservation_has_Appointment_Appointment1_idx` (`Appointment_idAppointment` ASC),
    CONSTRAINT `fk_Reservation_has_Appointment_Reservation1_idx`
        FOREIGN KEY (Reservation_idReservation)
            REFERENCES `motorhome`.`reservation` (`idReservation`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Reservation_has_Appointment_Appointment1_idx`
        FOREIGN KEY (`Appointment_idAppointment`)
            REFERENCES `motorhome`.`appointment` (`idAppointment`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `motorhome`.`Motorhome_has_Service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Motorhome_has_Service`
(
    `idMotorhome_Service`   INT NOT NULL AUTO_INCREMENT,
    `Motorhome_idMotorhome` INT NOT NULL,
    `Service_idService`     INT NOT NULL,
    PRIMARY KEY (`idMotorhome_Service`, `Motorhome_idMotorhome`, `Service_idService`),
    INDEX `fk_Motorhome_has_Service_Service1_idx` (`Service_idService` ASC),
    INDEX `fk_Motorhome_has_Service_Motorhome1_idx` (`Motorhome_idMotorhome` ASC),
    CONSTRAINT `fk_Motorhome_has_Service_Motorhome1`
        FOREIGN KEY (`Motorhome_idMotorhome`)
            REFERENCES `motorhome`.`Motorhome` (`idMotorhome`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Motorhome_has_Service_Service1`
        FOREIGN KEY (`Service_idService`)
            REFERENCES `motorhome`.`Service` (`idService`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Invoice_has_Service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Invoice_has_Service`
(
    `idInvoice_Service` INT NOT NULL AUTO_INCREMENT,
    `Invoice_idInvoice` INT NOT NULL,
    `Service_idService` INT NOT NULL,
    PRIMARY KEY (`idInvoice_Service`, `Invoice_idInvoice`, `Service_idService`),
    INDEX `fk_Invoice_has_Service_Service1_idx` (`Service_idService` ASC),
    INDEX `fk_Invoice_has_Service_Invoice1_idx` (`Invoice_idInvoice` ASC),
    CONSTRAINT `fk_Invoice_has_Service_Invoice1`
        FOREIGN KEY (`Invoice_idInvoice`)
            REFERENCES `motorhome`.`Invoice` (`idInvoice`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Invoice_has_Service_Service1`
        FOREIGN KEY (`Service_idService`)
            REFERENCES `motorhome`.`Service` (`idService`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `motorhome`.`Employee_has_Appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `motorhome`.`Employee_has_Appointment`
(
    `Employee_idEmployee`       INT NOT NULL,
    `Appointment_idAppointment` INT NOT NULL,
    PRIMARY KEY (`Employee_idEmployee`, `Appointment_idAppointment`),
    INDEX `fk_Employee_has_Appointment_Appointment1_idx` (`Appointment_idAppointment` ASC),
    INDEX `fk_Employee_has_Appointment_Employee1_idx` (`Employee_idEmployee` ASC),
    CONSTRAINT `fk_Employee_has_Appointment_Employee1`
        FOREIGN KEY (`Employee_idEmployee`)
            REFERENCES `motorhome`.`Employee` (`idEmployee`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Employee_has_Appointment_Appointment1`
        FOREIGN KEY (`Appointment_idAppointment`)
            REFERENCES `motorhome`.`Appointment` (`idAppointment`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
