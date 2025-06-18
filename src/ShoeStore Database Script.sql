CREATE DATABASE ShoeStore;
GO

USE ShoeStore;
GO

CREATE TABLE [User] (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username NVARCHAR(50) UNIQUE NOT NULL,
    Password NVARCHAR(255) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    FullName NVARCHAR(100),
    Address NVARCHAR(255),
    Phone NVARCHAR(20),
    Role NVARCHAR(20) NOT NULL CHECK (Role IN ('User', 'Admin', 'Staff')),
    Status NVARCHAR(20) NOT NULL CHECK (Status IN ('Active', 'Banned')) DEFAULT 'Active'
);

CREATE TABLE Category (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    CategoryName NVARCHAR(50) NOT NULL
);

CREATE TABLE Size (
    SizeID INT PRIMARY KEY IDENTITY(1,1),
    SizeNumber INT NOT NULL
);

CREATE TABLE Shoe (
    ShoeID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(1000),
    Price DECIMAL(18,2) NOT NULL,
    Image NVARCHAR(255),
    CategoryID INT NOT NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

CREATE TABLE ShoeSize (
    ShoeID INT NOT NULL,
    SizeID INT NOT NULL,
    StockQuantity INT NOT NULL CHECK (StockQuantity >= 0),
    PRIMARY KEY (ShoeID, SizeID),
    FOREIGN KEY (ShoeID) REFERENCES Shoe(ShoeID),
    FOREIGN KEY (SizeID) REFERENCES Size(SizeID)
);

CREATE TABLE CartItem (
    CartItemID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    ShoeID INT NOT NULL,
    SizeID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    FOREIGN KEY (UserID) REFERENCES [User](UserID),
    FOREIGN KEY (ShoeID, SizeID) REFERENCES ShoeSize(ShoeID, SizeID),
    UNIQUE (UserID, ShoeID, SizeID)
);

CREATE TABLE [Order] (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    OrderDate DATETIME NOT NULL DEFAULT GETDATE(),
    Status NVARCHAR(20) NOT NULL CHECK (Status IN ('Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled')) DEFAULT 'Pending',
    TotalAmount DECIMAL(18,2) NOT NULL,
    ShippingAddress NVARCHAR(255) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES [User](UserID)
);

CREATE TABLE OrderDetail (
    OrderDetailID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT NOT NULL,
    ShoeID INT NOT NULL,
    SizeID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    Price DECIMAL(18,2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
    FOREIGN KEY (ShoeID, SizeID) REFERENCES ShoeSize(ShoeID, SizeID)
);

CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT NOT NULL,
    PaymentDate DATETIME NOT NULL DEFAULT GETDATE(),
    Amount DECIMAL(18,2) NOT NULL,
    PaymentStatus NVARCHAR(20) NOT NULL CHECK (PaymentStatus IN ('Success', 'Failed', 'Pending')),
    TransactionID NVARCHAR(100),
    FOREIGN KEY (OrderID) REFERENCES [Order](OrderID)
);

CREATE TABLE PasswordResetToken (
    TokenID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    Token NVARCHAR(255) NOT NULL,
    ExpiryDate DATETIME NOT NULL,
    FOREIGN KEY (UserID) REFERENCES [User](UserID)
);