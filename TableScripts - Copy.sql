-- Books Table
CREATE TABLE Books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    genre VARCHAR(50),
    stock_quantity INT,
    author_id INT,
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES Authors(author_id)
);

-- Authors Table
CREATE TABLE Authors (
    author_id SERIAL PRIMARY KEY,
    author_name VARCHAR(100),
    country VARCHAR(100)
);

-- Customers Table
CREATE TABLE Customers (
    customer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    address VARCHAR(200)
);

-- Orders Table
CREATE TABLE Orders (
    order_id SERIAL PRIMARY KEY,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount NUMERIC(10, 2),
    customer_id INT REFERENCES Customers(customer_id)
);

-- Order Details Table
CREATE TABLE orderdetail (
    order_detail_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

-- Inserting book titles along with author names and genres into the Books table
INSERT INTO Books (title, genre, author_id)
VALUES 
    ('Letter from an Unknown Woman', 'Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Stefan Zweig')),
    ('Fantastic night', 'Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Stefan Zweig')),
    ('Murder on the Orient Express', 'Mystery', (SELECT author_id FROM Authors WHERE author_name = 'Agatha Christie')),
    ('The Mystery of the Blue Train', 'Mystery', (SELECT author_id FROM Authors WHERE author_name = 'Agatha Christie')),
    ('The Old man and The Sea', 'Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Ernest Hemingway')),
    ('A Farewell to Arms', 'Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Ernest Hemingway')),
    ('1984', 'Science Fiction', (SELECT author_id FROM Authors WHERE author_name = 'George Orwell')),
    ('Down and Out in Paris and London', 'Biography', (SELECT author_id FROM Authors WHERE author_name = 'George Orwell')),
    ('Animal Farm', 'Political Satire', (SELECT author_id FROM Authors WHERE author_name = 'George Orwell')),
    ('Anna Karenina', 'Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Lev Tolstoy')),
    ('A confession', 'Philosophy', (SELECT author_id FROM Authors WHERE author_name = 'Lev Tolstoy')),
    ('What Men live by', 'Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Lev Tolstoy')),
    ('Crime and Punishment', 'Psychological Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Fyodor Dostoyevsky')),
    ('Notes from underground', 'Philosophical Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Fyodor Dostoyevsky')),
    ('Civilization and its discontents', 'Psychology', (SELECT author_id FROM Authors WHERE author_name = 'Sigmund Freud')),
    ('The Power of your subconscious mind', 'Psychology', (SELECT author_id FROM Authors WHERE author_name = 'Joseph Murphy')),
    ('The Devil Inside Us', 'Drama', (SELECT author_id FROM Authors WHERE author_name = 'Sabahattin Ali')),
    ('New World', 'Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Sabahattin Ali')),
    ('The Miserables', 'Historical Fiction', (SELECT author_id FROM Authors WHERE author_name = 'Victor Hugo')),
    ('Ali and Nino', 'Romance', (SELECT author_id FROM Authors WHERE author_name = 'Qurban Said'));
--customers, orders, orderdetail table infomations are random
--for authors table you can use what we inserted to books table

