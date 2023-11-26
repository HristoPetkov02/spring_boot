CREATE TABLE IF NOT EXISTS Accounts (
    ID_Account IDENTITY(1,1) NOT NULL PRIMARY KEY, --IDENTITY за auto increment
    Username VARCHAR(32) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Administrator BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS Faculties (
    ID_Faculty IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Faculty_Code VARCHAR(10),
    Faculty_Name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Departments (
    ID_Department IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Department_Code VARCHAR(10),
    Department_Name VARCHAR(50) NOT NULL,
    Faculty_ID INT,

    FOREIGN KEY (Faculty_ID) REFERENCES Faculties(ID_Faculty)
);

CREATE TABLE IF NOT EXISTS Colleges (
    ID_Collage IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Collage_Name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Buildings (
    ID_Building IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Building_Name VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS Rooms (
    ID_Room IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Room_Number INT NOT NULL,
    Phone VARCHAR(14) NOT NULL,
    Building_ID INT,

    FOREIGN KEY (Building_ID) REFERENCES Buildings(ID_Building)
);

CREATE TABLE IF NOT EXISTS Students (
    ID_Student IDENTITY(1,1) NOT NULL PRIMARY KEY,
    FN VARCHAR(12) NOT NULL,
    Department_ID INT,
    Collage_ID INT,
    Room_ID INT,

    FOREIGN KEY (Department_ID) REFERENCES Departments(ID_Department),
    FOREIGN KEY (Collage_ID) REFERENCES Colleges(ID_Collage),
    FOREIGN KEY (Room_ID) REFERENCES Rooms(ID_Room)
);