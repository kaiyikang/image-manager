-- CREATE TYPE
DROP TYPE IF EXISTS item_type;
CREATE TYPE item_type AS ENUM {
    'A',
    'B',
    'C'
}

-- CREATE TABLE
DROP TABLE IF EXISTS test;
CREATE TABLE test (
    id SERIAL PRIMARY KEY,
    item VARCHAR NOT NULL,
    item_type item_type,
    price NUMERIC(4,2)
)