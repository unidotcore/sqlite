import sys._
import java.io._
import scala.io._
import scala.util.{Try, Success, Failure, Using}
import java.sql.{DriverManager, Connection, Statement}

object Main {

    val TableName: String = "users"
    val DatabaseFilename: String = "database.db"

    def main(args: Array[String]): Unit = {
        Class.forName("org.sqlite.JDBC")
		val databaseURL: String = String.format("jdbc:sqlite:%s", DatabaseFilename)
		val connection: Connection = Try(DriverManager.getConnection(databaseURL)) match {
			case Success(connection) => connection
			case Failure(_) => {
				println("Unable to open database.")
				sys.exit(1)
			}
		}
		val statement: Statement = connection.createStatement
		val query: String = String.format("SELECT * FROM %s", TableName)
		val users: Try[Seq[User]] = {
			Using(statement.executeQuery(query)) {
				Iterator.continually(_).takeWhile(_.next).map {
					rs => new User(rs.getInt("id"), rs.getString("name"), rs.getInt("age"))
				}.toList
			}
		}
		users match {
			case Success(users) => {
				println(String.format("%-10s%-15s%-10s", "ID", "NAME", "AGE"))
				users.foreach((x) => println(String.format("%-10d%-15s%-10d", x.getId, x.getName, x.getAge)))
			}
			case Failure(_) => println("Unable to execute query.")
		}
		connection.close
    }

    class User(private val id: Int, private val name: String, private val age: Int) {

		def getId(): Int = id

		def getName(): String = name

		def getAge(): Int = age
		
	}

}