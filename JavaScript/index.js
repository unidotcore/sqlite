const sqlite3 = require('sqlite3');
const sprintf = require('sprintf-js').sprintf

const TABLE_NAME = 'users';
const DATABASE_FILENAME = 'database.db';

const database = new sqlite3.Database(DATABASE_FILENAME, (err) => {
    if (err) {
        console.error('Unable to open database.');
        process.exit(1);
    }
});

const query = `SELECT * FROM ${TABLE_NAME}`;

database.all(query, [], (err, rows) => {
    if (err) {
        console.error('Unable to execute query.');
        process.exit(1);
    }
    const users = []
    rows.forEach((row) => users.push(new User(row.id, row.name, row.age)));
    console.log(sprintf('%-10s%-15s%-10s', 'ID', 'NAME', 'AGE'));
    users.forEach((user) => console.log(sprintf('%-10d%-15s%-10d', user.id, user.name, user.age)));
});

database.close();

function User(id, name, age) {
    this.id = id;
    this.name = name;
    this.age = age;
}