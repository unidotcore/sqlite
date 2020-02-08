extern crate rusqlite;

use std::process;
use rusqlite::{params, Connection, Result};

const TABLE_NAME: &str = "users";
const DATABASE_FILENAME: &str = "database.db";

struct User {
    id: u32,
    name: String,
    age: u32
}

fn main() -> Result<()> {
    let database = match Connection::open(DATABASE_FILENAME) {
        Ok(database) => database,
        Err(_) => {
            println!("Unable to open database.");
            process::exit(1);
        }
    };
    let query = format!("SELECT * FROM {}", TABLE_NAME);
    let mut statement_handle = match database.prepare(&query) {
        Ok(statement_handle) => statement_handle,
        Err(_) => {
            println!("Unable to execute query.");
            process::exit(1);
        }
    };
    let mut users = statement_handle.query_map(params![], |row| {
        Ok(User {
            id: row.get(0)?,
            name: row.get(1)?,
            age: row.get(2)?
        })
    })?;
    println!("{:<10}{:<15}{:<10}", "ID", "NAME", "AGE");
    while match users.next() {
        Some(Ok(user)) => {
            println!("{:<10}{:<15}{:<10}", user.id, user.name, user.age);
            true
        },
        Some(Err(_)) | None => false
    } {}
    drop(users);
    statement_handle.finalize()?;
    return match database.close() {
        Ok(()) => Ok(()),
        Err(e) => Err(e.1)
    };
}