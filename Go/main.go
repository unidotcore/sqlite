package main

import "os"
import "fmt"
import "database/sql"
import _ "github.com/mattn/go-sqlite3"

const TABLE_NAME string = "users"
const DATABASE_FILENAME string = "database.db"

type user struct {
    id int
    name string
    age int
}

func main() {
    database, err := sql.Open("sqlite3", DATABASE_FILENAME)
    if err != nil {
        fmt.Println("Unable to open database.")
        os.Exit(1)
    }
    query := fmt.Sprintf("SELECT * FROM %s", TABLE_NAME)
    rows, err := database.Query(query)
    if err != nil {
        fmt.Println("Unable to execute query.")
        os.Exit(1)
    }
    users := []user{}
    for rows.Next() {
        usr := user{}
        rows.Scan(&usr.id, &usr.name, &usr.age)
        users = append(users, usr)
    }
    fmt.Println(fmt.Sprintf("%-10s%-15s%-10s", "ID", "NAME", "AGE"))
    for i := range users {
        usr := users[i]
        fmt.Println(fmt.Sprintf("%-10d%-15s%-10d", usr.id, usr.name, usr.age))
    }
}